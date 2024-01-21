package com.example.moodtraker.screen

import android.util.Log
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtraker.DiaryPost
import com.example.moodtraker.R
import com.example.moodtraker.UserPK
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import com.example.moodtraker.user
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

//@Composable
//fun LogScreen(resultTime: String?) {
//    val scrollState = rememberScrollState()
//
//
//    Surface(
//        //color = MaterialTheme.colorScheme.backgroundv
//        modifier = Modifier.fillMaxSize(),
//        //color = Color(0xFF3F274A)
//
//    ) {
//            LogScaffold()
//
//    }
//}


@Composable
fun topLogBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )) {
            Text(
                text = "<",
                fontSize = 30.sp
            )
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDatePicker( state : Boolean, onDismiss : () -> Unit,  onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState, Modifier)

    if (state) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }


        DatePickerDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = selectedDateMillis

                            // 선택한 날짜의 연, 월, 일을 얻음
                            onDateSelected(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}



fun extractYearAndMonth(dateString: String): List<Int> {
    val pattern = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
    val date = pattern.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date ?: Date()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줍니다.

    return listOf(year, month)
}

fun extractYearAndMonthAndDay(dateString: String): List<Int> {
    val pattern = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA) // dd일을 추가했습니다.
    val date = pattern.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date ?: Date()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줍니다.
    val day = calendar.get(Calendar.DAY_OF_MONTH) // 일을 추가했습니다.

    return listOf(year, month, day)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogHeader(calendarInstance: Calendar, resultTime: String, write: Boolean, standby:Boolean, detail: Boolean,
              onBackClick: () -> Unit, onDoneClick: () -> Unit, onMenuClick: () -> Unit, onModify: () -> Unit, onDelete: () -> Unit, updateResultTime: () -> Unit){

//    // 날짜 추출
//    val yearAndMonth = extractYearAndMonth(resultTime.toString())
//    val year = yearAndMonth[0]
//    val month = yearAndMonth[1]
//    val day = resultDay
//
//    // 선택된 날짜로 설정
//    val calendarInstance = remember {
//        Calendar.getInstance().apply {
//            set(Calendar.YEAR, year)
//            set(Calendar.MONTH, month - 1) // Calendar.MONTH는 0부터 시작하므로 -1 해줍니다.
//            set(Calendar.DAY_OF_MONTH, day!!)
//        }
//    }
//
//    // 날짜 포맷
//    var resultTime by remember { mutableStateOf(SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(calendarInstance.time)) }
//
//    // 날짜 업데이트
//    fun updateResultTime() {
//        resultTime = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(calendarInstance.time)
//        Log.d("ResultTimeDebug", "Updated resultTime: $resultTime")
//    }


    Log.d("로그화면 resultTime", "resultTime: $resultTime")


    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("수정") }
    val items = listOf("수정", "삭제")
    var deleteDialog by remember { mutableStateOf(false) }
    val calendarState = rememberSheetState()

    var openDialog = remember { mutableStateOf(false) }



    // 데이트 피커 세팅
    LogDatePicker(
        state = openDialog.value,
        onDismiss = { openDialog.value = false },
        onDateSelected = { year, month, day ->

            Log.d("datepicker", "$year-$month-$day")

            calendarInstance.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1) // Calendar.MONTH는 0부터 시작하므로 -1 해줍니다.
                set(Calendar.DAY_OF_MONTH, day!!)
            }

            updateResultTime()

        }
    )



    if (write == true || detail == true) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {

            Button(
                onClick = {
                    // 작성X 화면으로 이동
                    onBackClick()
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
                color = Color.White,
                modifier = Modifier.clickable(
                    onClick = { openDialog.value = true }
                )
            )

            if (standby == false) {
                IconButton(onClick = {
                    onDoneClick()
                    Log.d("doneClick", "standby: $standby")
                }) {
                    Icon(Icons.Default.Done, contentDescription = "Done", tint = Color.White)
                }
            }
            else {
                IconButton(onClick = {
                    expanded = true
                    onMenuClick()
                    Log.d("menuClick", "standby: $standby")
                }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset((-32).dp, (-4).dp),
                    modifier = Modifier
                        .background(Color(0xFF594C7B))
                        .padding(start = 16.dp)


                ) {

                    DropdownMenuItem(
                        text = { Text(text = "수정", color = Color.White)},
                        onClick = {
                            //counter++
                            expanded = false
                            onModify()

                        },
//                        modifier = Modifier.border(
//                            border = BorderStroke(1.dp, Color.White),
//                            shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 0.dp)
//                        ),



                    )
                    DropdownMenuItem(
                        text = { Text(text = "삭제", color = Color.White)},
                        onClick = {
                            //counter++
                            expanded = false
                            deleteDialog = true
                            onDelete()
                        },
//                        modifier = Modifier.border(
//                            border = BorderStroke(1.dp, Color.White),
//                            shape = RoundedCornerShape(0.dp, 0.dp, 4.dp, 4.dp)
//                        )

                    )



//                    items.forEach { item ->
//                        DropdownMenuItem(onClick = {
//                            selectedItem = item
//                            expanded = false
//                        }) {
//                            Text(text = item)
//                        }
//                    }
                }
            }

        }
        if (deleteDialog) {
            LogAlertDialog(dialogText = "정말 삭제하시겠습니까?", onDismissRequest = {deleteDialog = false}, onConfirmation = {deleteDialog = false})
        }
    }

    else{

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {

            Button(
                onClick = {
//                    val newDate = Calendar.getInstance()
//                    newDate.time = date.value.time
//                    newDate.add(Calendar.DATE, -1)
//                    date.value = newDate
                    calendarInstance.add(Calendar.DATE, -1)
                    //resultTime = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(calendarInstance.time)
                    updateResultTime()
                    Log.d("ResultTimeDebug", "Updated resultTime minus: $resultTime")
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
                color = Color.White,
                modifier = Modifier.clickable(
                    onClick = { openDialog.value = true }
                )
            )


            Button(
                onClick = {
//                    val newDate = Calendar.getInstance()
//                    newDate.time = date.value.time
//                    newDate.add(Calendar.DATE, +1)
//                    date.value = newDate
                    calendarInstance.add(Calendar.DATE, +1)
                    //resultTime = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(calendarInstance.time)
                    updateResultTime()
                    Log.d("ResultTimeDebug", "Updated resultTime plus: $resultTime")
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
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LogDatePicker() {
//    val calendarState = rememberSheetState()
//
//    CalendarDialog(
//        state = calendarState,
//        config = CalendarConfig(
//            monthSelection = true,
//            yearSelection = true,
//            style = CalendarStyle.MONTH,
//            //disabledDates = listOf(LocalDate.now().plusDays(7))
//        ),
//        selection = CalendarSelection.Date { date ->
//            Log.d("SelectedDate", "$date")
//        }
//    )
//
//}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogAlertDialog(
    dialogText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit

) {
    AlertDialog(
        text = {
            Text(text = dialogText, color = Color.White)
        },
        onDismissRequest = {
            onDismissRequest()
        },

        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = "취소", color = Color(0xFFD0BCFF))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = "확인", color = Color(0xFFD0BCFF))
            }
        },
        containerColor = Color(0xFF5A5788)
    )
}

@Composable
fun emotionBox(happiness : MutableState<Int>, gloom : MutableState<Int>, anxiety : MutableState<Int>, stress : MutableState<Int>, sleep : MutableState<Int>){


    var expanded by remember { mutableStateOf(false) }
    Log.d("로그화면 emotionBox", "${happiness.value}, ${gloom.value}, ${anxiety.value}, ${stress.value}, ${sleep.value}")

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
//                    .background(
//                        color = Color.Transparent,
//                        shape = RoundedCornerShape(8.dp)
//                    )
        .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
        //.clickable { onToggle() }
    ) {
        if(!expanded) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 4.dp),
                    //horizontalArrangement = Arrangement.SpaceEvenly

                )
                {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFFBB9DC9),
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                            .rotate(270f)
                            .padding(start = 4.dp, end = 16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "행복",
                            color = Color(0xFFBB9DC9),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "우울",
                            color = Color(0xFFBB9DC9),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "불안",
                            color = Color(0xFFBB9DC9),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "스트레스",
                            color = Color(0xFFBB9DC9),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "수면시간",
                            color = Color(0xFFBB9DC9),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, end = 8.dp),
                    //horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //
                    Text(
                        text = happiness.value.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 56.dp)
                    )
                    Text(
                        gloom.value.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 40.dp)
                    )
                    Text(
                        anxiety.value.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 42.dp)
                    )
                    Text(
                        stress.value.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 54.dp)
                    )
                    Text(
                        sleep.value.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 60.dp)
                    )
                }
            }
        }

        else{
            Column(){
                Row(
                    modifier = Modifier.padding(start = 16.dp, end= 16.dp, top = 16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFFBB9DC9),
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "행복",
                        color = Color(0xFFBB9DC9),
                        modifier = Modifier.width(90.dp),
                        textAlign = TextAlign.Center
                    )
                    //Spacer(modifier = Modifier.width(10.dp))
                    EmotionSlider(initialValue = happiness) { emotion ->
                        happiness.value = emotion
                    }
                    //happy =

                }
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "우울",
                        color = Color(0xFFBB9DC9),
                        modifier = Modifier.width(90.dp),
                        textAlign = TextAlign.Center
                    )
                    EmotionSlider(initialValue = gloom) { emotion ->
                        gloom.value = emotion
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "불안",
                        color = Color(0xFFBB9DC9),
                        modifier = Modifier.width(90.dp),
                        textAlign = TextAlign.Center
                    )
                    EmotionSlider(initialValue = anxiety) { emotion ->
                        anxiety.value = emotion
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "스트레스",
                        color = Color(0xFFBB9DC9),
                        modifier = Modifier.width(90.dp),
                        textAlign = TextAlign.Center
                    )
                    EmotionSlider(initialValue = stress) { emotion ->
                        stress.value = emotion
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "수면시간",
                        color = Color(0xFFBB9DC9),
                        modifier = Modifier.width(90.dp),
                        textAlign = TextAlign.Center
                    )
                    EmotionSlider(initialValue = sleep) { emotion ->
                        sleep.value = emotion
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
        }

    }
}

//@Composable
//fun EmotionSlider() {
//    var sliderPosition by remember { mutableStateOf(0f) }
//    var emotion by remember { mutableStateOf(0) }
//    Column {
//
//        Slider(
//            value = sliderPosition.toFloat(), // Float로 받기 때문에 변환
//            onValueChange = { sliderPosition = it.roundToInt().toFloat() }, // 반올림하여 정수로 저장
//            colors = SliderDefaults.colors(
//                thumbColor = MaterialTheme.colorScheme.secondary,
//                activeTrackColor = MaterialTheme.colorScheme.secondary,
//                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            steps = 9,
//            valueRange = 0f..10f
//        )
//        emotion = sliderPosition.roundToInt()
//        //Text(text = sliderPosition.roundToInt().toString())
//
//
//    }
//
//}

@Composable
fun EmotionSlider(initialValue: MutableState<Int>, onEmotionChanged: (Int) -> Unit) {

    var sliderPosition by remember { mutableStateOf(initialValue.value.toFloat()) }

    Column {
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { value ->
                sliderPosition = value.roundToInt().toFloat()
                onEmotionChanged(sliderPosition.roundToInt())
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 9,
            valueRange = 0f..10f
        )
    }
}

//@Composable
//fun SleepSlider(initialValue: Int, onEmotionChanged: (Int) -> Unit) {
//    var sliderPosition by remember { mutableStateOf(initialValue.toFloat()) }
//
//    Column {
//        Slider(
//            value = sliderPosition.toFloat(),
//            onValueChange = { value ->
//                sliderPosition = value.roundToInt().toFloat()
//                onEmotionChanged(sliderPosition.roundToInt())
//            },
//            colors = SliderDefaults.colors(
//                thumbColor = MaterialTheme.colorScheme.secondary,
//                activeTrackColor = MaterialTheme.colorScheme.secondary,
//                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            steps = 12,
//            valueRange = 0f..12f
//        )
//    }
//}


@Composable
fun extraSlider() {

    var sliderValue by remember { mutableStateOf(5f) }

    SliderWithLabel(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        label = "Slider Label"
    )

}

@Composable
fun SliderWithLabel(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
            valueRange = 0f..10f,
            steps = 10,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = 16.dp) // Adjust as needed
        )



        Text(
            text = value.roundToInt().toString(),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp) // Adjust as needed
        )
    }
}


@Composable
fun LogDiary(contentList : MutableState<List<String>>, onDetail: (String) -> Unit) {

    for (i in 0 until contentList.value.size) {

        if (contentList.value[i] != null && contentList.value[i]!="") {

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
//                    .background(
//                        color = Color.Transparent,
//                        shape = RoundedCornerShape(8.dp)
//                    )
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                .clickable { onDetail(contentList.value[i]) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "${contentList.value[i]}",
                        color = Color.White
                    )


                }

            }

        }

    }

}


fun DataSet(resultTime: String): String {

    // 통신 데이터 가공
    val yearAndMonth = extractYearAndMonthAndDay(resultTime.toString())
    Log.d("로그화면 yearMonthDay", "yearAndMonth: $yearAndMonth")
    val year = yearAndMonth[0]
    val month = yearAndMonth[1]
    val day = yearAndMonth[2]
    var dayString = ""
    var monthString = ""
    var tmp = ""

    if (day < 10 || month < 10) {
        if (day < 10 && month < 10) {
            dayString = "0" + day.toString()
            monthString = "0" + month.toString()
        } else {
            if (day < 10) {
                dayString = "0" + day.toString()
                monthString = month.toString()
            }
            if (month < 10) {
                monthString = "0" + month.toString()
                dayString = day.toString()
            }
        }

        tmp = "$year-$monthString-$dayString"
    } else {
        tmp = "$year-$month-$day"
    }

    Log.d("로그화면 tmp", "tmp: $tmp")

    return tmp
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LogScaffold(resultTime: String?, resultDay: Int?){

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val userPk = user.userPk
    Log.d("로그화면 로그인 userPk", "userPk: $userPk")


    var write by remember { mutableStateOf(false) }
    //val calendarInstance = Calendar.getInstance()
//    val time = remember {
//        mutableStateOf(calendarInstance)
//    }
    var count by remember { mutableStateOf(0) }
    var contentSize by remember { mutableStateOf(0) }

    var standby by remember { mutableStateOf(false) }
    var update by remember { mutableStateOf(false) }
    var detail by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    var content by remember { mutableStateOf("") }
    var realContent by remember { mutableStateOf("") }
    var tmpContent by remember { mutableStateOf("") }
    var detailContent by remember { mutableStateOf("") }

    var contentList = remember { mutableStateListOf<String>() }


    // 날짜 추출
    val yearAndMonth = extractYearAndMonth(resultTime.toString())
    val year = yearAndMonth[0]
    val month = yearAndMonth[1]
    val day = resultDay

    // 선택된 날짜로 설정
    val calendarInstance = remember {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // Calendar.MONTH는 0부터 시작하므로 -1 해줍니다.
            set(Calendar.DAY_OF_MONTH, day!!)
        }
    }

    // 날짜 포맷
    var resultTime by remember { mutableStateOf(SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(calendarInstance.time)) }

    // 날짜 업데이트
    fun updateResultTime() {
        resultTime = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(calendarInstance.time)
        Log.d("ResultTimeDebug", "Updated resultTime: $resultTime")
    }

    // 감정 변수
    var happiness by remember { mutableStateOf(0) }
    var gloom by remember { mutableStateOf(0) }
    var anxiety by remember { mutableStateOf(0) }
    var stress by remember { mutableStateOf(0) }
    var sleep by remember { mutableStateOf(0) }


    var isContentListLoaded = remember { mutableStateOf(false) }





    LaunchedEffect(resultTime) {

        content = ""
        realContent = ""
        contentList.clear()
        isContentListLoaded.value = false


        coroutineScope.launch {
            Log.d("로그화면 클릭", "")
            Log.d("로그화면 클릭", "$resultTime")

            var tmp = DataSet(resultTime)
            Log.d("로그화면 DataSet 함수", "tmp: $tmp")



            val logRequest =
                UserPK(userPk = userPk)
            Log.d("로그화면 request", "$logRequest")

            try {

                Log.d("로그화면 1111", "1111")
                val response = myApi.log(logRequest.userPk, tmp)
                Log.d("로그화면 response.body", "${response.body()}")
                Log.d("로그화면 응답 코드", "HTTP status code: ${response.code()}")
                Log.d("로그화면 응답 성공 여부", "Is successful: ${response.isSuccessful}")


                if(response.isSuccessful) {

                    Log.d("로그화면 response", "response is successful")

                    val json = response.body()
                    Log.d("로그화면 json", "$json")
                    val status = json?.status
                    val message = json?.message
                    val data = json?.data

                    happiness = data?.happiness!!.toInt()
                    gloom = data?.gloom!!.toInt()
                    anxiety = data?.anxiety!!.toInt()
                    stress = data?.stress!!.toInt()
                    sleep = data?.sleep!!.toInt()
                    val todayDiaries = data?.todayDiaries

                    Log.d("로그화면 데이터 셋", "$status $message $data")
                    Log.d("로그화면 데이터 감정", "$happiness $gloom $anxiety $stress $sleep")
                    Log.d("로그화면 데이터 일기 리스트", "$todayDiaries")

                    todayDiaries?.forEach { diaryExist ->
                        val diaryPk = diaryExist.diaryPk
                        val content = diaryExist.content

                        if (content != "" || content != null) {
                            Log.d("로그화면 contentList 1", "contentList: $contentList")
                            contentList.add(content)
                            Log.d("로그화면 contentList 2", "contentList: $contentList")
                        }

                        Log.d("로그화면 상세 데이터", "$diaryPk $content")
                    }

                    Log.d("로그화면 contentList", "contentList: ${contentList.joinToString()}")
                    Log.d("로그화면 contentList", "contentList.size: ${contentList.size}")
                    contentSize = contentList.size



                }

                else {
                    val errorMessage = response.errorBody()?.string()
                    Log.d("로그화면 오류 메시지", "Error message: $errorMessage")
                    val jsonObject = JSONObject(errorMessage)
                    val status = jsonObject.getInt("status")
                    val message = jsonObject.getString("message")
                    Log.d("로그화면 오류 상태 코드", "Error status: $status")
                    Log.d("로그화면 오류 메시지", "Error message: $message")
                }


            }catch (e:Exception){
                Log.d("로그 오류", "$e")
            }

        }
    }



    updateResultTime()
    count = contentList.size


    Scaffold(
//        topBar = {
//            LogTopBar()
//        },
        floatingActionButton = {
            if (write == false) {
                LogFloatingActionButton(count) {
                    write = !write   // 클릭 후에 버튼을 숨김
                    count++
                    Log.d("floatingActionButtonClick", "count: $count")
                }
            }

        }

    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(10.dp))
                    LogHeader(calendarInstance, resultTime, write, standby, detail, onBackClick = { write = !write; standby = !standby; },
                        onDoneClick = { standby = !standby; focusManager.clearFocus(); update = !update },
                        onMenuClick = {  }, onModify = { standby = !standby }, onDelete = { write = !write; count-- }, updateResultTime = :: updateResultTime)
                    //if(content == null || content == "") count--
                    Log.d("로그화면 update LogHeader 밑", "update: $update")


                    if (count == 0) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(


                            ) {
                                //present?.let { Text(it) }
                                //Text("$resultTime ${resultDay}일")
                                Image(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(150.dp)
                                        .padding(0.dp),
                                    painter = painterResource(id = R.drawable.emptylog),
                                    contentDescription = "emptylog"
                                )
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    text = "오늘 하루를 기록해보세요",
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }

                    }
                    else {


                        // 작성화면 여부와 관계없이 count 값이 1 이상이면 emotionBox()는 동일
                        //emotionBox(happiness, gloom, anxiety, stress, sleep)
                        emotionBox(remember { mutableStateOf(happiness) }, remember { mutableStateOf(gloom) }, remember { mutableStateOf(anxiety) }, remember { mutableStateOf(stress) }, remember { mutableStateOf(sleep) })


                        if (write == false) {   // 작성화면이 아닐때

                            Log.d("로그화면 content", "content: $content")
                            realContent = content
                            Log.d("로그화면 realContent", "realContent: $realContent")

                            if (detail == false) {
                                Column(modifier = Modifier.verticalScroll(scrollState)) {
                                    LogDiary(remember { mutableStateOf(contentList) }, onDetail = { content ->
                                        detailContent = content; detail = !detail
                                    })
                                }
                            } else {
                                LogStandby(detailContent)
                            }


                        }

                        else {  // 작성화면일때


                            if (keyboardController != null) {
                                Log.d("click", "standby: $standby")
                                if(standby == false) {
                                    LogTextField(
                                        onTextState = {updatedTextState ->
                                            content = updatedTextState
                                        },

                                    )
                                    tmpContent = content
                                }
                                else {

                                    LogStandby(tmpContent)



                                }
                            }

                        }


                        LaunchedEffect(realContent) {

                            coroutineScope.launch {

                                var tmp = DataSet(resultTime)
                                Log.d("로그화면 DataSet 함수", "tmp: $tmp")

                                if (realContent != "" && realContent != null) {
                                    Log.d("로그화면 realContent", "realContent: $realContent")

                                    val diaryRequest =
                                        DiaryPost(
                                            localDate = tmp, userPk = userPk, content = realContent, happiness = happiness, gloom = gloom, anxiety = anxiety, stress = stress, sleep = sleep
                                        )
                                    Log.d("로그화면 request", "$diaryRequest")

                                    try {

                                        Log.d("로그화면 1111", "1111")
                                        val response = myApi.write(diaryRequest)
                                        Log.d("로그화면 response.body", "${response.body()}")
                                        Log.d("로그화면 응답 코드", "HTTP status code: ${response.code()}")
                                        Log.d("로그화면 응답 성공 여부", "Is successful: ${response.isSuccessful}")


                                        if (response.isSuccessful) {

                                            Log.d("로그화면 response", "response is successful")

                                            val json = response.body()
                                            Log.d("로그화면 json", "$json")
                                            val status = json?.status
                                            val message = json?.message
                                            Log.d("로그화면 데이터 셋", "$status $message")


                                        } else {
                                            val errorMessage = response.errorBody()?.string()
                                            Log.d("로그화면 오류 메시지", "Error message: $errorMessage")
                                            val jsonObject = JSONObject(errorMessage)
                                            val status = jsonObject.getInt("status")
                                            val message = jsonObject.getString("message")
                                            Log.d("로그화면 오류 상태 코드", "Error status: $status")
                                            Log.d("로그화면 오류 메시지", "Error message: $message")
                                        }


                                    } catch (e: Exception) {
                                        Log.d("로그화면 오류", "$e")
                                    }


                                }
                            }

                        }

                    }

                }
            }


        }


    }
}

@Composable
fun LogStandby(content: String) {

    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
//                    .background(
//                        color = Color.Transparent,
//                        shape = RoundedCornerShape(8.dp)
//                    )
//        .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
        //.clickable { onToggle() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)

        ) {

            Column(modifier = Modifier.verticalScroll(scrollState)) {

                Text(
                    text = "${content}",
                    color = Color.White
                )
            }




        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LogTextField(onTextState: (String) -> Unit){


    var textState by remember { mutableStateOf("") }
    var enteredText by remember { mutableStateOf("")}



    TextField(
        value = textState,
        onValueChange = { newText: String ->
            textState = newText
            onTextState(newText)
        },
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        placeholder = {
            Text(
                text = "내용을 입력하세요",
                color = Color(0xFFE6E0E9)
            )
        },
        shape = RoundedCornerShape(16.dp),


    )

//    Button(
//        onClick = {
//            enteredText = textState
//        }
//    ) {
//
//    }
//
//    Text(
//        text = "${enteredText}"
//    )


}

//@Composable
//fun LogNav() {
//
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "")
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTopBar() {

    TopAppBar(
        title = {
            Text(text = "TopBar")
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent)
    )
}

@Composable
fun LogFloatingActionButton(count: Int, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()   // 클릭 후에 write 상태 변경
        },
        containerColor = Color(0xFF5A5788),
        contentColor = Color.White
    ) {
        Icon(Icons.Default.Edit, contentDescription = "Edit")
    }
}

@Preview(showBackground = true)
@Composable
fun LogScreenPreview() {
    MoodtrakerTheme {
        //LogScreen(navBackStackEntry.arguments?.getString("resultTime"))
    }
}