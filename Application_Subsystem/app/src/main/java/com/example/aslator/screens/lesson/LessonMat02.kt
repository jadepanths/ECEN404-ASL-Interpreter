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
import androidx.compose.ui.tooling.preview.Preview
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
fun LessonMat02(
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
            text = "In the last lesson, we went over the alphabet and how it is used in ASL. As " +
                    "mentioned, one of the biggest uses of the alphabet in ASL (besides being " +
                    "the alphabet) is to communicate names to one another. In this lesson, we " +
                    "will go over how to introduce yourself by showing you your first words and " +
                    "how to tie in spelling from last lesson.",
            modifier = Modifier.padding( 10.dp )
        )
        Text(
            text = "The most important thing to realize beforehand is that the grammar of ASL " +
                    "is different than that of ordinary English. The goal of this lesson plan is " +
                    "to teach you enough ASL to get by and reduce dependency on the " +
                    "translator feature of this application. However, to completely understand " +
                    "the grammar is beyond the scope of this lesson.",
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "With that being said, lets go ahead and begin practicing how to introduce " +
                    "yourself if your name was John. The most efficient way to do this is to " +
                    "sign \"Hello! I John\". Again, although this is \"improper English\" " +
                    "it is proper ASL and it will look as follows: ",
            modifier = Modifier.padding(10.dp)
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/hello_i_john.gif", 250.dp)
        Text(
            text = "The first sign that looks like a salute is \"hello\", then pointing at one's " +
                    "self is the sign for \"I\" or \"me\", and then the final sequence is the " +
                    "alphabet signing of \"John\".",
            modifier = Modifier.padding( 10.dp )
        )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 3) {
                    userStats.setLessons(3)
                    uploadManager.lessonSet("3")
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