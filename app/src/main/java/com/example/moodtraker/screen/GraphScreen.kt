package com.example.moodtraker.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtraker.AnxietyList
import com.example.moodtraker.GloomList
import com.example.moodtraker.UserPK
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import com.example.moodtraker.user
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.horizontalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.LocalChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.Chart
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.decoration.Decoration
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.chart.values.ChartValuesProvider
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.ChartModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.diff.MutableExtraStore
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.legend.HorizontalLegend
import com.patrykandpatrick.vico.core.legend.Legend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun GraphScreen(){
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

        val coroutineScope = rememberCoroutineScope()

        val userPk = user.userPk

        val calendarInstance = Calendar.getInstance()
        val time = remember {
            mutableStateOf(calendarInstance)
        }
        val timeYear = remember {
            mutableStateOf(calendarInstance)
        }

        val resultTime = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(time.value.time)

        var happinessList = remember { mutableStateListOf<Int>() }
        var gloomList = remember { mutableStateListOf<Int>() }
        var anxietyList = remember { mutableStateListOf<Int>() }
        var stressList = remember { mutableStateListOf<Int>() }
        var sleepList = remember { mutableStateListOf<Int>() }



        LaunchedEffect(resultTime) {

            //existList = mutableStateListOf<Boolean>()

            happinessList.clear()
            gloomList.clear()
            anxietyList.clear()
            stressList.clear()
            sleepList.clear()


            Log.d("통계 업데이트", "resultLog: $resultTime")
            var resultLog = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(time.value.time)
            var dayMax = time.value.getActualMaximum(Calendar.DAY_OF_MONTH)
            var tmp = "$resultLog-$dayMax"
            Log.d("캘린더 tmp", "$tmp")


            coroutineScope.launch {
                Log.d("통계 클릭", "")



                val graphRequest =
                    UserPK(userPk = userPk)
                Log.d("통계 request", "$graphRequest")


                try {

                    Log.d("통계 1111", "1111")
                    val response = myApi.graph(graphRequest.userPk, tmp)
                    Log.d("통계 response.body", "${response.body()}")
                    Log.d("통계 응답 코드", "HTTP status code: ${response.code()}")
                    Log.d("통계 응답 성공 여부", "Is successful: ${response.isSuccessful}")


                    if(response.isSuccessful) {

                        Log.d("통계 response", "response is successful")

                        val json = response.body()
                        Log.d("통계 json", "$json")
                        val status = json?.status
                        val message = json?.message
                        val data = json?.data
                        val happiness = data?.happiness
                        val gloom = data?.gloom
                        val anxiety = data?.anxiety
                        val stress = data?.stress
                        val sleep = data?.sleep

                        Log.d("통계 데이터", "$status $message $data")

                        happiness?.forEach { emotion ->
                            val localDate = emotion.localDate
                            val value = emotion.value

                            happinessList.add(value)

                            Log.d("통계 상세 데이터", "$happinessList")
                        }

                        gloom?.forEach { emotion ->
                            val localDate = emotion.localDate
                            val value = emotion.value

                            gloomList.add(value)

                            Log.d("통계 상세 데이터", "$gloomList")
                        }

                        anxiety?.forEach { emotion ->
                            val localDate = emotion.localDate
                            val value = emotion.value

                            anxietyList.add(value)

                            Log.d("통계 상세 데이터", "$anxietyList")
                        }

                        stress?.forEach { emotion ->
                            val localDate = emotion.localDate
                            val value = emotion.value

                            stressList.add(value)

                            Log.d("통계 상세 데이터", "$stressList")
                        }

                        sleep?.forEach { emotion ->
                            val localDate = emotion.localDate
                            val value = emotion.value

                            sleepList.add(value)

                            Log.d("통계 상세 데이터", "$sleepList")
                        }


                        Log.d("통계 happinessList", "happinessList: ${happinessList.joinToString()}")
                        Log.d("통계 happinessList", "happinessList: ${happinessList.size}")
                        Log.d("통계 gloomList", "gloomList: ${gloomList.joinToString()}")
                        Log.d("통계 gloomList", "gloomList: ${gloomList.size}")
                        Log.d("통계 anxietyList", "anxietyList: ${anxietyList.joinToString()}")
                        Log.d("통계 anxietyList", "anxietyList: ${anxietyList.size}")
                        Log.d("통계 stressList", "stressList: ${stressList.joinToString()}")
                        Log.d("통계 stressList", "stressList: ${stressList.size}")
                        Log.d("통계 sleepList", "sleepList: ${sleepList.joinToString()}")
                        Log.d("통계 sleepList", "sleepList: ${sleepList.size}")



                    }

                    else {
                        val errorMessage = response.errorBody()?.string()
                        Log.d("통계 오류 메시지", "Error message: $errorMessage")
                        val jsonObject = JSONObject(errorMessage)
                        val status = jsonObject.getInt("status")
                        val message = jsonObject.getString("message")
                        Log.d("통계 오류 상태 코드", "Error status: $status")
                        Log.d("통계 오류 메시지", "Error message: $message")
                    }


                }catch (e:Exception){
                    Log.d("통계 오류", "$e")
                }


            }

        }


        Column {
            Spacer(modifier = Modifier.height(20.dp))
//            Icon(Icons.Default.HelpOutline, contentDescription = "help",tint = Color.White,
//                modifier = Modifier
//                    .align(Alignment.End)
//                    .padding(end = 16.dp))
            ChartHeader(time, resultTime)
            monthChart(happinessList, gloomList, anxietyList, stressList, sleepList)
            Spacer(modifier = Modifier.height(20.dp))
            ChartHeaderYear(timeYear)
            yearChart()
            Spacer(modifier = Modifier.height(20.dp))

        }

    }

}

@Composable
fun ChartHeader(date: MutableState<Calendar>, resultTime:String){


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
                fontSize = 28.sp
            )
        }

        Text(
            text = resultTime,
            fontSize = 24.sp,
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
                fontSize = 28.sp
            )
        }
    }
}

@Composable
fun ChartHeaderYear(date: MutableState<Calendar>){
    val resultTime = SimpleDateFormat("yyyy년", Locale.KOREA).format(date.value.time)

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
                newDate.add(Calendar.YEAR, -1)
                date.value = newDate
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )

        ) {
            Text(
                text = "<",
                fontSize = 28.sp
            )
        }

        Text(
            text = resultTime,
            fontSize = 24.sp,
            color = Color.White
        )


        Button(
            onClick = {
                val newDate = Calendar.getInstance()
                newDate.time = date.value.time
                newDate.add(Calendar.YEAR, +1)
                date.value = newDate
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )) {
            Text(
                text = ">",
                fontSize = 28.sp
            )
        }
    }
}





//@Composable
//fun ComposedChart() {
//
//
//    val composedChartEntryModelProducer = ComposedChartEntryModelProducer.build {
//        add(entriesOf(9, 10, 1, 2, 3, 4, 5, 6, 2, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 7, 6, 5, 4, 3, 2, 8, 7, 6, 10))
//        add(entriesOf(10, 8, 4, 5, 6, 7, 8, 9, 0, 6, 7, 12, 4, 8, 9, 0, 7, 6, 5, 4, 3, 2, 8, 7, 6, 7, 6, 10, 6))
//        add(entriesOf(1, 2, 3, 4 ,5 , 6, 7, 8, 9))
//        add(entriesOf(5, 0, 10, 7, 2, 3, 7, 3, 4, 4, 4, 4, 4, 5))
//        add(entriesOf(5, 5, 5, 5, 5, 5, 5 ,5 ,5, 5, 10, 10, 10, 10))
//
//    }
//
//
//    var sleepChart = columnChart()
//    var happyChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFE16F6F)))) // 핑크
//    var gloomChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFB3F4FD)))) // 하늘
//    var anxietyChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFF8FA93)))) // 노랑
//    var stressChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFF97F98F)))) // 초록
//
//    //val composedChart = remember(sleepChart, happyChart, gloomChart, anxietyChart, stressChart) { sleepChart + happyChart + gloomChart + anxietyChart + stressChart }
//    val composedChart = remember(
//        sleepChart,
//        happyChart,
//        gloomChart,
//        anxietyChart,
//        stressChart
//    ) {
//        sleepChart + happyChart + gloomChart + anxietyChart + stressChart
//    }
//
//
//
//    Chart(
//
//        chart = composedChart,
//        chartModelProducer = composedChartEntryModelProducer,
//        startAxis = rememberStartAxis(),
//        bottomAxis = rememberBottomAxis(),
//        chartScrollState = rememberChartScrollState(),
//        modifier = Modifier.height(250.dp)
//
//    )
//
//    Log.d("ComposedChart", "Sleep Chart Data: ${composedChartEntryModelProducer.getModel()}")
//
//}



//--------------------------

@Composable
fun rememberChartStyle(columnChartColors: List<Color>): ChartStyle {

    val columnChartColors = listOf(Color(0xFF3E3B79))

    // 312F54
    // 3E3B79

    return ChartStyle(
        axis = ChartStyle.Axis(
            axisLabelColor = Color.White,
            axisGuidelineColor = Color.Transparent,
            axisLineColor = Color.White
        ),
        columnChart = ChartStyle.ColumnChart(
            columns = columnChartColors.map { columnColor ->
                LineComponent(
                    // color = columnColor.toArgb(),
                    color = columnColor.toArgb(),
                    thicknessDp = 20f,
                    shape = Shapes.cutCornerShape(topRightPercent = 20, topLeftPercent = 20)
                )
            },
            //dataLabel = TextComponent.Builder().build()
        ),
        lineChart = ChartStyle.LineChart(lines = emptyList()),
        marker = ChartStyle.Marker(),
        elevationOverlayColor = Color.White
    )

}

@Composable
fun monthChart(happinessList: SnapshotStateList<Int>, gloomList: SnapshotStateList<Int>, anxietyList: SnapshotStateList<Int>, stressList: SnapshotStateList<Int>, sleepList: SnapshotStateList<Int>) {

    val maxYRange = 10

    val colorList = listOf(Color(0xFF3E3B79), Color(0xFFE16F6F), Color(0xFFB3F4FD), Color(0xFFF8FA93), Color(0xFF97F98F))


    ProvideChartStyle(rememberChartStyle(listOf(Color.White))) {

        val composedChartEntryModelProducer = ComposedChartEntryModelProducer.build {
            //add(entriesOf(9, 9, 1, 2, 3, 4, 5, 6, 2, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 7, 6, 5, 4, 3, 2, 8, 7, 6, 9))
//            add(entriesOf(9, 8, 4, 5, 6, 7, 8, 9, 0, 6, 7, 9, 4, 8, 9, 0, 7, 6, 5, 4, 3, 2, 8, 7, 6, 7, 6, 9, 6))
//            add(entriesOf(1, 2, 3, 4 ,5 , 6, 7, 8, 9))
//            add(entriesOf(5, 0, 9, 7, 2, 3, 7, 3, 4, 4, 4, 4, 4, 5))
//            add(entriesOf(5, 5, 5, 5, 5, 5, 5 ,5 ,5, 5, 9, 9, 9, 9))

            add(entriesOf(*sleepList.map { it as Number }.toTypedArray()))
            add(entriesOf(*happinessList.map { it as Number }.toTypedArray()))
            add(entriesOf(*gloomList.map { it as Number }.toTypedArray()))
            add(entriesOf(*anxietyList.map { it as Number }.toTypedArray()))
            add(entriesOf(*stressList.map { it as Number }.toTypedArray()))


        }


        var sleepChart = columnChart(
            mergeMode = ColumnChart.MergeMode.Grouped,
            axisValuesOverrider = AxisValuesOverrider.fixed(
                minY = 0f,
                maxY = maxYRange.toFloat()
            ),
            spacing = 0.dp
        )
        var happyChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFE16F6F)))) // 핑크
        var gloomChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFB3F4FD)))) // 하늘
        var anxietyChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFF8FA93)))) // 노랑
        var stressChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFF97F98F)))) // 초록

        //val composedChart = remember(sleepChart, happyChart, gloomChart, anxietyChart, stressChart) { sleepChart + happyChart + gloomChart + anxietyChart + stressChart }
        val composedChart = remember(
            sleepChart,
            happyChart,
            gloomChart,
            anxietyChart,
            stressChart
        ) {
            sleepChart + happyChart + gloomChart + anxietyChart + stressChart
        }


        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 15.dp, vertical = 5.dp),
            chart = composedChart,
            legend = rememberLegend(colors = colorList),
            chartModelProducer = composedChartEntryModelProducer,
            startAxis = rememberStartAxis(
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = maxYRange / 10 + 1)
            ),
//            endAxis = rememberEndAxis(
//                //itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = maxYRange / 10 + 1)
//                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 0)
//            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                    ("${value.toInt()+1}")
//                    val intValue = value.toInt() + 1
//                    if (intValue % 5 == 0 && intValue <= 30) {
//                        ("${value.toInt()+1}")
//                    } else {
//                        ""
//                    }
                }
            ),
            runInitialAnimation = true,
            chartScrollState = rememberChartScrollState()
        )
    }
}


@Composable
fun yearChart() {

    val maxYRange = 10

    val colorList = listOf(Color(0xFF3E3B79), Color(0xFFE16F6F), Color(0xFFB3F4FD), Color(0xFFF8FA93), Color(0xFF97F98F))


    ProvideChartStyle(rememberChartStyle(listOf(Color.White))) {

        val composedChartEntryModelProducer = ComposedChartEntryModelProducer.build {
            add(entriesOf(9, 9, 1, 2, 3, 4, 5, 6, 2, 8, 1, 2))
            add(entriesOf(9, 8, 4, 5, 6, 7, 8, 9, 0, 6, 7, 9))
            add(entriesOf(1, 2, 3, 4 ,5, 6, 7, 8, 9, 7, 6, 7))
            add(entriesOf(5, 0, 9, 7, 2, 3, 7, 3, 4, 4, 4, 5))
            add(entriesOf(5, 5, 5, 5, 5, 5, 5, 5, 9, 9, 9, 9))

        }


        var sleepChart = columnChart(
            mergeMode = ColumnChart.MergeMode.Grouped,
            axisValuesOverrider = AxisValuesOverrider.fixed(
                minY = 0f,
                maxY = maxYRange.toFloat()
            ),
            spacing = 0.dp
        )
        var happyChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFE16F6F)))) // 핑크
        var gloomChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFB3F4FD)))) // 하늘
        var anxietyChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFFF8FA93)))) // 노랑
        var stressChart = lineChart(lines = listOf(lineSpec(lineColor = Color(0xFF97F98F)))) // 초록

        //val composedChart = remember(sleepChart, happyChart, gloomChart, anxietyChart, stressChart) { sleepChart + happyChart + gloomChart + anxietyChart + stressChart }
        val composedChart = remember(
            sleepChart,
            happyChart,
            gloomChart,
            anxietyChart,
            stressChart
        ) {
            sleepChart + happyChart + gloomChart + anxietyChart + stressChart
        }


        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 15.dp, vertical = 5.dp),
            chart = composedChart,
            legend = rememberLegend(colors = colorList),
            chartModelProducer = composedChartEntryModelProducer,
            startAxis = rememberStartAxis(
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = maxYRange / 10 + 1)
            ),
//            endAxis = rememberEndAxis(
//                //itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = maxYRange / 10 + 1)
//                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 0)
//            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                    ("${value.toInt()+1}")
//                    val intValue = value.toInt() + 1
//                    if (intValue % 5 == 0 && intValue <= 30) {
//                        ("${value.toInt()+1}")
//                    } else {
//                        ""
//                    }
                }
            ),
            runInitialAnimation = true,
            chartScrollState = rememberChartScrollState()
        )
    }
}


@Composable
fun rememberLegend(colors: List<Color>): HorizontalLegend {
    val labelTextList = listOf("수면시간", "행복", "우울", "불안", "스트레스")

    return horizontalLegend(
        items = List(labelTextList.size) { index ->
            legendItem(
                icon = shapeComponent(
                    shape = Shapes.pillShape,
                    color = colors[index]
                ),
                label = textComponent {
                    color = android.graphics.Color.WHITE
                },
                labelText = labelTextList[index]
            )
        },
        iconSize = 10.dp,
        iconPadding = 8.dp,
        spacing = 10.dp,
        padding = dimensionsOf(top = 8.dp)
    )
}


private fun intListAsFloatEntryList(list: List<Int>): List<FloatEntry> {
    val floatEntryList = arrayListOf<FloatEntry>()
    floatEntryList.clear()

    list.forEachIndexed { index, item ->
        floatEntryList.add(entryOf(x = index.toFloat(), y = item.toFloat()))
    }

    return floatEntryList
}



//-------------------------------------------------------


//@Composable
//fun rememberChartStyle(columnChartColors: List<Color>): ChartStyle {
//    val isSystemInDarkTheme = isSystemInDarkTheme()
//    return remember(columnChartColors, isSystemInDarkTheme) {
//        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
//
//        ChartStyle(
//            axis = ChartStyle.Axis(
//                axisLabelColor = Color(defaultColors.axisLabelColor),
//                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
//                axisLineColor = Color(defaultColors.axisLineColor)
//            ),
//            columnChart = ChartStyle.ColumnChart(
//                columns = columnChartColors.map { columnColor ->
//                    LineComponent(
//                        color = columnColor.toArgb(),
//                        thicknessDp = 25f,
//                        shape = Shapes.cutCornerShape(topRightPercent = 20, topLeftPercent = 20)
//                    )
//                },
//                dataLabel = TextComponent.Builder().build()
//            ),
//            lineChart = ChartStyle.LineChart(lines = emptyList()),
//            marker = ChartStyle.Marker(),
//            elevationOverlayColor = Color(defaultColors.elevationOverlayColor)
//        )
//    }
//}


//@Composable
//fun testChart() {
//
//    val maxYRange = 10
//    val colorList = listOf(Color.White, Color.Yellow)
//    val completedPlanList = listOf(9, 10, 1, 2, 3, 4, 5, 6, 2, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 7, 6, 5, 4, 3, 2, 8, 7, 6, 10)
//    val completedRateList = listOf(10, 8, 4, 5, 6, 7, 8, 9, 0, 6, 7, 12, 4, 8, 9, 0, 7, 6, 5, 4, 3, 2, 8, 7, 6, 7, 6, 10, 6)
//
//    ProvideChartStyle(rememberChartStyle(columnChartColors = colorList)) {
//        val completedPlanChart = columnChart(
//            mergeMode = ColumnChart.MergeMode.Grouped,
//            axisValuesOverrider = AxisValuesOverrider.fixed(
//                minY = 0f,
//                maxY = maxYRange.toFloat()
//            ),
//            spacing = 100.dp
//        )
//        val completedRateChart = columnChart(
//            mergeMode = ColumnChart.MergeMode.Grouped,
//            axisValuesOverrider = AxisValuesOverrider.fixed(
//                minY = 0f,
//                maxY = maxYRange.toFloat()
//            ),
//            spacing = 100.dp
//        )
//
//        val completedPlanEntry = ChartEntryModelProducer(intListAsFloatEntryList(completedPlanList))
//        val completedRateEntry = ChartEntryModelProducer(intListAsFloatEntryList(completedRateList))
//
//        Chart(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(400.dp)
//                .padding(horizontal = 15.dp, vertical = 5.dp),
//            chart = remember(completedPlanChart, completedRateChart) {
//                completedPlanChart + completedRateChart
//            },
//            legend = rememberLegend(colors = colorList),
//            chartModelProducer = ComposedChartEntryModelProducer(completedPlanEntry.plus(completedRateEntry)),
//            startAxis = rememberStartAxis(
//                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = maxYRange / 10 + 1)
//            ),
//            bottomAxis = rememberBottomAxis(
//                valueFormatter = { value, _ ->
//                    ("${value.toInt()+1}주 전")
//                }
//            ),
//            runInitialAnimation = true,
//            chartScrollState = rememberChartScrollState()
//        )
//    }
//}
//
//@Composable
//fun rememberLegend(colors: List<Color>): HorizontalLegend {
//    val labelTextList = listOf("완료한 일정(개)", "일정 완료율(%)")
//
//    return horizontalLegend(
//        items = List(labelTextList.size) { index ->
//            legendItem(
//                icon = shapeComponent(
//                    shape = Shapes.pillShape,
//                    color = colors[index]
//                ),
//                label = textComponent(),
//                labelText = labelTextList[index]
//            )
//        },
//        iconSize = 10.dp,
//        iconPadding = 8.dp,
//        spacing = 10.dp,
//        padding = dimensionsOf(top = 8.dp)
//    )
//}
//
//
//private fun intListAsFloatEntryList(list: List<Int>): List<FloatEntry> {
//    val floatEntryList = arrayListOf<FloatEntry>()
//    floatEntryList.clear()
//
//    list.forEachIndexed { index, item ->
//        floatEntryList.add(entryOf(x = index.toFloat(), y = item.toFloat()))
//    }
//
//    return floatEntryList
//}









//@Composable
//internal fun rememberChartStyle(
//    columnLayerColors: List<Color>,
//    lineLayerColors: List<Color>,
//): ChartStyle {
//    val isSystemInDarkTheme = isSystemInDarkTheme()
//    return remember(columnLayerColors, lineLayerColors, isSystemInDarkTheme) {
//        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
//        ChartStyle(
//            ChartStyle.Axis(
//                axisLabelColor = Color(defaultColors.axisLabelColor),
//                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
//                axisLineColor = Color(defaultColors.axisLineColor),
//            ),
//            ChartStyle.ColumnLayer(
//                columnLayerColors.map { columnChartColor ->
//                    LineComponent(
//                        columnChartColor.toArgb(),
//                        DefaultDimens.COLUMN_WIDTH,
//                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
//                    )
//                },
//            ),
//            ChartStyle.LineLayer(
//                lineLayerColors.map { lineChartColor ->
//                    LineCartesianLayer.LineSpec(
//                        shader = DynamicShaders.color(lineChartColor),
//                        backgroundShader =
//                        DynamicShaders.fromBrush(
//                            Brush.verticalGradient(
//                                listOf(
//                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
//                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
//                                ),
//                            ),
//                        ),
//                    )
//                },
//            ),
//            ChartStyle.Marker(),
//            Color(defaultColors.elevationOverlayColor),
//        )
//    }
//}
//
//@Composable
//internal fun rememberChartStyle(chartColors: List<Color>) =
//    rememberChartStyle(columnLayerColors = chartColors, lineLayerColors = chartColors)
//

@Preview(showBackground = true)
@Composable
fun GraphScreenPreview() {
    MoodtrakerTheme {
        GraphScreen()
    }
}

