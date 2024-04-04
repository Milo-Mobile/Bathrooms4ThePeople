package com.milomobile.bathrooms4tp.presentation.create_bathroom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.presentation.base.base_screen.BaseScreen
import com.milomobile.bathrooms4tp.presentation.base.mapErrorHandling
import com.milomobile.bathrooms4tp.presentation.create_bathroom.components.BathroomDigitField
import com.milomobile.bathrooms4tp.presentation.create_bathroom.components.BathroomField
import com.milomobile.bathrooms4tp.presentation.create_bathroom.components.BathroomTextField
import com.milomobile.bathrooms4tp.util.baseLog
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateBathroom(
    navController: NavController,
    viewModel: CreateBathroomViewModel = koinViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    val focusRequesterStreet = remember { FocusRequester() }
    val focusRequesterCity = remember { FocusRequester() }
    val focusRequesterState = remember { FocusRequester() }
    val focusRequesterZipcode = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    //TODO: BathroomCreation alternate UI
    // Step by step to fit screen
    //      Can use AnimatedContent with enum state

    BaseScreen(
        loadingTrigger = state.loading,
        errorHandling = state.uiError?.mapErrorHandling(context) {
            viewModel.onClearUIError()
        },
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Submit A\nBathroom 4\nThe People",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 36.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                BathroomField(
                    modifier = Modifier.focusRequester(focusRequesterStreet),
                    asRow = false,
                    label = "Street",
                    field = {
                        BathroomTextField(
                            value = state.newBathroom.address.street,
                            onValueChange = viewModel::onStreetUpdated,
                            onValueSubmit = {
                                focusRequesterCity.requestFocus()
                            }
                        )
                    }
                )

                state.fieldErrors?.contains(Bathroom.MissingCriticalBathroomData.InvalidStreet)?.let {
                    if (it) {
                        Text(
                            text = "Invalid Street",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BathroomField(
                    modifier = Modifier.focusRequester(focusRequesterCity),
                    asRow = false,
                    label = "City",
                    field = {
                        BathroomTextField(
                            value = state.newBathroom.address.city,
                            onValueChange = viewModel::onCityUpdated,
                            onValueSubmit = {
                                focusRequesterState.requestFocus()
                            }
                        )
                    }
                )
                state.fieldErrors?.contains(Bathroom.MissingCriticalBathroomData.InvalidCity)?.let {
                    if (it) {
                        Text(
                            text = "Invalid City",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BathroomField(
                    modifier = Modifier.focusRequester(focusRequesterState),
                    asRow = false,
                    label = "State",
                    field = {
                        BathroomTextField(
                            value = state.newBathroom.address.state,
                            onValueChange = viewModel::onStateUpdated,
                            onValueSubmit = {
                                focusRequesterZipcode.requestFocus()
                            }
                        )
                    }
                )
                state.fieldErrors?.contains(Bathroom.MissingCriticalBathroomData.InvalidState)?.let {
                    if (it) {
                        Text(
                            text = "Invalid State",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BathroomField(
                    modifier = Modifier.focusRequester(focusRequesterZipcode),
                    asRow = false,
                    label = "Zipcode",
                    field = {
                        BathroomDigitField(
                            value = state.newBathroom.address.zipcode,
                            onValueChange = viewModel::onZipcodeUpdated,
                            onValueSubmit = {
                                focusRequesterZipcode.freeFocus()
                                keyboardController?.hide()
                                viewModel.onFieldsEntered()
                            }
                        )
                    }
                )
                state.fieldErrors?.contains(Bathroom.MissingCriticalBathroomData.InvalidZipcode)?.let {
                    if (it) {
                        Text(
                            text = "Invalid Zipcode",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.bathroomSubmittedToDatabase) {
                BathroomSubmittedAlert {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun BathroomSubmittedAlert(
    onDismissAlert: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.4f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {},
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(Color.DarkGray)
                .clickable {
                    onDismissAlert()
                }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Congrats", color = Color.White, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Your bathroom submission was recorded!",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}