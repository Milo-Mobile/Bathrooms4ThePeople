package com.milomobile.bathrooms4tp.presentation.create_bathroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.fx.coroutines.parZip
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.model.bathroom_models.BathroomAdapter
import com.milomobile.bathrooms4tp.data.model.bathroom_models.address
import com.milomobile.bathrooms4tp.data.model.bathroom_models.city
import com.milomobile.bathrooms4tp.data.model.bathroom_models.state
import com.milomobile.bathrooms4tp.data.model.bathroom_models.street
import com.milomobile.bathrooms4tp.data.model.bathroom_models.zipcode
import com.milomobile.bathrooms4tp.data.repository.BathroomRepository
import kotlinx.coroutines.launch

class CreateBathroomViewModel(
    private val bathroomRepository: BathroomRepository,
) : ViewModel() {
    //If we use by then we lose out updateCopy from kotlin arrow
    //If we use by, we have a direct getter to the value, without having to call state.value
    var state by mutableStateOf(CreateBathroomState())
        private set


    fun onClearUIError() {
        //When using updateCopy {}, getting runtime exceptions for "no static method copy"
        //Will research why this occurs at a later point
        state = state.copy(
            uiError = null
        )
    }

    private fun onClearFieldErrors() {
        state = state.copy(
            fieldErrors = null
        )
    }

    fun onStreetUpdated(value: String) {
        state = state.copy(
            newBathroom = Bathroom.address.street.set(state.newBathroom, value)
        )
    }


    /**
     * Two example usages of Arrow optics to update nested data
     */
    fun onStreetUpdatedFoo(value: String) {
        //Example of Optics using copy {}, but copy {} requires a type as it's receiver?
//        state.value = state.value.copy(
//            newBathroom = state.value.newBathroom.updateStreet(value)
//        )

        //Example of Optics using set(), but set() requires passing in the source data and the new value
        // good for one property changes
//        state = state.copy(
//            newBathroom = Bathroom.address.street.set(state.newBathroom, value)
//        )
    }

    //Example of Optics using copy {}, but copy {} requires a type as it's receiver?
    // good for multi property changes
    // Might only work when using direct arrow-optics, NOT when using arrow-optics-compose
//    private fun Bathroom.updateStreet(value: String): Bathroom = copy {
//        Bathroom.address.street set value
//    }


    fun onCityUpdated(value: String) {
        state = state.copy(
            newBathroom = Bathroom.address.city.set(state.newBathroom, value)
        )
    }


    fun onStateUpdated(value: String) {
        state = state.copy(
            newBathroom = Bathroom.address.state.set(state.newBathroom, value)
        )
    }


    fun onZipcodeUpdated(value: Long) {
        state = state.copy(
            newBathroom = Bathroom.address.zipcode.set(state.newBathroom, value)
        )
    }

    fun onFieldsEntered() {
        viewModelScope.launch {
            submitBathroom()
        }
    }

    private suspend fun submitBathroom() {
        state = state.copy(loading = true)
        parZip(
            { state.newBathroom.validateCriticalDataAccumulate() },//Validate data
            { BathroomAdapter().modelToDocument(state.newBathroom) }//Map data
        ) { validBathroomData, bathroomMap ->
            //TODO: Clean up this onRight/onLeft dance
            validBathroomData
                .onRight {
                    bathroomMap
                        .onRight {
                            bathroomRepository.submitBathroom(it)
                                .onRight {
                                    onClearFieldErrors()
                                    state = state.copy(bathroomSubmittedToDatabase = true)
                                }
                                .onLeft { submitBathroomError ->
                                    onClearFieldErrors()
                                    state = state.copy(uiError = submitBathroomError)
                                }
                        }.onLeft {
                            onClearFieldErrors()
                            state = state.copy(uiError = it)
                        }
                }.onLeft {
                    state = state.copy(fieldErrors = it)
                }
        }
        state = state.copy(loading = false)
    }

    fun resetState() {
        state = CreateBathroomState()
    }
}
