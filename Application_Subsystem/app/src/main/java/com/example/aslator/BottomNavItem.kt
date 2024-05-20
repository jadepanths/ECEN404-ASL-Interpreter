package com.example.aslator

import android.os.Bundle

sealed class BottomNavItem( var title: String, var icon: Int, var screen_route: String ) {
    object Profile : BottomNavItem( "Settings", R.drawable.ic_profile, "settings" )
    object Lessons : BottomNavItem( "Lessons", R.drawable.ic_lessons, "lessons" )
    object Lesson01 : BottomNavItem( "LessonMat01", R.drawable.ic_lessons, "lessonmat01")
    object Lesson02 : BottomNavItem( "LessonMat02", R.drawable.ic_lessons, "lessonmat02")
    object Lesson03 : BottomNavItem( "LessonMat03", R.drawable.ic_lessons, "lessonmat03")
    object Lesson04 : BottomNavItem( "LessonMat04", R.drawable.ic_lessons, "lessonmat04")
    object Lesson05 : BottomNavItem( "LessonMat05", R.drawable.ic_lessons, "lessonmat05")
    object Lesson06 : BottomNavItem( "LessonMat06", R.drawable.ic_lessons, "lessonmat06")
    object Lesson07 : BottomNavItem( "LessonMat07", R.drawable.ic_lessons, "lessonmat07")
    object Lesson08 : BottomNavItem( "LessonMat08", R.drawable.ic_lessons, "lessonmat08")
    object Lesson09 : BottomNavItem( "LessonMat09", R.drawable.ic_lessons, "lessonmat09")
    object Lesson10 : BottomNavItem( "LessonMat10", R.drawable.ic_lessons, "lessonmat10")
    object Video : BottomNavItem( "Video", R.drawable.ic_profile, "video" )
    object Upload : BottomNavItem( "Upload", R.drawable.ic_profile, "upload" )
    object Success : BottomNavItem( "Success", R.drawable.ic_profile, "success" )
    object UploadFailure : BottomNavItem( "UploadFailure", R.drawable.ic_profile, "upload_failure" )
    object TranslationFailure : BottomNavItem( "TranslationFailure", R.drawable.ic_profile, "translation_failure" )
    object Preview : BottomNavItem( "Preview", R.drawable.ic_profile, "preview?${ARG_FILE_PATH}={$ARG_FILE_PATH}" ) {
        fun createRoute(filePath: String): String {
            return "preview?${ARG_FILE_PATH}=${filePath}"
        }

        fun getFilePath(bundle: Bundle?): String {
            return bundle?.getString(ARG_FILE_PATH)!!
        }
    }
    object Playback : BottomNavItem( "Playback", R.drawable.ic_profile, "playback?${ARG_FILE_PATH}={$ARG_FILE_PATH}" ) {
        fun createRoute(filePath: String): String {
            return "playback?${ARG_FILE_PATH}=${filePath}"
        }

        fun getFilePath(bundle: Bundle?): String {
            return bundle?.getString(ARG_FILE_PATH)!!
        }
    }

    companion object {
        const val ARG_FILE_PATH: String = "arg_file_path"
    }
}
