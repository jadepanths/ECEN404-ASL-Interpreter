package com.example.aslator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.google.android.material.snackbar.Snackbar
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aslator.screens.lesson.*
import com.example.aslator.screens.profile.ProfileScreen
import com.example.aslator.screens.translator.*
import com.example.aslator.screens.translator.PlaybackScreen
import com.example.aslator.screens.translator.RecordingScreen
import com.example.aslator.shared.composables.PermissionsHandler
import com.example.aslator.shared.utils.FileManager
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.shared.utils.StatTracker

class MainActivity : ComponentActivity() {


    private val fileManager = FileManager(this)

    private val permissionsHandler = PermissionsHandler()

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecordingViewModel::class.java)) {
                return RecordingViewModel(fileManager, permissionsHandler) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val mainViewModel: MainViewModel by viewModels()

    private val uploadManager = NetUpload()

    private val userStats = StatTracker()

    override fun onCreate(savedInstanceState: Bundle?) {

        mainViewModel.setContext( this )
        mainViewModel.setUploadManagerHere( uploadManager )
        mainViewModel.setStatTracker( userStats )
        uploadManager.setStatTracker( userStats )

        super.onCreate( savedInstanceState )
        setContent {
            ApplicationView()
        }
    }

    private fun showMessage(message: Int) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    @Composable
    private fun ApplicationView() {
        val navController = rememberNavController()
        mainViewModel.setNavHost( navController )

        MaterialTheme {
            Scaffold( bottomBar = { BottomNavigation( navController ) } ) {

                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Profile.screen_route
                ) {
                    composable( BottomNavItem.Profile.screen_route ) { ProfileScreen( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lessons.screen_route ) { LessonsScreen( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Video.screen_route ) { RecordingScreen( navController, viewModelFactory, mainViewModel ) { showMessage(it) } }
                    composable( BottomNavItem.Success.screen_route ) { SuccessScreen( navController, mainViewModel, uploadManager ) }
                    composable( BottomNavItem.UploadFailure.screen_route ) { UploadFailureScreen( navController, mainViewModel, uploadManager ) }
                    composable( BottomNavItem.TranslationFailure.screen_route ) { TranslationFailureScreen( navController, mainViewModel, uploadManager ) }
                    composable( BottomNavItem.Upload.screen_route ) { UploadScreen( navController, mainViewModel, uploadManager ) }
                    composable( BottomNavItem.Preview.screen_route ) { PreviewScreen(filePath = BottomNavItem.Preview.getFilePath(it.arguments) ) }
                    composable( BottomNavItem.Playback.screen_route ) {
                        val filePath = BottomNavItem.Playback.getFilePath(it.arguments)
                        PlaybackScreen(
                            filePath = filePath,
                            navHostController = navController,
                            uploadManager = uploadManager,
                            viewModel = mainViewModel,
                            userStats = userStats
                        )
                    }
                    composable( BottomNavItem.Lesson01.screen_route ) { LessonMat01( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson02.screen_route ) { LessonMat02( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson03.screen_route ) { LessonMat03( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson04.screen_route ) { LessonMat04( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson05.screen_route ) { LessonMat05( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson06.screen_route ) { LessonMat06( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson07.screen_route ) { LessonMat07( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson08.screen_route ) { LessonMat08( navController, mainViewModel, uploadManager, userStats) }
                    composable( BottomNavItem.Lesson09.screen_route ) { LessonMat09( navController, mainViewModel, uploadManager, userStats ) }
                    composable( BottomNavItem.Lesson10.screen_route ) { LessonMat10( navController, mainViewModel, uploadManager, userStats ) }
                }

                BackHandler {
                    navController.popBackStack()
                }
            }
        }
    }
}

fun NavHostController.navigateTo(route: String) = navigate(route) {
    popUpTo(route)
    launchSingleTop = true
}

@Composable
fun BottomNavigation( navController: NavController ) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Video,
        BottomNavItem.Lessons
    )
    //Initialize each tab of the app from the BottomNavItem class which specifies
    //title, icon, etc.

    BottomNavigation(
        backgroundColor = colorResource( id = R.color.gray ),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            // Constructs each tab from the earlier BottomNavItem class in the app using parameters
            // defined in the class
            BottomNavigationItem(
                icon = { Icon( painterResource( id = item.icon ), contentDescription = item.title ) },
                label = { Text( text = item.title,
                    fontSize = 9.sp ) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy( 0.4f ),
                // Allows a lighter shade of black to be used to provide visual confirmation
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    //When an icon on the bottom navigation bar is tapped, the following code
                    //allows the application to use the navigation controller to change the view
                    //for the user
                    navController.navigate( item.screen_route ) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo( screen_route ) {
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