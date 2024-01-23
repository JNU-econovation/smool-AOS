package com.example.moodtraker

import android.app.Activity
import android.content.Context
import android.content.Intent
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
//import com.example.moodtraker.UserPkSet.userPk
import com.example.moodtraker.ui.theme.MoodtrakerTheme
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.text.DateFormat

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            LoginScreen()
            //SignupScreen()
        }
    }
}

data class Post(
    val userId : String,
    val password : String
)

data class JoinPost(
    val userId : String,
    val password : String,
    val checkPassword : String
)

data class BaseResponse(
    val status : Int,
    val message : String
)

data class UserResponse(
    val status : Int,
    val message : String,
    val data : UserPK
)

data class UserPK(
    val userPk : Int
)



// 캘린더

data class CalendarResponse(
    val status : Int,
    val message : String,
    val data : CalendarData
)

data class CalendarData(
    val existDates: List<ExistDate>
)

data class ExistDate(
    val localDate: String,
    val exist: Boolean
)


// 로그

data class LogResponse(
    val status : Int,
    val message : String,
    val data : Emotions
)

data class Emotions(
    val happiness : Int,
    val gloom : Int,
    val anxiety : Int,
    val stress : Int,
    val sleep : Int,
    val todayDiaries : List<DiaryContent>
)

data class DiaryContent(
    val diaryPk : Int,
    val content : String
)


// 일기 작성
data class DiaryPost(
    val localDate: String,
    val userPk : Int,
    val content : String,
    val happiness: Int,
    val gloom: Int,
    val anxiety: Int,
    val stress: Int,
    val sleep: Int
)


// 일기 읽기
data class DiaryResponse(
    val status : Int,
    val message : String,
    val data : DiaryGet
)

data class DiaryGet(
    val content : String,
    val happiness : Int,
    val gloom : Int,
    val anxiety : Int,
    val stress : Int,
    val sleep : Int
)


// 일기 수정
data class DiaryPutPost(
    val diaryPk: Int,
    val content: String,
    val happiness: Int,
    val gloom: Int,
    val anxiety: Int,
    val stress: Int,
    val sleep: Int
)



// 통계
data class GraphResponse(
    val status : Int,
    val message : String,
    val data : EmotionList
)

data class EmotionList(
    val happiness: List<HappinessList>,
    val gloom: List<GloomList>,
    val anxiety: List<AnxietyList>,
    val stress: List<StressList>,
    val sleep: List<SleepList>
)

data class HappinessList(
    val localDate: String,
    val value: Int
)

data class GloomList(
    val localDate: String,
    val value: Int
)

data class AnxietyList(
    val localDate: String,
    val value: Int
)

data class StressList(
    val localDate: String,
    val value: Int
)

data class SleepList(
    val localDate: String,
    val value: Int
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


class User {
    var userPk: Int = 0
        get() = field
        set(value) {
            field = value
        }
}


val user = User()




//object UserPkSet {
//
//    // userPk 값 초기화
//    var userPk = 0
//
//    // userPk의 getter 메서드
//    fun getUserPk(): Int {
//        return userPk
//    }
//
//    // userPk 값을 설정하는 메서드
//    fun setUserPk(value: Int) {
//        userPk = value
//    }
//}



interface MyApi {

    // 회원가입 에러
    @POST("/user/join")
    suspend fun joinError(@Body post: JoinPost) : Response<BaseResponse>


    // 회원가입
    @POST("/user/join")
    suspend fun join(@Body post: JoinPost) : Response<UserResponse>

    // 로그인
    @POST("/user/login")
    suspend fun login(@Body post: Post) : Response<UserResponse>

    // 로그아웃
    @POST("/user/logout")
    suspend fun logout() : Response<BaseResponse>

    // 캘린더 화면 표시
    @GET("/calendar/{userPk}/{dates}")
    suspend fun calendar(
        @Path("userPk") userPk: Int,
        @Path("dates") dates: String
    ) : Response<CalendarResponse>


    // 로그 화면
    @GET("calendar/{userPk}/date/{date}")
    suspend fun log(
        @Path("userPk") userPk: Int,
        @Path("date") date: String
    ) : Response<LogResponse>


    // 일기 작성 화면
    @POST("diaries")
    suspend fun write(@Body post: DiaryPost) : Response<BaseResponse>


    // 일기 읽기 화면
    @GET("diaries/{id}")
    suspend fun getlog(@Path("id") id: Int) : Response<DiaryResponse>


    // 일기 수정
    @PUT("diaries/{id}")
    suspend fun modify(
        @Path("id") id: Int,
        @Body post: DiaryPutPost
    ) : Response<BaseResponse>



    // 일기 삭제
    @DELETE("diaries/{id}")
    suspend fun delete(@Path("id") id: Int) : Response<BaseResponse>



    // 통계
    @GET("emotion/{userPk}/statistics/{dates}")
    suspend fun graph(
        @Path("userPk") userPk: Int,
        @Path("dates") dates: String
    ) : Response<GraphResponse>



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
                                    Log.d("로그인 응답 코드", "HTTP status code: ${response.code()}")
                                    Log.d("로그인 응답 성공 여부", "Is successful: ${response.isSuccessful}")


                                    if (response.isSuccessful) {
                                        // 응답이 성공적일 때

                                        val userResponse = response.body()
                                        if(userResponse != null) {
                                            val status = userResponse.status
                                            val message = userResponse.message
                                            val data = userResponse.data

                                            Log.d("로그인 LoginActivity", "${response.body()}")

                                            if (data != null) {
                                                //userPk = data.userPk

                                                user.userPk = data.userPk  // setter 메소드를 통한 userPk 변경
                                                Log.d("로그인 userPk", "userPk: ${user.userPk}") // getter 메소드를 통한 userPk 가져오기
                                                Log.d("로그인 성공", "$status $message, userPk: ${user.userPk}")

                                                context.startActivity(Intent(context, MainActivity::class.java))
                                                (context as Activity).finish()

                                                //checkCredentialJoin(credentialJoin, context)
                                            } else {
                                                Log.d("로그인 실패", "$status $message, data is null")
                                                Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
                                            }

                                        } else {
                                            Log.d("로그인 오류", "Response body is null")
                                        }

                                    } else {
                                        // 응답이 실패했을 때

                                        val errorMessage = response.errorBody()?.string()
                                        Log.d("로그인 오류 메시지", "Error message: $errorMessage")
                                        val jsonObject = JSONObject(errorMessage)
                                        val status = jsonObject.getInt("status")
                                        val message = jsonObject.getString("message")
                                        Log.d("로그인 오류 상태 코드", "Error status: $status")
                                        Log.d("로그인 오류 메시지", "Error message: $message")

                                        Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()

                                    }




//                                    val json = JSONObject(response.body().toString())
//                                    val status = json.getInt("status")
//                                    val message = json.getString("message")
//                                    val data = json.getJSONObject("data")
//                                    val userPk = data.getInt("userPk")


//                                    멘토링 코드
//                                    val Json = Gson().toJson(response.body())
//                                    val data = JSONObject(Json.toString())
//                                    val status = data.getInt("status")
//                                    val message = data.getString("message")

//
//                                    Log.d("로그인 LoginActivity", "${response.body()}")
//                                    Log.d("로그인 성공", "$status $message, userPk: $userPk")
//                                    checkCredentials(credentials, context)




                                }catch (e:Exception){
                                    Log.d("로그인 오류", "$e")
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


@Composable
fun SignupScreen(){

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

        var credentialJoin by remember { mutableStateOf(CredentialJoin()) }
        var context = LocalContext.current
        var exPwd = ""

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
                        value = credentialJoin.id,
                        onChange = { data -> credentialJoin = credentialJoin.copy(id = data) }
                    )
                    PasswordField(
                        value = credentialJoin.pwd,
                        onChange = { data -> credentialJoin = credentialJoin.copy(pwd = data) },
                        submit = {  }
                    )
                    //checkCredentials(credentialJoin, context)
                    PasswordCheckField(
                        value = credentialJoin.pwdck,
                        onChange = { data -> credentialJoin = credentialJoin.copy(pwdck = data) },
                        submit = {
//                            if (credentialJoin.pwd != credentialJoin.pwdck)
//                                Toast.makeText(context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                            //checkCredentialJoin(credentialJoin, context)
                        }
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "로그인 하기",
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.clickable {
                                // 클릭시 로그인 화면으로 넘어가기
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {


                            coroutineScope.launch {
                                Log.d("회원가입 클릭", "")
                                val joinRequest =
                                    JoinPost(userId = credentialJoin.id, password = credentialJoin.pwd, checkPassword = credentialJoin.pwdck)



                                try {

                                    Log.d("회원가입 111111", "111111")

                                    val response = myApi.join(joinRequest)
                                    Log.d("회원가입 LoginActivity", "${response.body()}")
                                    Log.d("회원가입 응답 코드", "HTTP status code: ${response.code()}")
                                    Log.d("회원가입 응답 성공 여부", "Is successful: ${response.isSuccessful}")

                                    if (response.isSuccessful) {
                                        // 응답이 성공적일 때

                                        val userResponse = response.body()
                                        if(userResponse != null) {
                                            val status = userResponse.status
                                            val message = userResponse.message
                                            user.userPk = userResponse.data.userPk

                                            Log.d("회원가입 LoginActivity", "${response.body()}")
                                            Log.d("회원가입 성공", "$status $message, userPk: $user.userPk")

                                            context.startActivity(Intent(context, MainActivity::class.java))
                                            (context as Activity).finish()

                                            //checkCredentialJoin(credentialJoin, context)
                                        } else {
                                            Log.d("회원가입 오류", "Response body is null")
                                        }

                                    } else {
                                        // 응답이 실패했을 때

                                        val errorMessage = response.errorBody()?.string()
                                        Log.d("회원가입 오류 메시지", "Error message: $errorMessage")
                                        val jsonObject = JSONObject(errorMessage)
                                        val status = jsonObject.getInt("status")
                                        val message = jsonObject.getString("message")
                                        Log.d("회원가입 오류 상태 코드", "Error status: $status")
                                        Log.d("회원가입 오류 메시지", "Error message: $message")

                                        Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()

                                    }



//                                    val json = JSONObject(response.body().toString())
//                                    val status = json.getInt("status")
//                                    val message = json.getString("message")
//                                    val data = json.getJSONObject("data")
//                                    val userPk = data.getInt("userPk")


//                                    val Json = Gson().toJson(response.body())
//                                    val json = JSONObject(Json.toString())
//                                    val status = json.getInt("status")
//                                    val message = json.getString("message")
//                                    val data = json.getJSONObject("data")
//                                    val userPk = data.getInt("userPk")


//                                    멘토링 코드
//                                    val Json = Gson().toJson(response.body())
//                                    val data = JSONObject(Json.toString())
//                                    val status = data.getInt("status")
//                                    val message = data.getString("message")



                                }catch (e:Exception){
                                    Log.d("회원가입 오류", "$e")

                                }


                            }

                        },
                        enabled = credentialJoin.isNotEmpty(),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("회원가입")
                    }

                }
            }
        }
    }
}







// 로그아웃 버튼 클릭시
//coroutineScope.launch {
//    Log.d("회원가입 클릭", "")
////                                val loginRequest =
////                                    Post(userId = credentials.id, password = credentials.pwd)
//
//    try {
//        val response = myApi.logout()
//        Log.d("회원가입 LoginActivity", "${response.body()}")
//        val Json = Gson().toJson(response.body())
//        val data = JSONObject(Json.toString())
//        val status = data.getInt("status")
//        val message = data.getString("message")
//
//        Log.d("회원가입 LoginActivity", "${response.body()}")
//        Log.d("회원가입 성공", "")
//        Log.d("회원가입", "$status $message")
//        checkCredentials(credentials, context)
//
//
//    }catch (e:Exception){
//        Log.d("회원가입 오류", "")
//        Log.d("$status", "")
//    }
//
//
//}



fun checkCredentials(creds: Credentials, context: Context) {

    if(creds.isNotEmpty() && creds.id == "abc123") {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    } else {
        Toast.makeText(context, "아이디 또는 비밀번호가 옳지 않습니다", Toast.LENGTH_SHORT).show()
    }
}

fun checkCredentialJoin(creds: CredentialJoin, context: Context) {

    if(creds.isNotEmpty()) {
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

data class CredentialJoin(var id : String = "", var pwd : String = "", var pwdck : String = "", var remember : Boolean = false) {
    fun isNotEmpty() : Boolean {
        return id.isNotEmpty() && pwd.isNotEmpty() && pwdck.isNotEmpty()
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
            //tint = Color.White
            tint = MaterialTheme.colorScheme.primary
        )
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = VisualTransformation.None,
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = Color(0xFF48447A),
//        ),
        //textStyle = LocalTextStyle.current.copy(color = Color.White)
    )

//    BasicTextField(
//        value = value,
//        onValueChange = onChange,
//        modifier = Modifier
//            .padding(horizontal = 28.dp)
//            .fillMaxWidth()
//            .padding(16.dp)
//            .background(color = Color(0xFF48447A)),
//        textStyle = LocalTextStyle.current.copy(color = Color.White),
//        singleLine = true,
//        visualTransformation = VisualTransformation.None,
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = ImeAction.Next
//        ),
//        keyboardActions = KeyboardActions(
//            onNext = { focusManager.moveFocus(FocusDirection.Down) }
//        )
//    ) {
//        if (value.isEmpty()) {
//            Text(
//                text = placeholder,
//                color = Color.White,
//                style = LocalTextStyle.current.copy(
//                    color = Color.White
//                )
//            )
//        }
//    }
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


@Composable
fun PasswordCheckField(
    value : String,
    onChange : (String) -> Unit,
    placeholder : String = "비밀번호 확인",
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
            onDone = { focusManager.clearFocus(); }

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
