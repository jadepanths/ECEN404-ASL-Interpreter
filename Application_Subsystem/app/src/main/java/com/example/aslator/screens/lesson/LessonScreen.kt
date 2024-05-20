package com.example.aslator.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aslator.BottomNavItem
import com.example.aslator.MainViewModel
import com.example.aslator.R
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.shared.utils.StatTracker

@Composable
fun LessonsScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    uploadManager : NetUpload,
    userStats : StatTracker
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryBright))
            .verticalScroll(rememberScrollState())
            //.wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Lessons Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align( Alignment.CenterHorizontally ),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        LessonDisplay( 1, false, navController, BottomNavItem.Lesson01.screen_route, userStats )
        LessonDisplay( 2, false, navController, BottomNavItem.Lesson02.screen_route, userStats )
        LessonDisplay( 3, false, navController, BottomNavItem.Lesson03.screen_route, userStats )
        LessonDisplay( 4, false, navController, BottomNavItem.Lesson04.screen_route, userStats )
        LessonDisplay( 5, false, navController, BottomNavItem.Lesson05.screen_route, userStats )
        LessonDisplay( 6, false, navController, BottomNavItem.Lesson06.screen_route, userStats )
        LessonDisplay( 7, false, navController, BottomNavItem.Lesson07.screen_route, userStats )
        LessonDisplay( 8, false, navController, BottomNavItem.Lesson08.screen_route, userStats )
        LessonDisplay( 9, false, navController, BottomNavItem.Lesson09.screen_route, userStats )
        LessonDisplay( 10, true, navController, BottomNavItem.Lesson10.screen_route, userStats )
        Spacer(
            modifier = Modifier.height( 75.dp )
        )
    }
}

@Composable
fun LessonDisplay(
    lessonNumber: Int,
    isEnd: Boolean,
    navController: NavHostController,
    screenRoute: String,
    userStats : StatTracker
) {

    val lessonName: String

    when ( lessonNumber ) {
        1 -> {
            lessonName = stringResource(R.string.lesson01)
        }
        2 -> {
            lessonName = stringResource(R.string.lesson02)
        }
        3 -> {
            lessonName = stringResource(R.string.lesson03)
        }
        4 -> {
            lessonName = stringResource(R.string.lesson04)
        }
        5 -> {
            lessonName = stringResource(R.string.lesson05)
        }
        6 -> {
            lessonName = stringResource(R.string.lesson06)
        }
        7 -> {
            lessonName = stringResource(R.string.lesson07)
        }
        8 -> {
            lessonName = stringResource(R.string.lesson08)
        }
        9 -> {
            lessonName = stringResource(R.string.lesson09)
        }
        10 -> {
            lessonName = stringResource(R.string.lesson10)
        }
        else -> {
            lessonName = "Lesson Not Found!"
        }
    }

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.colorPrimaryBright))
            .fillMaxWidth()
    ) {
        Divider(
            thickness = 2.dp,
            color = Color.Black,
        )
        Text(
            text = stringResource(R.string.lesson_text) + lessonNumber,
            color = Color.Black,
            fontSize = 20.sp,
        )
        Text(
            text = lessonName,
            color = Color.Black,
            fontSize = 20.sp,
        )
        Spacer(
            modifier = Modifier.height( 80.dp )
        )

        println(userStats.getLessons())
        if (userStats.getLessons() < lessonNumber) {
            Button(
                modifier = Modifier
                    .align(Alignment.End),
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Gray,
                    contentColor = Color.Black)
            ) {
                Text(stringResource(id = R.string.lesson_unavailable_button))
            }
        }
        else {
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    navController.navigate(screenRoute)
                },
            ) {
                Text(stringResource(id = R.string.lesson_button))
            }
        }
        if ( isEnd ) {
            Divider(
                thickness = 2.dp,
                color = Color.Black,
            )
        }
    }
}