package com.shaner.base.data

import android.provider.Settings
import android.util.Log
import com.shaner.base.data.model.LoginResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.lang.Error
import java.util.concurrent.TimeUnit



/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val client = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build()
    private val json: MediaType = "application/json".toMediaType()

    fun login(username: String, password: String): Result<LoginResult> {
        try {
            // DONE: handle loggedInUser authentication
            val baseUrl = "http://10.2.2.144:8088"
            val url = "$baseUrl/"
            var result: LoginResult? = null
//            val android_ID = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            val requestBodyJson = JSONObject()
            requestBodyJson.put("username", username)
//            requestBodyJson.put("password", password)
            requestBodyJson.put("Version","1.0.0")
            val requestBody: RequestBody = requestBodyJson.toString().toRequestBody(this.json)
            val builder = Request.Builder()
            builder.url(url)
            builder.addHeader("Content-Type","application/json").post(requestBody)

//            fun enqueue

            client.newCall(builder.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }
                override fun onResponse(call: Call, response: Response) {

                    val responseJSON = JSONObject(response.body!!.string())
                    if (responseJSON.get("status").toString() == "200")
                    {
                        result = LoginResult("200", responseJSON.get("result").toString())

                    } else {
                        throw error(responseJSON.get("error").toString())
                    }
                }
            })
            Thread.sleep(100)

            println(result)
            return Result.Success(result)

        }catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}