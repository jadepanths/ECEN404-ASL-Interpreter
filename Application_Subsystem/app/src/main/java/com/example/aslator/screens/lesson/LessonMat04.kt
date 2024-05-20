package com.example.aslator.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aslator.BottomNavItem
import com.example.aslator.MainViewModel
import com.example.aslator.R
import com.example.aslator.shared.composables.showGIF
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.shared.utils.StatTracker

@Composable
fun LessonMat04(
    navController: NavHostController,
    viewModel : MainViewModel,
    uploadManager : NetUpload,
    userStats : StatTracker
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryBright))
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "The next thing we will be learning is more of a polite greeting or farewell " +
                    "that everyone should be familiar with: good morning and good night! " +
                    "This lesson will focus primarily on the two different phrases and assume at " +
                    "this point that you know how to sign your (or other's names) confidently. ",
            modifier = Modifier
                .padding( 10.dp )
        )
        Text(
            text = "Since most people will say \"Good morning\" and \"Good night\" to each other " +
                    "at some point in the day, lets go ahead and learn how to sign them. In the " +
                    "two clips below, the first shows \"Good morning\" while the second " +
                    "shows \"Good night\".",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/good_morning.gif", 200.dp)
        Spacer( modifier = Modifier.height( 10.dp ) )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/good_night.gif", 230.dp)
        Text(
            text = "See if you can find the common sign between the two videos to identify what " +
                    "\"good\" is!",
            modifier = Modifier.padding( 10.dp )
        )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 5) {
                    userStats.setLessons(5)
                    uploadManager.lessonSet("5")
                }
                navController.navigate( BottomNavItem.Lessons.screen_route )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text(
                text = "Finish Lesson",
                fontSize = 20.sp
            )
        }
        Spacer( modifier = Modifier.height( 75.dp ) ) //Used to space from bottom nav bar
    }
}