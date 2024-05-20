package com.example.aslator.screens.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun LessonMat01(
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
            text = "Hello and welcome to the ASL lesson plan! The goal is to teach" +
                    " ASL in small bite-sized pieces to help you grasp the basics" +
                    " efficiently so that you can begin to sign to others in no time." +
                    "\n\n" +
                    "On that note, the first step to learning any language is" +
                    " the alphabet. Since ASL is based on converting the English" +
                    " language to gestures, the same alphabet is used. However," +
                    " instead of writing each letter, a different hand sign is used." +
                    " Please refer to the chart provided below for each hand sign.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/img/asl_alphabet.png", 250.dp)
        Text(
            text = "Spend a few minutes here to study the alphabet and practice signing it." +
                    "Once you feel comfortable, feel free to move on to the next lesson." +
                    "Try your best to get a good grasp of the alphabet since names, like " +
                    "John or Jane, are communicated using the alphabet rather than a \"word\".",
            modifier = Modifier.padding( 10.dp )
        )
        Spacer( modifier = Modifier.height( 75.dp ) )
        Button(
            onClick = {
                if (userStats.getLessons() < 2) {
                    userStats.setLessons(2)
                    uploadManager.lessonSet("2")
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
        Spacer( modifier = Modifier.height( 75.dp ) )
    }
}