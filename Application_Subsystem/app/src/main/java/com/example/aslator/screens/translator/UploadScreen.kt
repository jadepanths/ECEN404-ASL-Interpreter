package com.example.aslator.screens.translator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aslator.MainViewModel
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.R

@Composable
fun UploadScreen(
    navController: NavHostController,
    viewModel : MainViewModel,
    uploadManager: NetUpload
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(colorResource(id = R.color.colorPrimaryBright))
    ) {
        Spacer(
            modifier = Modifier.height(250.dp)
        )
        Text(
            text = "Uploading...",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align( Alignment.CenterHorizontally ),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(
            modifier = Modifier.height(50.dp)
        )
        CircularProgressIndicator(
            modifier = Modifier
                .align( Alignment.CenterHorizontally )
        )
    }
}