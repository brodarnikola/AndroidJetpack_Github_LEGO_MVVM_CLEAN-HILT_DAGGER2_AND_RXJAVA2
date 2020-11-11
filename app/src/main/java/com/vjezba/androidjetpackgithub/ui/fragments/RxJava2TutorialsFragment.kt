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

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vjezba.androidjetpackgithub.databinding.FragmentRxjava2TutorialBinding
import com.vjezba.androidjetpackgithub.ui.adapters.ReposRxJava2FlatMapAdapter
import com.vjezba.androidjetpackgithub.viewmodels.RxJava2ViewModel
import com.vjezba.data.Post
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.data.networking.model.RepositoryResponseApi
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_rxjava2_tutorial.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.jvm.Throws


private const val UPDATE_PERIOD = 10000L

@AndroidEntryPoint
class RxJava2TutorialsFragment : Fragment() {

    private val viewModel : RxJava2ViewModel by viewModels()

    private var automaticIncreaseNumberByOne: Job? = null

    var githubReposCompositeDisposable: CompositeDisposable? = null

    val BASE_URL = "https://api.github.com/"

    private var adapter: ReposRxJava2FlatMapAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRxjava2TutorialBinding.inflate(inflater, container, false)
        context ?: return binding.root

        viewModel.incrementNumberAutomaticallyByOne.observe(viewLifecycleOwner, Observer { currentNumber ->
            tvNumberIncreaseAutomatically.text = "" + currentNumber
        })

        viewModel.observeRepos("Java").observe(viewLifecycleOwner, Observer { repos ->
            var reposResult = ""
            repos.items.forEach { repos ->
                reposResult += "Name of repository: " + repos.name + "\n"
            }
            tvConvertFlowableToLiveData.setText(reposResult)
            println("Size of data is: ${repos.items.size}")
        })

        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ReposRxJava2FlatMapAdapter()
        list_repos_flat_map.setAdapter(adapter)
    }


    override fun onResume() {
        super.onResume()

        rxJava2Tutorials()

        rxJava2FlatMapExample()

        automaticIncreaseNumberByOne?.cancel()
        automaticIncreaseNumberByOne = lifecycleScope.launch {
            while (true) {
                try {
                    viewModel.incrementAutomaticallyByOne()
                } catch (ex: Exception) {
                    Log.v("ERROR","Periodic remote-update failed...", ex)
                }
                delay(UPDATE_PERIOD)
            }
        }
    }

    private fun rxJava2FlatMapExample() {
        initRecyclerView()
        getPostObservable()
            .subscribeOn(Schedulers.io())
            .flatMap { posts ->
                Log.d(ContentValues.TAG, "Da li ce uci unutra 777")
                getCommentsObservable(posts)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<Post> {


                override fun onComplete() {}


                override fun onSubscribe(d: Disposable) {
                    githubReposCompositeDisposable?.add(d)
                }

                override fun onNext(post: Post) {
                    adapter?.updatePost(post)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }
            })
    }

    private fun setupRetrofitFlatMap(): GithubRepositoryApi {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GithubRepositoryApi::class.java)
    }

    private fun getPostObservable() : Observable<Post> {
        val resultPost = setupRetrofitFlatMap()
            .getPosts( )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap( object :  io.reactivex.functions.Function< List<Post> , ObservableSource<Post>> {
                @Throws
                override fun apply( posts: List<Post> ) : ObservableSource<Post>  {
                    Log.d(ContentValues.TAG, "Da li ce uci unutra 555")
                    adapter?.setPosts(posts.toMutableList())
                    return Observable.fromIterable(posts)
                        .subscribeOn(Schedulers.io())
                }

            })
        return resultPost
    }

    private fun getCommentsObservable(post: Post) : Observable<Post> {
        val resultPostComments = setupRetrofitFlatMap()
            .getComments( post.id )
            .map { comments ->
//                That is good example to use with flatMap.. Then you can see, that execution of flatMap is not in the same order.. flatMap get executed randomly
                val delay: Int = (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms

                Thread.sleep(delay.toLong())
                Log.d(
                    TAG,
                    "apply: sleeping thread " + Thread.currentThread()
                        .name + " for " + delay.toString() + "ms"
                )

                Log.d(ContentValues.TAG, "Da li ce uci unutra 9999")
                post.comments = comments
                post
            }
            .subscribeOn(Schedulers.io())

        return resultPostComments
    }

    private fun rxJava2Tutorials() {

        setupCompositeDisposable(BASE_URL)

        simpleObservablesAndObservers()
    }

    private fun setupRetrofit(): GithubRepositoryApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GithubRepositoryApi::class.java)
    }

    private fun setupCompositeDisposable(baseUrl : String) {

        val requestInterface = setupRetrofit()

        githubReposCompositeDisposable = CompositeDisposable()
        githubReposCompositeDisposable?.addAll(
            searchGithubRepos(requestInterface, 15),
            searchGithubRepos(requestInterface, 10)
        )
    }

    private fun searchGithubRepos(requestInterface: GithubRepositoryApi, sizeOfGithubRepos: Int): Disposable? {
        return requestInterface.searchGithubRepositoryWithRxJava2("java", 1, sizeOfGithubRepos)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse)
    }

    private fun handleResponse(repositoryResponseApi: RepositoryResponseApi) {

        Log.d(ContentValues.TAG, "Size of composite disposable stream and rest api from github: " + repositoryResponseApi.items.size)
        var reposResult = ""
        repositoryResponseApi.items.forEach { repos ->
            reposResult += "Name of repository: " + repos.name + "\n"
        }
        tvCompositeDisposableValue.setText(reposResult)
    }

    private fun simpleObservablesAndObservers() {
        val animalsObservable =
            Observable.just("Ant", "Bee", "Cat", "Dog", "Fox")

        val animalObserver= getAnimalsObserver()

        animalsObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { name -> name.toUpperCase() }
            .subscribe(animalObserver)


        val d = Observable.just(1, 2, 3)
            .map { i: Int -> i * i }
            .map { i: Int -> i * i }
            .filter { i: Int -> i > 10 }
            .subscribe { x: Int? ->
                Log.d(ContentValues.TAG, "Numbers greather than 10 are: " + x)
                println(x) }
    }

    private fun getAnimalsObserver(): io.reactivex.Observer<String?> {
        return object : io.reactivex.Observer<String?> {

            override fun onNext(s: String) {
                Log.d(ContentValues.TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(
                    ContentValues.TAG,
                    "onError: " + e.message
                )
            }

            override fun onComplete() {
                Log.d(
                    ContentValues.TAG,
                    "All items are emitted!"
                )
            }
            override fun onSubscribe(d: Disposable) {
                Log.d(ContentValues.TAG, "onSubscribe")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        githubReposCompositeDisposable?.clear()
    }

    override fun onStop() {
        super.onStop()
        githubReposCompositeDisposable?.apply {
            if( !isDisposed ) {
                dispose()
                githubReposCompositeDisposable = null
            }
        }
    }


}