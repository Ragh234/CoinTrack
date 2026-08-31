package com.compose.cryptocurrency.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.compose.cryptocurrency.R
import com.compose.cryptocurrency.presentation.Screen
import com.compose.cryptocurrency.presentation.coindetail.CoinDetailScreen
import com.compose.cryptocurrency.presentation.coinlist.CoinListScreen
import com.compose.cryptocurrency.presentation.portfolio.PortfolioScreen
import com.compose.cryptocurrency.presentation.watchlist.WatchlistScreen

@Composable
fun CoinBottomNav(
    navController: NavController,
    bottomBarState: MutableState<Boolean>
) {

    val items = listOf(
        CoinBottomNavItem.Market,
        CoinBottomNavItem.Watchlist,
        CoinBottomNavItem.Portfolio
    )

    AnimatedVisibility(visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            BottomNavigation(
                backgroundColor = /*colorResource(id = R.color.primary)*/androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = Color.Black
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->

                    //
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 9.sp
                            )
                        },
                        selectedContentColor = colorResource(id = R.color.white),
                        unselectedContentColor = colorResource(id = R.color.dark_gray).copy(0.4f),
                        alwaysShowLabel = true,
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {

                                navController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

            }
        }
    )

}


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    bottomBarState: MutableState<Boolean>
) {
    NavHost(
        navController,
        startDestination = CoinBottomNavItem.Market.route,
        modifier = modifier
    ) {
        composable(CoinBottomNavItem.Market.route) {
            LaunchedEffect(Unit) {
                bottomBarState.value = true
            }
            CoinListScreen(navController = navController)
        }

        composable(CoinBottomNavItem.Watchlist.route) {
            LaunchedEffect(Unit) {
                bottomBarState.value = true
            }
            WatchlistScreen(navController = navController)
        }

        composable(CoinBottomNavItem.Portfolio.route) {
            LaunchedEffect(Unit) {
                bottomBarState.value = true
            }
            PortfolioScreen()
        }

        composable(
            route = Screen.CoinDetailScreen.route + "/{coinId}"
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            CoinDetailScreen()
        }
    }
}

sealed class CoinBottomNavItem(val route: String, val icon: Int, val title: String) {
    object Market : CoinBottomNavItem("market", R.drawable.ic_baseline_home_24, "Market")
    object Watchlist :
        CoinBottomNavItem("watchlist", R.drawable.ic_baseline_notifications_24, "Watchlist")

    object Portfolio : CoinBottomNavItem("portfolio", R.drawable.ic_wallet, "Portfolio")
}
