package com.vjezba.androidjetpackgithub.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.vjezba.data.di.GithubNetwork
import com.vjezba.domain.model.RepositoryResponse
import com.vjezba.domain.repository.GithubRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class RxJava2ViewModel @Inject constructor(
    @GithubNetwork private val repository: GithubRepository
) : ViewModel() {

    private val authUser: MediatorLiveData<RepositoryResponse> =
        MediatorLiveData<RepositoryResponse>()

    fun observeRepos(query: String): LiveData<RepositoryResponse> {

        var source: LiveData<RepositoryResponse>? = null
        try {
            source = LiveDataReactiveStreams.fromPublisher(
                repository.getSearchRepositorieWithFlowableRxJava2(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn { error ->
                        Log.e(TAG, "onError received: ${error}")
                        RepositoryResponse(0, false, listOf())
                    }
            )
        } catch (e: Exception) {
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

    // this is a single example of rxjava2 for github repositories
    fun getGithubRepositories(query: String, page: Int, perPage: Int) : Single<RepositoryResponse> {
        return repository.getSearchRepositorieRxJava2(query, page, perPage)
    }

    private val _incrementNumberAutomaticallyByOne = MediatorLiveData<Int>().apply {
        value = 0
    }

    val incrementNumberAutomaticallyByOne: LiveData<Int> = _incrementNumberAutomaticallyByOne

    fun incrementAutomaticallyByOne() {
        Log.d(
            "TestVM",
            "The automatic amount is being increment, current value = ${_incrementNumberAutomaticallyByOne.value}"
        )
        _incrementNumberAutomaticallyByOne.value?.let { number ->
            _incrementNumberAutomaticallyByOne.value = number + 1
        }
    }

}