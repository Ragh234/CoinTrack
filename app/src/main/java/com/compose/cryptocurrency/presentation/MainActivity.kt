package com.compose.cryptocurrency.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.compose.cryptocurrency.R
import com.compose.cryptocurrency.presentation.navigation.CoinBottomNav
import com.compose.cryptocurrency.presentation.navigation.CoinBottomNavItem
import com.compose.cryptocurrency.presentation.navigation.NavigationGraph
import com.compose.cryptocurrency.presentation.ui.theme.CryptocurrencyAppYTTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptocurrencyAppYTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DashboardScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun DashboardScreen() {
    val navController = rememberNavController()

    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (navBackStackEntry?.destination?.route) {
        CoinBottomNavItem.Market.route,
        CoinBottomNavItem.Watchlist.route,
        CoinBottomNavItem.Portfolio.route -> {
            topBarState.value = true
            bottomBarState.value = true
        }

        Screen.CoinDetailScreen.route + "/{coinId}" -> {
            topBarState.value = true
            bottomBarState.value = false
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CoinTopAppBar(navController = navController, topBarState, scrollBehavior)
        },
        bottomBar = {
            CoinBottomNav(
                navController = navController,
                bottomBarState
            )
        }) { innerPadding ->

        NavigationGraph(
            navController = navController,
            Modifier.padding(innerPadding),
            bottomBarState
        )
    }
}


@ExperimentalMaterial3Api
@Composable
fun CoinTopAppBar(
    navController: NavController, topAppBarState: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    /*val scrollBehavior =  TopAppBarDefaults.exitUntilCollapsedScrollBehavior()*/

    val headerTitle: String = when (navBackStackEntry?.destination?.route) {
        Screen.CoinDetailScreen.route + "/{coinId}" -> {
            "Coin Details"
        }

        else -> {
            getString(context, R.string.app_name)
        }
    }

    AnimatedVisibility(visible = topAppBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopAppBar(
                title = {
                    Text(
                        text = headerTitle,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colors.primary),
                scrollBehavior = scrollBehavior
            )
        })


}
