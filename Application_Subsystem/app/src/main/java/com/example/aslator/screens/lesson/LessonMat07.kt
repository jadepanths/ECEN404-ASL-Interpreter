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
fun LessonMat07(
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
            text = "An easy way to keep a conversation going is to talk about the weather. In " +
                    "this lesson, we're going to learn the signs for a few different types " +
                    "of weather and how to ask, \"How is the weather\". For starters, lets take " +
                    "a look at that first statement and how to sign it. The below video shows " +
                    "the two signs, 'Weather', 'What', in that order which is how to ask how " +
                    "the weather is in ASL.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/weather_what.gif", 250.dp)
        Text(
            text = "The following clips will show you how to sign a few different types of " +
                    "weather. As always, this is not a comprehensive list so once you get " +
                    "comfortable with these, feel free to go look up more! Certain types of " +
                    "weather may not have a sign associated with them, so always feel free " +
                    "to spell them out as well. (The signs in the following clips are as " +
                    "follows: warm, cold, sunny, rain)",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF( stringResource(id = R.string.ipaddress) + "/gifs/weather_types_1.gif", 200.dp )
        Spacer( modifier = Modifier.height( 10.dp ) )
        showGIF( stringResource(id = R.string.ipaddress) + "/gifs/weather_types_2.gif", 200.dp )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 8) {
                    userStats.setLessons(8)
                    uploadManager.lessonSet("8")
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