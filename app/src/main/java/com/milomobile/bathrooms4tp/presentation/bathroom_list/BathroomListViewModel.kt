package com.milomobile.bathrooms4tp.presentation.bathroom_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.repository.BathroomRepository
import com.milomobile.bathrooms4tp.util.baseLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BathroomListViewModel(
    private val bathroomRepository: BathroomRepository
) : ViewModel() {

    var state = MutableStateFlow(BathroomListState())
        private set

    fun onStart() {
        loadBathrooms()
    }

    fun loadBathrooms() {
        setLoadingState(true)
        viewModelScope.launch {
            bathroomRepository.getBathrooms()
                .onLeft {
                    state.value = state.value.copy(uiError = it)
                }.onRight { bathrooms ->
                    state.value =
                        state.value.copy(bathrooms = bathrooms.sortedByDescending { it.rating })
                }
        }
        setLoadingState(false)
    }

    private fun setLoadingState(loading: Boolean) {
        state.value = state.value.copy(loading = loading)
    }

    fun onClearUIError() {
        state.value = state.value.copy(uiError = null)
    }

    fun onBathroomItemSelected(selectedBathroom: Bathroom) {
        state.value = state.value.copy(selectedBathroom = selectedBathroom)
    }

    fun onBathroomDetailsClosed() {
        state.value = state.value.copy(selectedBathroom = null)
    }
}