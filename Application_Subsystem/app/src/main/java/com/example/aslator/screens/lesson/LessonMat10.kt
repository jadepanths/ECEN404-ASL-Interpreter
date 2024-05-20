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
fun LessonMat10(
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
            text = "Congratulations, you've made it to the last lesson! There is no better way " +
                    "to end this introductory lesson plan than to cover the topic of Graduation. " +
                    "We will show you some final signs (such as \"graduation\" itself) and " +
                    "introduce you to another resource that can provide a more in-depth " +
                    "curriculum if you would like to understand all of the details and " +
                    "specifics regarding ASL. The clip below shows the word \"graduation\" but " +
                    "be careful, it can be used as both a verb (graduate) or noun (graduation) " +
                    "depending on its location in a sentence.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/graduation.gif", 200.dp)
        Text(
            text = "An example of using this in a sentence would be as seen in the " +
                    "following video: (I graduate college this week). Another important " +
                    "word to learn is degree, such as if you were asking someone if they had " +
                    "a B.S. degree, Ph.D degree, etc. These will be shown below in the second " +
                    "video. As always feel free to combine these words with others you have " +
                    "learned along the way in the lesson plan.",
            modifier = Modifier.padding( 10.dp )
        )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/i_graduate_college.gif", 200.dp)
        Spacer( modifier = Modifier.height( 10.dp ) )
        showGIF(stringResource(id = R.string.ipaddress) + "/gifs/degree.gif", 200.dp)
        Text(
            text = "This completes the ASLator Lesson Plan! I hope you've gained a bare" +
                    "understanding and enough ASL to get you by. If you've enjoyed this course " +
                    "and want to obtain a deeper understanding of ASL, check out some of the " +
                    "online resources available. One of the better and more comprehensive " +
                    "sites is https://www.lifeprint.com",
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "Thank you again and I hope you enjoy using the translator portion of the " +
                    "application!",
            modifier = Modifier.padding(10.dp)
        )
        Spacer( modifier = Modifier.height( 40.dp ) )
        Button(
            onClick = {
                userStats.setLessons(10)
                uploadManager.lessonSet("10")
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