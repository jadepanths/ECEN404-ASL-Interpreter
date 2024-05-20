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
fun LessonMat03(
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
            text = "An important aspect of basic communication is to know how to get where " +
                    "you're going. In a situation where you get lost, it will be highly " +
                    "beneficial to learn how to sign a few basic things that will get you " +
                    "pointed in the right direction.",
            modifier = Modifier.padding( 10.dp )
        )
        Text(
            text = "The most basic question to ask when lost is \"How do I get to <blank>\", " +
                    "with <blank> being the location you are trying to get to. See the video " +
                    "below on how to sign \"how\" and \"go\". Using these signs in this order " +
                    "after signing the name of the place will let others know that you are " +
                    "asking for directions to said place.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/how_go.gif", 200.dp )
        Text(
            text = "Some other basic terms that would be helpful are how to sign the words " +
                    "\"left\" and \"right\". The majority of directions given will be a " +
                    "combination of these signs along with other words to communicate " +
                    "effectively. The following clips show \"right\" and \"left\", respectively.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/right_left.gif", 200.dp )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 4) {
                    userStats.setLessons(4)
                    uploadManager.lessonSet("4")
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