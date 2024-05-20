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
fun LessonMat09(
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
            text = "As we approach the end of these lessons, we'll begin talking about a topic " +
                    "that is slightly more involved but nonetheless a common point in " +
                    "conversations. In this lesson, we will go over how to sign some " +
                    "popular sports from around the world such as American football, basketball, " +
                    "baseball, and soccer.",
            modifier = Modifier.padding( 10.dp )
        )
        Text(
            text = "A question that can be asked in any friendly conversation would be " +
                    "\"What is your favorite sport\" as well as \"What is your favorite " +
                    "team\". Take a look at the below clips which show the signs for " +
                    "baseball, soccer, football, and basketball respectively and try to see " +
                    "how to incorporate them into your own sentences using words and context " +
                    "clues you've picked up from the previous lessons. ",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/baseball_soccer.gif", 200.dp)
        Spacer( modifier = Modifier.height( 10.dp ) )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/football_basketball.gif", 200.dp)
        Text(
            text = "Another thing to know how to ask would be \"Who won\". Check out the " +
                    "following video for how to sign this. Now that you've started putting a " +
                    "lot of vocabulary under your belt, you'll soon start picking up on other " +
                    "context clues when communicating with ASL.",
                    modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/who_won.gif", 200.dp)
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 10) {
                    userStats.setLessons(10)
                    uploadManager.lessonSet("10")
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