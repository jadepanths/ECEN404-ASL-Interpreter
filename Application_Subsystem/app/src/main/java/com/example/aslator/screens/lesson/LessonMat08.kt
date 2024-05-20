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
fun LessonMat08(
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
            text = "Everybody likes to go out and have a nice meal once in a while. In this " +
                    "lesson, we will go over how to order food at a basic level. Lets take for " +
                    "example ordering a soda from a restaurant. First, you would have to ask for " +
                    "the soda itself. Since a branded item (such as Coca-Cola or Dr. Pepper) " +
                    "will not have its own exclusive sign, feel free to spell it out! In the " +
                    "clip below, I am going to sign \"I want soda\".",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/want_soda.gif", 250.dp)
        Text(
            text = "Again, due to ASL's unique structure the translation from English to ASL is " +
                    "not necessarily linear as the word \"would\" doesn't really exist in ASL. " +
                    "Instead, the statements are simplified with facial expressions conveying " +
                    "the rest of the meaning of a signer's statements. As before, feel free to " +
                    "learn more words on your own to increase your ability to communicate!",
            modifier = Modifier.padding( 10.dp )
        )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 9 ) {
                    userStats.setLessons(9)
                    uploadManager.lessonSet("9")
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