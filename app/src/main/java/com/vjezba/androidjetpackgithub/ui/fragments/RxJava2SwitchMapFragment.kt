/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.androidjetpackgithub.ui.fragments

import android.R
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vjezba.androidjetpackgithub.databinding.FragmentRxjava2SwitchMapBinding
import com.vjezba.androidjetpackgithub.ui.adapters.RxJava2SwitchMapAdapter
import com.vjezba.data.Post
import com.vjezba.data.networking.GithubRepositoryApi
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_rxjava2_switch_map.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws


@AndroidEntryPoint
class RxJava2SwitchMapFragment : Fragment(), RxJava2SwitchMapAdapter.OnPostClickListener {

    var compositeDisposable: CompositeDisposable? = null

    private var adapter: RxJava2SwitchMapAdapter? = null
    private val PERIOD = 100

    private var publishSubject =
        PublishSubject.create<Post>() // for selecting a post

    var userClickedPosition = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRxjava2SwitchMapBinding.inflate(inflater, container, false)
        context ?: return binding.root
        activity?.speedDial?.visibility = View.GONE

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        initRecyclerView()
        retrievePosts()

        progress_bar.setProgress(0)
        rxJava2SwitchMapExample()
    }

    private fun retrievePosts() {
        setupRetrofitFlatMap()
            .getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<List<Post>> {

                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable?.add(d)
                }

                override fun onNext(posts: List<Post>) {
                    Log.d(TAG, "da li ce doci simm: EEEEEE")
                    adapter!!.setPosts(posts.toMutableList())
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }
            })
    }

    override fun onPostClick(position: Int) {
        userClickedPosition = position + 1
        // submit the selected post object to be queried
        publishSubject.onNext(adapter?.getPosts()?.get(position) ?: Post())
    }

    private fun rxJava2SwitchMapExample() {

        publishSubject // apply switchmap operator so only one Observable can be used at a time.
            // it clears the previous one
            .switchMap(object : io.reactivex.functions.Function<Post?, ObservableSource<Post?>> {

                @Throws(Exception::class)
                override fun apply(post: Post): ObservableSource<Post?> {
                    return Observable // simulate slow network speed with interval + takeWhile + filter operators
                        .interval(PERIOD.toLong(), TimeUnit.MILLISECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .takeWhile(object : Predicate<Long> {
                            // stop the process if more than 5 seconds passes
                            @Throws(Exception::class)
                            override fun test(aLong: Long): Boolean {
                                Log.d(
                                    TAG,
                                    "test: " + Thread.currentThread()
                                        .name + ", " + aLong
                                )
                                progress_bar.setMax(3000 - PERIOD)
                                progress_bar.setProgress(
                                    (aLong * PERIOD + PERIOD).toString().toInt()
                                )
                                return aLong <= 3000 / PERIOD
                            }
                        })
                        .filter(object : Predicate<Long> {
                            @Throws(Exception::class)
                            override fun test(aLong: Long): Boolean {
                                return aLong >= 3000 / PERIOD
                            }
                        })
                        // flatmap to convert Long from the interval operator into a Observable<Post>
                        .subscribeOn(Schedulers.io())
                        .flatMap(object : io.reactivex.functions.Function<Long?, ObservableSource<Post?>> {
                            @Throws(Exception::class)
                            override fun apply(longNumber: Long): ObservableSource<Post?> {
                                return setupRetrofitFlatMap().getPost(userClickedPosition)
                            }
                        })
                }
            })
            .subscribe(object : Observer<Post?> {

                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable?.add(d)
                }

                override fun onNext(post: Post) {
                    Log.d(TAG, "onNext: done.")
                    val snackBar: Snackbar = Snackbar.make(llMain,
                        "Data of post: ${post.title}", Snackbar.LENGTH_LONG
                    )
                    snackBar.show()
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }
            })
    }

    private fun initRecyclerView() {
        adapter = RxJava2SwitchMapAdapter(this)
        list_post_switch_map.setAdapter(adapter)
    }

    private fun setupRetrofitFlatMap(): GithubRepositoryApi {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GithubRepositoryApi::class.java)
    }


    override fun onPause() {
        super.onPause()
        compositeDisposable?.clear()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.apply {
            if( !isDisposed ) {
                dispose()
                compositeDisposable = null
            }
        }
    }


}