package com.example.aslator.shared.utils

class StatTracker() {
    private var lessonsCompleted : Int? = null
    private var wordsTranslated : Int? = null

    fun setLessons(_input : Int) {
        lessonsCompleted = _input
        return
    }

    fun getLessons(): Int {
        if (lessonsCompleted == null) {
            return 0
        }
        return lessonsCompleted!!.toInt()
    }

    fun setWords(_input : Int) {
        wordsTranslated = _input
        return
    }

    fun getWords() : Int {
        if (wordsTranslated == null) {
            return 0
        }
        return wordsTranslated!!.toInt()
    }
}