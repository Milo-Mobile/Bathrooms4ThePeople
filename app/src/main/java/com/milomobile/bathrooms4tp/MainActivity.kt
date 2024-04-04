package com.milomobile.bathrooms4tp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.milomobile.bathrooms4tp.presentation.Route
import com.milomobile.bathrooms4tp.presentation.bathroom_list.BathroomList
import com.milomobile.bathrooms4tp.presentation.create_bathroom.CreateBathroom
import com.milomobile.bathrooms4tp.presentation.maps.BathroomMap
import com.milomobile.bathrooms4tp.ui.theme.Bathrooms4ThePeopleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Bathrooms4ThePeopleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun BaseNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Route.ListRoute.path) {
        composable(route = Route.ListRoute.path) {
            BathroomList(navController = navController)
        }
        composable(route = Route.CreateBathroomRoute.path) {
            CreateBathroom(navController = navController)
        }
        composable(route = Route.MapRoute.path) {
            BathroomMap()
        }
    }
}

