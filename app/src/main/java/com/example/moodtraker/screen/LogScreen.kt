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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.example.moodtraker.R
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogHeader(resultTime: String?, resultDay: Int?, write: Boolean, standby:Boolean,
              onBackClick: () -> Unit, onDoneClick: () -> Unit, onMenuClick: () -> Unit, onModify: () -> Unit, onDelete: () -> Unit){

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


    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("수정") }
    val items = listOf("수정", "삭제")
    var deleteDialog by remember { mutableStateOf(false) }
    val calendarState = rememberSheetState()

    var openDialog = remember { mutableStateOf(false) }



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



    if (write == true) {

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
fun emotionBox(){

    var happiness by remember { mutableStateOf(0) }
    var gloom by remember { mutableStateOf(0) }
    var anxiety by remember { mutableStateOf(0) }
    var stress by remember { mutableStateOf(0) }
    var sleep by remember { mutableStateOf(0) }

    var expanded by remember { mutableStateOf(false) }

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
                        text = happiness.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 56.dp)
                    )
                    Text(
                        gloom.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 40.dp)
                    )
                    Text(
                        anxiety.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 42.dp)
                    )
                    Text(
                        stress.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 54.dp)
                    )
                    Text(
                        sleep.toString(),
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
                        happiness = emotion
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
                        gloom = emotion
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
                    EmotionSlider(initialValue = anxiety) {emotion ->
                        anxiety = emotion
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
                    EmotionSlider(initialValue = stress) {emotion ->
                        stress = emotion
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
                    EmotionSlider(initialValue = sleep) {emotion ->
                        sleep = emotion
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
fun EmotionSlider(initialValue: Int, onEmotionChanged: (Int) -> Unit) {
    var sliderPosition by remember { mutableStateOf(initialValue.toFloat()) }

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
fun LogDiary(content: String) {

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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${content}",
                color = Color.White
            )


        }
            
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LogScaffold(resultTime: String?, resultDay: Int?){

    var write by remember { mutableStateOf(false) }
    //val calendarInstance = Calendar.getInstance()
//    val time = remember {
//        mutableStateOf(calendarInstance)
//    }
    var count by remember { mutableStateOf(0) }
    var standby by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var content by remember { mutableStateOf("") }

    //var present by remember { mutableStateOf(resultTime) }



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
                    LogHeader(resultTime, resultDay, write, standby, onBackClick = { write = !write; standby = !standby;  },
                        onDoneClick = { standby = !standby; focusManager.clearFocus(); },
                        onMenuClick = {  }, onModify = { standby = !standby }, onDelete = { write = !write; count-- })
                    //if(content == null || content == "") count--

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
                        emotionBox()

                        if (write == false) {   // 작성화면이 아닐때

                            if (content != "" || content != null) {
                                Log.d("로그 content", "content: $content")
                                LogDiary(content)
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
                                }
                                else {
                                    LogStandby(content)
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
            Text(
                text = "${content}",
                color = Color.White
            )


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


//        keyboardActions = KeyboardActions(
//            onDone = {
//                onDoneClick()
//                keyboardController?.hide() // 키보드 숨기기
//            }
//        )

        //cursorBrush = SolidColor(Color.White), // 커서 색상 변경

//        keyboardActions = KeyboardActions(
//            onDone = {
//                // 이 부분에서 키보드의 Done 버튼을 눌렀을 때의 동작을 설정할 수 있습니다.
//
//            }
//        ),

        //interactionSource = remember { MutableInteractionSource() },

    )



    Button(
        onClick = {
            enteredText = textState
        }
    ) {

    }

    Text(
        text = "${enteredText}"
    )


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