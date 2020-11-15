package com.vjezba.androidjetpackgithub.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.vjezba.domain.model.RepositoryResponse
import com.vjezba.domain.repository.GithubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class RxJava2ViewModel @ViewModelInject constructor (
    private val repository: GithubRepository
) : ViewModel() {

    private val authUser: MediatorLiveData<RepositoryResponse> = MediatorLiveData<RepositoryResponse>()

    fun observeRepos(query: String) : LiveData<RepositoryResponse> {

        var source: LiveData<RepositoryResponse>? = null
        try {
            source = LiveDataReactiveStreams.fromPublisher(
            repository.getSearchRepositorieWithFlowableRxJava2(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error ->
                    print("onError crash: ${error}")
                    throw error
                }
            )
        }
        catch (e : Exception) {
            print("Exception: ${e}")
        }

        authUser.addSource(source!!, object : Observer<RepositoryResponse?> {
            override fun onChanged(user: RepositoryResponse?) {
                authUser.setValue(user)
                authUser.removeSource(source)
            }
        })

        return authUser
    }

    private val _incrementNumberAutomaticallyByOne = MediatorLiveData<Int>().apply {
        value = 0
    }

    val incrementNumberAutomaticallyByOne: LiveData<Int> = _incrementNumberAutomaticallyByOne

    fun incrementAutomaticallyByOne() {
        Log.d("TestVM", "The automatic amount is being increment, current value = ${_incrementNumberAutomaticallyByOne.value}")
        _incrementNumberAutomaticallyByOne.value?.let { number ->
            _incrementNumberAutomaticallyByOne.value = number + 1
        }
    }

}
