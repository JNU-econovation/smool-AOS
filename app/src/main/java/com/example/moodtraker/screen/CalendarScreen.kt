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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
//import com.example.moodtraker.CalendarRequest
import com.example.moodtraker.LoginActivity
import com.example.moodtraker.MyApi
import com.example.moodtraker.RetrofitInstance
import com.example.moodtraker.UserPK
//import com.example.moodtraker.UserPkSet
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import com.example.moodtraker.user
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun CalendarScreen() {


    LogNav(navHostController = rememberNavController())

}


val retrofit = RetrofitInstance.getInstance()
val myApi = RetrofitInstance.getInstance().create(MyApi::class.java)



@Composable
fun LogNav(navHostController: NavHostController) {

    Log.d("캘린더 반복", "LogNav 호출")

    NavHost(navController = navHostController, startDestination = "CalendarScreen") {
        composable("CalendarScreen") {
            Log.d("캘린더 반복", "calendar 화면 전환")
            CalendarContent(navHostController)

        }
//        composable("LogScreen") { navBackStackEntry ->
//            LogScaffold(resultTime = navBackStackEntry.arguments?.getString("resultTime"))
//
//        }
        composable(
            "LogScreen/{resultTime}/{resultDay}",
            arguments = listOf(
                navArgument("resultTime") { type = NavType.StringType },
                navArgument("resultDay") { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            // 여기서 필요한 데이터를 추출하여 LogScreen에 전달
            val resultTime = navBackStackEntry.arguments?.getString("resultTime")
            val resultDay = navBackStackEntry.arguments?.getInt("resultDay")
            LogScaffold(resultTime = resultTime, resultDay = resultDay)
            Log.d("캘린더 반복", "log 화면 전환")
        }

    }
}

@Composable
fun CalendarContent(navHostController: NavHostController) {

    Log.d("캘린더 반복", "CalendarContent 호출")

    val calendarInstance = Calendar.getInstance()
    val time = remember {
        mutableStateOf(calendarInstance)
    }

    val todayInstance = Calendar.getInstance()
    val today = remember {
        mutableStateOf(todayInstance)
    }


//    val resultTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(time.value.time)
//
//    var recentDay by remember { mutableStateOf("") }



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
//                CalendarHeader(time) { resultTime ->
//                    //SomeOtherComposable(resultTime)
//                }
//                //CalendarHeaderBtn(time)
//                Spacer(modifier = Modifier.height(16.dp))
//                CalendarDayName()
//                CalendarDayList(time, navHostController)


                CalendarContents(
                    date = time,
                    today = today,
                    navController = navHostController
                )
            }

        }

    }
}

@Composable
fun CalendarContents(
    date: MutableState<Calendar>,
    today: MutableState<Calendar>,
    navController: NavHostController,
) {

    Log.d("캘린더 반복", "calendarContents 호출")


    val coroutineScope = rememberCoroutineScope()

    val loginActivityInstance = LoginActivity()
    val userPk = user.userPk
    Log.d("캘린더 로그인 userPk", "userPk: $userPk")


    // 데이터 세팅
    var resultTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.value.time)
    val todayDate = SimpleDateFormat("dd", Locale.KOREA).format(today.value.time).toInt()
    val todayTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(today.value.time)

    var resultLog = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(date.value.time)

    var existList = remember { mutableStateListOf<Boolean>() }

    var isExistListLoaded = remember { mutableStateOf(false) }


    LaunchedEffect(resultLog) {

        //existList = mutableStateListOf<Boolean>()

        existList.clear()
        isExistListLoaded.value = false


        Log.d("캘린더 업데이트", "resultLog: $resultLog")
        var dayMax = date.value.getActualMaximum(Calendar.DAY_OF_MONTH)
        var tmp = "$resultLog-$dayMax"
        Log.d("캘린더 tmp", "$tmp")
        coroutineScope.launch {
            Log.d("캘린더 클릭", "")
            val calendarRequest =
                UserPK(userPk = userPk)
            Log.d("캘린더 request", "$calendarRequest")

            try {

                Log.d("캘린더 1111", "1111")   // 백엔드가 코드 바꿔줘야 함. response에서 형태 막힘
                val response = myApi.calendar(calendarRequest.userPk, tmp)
                Log.d("캘린더 response.body", "${response.body()}")
                Log.d("캘린더 응답 코드", "HTTP status code: ${response.code()}")
                Log.d("캘린더 응답 성공 여부", "Is successful: ${response.isSuccessful}")


                if(response.isSuccessful) {

                    Log.d("캘린더 response", "response is successful")

                    val json = response.body()
                    Log.d("캘린더 json", "$json")
                    val status = json?.status
                    val message = json?.message
                    val data = json?.data
                    val existDates = data?.existDates

                    Log.d("캘린더 데이터", "$status $message $data $existDates")

                    existDates?.forEach { dateExist ->
                        val localDate = dateExist.localDate
                        val exist = dateExist.exist

                        existList.add(exist)

                        Log.d("캘린더 상세 데이터", "$localDate $exist")
                    }

                    Log.d("캘린더 existList", "existList: ${existList.joinToString()}")
                    Log.d("캘린더 existList", "existList: ${existList.size}")

                    isExistListLoaded.value = true

                }

                else {
                    val errorMessage = response.errorBody()?.string()
                    Log.d("캘린더 오류 메시지", "Error message: $errorMessage")
                    val jsonObject = JSONObject(errorMessage)
                    val status = jsonObject.getInt("status")
                    val message = jsonObject.getString("message")
                    Log.d("캘린더 오류 상태 코드", "Error status: $status")
                    Log.d("캘린더 오류 메시지", "Error message: $message")
                }


            }catch (e:Exception){
                Log.d("캘린더 오류", "$e")
            }


        }

    }

    Log.d("캘린더", "resultTime: $resultTime")




//---------

//    val yearAndMonth = extractYearAndMonth(resultTime.toString())
//    val year = yearAndMonth[0]
//    val month = yearAndMonth[1]
//    //val day = resultDay
//
//
//    val calendarInstanceLast = remember {
//        Calendar.getInstance().apply {
//            set(Calendar.YEAR, year)
//            set(Calendar.MONTH, month - 1) // Calendar.MONTH는 0부터 시작하므로 -1 해줍니다.
//            val lastDayOfMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
//            set(Calendar.DAY_OF_MONTH, lastDayOfMonth)
//            //set(Calendar.DAY_OF_MONTH, day!!)
//        }
//    }
//
//    // 날짜 포맷
//    var resultTimeLast by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(calendarInstanceLast.time)) }
//    Log.d("캘린더", "해당 월 마지막 일: $resultTimeLast")
//
//    fun updateResultTime() {
//        resultTimeLast = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(calendarInstanceLast.time)
//        Log.d("ResultTimeLast", "Updated resultTimeLast: $resultTimeLast")
//    }



    // 캘린더 헤더


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
                Log.d("캘린더 updatecalendar", "1 start")
                //updateCalendar()
                resultLog = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(date.value.time)
                Log.d("캘린더 updatecalendar", "1 end")

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
                Log.d("캘린더 updatecalendar", "2 start")
                //updateCalendar()
                resultLog = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(date.value.time)
                Log.d("캘린더 updatecalendar", "2 end")

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


    Spacer(modifier = Modifier.height(16.dp))


    // 요일 이름

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

    // 캘린더 날짜


    date.value.set(Calendar.DAY_OF_MONTH, 1)

    val monthDayMax = date.value.getActualMaximum(Calendar.DAY_OF_MONTH)    // 현재 달의 max
    val monthFirstDay = date.value.get(Calendar.DAY_OF_WEEK) - 1    // 1일이 무슨 요일부터인지
    val monthWeeksCount = (monthDayMax + monthFirstDay + 6) / 7     // 현재 달의 week 수

    var exist by remember { mutableStateOf(false) }

    Log.d("monthDayMax", monthDayMax.toString())
    Log.d("monthFirstDay", monthFirstDay.toString())
    Log.d("monthWeeksCount", monthWeeksCount.toString())

    Log.d("캘린더 updatecalendar", "본문 start")
    //updateCalendar()
    resultLog = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(date.value.time)
    Log.d("캘린더 updatecalendar", "본문 end")

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
                                //.padding(10.dp)
                                .clickable {
                                    // 로그 페이지로 이동
                                    navController.navigate("LogScreen/$resultTime/$resultDay") {
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .background(
                                            shape = CircleShape,
                                            color = if (todayDate == resultDay && todayTime == resultTime) Color(
                                                0xFF5A5788
                                            ) else Color.Transparent
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = resultDay.toString(),
                                        fontSize = 20.sp,
                                        color = Color.White,
                                    )
                                }


                                if (isExistListLoaded.value && (resultDay - 1) < existList.size) {
                                    if (existList[resultDay-1] == true) {
                                        Log.d("캘린더 circle 아이콘", "existList[resultDay]: ${existList[resultDay-1]}")
                                        Icon(Icons.Default.Circle, contentDescription = "circle", tint = Color.White, modifier = Modifier.size(10.dp))
                                    }
                                } else {
                                    Log.d("캘린더 existList 오류","existList: ${existList.joinToString()}")
                                    Log.d("캘린더 existList 오류","resultDay: $resultDay")
                                    Log.d("캘린더 existList 오류","existList.size: ${existList.size}")
                                }



                            }

                        }

                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
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





//@Composable
//fun CalendarHeader(date: MutableState<Calendar>, onUpdate: (resultTime: String) -> Unit){
//    val resultTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.value.time)
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 30.dp, end = 30.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//
//    ) {
//        Button(
//            onClick = {
//                val newDate = Calendar.getInstance()
//                newDate.time = date.value.time
//                newDate.add(Calendar.MONTH, -1)
//                date.value = newDate
//                onUpdate(resultTime)
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Transparent
//            )
//
//        ) {
//            Text(
//                text = "<",
//                fontSize = 30.sp
//            )
//        }
//
//        Text(
//            text = resultTime,
//            fontSize = 28.sp,
//            color = Color.White
//        )
//
//
//        Button(
//            onClick = {
//                val newDate = Calendar.getInstance()
//                newDate.time = date.value.time
//                newDate.add(Calendar.MONTH, +1)
//                date.value = newDate
//                onUpdate(resultTime)
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


//
//@Composable
//fun CalendarDayName() {
//
//    val nameList = listOf("일", "월", "화", "수", "목", "금", "토")
//
//    Row() {
//        nameList.forEach {
//            Box(
//                modifier = Modifier.weight(1f),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = it,
//                    color = Color.White,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.ExtraBold
//                )
//            }
//        }
//    }
//}

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

//@Composable
//fun CalendarDayList(
//    date: MutableState<Calendar>,
//    navHostController: NavHostController
//) {
//
//    date.value.set(Calendar.DAY_OF_MONTH, 1)
//
//    val monthDayMax = date.value.getActualMaximum(Calendar.DAY_OF_MONTH)    // 현재 달의 max
//    val monthFirstDay = date.value.get(Calendar.DAY_OF_WEEK) - 1    // 1일이 무슨 요일부터인지
//    val monthWeeksCount = (monthDayMax + monthFirstDay + 6) / 7     // 현재 달의 week 수
//
//    var count by remember { mutableStateOf(0) }
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
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(80.dp)
//                                .padding(10.dp)
//                                .clickable {
//                                    // 로그 페이지로 이동
//                                    //resultTime
//                                    //navHostController.navigate("LogScreen/$resultTime")
//                                    //navHostController.navigate("LogScreen")
//                                },
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = resultDay.toString(),
//                                fontSize = 20.sp,
//                                color = Color.White,
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




@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    MoodtrakerTheme {
        CalendarScreen()
    }
}