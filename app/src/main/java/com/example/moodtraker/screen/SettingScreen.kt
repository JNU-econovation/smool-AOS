package com.example.moodtraker.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtraker.R
import com.example.moodtraker.ui.theme.MoodtrakerTheme


@Composable
fun SettingScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF433E76),
                        Color(0xFF9394BB)
                    )
                )
            )
    ) {
        SettingContent()
    }

}

@Composable
fun SettingContent() {

    Column(

        modifier = Modifier
            .fillMaxSize()


    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "설정",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.padding(20.dp)
        )
        Divider(color = Color.White)
        Row(

            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.personback),
                contentDescription = "person"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column() {
                Text(text = "admin", color = Color.White, fontSize = 20.sp)
                Text(text = "내 계정", color = Color.White)
            }

        }
        Divider(color = Color.White)
        Text(text = "알림", color = Color.White, modifier = Modifier.padding(20.dp))
        Divider(color = Color.White)
        Text(text = "테마", color = Color.White, modifier = Modifier.padding(20.dp))
        Divider(color = Color.White)
        Text(text = "공지사항", color = Color.White, modifier = Modifier.padding(20.dp))
        Divider(color = Color.White)
        Text(text = "QnA", color = Color.White, modifier = Modifier.padding(20.dp))
        Divider(color = Color.White)
        Text(text = "정보", color = Color.White, modifier = Modifier.padding(20.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    MoodtrakerTheme {
        SettingScreen()
    }
}