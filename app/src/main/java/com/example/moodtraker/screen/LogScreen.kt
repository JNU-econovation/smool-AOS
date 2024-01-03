package com.example.moodtraker.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.moodtraker.R
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun LogScreen() {
    val scrollState = rememberScrollState()


    Surface(
        //color = MaterialTheme.colorScheme.backgroundv
        modifier = Modifier.fillMaxSize(),
        //color = Color(0xFF3F274A)

    ) {
            LogScaffold()

    }
}

//@Composable
//fun topLogBar() {
//    Box() {
//        Column(
//                horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//            Button(
//                onClick = {
//
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Transparent
//                )) {
//                Text(
//                    text = ">",
//                    fontSize = 30.sp
//                )
//            }
//            IconButton(onClick = {}) {
//                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
//            }
//        }
//    }
//}

@Composable
fun LogHeader(date: MutableState<Calendar>){
    val resultTime = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(date.value.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Button(
            onClick = {
                val newDate = Calendar.getInstance()
                newDate.time = date.value.time
                newDate.add(Calendar.DATE, -1)
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
                newDate.add(Calendar.DATE, +1)
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
fun emotionBox(happy: Int, depress: Int, anxiety: Int, stress: Int, sleep: Int){
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
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        //expanded = !expanded
                    }
                )
                Text("행복")
                Text("우울")
                Text("불안")
                Text("스트레스")
                Text("수면시간")

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, bottom = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                //
                Text(happy.toString())
                Text(depress.toString())
                Text(anxiety.toString())
                Text(stress.toString())
                Text(sleep.toString())
            }
        }

    }
}

@Composable
fun LogDiary() {

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
        Column() {
//            Text(
//                text =
//            )


        }
            
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScaffold(){

    var expanded by remember { mutableStateOf(false) }
    val calendarInstance = Calendar.getInstance()
    val time = remember {
        mutableStateOf(calendarInstance)
    }
    var count by remember { mutableStateOf(1) }
    var happy by remember { mutableStateOf(0) }
    var depress by remember { mutableStateOf(0) }
    var anxiety by remember { mutableStateOf(0) }
    var stress by remember { mutableStateOf(0) }
    var sleep by remember { mutableStateOf(0) }

    Scaffold(
//        topBar = {
//            LogTopBar()
//        },
        floatingActionButton = {
            LogFloatingActionButton(count)
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
                                Color(0xFF494C8D),
                                Color(0xFF9394BB)
                            )
                        )
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    LogHeader(time)

                    if (count == 0) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(


                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .padding(0.dp),
                                    painter = painterResource(id = R.drawable.emptylog),
                                    contentDescription = "emptylog"
                                )
                                Text(

                                    text = "오늘 하루를 기록해보세요",
                                    color = Color.White
                                )
                            }
                        }

                    }
                    else {

                        emotionBox(happy, depress, anxiety, stress, sleep)
                    }



                }
            }


        }


    }
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
fun LogFloatingActionButton(count: Int) {
    FloatingActionButton(
        onClick = {
            if (count == 0) {

            }
            else {

            }
        }
    ) {
        Icon(Icons.Default.Edit, contentDescription = "Edit")
    }
}

@Preview(showBackground = true)
@Composable
fun LogScreenPreview() {
    MoodtrakerTheme {
        LogScreen()
    }
}