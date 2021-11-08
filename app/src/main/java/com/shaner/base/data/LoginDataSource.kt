package com.shaner.base.data

import com.shaner.base.data.model.LoggedInUser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val client = OkHttpClient()
    private val json: MediaType = "application/json".toMediaType()

//    val repository = LoginRepository{};
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val baseUrl = "http://10.2.2.144:8088"
            val url = "$baseUrl/"
            var responseBody = ""

            val requestBodyJson = JSONObject()
            requestBodyJson.put("username", username)
            requestBodyJson.put("password", password)
            requestBodyJson.put("Version","@string/app_version")
            val requestBody: RequestBody = requestBodyJson.toString().toRequestBody(this.json)
            val builder = Request.Builder()
            builder.url(url)
            builder.addHeader("Content-Type","application/json").post(requestBody)
            client.newCall(builder.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }
                override fun onResponse(call: Call, response: Response) {
                    responseBody = response.body!!.string()
                }
            })
            val result = LoggedInUser(java.util.UUID.randomUUID().toString(), responseBody)
            return Result.Success(result)
        }catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}