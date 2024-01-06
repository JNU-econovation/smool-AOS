package com.example.moodtraker.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(){

    LogNav(navHostController = rememberNavController())

}

@Composable
fun CalendarContent() {
    val calendarInstance = Calendar.getInstance()
    val time = remember {
        mutableStateOf(calendarInstance)
    }

    Surface(
        //color = MaterialTheme.colorScheme.backgroundv
        modifier = Modifier.fillMaxSize(),
    ) {

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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                CalendarHeader(time)
                //CalendarHeaderBtn(time)
                Spacer(modifier = Modifier.height(16.dp))
                CalendarDayName()
                CalendarDayList(time)
            }

        }

    }
}

//@Composable
//fun CalendarHeader(date: MutableState<Calendar>){
//    val resultTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.value.time)
//
//    Text(
//        text = resultTime,
//        fontSize = 28.sp,
//        color = Color.White
//    )
//}
//
//@Composable
//fun CalendarHeaderBtn(date: MutableState<Calendar>) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 30.dp, end = 30.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//
//    ) {
//        Button(
//            onClick = {
//                val newDate = Calendar.getInstance()
//                newDate.time = date.value.time
//                newDate.add(Calendar.MONTH, -1)
//                date.value = newDate
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Transparent
//            )
//
//            ) {
//            Text(
//                text = "<",
//                fontSize = 30.sp
//            )
//        }
//        Button(
//            onClick = {
//                val newDate = Calendar.getInstance()
//                newDate.time = date.value.time
//                newDate.add(Calendar.MONTH, +1)
//                date.value = newDate
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Transparent
//            )) {
//            Text(
//                text = ">",
//                fontSize = 30.sp
//            )
//        }
//    }
//}

@Composable
fun CalendarHeader(date: MutableState<Calendar>){
    val resultTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.value.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Button(
            onClick = {
                val newDate = Calendar.getInstance()
                newDate.time = date.value.time
                newDate.add(Calendar.MONTH, -1)
                date.value = newDate
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )

        ) {
            Text(
                text = "<",
                fontSize = 30.sp
            )
        }

        Text(
            text = resultTime,
            fontSize = 28.sp,
            color = Color.White
        )


        Button(
            onClick = {
                val newDate = Calendar.getInstance()
                newDate.time = date.value.time
                newDate.add(Calendar.MONTH, +1)
                date.value = newDate
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )) {
            Text(
                text = ">",
                fontSize = 30.sp
            )
        }
    }
}



@Composable
fun CalendarDayName() {

    val nameList = listOf("일", "월", "화", "수", "목", "금", "토")

    Row() {
        nameList.forEach {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

//@Composable
//fun CalendarDayList(date: MutableState<Calendar>) {
//
//    date.value.set(Calendar.DAY_OF_MONTH, 1)
//
//    val monthDayMax = date.value.getActualMaximum(Calendar.DAY_OF_MONTH)    // 현재 달의 max
//    val monthFirstDay = date.value.get(Calendar.DAY_OF_WEEK) - 1    // 1일이 무슨 요일부터인지
//    val monthWeeksCount = (monthDayMax + monthFirstDay + 6) / 7     // 현재 달의 week 수
//
//    Log.d("monthDayMax", monthDayMax.toString())
//    Log.d("monthFirstDay", monthFirstDay.toString())
//    Log.d("monthWeeksCount", monthWeeksCount.toString())
//
//    Column() {
//        repeat(monthWeeksCount) {week ->
//
//            Row() {
//                repeat(7) {day->
//
//                    // 날짜 구하는 공식
//                    val resultDay = week*7 + day - monthFirstDay + 1
//
//                    if (resultDay in 1 .. monthDayMax) {
//
//                        Button(
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(80.dp),
////                                .padding(4.dp),
//                            onClick = {
//                                // 버튼 클릭시 해당 날짜의 로그화면으로 전환
//                            },
////                                colors = ButtonDefaults.buttonColors(
////                                    containerColor = Color.Transparent
////                                )
//                        ) {
//                            Text(
//                                text = resultDay.toString(),
//                                letterSpacing = (-7).sp
////                                fontSize = 16.sp,
//                                //color = Color.White
//                            )
//                        }
//
//                    } else {
//                        Spacer(modifier = Modifier.weight(1f))
//                    }
//                }
//            }
//
//        }
//    }
//
//}

@Composable
fun CalendarDayList(date: MutableState<Calendar>) {

    date.value.set(Calendar.DAY_OF_MONTH, 1)

    val monthDayMax = date.value.getActualMaximum(Calendar.DAY_OF_MONTH)    // 현재 달의 max
    val monthFirstDay = date.value.get(Calendar.DAY_OF_WEEK) - 1    // 1일이 무슨 요일부터인지
    val monthWeeksCount = (monthDayMax + monthFirstDay + 6) / 7     // 현재 달의 week 수

    var count by remember { mutableStateOf(0) }

    Log.d("monthDayMax", monthDayMax.toString())
    Log.d("monthFirstDay", monthFirstDay.toString())
    Log.d("monthWeeksCount", monthWeeksCount.toString())

    Column() {
        repeat(monthWeeksCount) {week ->

            Row() {
                repeat(7) {day->

                    // 날짜 구하는 공식
                    val resultDay = week*7 + day - monthFirstDay + 1

                    if (resultDay in 1 .. monthDayMax) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .padding(10.dp)
                                .clickable {
                                    // 로그 페이지로 이동
                                    //navHostController.navigate("LogScreen/$resultTime")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = resultDay.toString(),
                                fontSize = 20.sp,
                                color = Color.White,
                            )
                        }

                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        }
    }

}


@Composable
fun LogNav(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "CalendarScreen") {
        composable("CalendarScreen") {
            CalendarContent()
        }
        composable("LogScreen/{resultTime}") { navBackStackEntry ->
            //LogScreen(resultTime = navBackStackEntry.arguments?.getString("resultTime"))

        }

    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    MoodtrakerTheme {
        //CalendarScreen()
    }
}