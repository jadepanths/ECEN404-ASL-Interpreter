package com.example.aslator.screens.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
fun LessonMat05(
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
            text = "One thing that everyone needs to know is how to find a bathroom. There will " +
                    "always be a situation for everyone when they need to go. In this lesson, " +
                    "we will learn how to sign the crucial question, \"Where is the bathroom\".",
            modifier = Modifier.padding( 10.dp )
        )
        Text(
            text = "Looking at the sign video below, you will see the following signs: " +
                    "\"Bathroom\" and \"where\". This goes back to what was mentioned back " +
                    "in lesson 2 where ASL operates off of a different grammatical structure. " +
                    "Because of its grammar, ASL uses fewer words to communicate the same " +
                    "message, which helps maintain the flow of conversation since the process " +
                    "is more involved than normal speech.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/where_bathroom.gif", 200.dp)
        Text(
            text = "One thing to note again is that facial expressions play a very large role " +
                    "in efficient ASL communication. Simply signing the word \"bathroom\" " +
                    "with a questioning look conveys the same meaning as physically signing " +
                    "both \"bathroom\" and \"where\".",
            modifier = Modifier.padding( 10.dp )
        )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 6) {
                    userStats.setLessons(6)
                    uploadManager.lessonSet("6")
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