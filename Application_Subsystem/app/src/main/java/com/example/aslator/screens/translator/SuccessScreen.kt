package com.example.aslator.screens.translator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aslator.BottomNavItem
import com.example.aslator.MainViewModel
import com.example.aslator.R
import com.example.aslator.shared.utils.NetUpload

@Composable
internal fun SuccessScreen(
    navController: NavHostController,
    viewModel : MainViewModel,
    uploadManager : NetUpload
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(colorResource(id = R.color.colorPrimaryBright))
    ) {
        Spacer(
            modifier = Modifier.height( 250.dp )
        )
        Text(
            text = "Translation complete!",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align( Alignment.CenterHorizontally ),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Text(
            text = "Your message is:",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align( Alignment.CenterHorizontally ),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Text(
            text = uploadManager.translation.toString(),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align( Alignment.CenterHorizontally ),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(
            modifier = Modifier.height( 60.dp )
        )
        Button(
            modifier = Modifier.align( Alignment.CenterHorizontally ),
            onClick = {
                navController.navigate( BottomNavItem.Video.screen_route )
            },
        ) {
            Text( "Finish" )
        }
    }
}
