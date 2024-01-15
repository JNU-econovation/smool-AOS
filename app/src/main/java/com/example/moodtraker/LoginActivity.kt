package com.example.moodtraker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.moodtraker.RetrofitInstance.BASE_URL
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.text.DateFormat

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            LoginScreen()
        }
    }
}

data class Post(
    val userId : String,
    val password : String
)

data class BaseResponse(
    val status : Int,
    val message : String
)

data class LL(
    val userPK :Int
)

object RetrofitInstance {
    val BASE_URL = "http://192.168.0.251:8080"

    
    fun getInstance() : Retrofit {



        return retrofit
    }
}
val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

val gson: Gson = GsonBuilder().serializeNulls().setDateFormat(DateFormat.LONG).create()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(client)
    .build()




interface MyApi {
    @GET("/user/login")
    suspend fun login() : Response<Post>

    @POST("/user/login")
    suspend fun login(@Body post: Post) : Response<Int>

}

val myApi: MyApi by lazy {
    retrofit.create(MyApi::class.java)
}


@Composable
fun LoginScreen(){

    val coroutineScope = rememberCoroutineScope()

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
            ),
        contentAlignment = Alignment.Center
    ) {

        var credentials by remember { mutableStateOf(Credentials()) }
        var context = LocalContext.current

        Box(

        ) {

            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .width(300.dp)
                    .offset(0.dp, (-30).dp)

            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.personback),
                    contentDescription = "person"
                )
            }

            Box(
                //contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFF5A5788), shape = RoundedCornerShape(16.dp))
                    .width(300.dp)
                    .padding(start = 32.dp, end = 32.dp, top = 48.dp, bottom = 16.dp)
                    .zIndex(0f),



                ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginField(
                        value = credentials.id,
                        onChange = { data -> credentials = credentials.copy(id = data) }
                    )
                    PasswordField(
                        value = credentials.pwd,
                        onChange = { data -> credentials = credentials.copy(pwd = data) },
                        submit = { checkCredentials(credentials, context) }
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rememberInfo(
                            label = "로그인 유지",
                            onCheckChanged = {credentials = credentials.copy(remember = credentials.remember)},
                            isChecked = credentials.remember
                        )
                        Text(
                            text = "회원가입 하기",
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.clickable {
                                // 클릭시 회원가입 화면으로 넘어가기
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {


                            coroutineScope.launch {
                                Log.d("로그인 클릭", "")
                                val loginRequest =
                                    Post(userId = credentials.id, password = credentials.pwd)




                                try {
                                    val response = myApi.login(loginRequest)
                                    Log.d("로그인 LoginActivity", "${response.body()}")
//                                    val Json = Gson().toJson(response.body())
//                                    val data= JSONObject(Json.toString())
//                                    val status = data.getInt("status")
//                                    val message = data.getString("message")

                                    Log.d("로그인 LoginActivity", "${response.body()}")
                                        Log.d("로그인 성공", "")
                                        checkCredentials(credentials, context)


                                }catch (e:Exception){
                                    Log.d("로그인 오류", "")
                                }


                            }

                        },
                        enabled = credentials.isNotEmpty(),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("로그인")
                    }

                }
            }
        }
    }
}

fun checkCredentials(creds: Credentials, context: Context) {

    if(creds.isNotEmpty() && creds.id == "abc123") {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    } else {
        Toast.makeText(context, "아이디 또는 비밀번호가 옳지 않습니다", Toast.LENGTH_SHORT).show()
    }
}

data class Credentials(var id : String = "", var pwd : String = "", var remember : Boolean = false) {
    fun isNotEmpty() : Boolean {
        return id.isNotEmpty() && pwd.isNotEmpty()
    }
}


@Composable
fun rememberInfo(
    label : String,
    onCheckChanged : () -> Unit,
    isChecked : Boolean
) {
    var checkedState by remember { mutableStateOf(isChecked) }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                onClick = onCheckChanged
            )

    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = {
                checkedState = it
                onCheckChanged()
            },
            modifier = Modifier.scale(0.8f),
            colors = CheckboxDefaults.colors(
                //checkmarkColor = Color.Red, // 체크된 상태의 색
                //checkedColor = Color.Blue, // 체크된 상태의 배경 색
                //uncheckedColor = Color.Gray, // 체크되지 않은 상태의 배경 색
            )
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(text = label, fontSize = 12.sp, color = Color.White)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
    value : String,
    onChange : (String) -> Unit,
    placeholder : String = "아이디"
) {

    val focusManager = LocalFocusManager.current

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    TextField(
        value = value,
        onValueChange = onChange,
        //modifier = Modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value : String,
    onChange : (String) -> Unit,
    placeholder : String = "비밀번호",
    submit : () -> Unit
) {

    val focusManager = LocalFocusManager.current

    var passwordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Lock,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(
            onClick = { passwordVisible = !passwordVisible },
        ) {
            Icon(
                if(passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        //modifier = Modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus();  }
            //submit()
        ),
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}


@Preview(showBackground = true)
@Composable
fun LoginActivityScreenPreview() {
    MoodtrakerTheme {
        LoginScreen()
    }
}
