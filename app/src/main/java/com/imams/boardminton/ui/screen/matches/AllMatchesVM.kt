package com.imams.boardminton.ui.screen.matches

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toRepo
import com.imams.boardminton.domain.mapper.MatchRepoMapper.toVp
import com.imams.boardminton.domain.mapper.any
import com.imams.boardminton.domain.mapper.isSingle
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.Sort
import com.imams.data.match.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMatchesVM @Inject constructor(
    private var repository: MatchRepository,
): ViewModel() {

    private val _allMatches = mutableStateListOf<MatchViewParam>()
    private val iAllMatches = mutableStateListOf<MatchViewParam>()
    val allMatches: StateFlow<List<MatchViewParam>> = MutableStateFlow(iAllMatches).asStateFlow()

    private val _onGoingMatch = mutableStateListOf<MatchViewParam>()
    private val iOnGoingMatch = mutableStateListOf<MatchViewParam>()
    val onGoingMatches: StateFlow<List<MatchViewParam>> = MutableStateFlow(iOnGoingMatch).asStateFlow()

    private val _finishMatches = mutableStateListOf<MatchViewParam>()
    private val iFinishMatches = mutableStateListOf<MatchViewParam>()
    val finishMatches: StateFlow<List<MatchViewParam>> = MutableStateFlow(iFinishMatches).asStateFlow()

    fun fetchData() {
        viewModelScope.launch {
            repository.getAllMatches().collectLatest {
                _onGoingMatch.clear()
                _onGoingMatch.addAll(it.map { m -> m.toVp() }.filterNot { m -> m.winner.any() })
                _onGoingMatch.proceed(iOnGoingMatch)
                _finishMatches.clear()
                _finishMatches.addAll(it.map { m -> m.toVp() }.filter { m -> m.winner.any()})
                _finishMatches.proceed(iFinishMatches)
                _allMatches.clear()
                _allMatches.addAll(it.map { m -> m.toVp() }.sortedBy { m -> m.id })
                _allMatches.proceed(iAllMatches)
            }
        }
    }

    private fun List<MatchViewParam>.proceed(list: SnapshotStateList<MatchViewParam>) {
        list.clear()
        list.addAll(this)
    }

    fun sortOn(sortMatch: SortOn) {
        when (sortMatch) {
            is SortOn.AllMatch -> {
                iAllMatches.sortBy(sortMatch.sort)
            }
            is SortOn.Finished -> {
                iFinishMatches.sortBy(sortMatch.sort)
            }
            is SortOn.OnGoing -> {
                iOnGoingMatch.sortBy(sortMatch.sort)
            }
        }
    }

    private fun SnapshotStateList<MatchViewParam>.sortBy(sort: SortMatch) {
        when (sort) {
            is SortMatch.Id -> {
               if (sort.asc == Sort.Ascending) this.sortBy { it.id } else this.sortByDescending { it.id }
            }
            is SortMatch.Duration -> {
                if (sort.asc == Sort.Ascending) this.sortBy { it.matchDurations } else this.sortByDescending { it.matchDurations }
            }
            is SortMatch.LastUpdate -> {
                if (sort.asc == Sort.Ascending) this.sortBy { it.lastUpdate } else this.sortByDescending { it.lastUpdate }
            }
            is SortMatch.GameCount -> {
                if (sort.asc == Sort.Ascending) this.sortBy { it.games.size } else this.sortByDescending { it.games.size }
            }
        }
    }

    fun filter(filterOn: FilterOn) {
        when (filterOn) {
            is FilterOn.AllMatch -> {
                println("FilterOn: All")
                var temp = _allMatches.toList()
                filterOn.filter.type?.let {
                    temp = if (it.equals("all", true)) {
                        temp
                    } else if (it.equals("single", true)) {
                        temp.filter { m -> m.matchType.isSingle() }
                    } else {
                        temp.filterNot { m -> m.matchType.isSingle() }
                    }
                    temp.proceed(iAllMatches)
                }
            }
            is FilterOn.OnGoing -> {
                var temp = _onGoingMatch.toList()
                filterOn.filter.type?.let {
                    temp = if (it.equals("All", true)) temp
                    else if (it.equals("Single", true)) {
                        temp.filter { m -> m.matchType.isSingle() }
                    } else temp.filterNot { m -> m.matchType.isSingle() }
                    temp.proceed(iOnGoingMatch)
                }
            }
            is FilterOn.Finished -> {
                var temp = _finishMatches.toList()
                filterOn.filter.type?.let {
                    temp = if (it.equals("all", true)) temp
                    else if (it.equals("Single", true)) {
                        temp.filter { m -> m.matchType.isSingle() }
                    } else {
                        temp.filterNot { m -> m.matchType.isSingle() }
                    }
                    temp.proceed(iFinishMatches)
                }
            }
        }
    }

    fun remove(item: MatchViewParam) {
        viewModelScope.launch {
            repository.deleteMatch(item.toRepo())
        }
    }

}

data class FilterMatch(
    val type: String? = null,
)

sealed class SortMatch(val asc: Sort) {
    data class Id(val sort: Sort): SortMatch(sort)
    data class LastUpdate(val sort: Sort): SortMatch(sort)
    data class Duration(val sort: Sort): SortMatch(sort)
    data class GameCount(val sort: Sort): SortMatch(sort)
}

sealed class FilterOn(val params: FilterMatch) {
    data class AllMatch(val filter: FilterMatch): FilterOn(filter)
    data class OnGoing(val filter: FilterMatch): FilterOn(filter)
    data class Finished(val filter: FilterMatch): FilterOn(filter)
}
sealed class SortOn(val sort: SortMatch) {
    data class AllMatch(val asc: SortMatch): SortOn(asc)
    data class OnGoing(val asc: SortMatch): SortOn(asc)
    data class Finished(val asc: SortMatch): SortOn(asc)
}