package com.vjezba.androidjetpackgithub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.vjezba.domain.model.RepositoryDetailsResponse
import com.vjezba.domain.repository.GithubRepository
import kotlinx.coroutines.flow.*

class PaggingWithNetworkAndDbDataViewModel @ViewModelInject constructor (
    private val repository: GithubRepository
) : ViewModel() {

//    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
//
//    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
//    val posts = flowOf(
//        clearListCh.receiveAsFlow().map { PagingData.empty<RepositoryDetailsResponse>() },
//        savedStateHandle.getLiveData<String>(KEY_SUBREDDIT)
//            .asFlow()
//            .flatMapLatest { repository.getSearchRepositoriesWithMediatorAndPaggingData(it, 30) }
//    ).flattenMerge(2)

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<UiModel>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<UiModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel>> = repository.getSearchRepositoriesWithMediatorAndPaggingData(queryString)
            .map { pagingData -> pagingData.map { UiModel.RepoItem(it) } }
            .map {
                it.insertSeparators<UiModel.RepoItem, UiModel> { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators UiModel.SeparatorItem("${after.roundedStarCount}0.000+ stars")
                    }
                    // check between 2 items
                    if (before.roundedStarCount > after.roundedStarCount) {
                        if (after.roundedStarCount >= 1) {
                            UiModel.SeparatorItem("${after.roundedStarCount}0.000+ stars")
                        } else {
                            UiModel.SeparatorItem("< 10.000+ stars")
                        }
                    } else {
                        // no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}


sealed class UiModel {
    data class RepoItem(val repo: RepositoryDetailsResponse) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

private val UiModel.RepoItem.roundedStarCount: Int
    get() = this.repo.starts / 10_000