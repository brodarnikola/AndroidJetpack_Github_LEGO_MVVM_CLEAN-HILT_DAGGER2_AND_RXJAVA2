package com.vjezba.androidjetpackgithub.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.Observer;
import com.vjezba.domain.model.RepositoryResponse
import com.vjezba.domain.repository.GithubRepository
import io.reactivex.schedulers.Schedulers


class RxJava2ViewModel @ViewModelInject constructor (
    private val repository: GithubRepository
) : ViewModel() {

    private val authUser: MediatorLiveData<RepositoryResponse> = MediatorLiveData<RepositoryResponse>()

    fun observeRepos(query: String) : LiveData<RepositoryResponse> {
        val source: LiveData<RepositoryResponse> = LiveDataReactiveStreams.fromPublisher(
            repository.getSearchRepositorieWithFlowableRxJava2(query)
                .subscribeOn(Schedulers.io())
        )

        authUser.addSource(source, object : Observer<RepositoryResponse?> {
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
