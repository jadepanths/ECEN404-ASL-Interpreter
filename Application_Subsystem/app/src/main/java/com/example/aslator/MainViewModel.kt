package com.example.aslator

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.shared.utils.StatTracker

class MainViewModel : ViewModel() {
    var appJustLaunched by mutableStateOf( true )
    var userIsAuthenticated by mutableStateOf( false )

    var user by mutableStateOf( User() )

    private val TAG = "MainViewModel"
    private lateinit var account: Auth0
    private lateinit var context: Context

    var uploadManager : NetUpload ? = null
    var navHostController : NavHostController? = null
    var userStats : StatTracker? = null

    fun setUploadManagerHere(_uploadManager : NetUpload) {
        uploadManager = _uploadManager
    }

    fun setNavHost(navHost : NavHostController) {
        navHostController = navHost
    }

    fun setStatTracker(_userStats : StatTracker) {
        userStats = _userStats
    }

    fun setContext( activityContext: Context ) {
        context = activityContext
        account = Auth0(
            context.getString( R.string.com_auth0_client_id),
            context.getString( R.string.com_auth0_domain),
        )
    }

    fun login() {
        WebAuthProvider
            .login( account )
            .withScheme( context.getString( R.string.com_auth0_scheme ) )
            .start( context, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure( error: AuthenticationException ) {
                    Log.d( TAG, "Error occurred in login(): ${ error.toString() }" )
                }

                override fun onSuccess( result: Credentials ) {
                    val idToken = result.idToken
                    Log.d( TAG, "ID Token: $idToken" )
                    user = User( idToken )
                    userIsAuthenticated = true
                    appJustLaunched = false
                    uploadManager?.setAuthUsername(user.email)
                    uploadManager?.wordCountRetrieve()
                    uploadManager?.lessonRetrieve()
                    navHostController?.navigate( BottomNavItem.Profile.screen_route )
                }

            } )
    }

    fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    // For some reason, logout failed.
                    Log.d(TAG, "Error occurred in logout(): ${error.toString()} ")
                }

                override fun onSuccess(result: Void?) {
                    // The user successfully logged out.
                    user = User()
                    userIsAuthenticated = false
                    userStats?.setWords(0)
                    userStats?.setLessons(0)
                }

            })
    }
}