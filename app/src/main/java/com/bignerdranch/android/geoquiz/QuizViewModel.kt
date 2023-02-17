package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    var currentIndex = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    var isCheater = false
    var cheated = false
    var cheatedQuestions = mutableListOf<Boolean>()
    var cheatAmount = 3

    fun initCheatProtection() {
        for (i in questionBank.indices) {
            cheatedQuestions.add(false)
        }
    }

    fun setCheatedQuestion(questionIndex: Int) {
        cheatedQuestions[questionIndex] = true
    }

    fun checkAmountCheat(): Int{
        return cheatAmount
    }

    fun cheatDecrease(){
        cheatAmount -= 1
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        currentIndex= ((currentIndex + questionBank.size-1) % questionBank.size)
    }
    fun getQSize(): Int{
        return questionBank.size
    }
}