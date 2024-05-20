package com.example.aslator.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aslator.MainViewModel
import com.example.aslator.R
import androidx.navigation.NavHostController
import com.example.aslator.BottomNavItem
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.shared.utils.StatTracker

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel : MainViewModel,
    uploadManager : NetUpload,
    userStats : StatTracker
) {

    if (!viewModel.userIsAuthenticated) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorPrimaryBright))
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val title = if (viewModel.appJustLaunched) {
                    stringResource(id = R.string.initial_title)
                } else {
                    stringResource(id = R.string.logged_out_title)
                }
            Title(
                text = title
            )

            val buttonText: String = stringResource(id = R.string.log_in_button)
            val onClickAction: () -> Unit =
                {
                    viewModel.login()
                }
            Spacer(modifier = Modifier.height(200.dp))
            LogButton(
                text = buttonText,
                onClick = onClickAction,
            )
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(colorResource(R.color.colorPrimaryBright))
                .wrapContentSize(Alignment.Center)
        ) {
            Title(
                text = "Welcome back!"
            )
            UserInfoRow(
                label = stringResource(id = R.string.name_label),
                value = viewModel.user.name
            )
            UserInfoRow(
                label = stringResource(id = R.string.email_label),
                value = viewModel.user.email
            )
            Divider(
                thickness = 2.dp,
                color = Color.Black,
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 200.dp)
                .fillMaxWidth()
                .height(400.dp)
                .background(colorResource(R.color.colorPrimaryBright))
        ) {
            Row {
                Text(
                    text = "Words Translated:",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    ),
                )
                Spacer(
                    modifier = Modifier.width( 10.dp )
                )
                Text(
                    text = userStats.getWords().toString(),
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                    )
                )
            }

            Row {
                Text(
                    text = "Current Lesson:",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    ),
                )
                Spacer(
                    modifier = Modifier.width( 10.dp )
                )
                Text(
                    text = userStats.getLessons().toString(),
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                    )
                )
            }

            Spacer(
                modifier = Modifier.height( 100.dp )
            )

            Button(
                onClick = {
                    userStats.setWords(0)
                    uploadManager.wordCountSet("0")
                    navController.navigate( BottomNavItem.Profile.screen_route )
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text(
                    text = "Reset Word Count",
                    fontSize = 20.sp
                )
            }

            Spacer(
                modifier = Modifier.height( 25.dp )
            )

            Button(
                onClick = {
                    userStats.setLessons(1)
                    uploadManager.lessonSet("1")
                    navController.navigate( BottomNavItem.Profile.screen_route )
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text(
                    text = "Reset Lesson Progress",
                    fontSize = 20.sp
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 600.dp)
                .padding(bottom = 25.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(colorResource(id = R.color.colorPrimaryBright)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .width(200.dp)
                    .height(50.dp),
            ) {
                Text(
                    text = "Log Out",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun Title( text: String ) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        ),
    )
}

@Composable
fun LogButton(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(
                text = text,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun UserInfoRow( label: String, value: String ) {
    Row {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            ),
        )
        Spacer(
            modifier = Modifier.width( 10.dp )
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 20.sp,
            )
        )
    }
}