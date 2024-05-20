package com.example.aslator.shared.utils

import androidx.navigation.NavHostController
import com.example.aslator.BottomNavItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.util.concurrent.TimeUnit

class NetUpload() {
    var filepath : String? = null
    val uploadResponse = ServiceBuilder.buildService(PostService::class.java)
    val translateResponse = ServiceBuilder.buildService(GetService::class.java)
    val getWordResponse = ServiceBuilder.buildService(GetWordService::class.java)
    val getLessonResponse = ServiceBuilder.buildService(GetLessonService::class.java)
    val setWordResponse = ServiceBuilder.buildService(UploadWordService::class.java)
    val setLessonResponse = ServiceBuilder.buildService(UploadLessonService::class.java)
    var navHostController : NavHostController? = null
    var userStats : StatTracker? = null
    var filename : String? = null
    var username : String? = null
    var translation : String? = null

    fun setAuthUsername(name : String) {
        username = name
    }

    fun setFilePath(filePath : String) {
        filepath = filePath
    }

    fun setNavHost(navHost : NavHostController) {
        navHostController = navHost
    }

    fun setStatTracker(_userStats : StatTracker) {
        userStats = _userStats
    }

    fun networkUploadVideo() {
        if (filepath == null) {
            return
        }
        else {
            val newFile = File(filepath)
            val requestFile: RequestBody = newFile.asRequestBody("video/*".toMediaTypeOrNull())
            filename = username + "." + newFile.name
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", filename, requestFile)
            uploadResponse.uploadVideo(body).enqueue(
                object : Callback<UploadResponse> {
                    override fun onResponse(
                        call: Call<UploadResponse>,
                        response: Response<UploadResponse>
                    ) {
                        translateRetrieve()
                    }

                    override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        navHostController?.navigate(BottomNavItem.UploadFailure.screen_route)
                        filepath = null
                        username = null
                        filename = null
                    }

                }
            )
            return
        }
    }

    fun translateRetrieve() {
        if (filename == null) {
            return
        }

        var name = filename.toString().subSequence(0, filename.toString().length - 4).toString() + ".txt"

        translateResponse.getTranslation(name).enqueue(object : Callback<GetResponse> {
            override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                translation = response.body().toString()
                translation = translation.toString().subSequence(24, translation.toString().length - 1).toString()
                navHostController?.navigate(BottomNavItem.Success.screen_route)
                if (translation.equals("Not recognized. Please try again.") ) {

                }
                else if (translation.equals("Error: No keypoints generated.") ) {

                }
                else if (translation.equals("Error: Invalid Video (Can't detect signer)") ) {

                }
                else {
                    userStats?.setWords( userStats!!.getWords() + 1 )
                    wordCountSet( userStats?.getWords().toString() )
                }
                return
            }

            override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                println(t)
                filepath = null
                username = null
                filename = null
                translation = null
                navHostController?.navigate(BottomNavItem.TranslationFailure.screen_route)
                return
            }
        })

        println(translation)
        return
    }

    fun wordCountRetrieve() {
        var name = username.toString() + ".txt"
        var wordCount = ""

        getWordResponse.getWordCount(name).enqueue(object : Callback<GetWordResponse> {
            override fun onResponse(call: Call<GetWordResponse>, response: Response<GetWordResponse>) {
                wordCount = response.body().toString().subSequence(27, response.body().toString().length-1).toString()
                userStats?.setWords( wordCount.toInt() )
            }

            override fun onFailure(call: Call<GetWordResponse>, t: Throwable) {
                println(t)
                return
            }
        })

        println(wordCount)
        return
    }

    fun lessonRetrieve() {
        var name = username.toString() + ".txt"
        var lessonCount = ""

        getLessonResponse.getLessons(name).enqueue(object : Callback<GetLessonResponse> {
            override fun onResponse(call: Call<GetLessonResponse>, response: Response<GetLessonResponse>) {
                lessonCount = response.body().toString().subSequence(31, response.body().toString().length-1).toString()
                userStats?.setLessons( lessonCount.toInt() )
            }

            override fun onFailure(call: Call<GetLessonResponse>, t: Throwable) {
                println(t)
                return
            }
        })

        println(lessonCount)
        return
    }

    fun wordCountSet(wordCount : String) {
        var name = username.toString() + ".txt"
        var newWordCount = ""

        setWordResponse.setWordCount(name, wordCount).enqueue(object : Callback<UploadWordResponse> {
            override fun onResponse(call: Call<UploadWordResponse>, response: Response<UploadWordResponse>) {
                newWordCount = response.body().toString().subSequence(27, response.body().toString().length-1).toString()
                userStats?.setWords( wordCount.toInt() )
            }

            override fun onFailure(call: Call<UploadWordResponse>, t: Throwable) {
                println(t)
                return
            }
        })

        println(newWordCount)
        return
    }

    fun lessonSet(lessonCount : String) {
        var name = username.toString() + ".txt"
        var newLessonCount = ""

        setLessonResponse.setLessonCount(name, lessonCount).enqueue(object : Callback<UploadLessonResponse> {
            override fun onResponse(call: Call<UploadLessonResponse>, response: Response<UploadLessonResponse>) {
                newLessonCount = response.body().toString().subSequence(31, response.body().toString().length-1).toString()
                userStats?.setLessons( lessonCount.toInt() )
            }

            override fun onFailure(call: Call<UploadLessonResponse>, t: Throwable) {
                println(t)
                return
            }
        })

        println(newLessonCount)
        return
    }

    data class UploadResponse(
        val success : Boolean,
        val message : String,
        val filename : String,
        val filetmp : String,
        val filepath : String
    )

    data class GetResponse(
        val translation : String
    )

    data class GetWordResponse(
        val word_count : String
    )

    data class GetLessonResponse(
        val lesson_count : String
    )

    data class UploadWordResponse(
        val word_count : String
    )

    data class UploadLessonResponse(
        val lesson_count : String
    )

    interface PostService {
        @Multipart
        @POST("upload.php")
        fun uploadVideo(
            @Part file : MultipartBody.Part
        ): Call<UploadResponse>
    }

    interface GetService {
        @GET("translation.php")
        fun getTranslation(
            @Query("file") file: String
        ): Call<GetResponse>
    }

    interface GetWordService {
        @GET("get_words.php")
        fun getWordCount(
            @Query("file") file: String
        ): Call<GetWordResponse>
    }

    interface GetLessonService {
        @GET("get_lessons.php")
        fun getLessons(
            @Query("file") file: String
        ): Call<GetLessonResponse>
    }

    interface UploadWordService {
        @GET("set_words.php")
        fun setWordCount(
            @Query("file") file : String,
            @Query("count") count : String
        ): Call<UploadWordResponse>
    }

    interface UploadLessonService {
        @GET("set_lessons.php")
        fun setLessonCount(
            @Query("file") file : String,
            @Query("count") count : String
        ): Call<UploadLessonResponse>
    }

    object ServiceBuilder {
        private val interceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val client = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("http://47.218.188.73:81/") //
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }
    }
}