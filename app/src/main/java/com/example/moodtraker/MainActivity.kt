package com.example.moodtraker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moodtraker.screen.CalendarScreen
import com.example.moodtraker.screen.GraphScreen
import com.example.moodtraker.screen.LogScreen
import com.example.moodtraker.ui.theme.MoodtrakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodtrakerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //color = MaterialTheme.colorScheme.backgroundv
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF3F274A)
                ) {
                    AppMain()
                    //LogoScreen()
                    //CalendarScreen()
                    //LogScreen()

                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMain(){

    val navController = rememberNavController()

    val navItems = listOf(
        NavigationItem("캘린더", Icons.Default.DateRange, "tab1"),
        NavigationItem("통계", Icons.Default.Leaderboard, "tab2"),
        NavigationItem("설정", Icons.Default.Settings, "tab3")
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF6265B1)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    navItems.forEach { nav ->

                        // 현재 route
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                navController.navigate(nav.route)
                            }
                        ) {

                            // True -> 현재 클릭된 탭
                            // False -> 현재 클릭되지 않은 탭
                            val isCurrentRoute = nav.route == currentRoute

                            Icon(
                                imageVector = nav.icon,
                                contentDescription = nav.name,
                                tint = if (isCurrentRoute) Color.White else Color(0xFFD4D6E9),
                                modifier = Modifier.size(30.dp)
                            )
//                            Text(
//                                text = nav.name,
//                                color = if (isCurrentRoute) Color.White else Color(0xFFD4D6E9)
//                            )
                        }
                    }
                }
            }
        }

    ) {paddingValues ->

        NavHost(
            navController = navController,
            startDestination = navItems.first().route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("tab1") {
                CalendarScreen()
            }
            composable("tab2") {
                LogScreen()
            }
            composable("tab3") {
                GraphScreen()
            }
        }

//        Box(
//            modifier = Modifier.padding(paddingValues)
//        ) {
//
//        }

    }

}

data class NavigationItem(
    val name : String,
    val icon : ImageVector,
    val route : String
)



@Composable
fun LogoScreen(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ImageLogo()


    }
}

@Composable
fun ImageLogo(){
    Card(
        shape = RoundedCornerShape(8.dp),
        //elevation = 5.dp
    ) {
        Box(
            modifier = Modifier.size(100.dp)

        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )


        }
    }
}


//@Composable
//fun navigationBar(){
//    var selectedItem by remember { mutableIntStateOf(0) }
//    val items = listOf("Songs", "Artists", "Playlists")
//
//    NavigationBar {
//        items.forEachIndexed { index, item ->
//            NavigationBarItem(
//                icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
//                label = { Text(item) },
//                selected = selectedItem == index,
//                onClick = { selectedItem = index }
//            )
//        }
//    }
//}


//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MoodtrakerTheme {
//        Greeting("Android")
//    }
//}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    MoodtrakerTheme {
        //CalendarScreen()
    }
}