package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.Math.abs

private const val TAG = "MainActivity1"
private const val KEY_INDEX = "index"
private const val CHEAT_INDEX = "CHEAT"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private lateinit var cheatAmountTextView: TextView
    private var results= 0


    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        val cheater =savedInstanceState?.getBoolean(CHEAT_INDEX, false) ?: false
        quizViewModel.isCheater = cheater


        Log.d(TAG, "onCreate(Bundle?) called lol")
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatAmountTextView = findViewById(R.id.cheat_amount)

        cheatButton.setOnClickListener {
            // Начало CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            val options = ActivityOptions
                .makeClipRevealAnimation(it, 1000, 50, it.width, it.height)
            startActivityForResult(intent,REQUEST_CODE_CHEAT,options.toBundle())
            quizViewModel.cheatDecrease()
            if (quizViewModel.checkAmountCheat()<=0){
                cheatButton.isEnabled = false
            }
            cheatAmountTextView.text = quizViewModel.checkAmountCheat().toString()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }


        prevButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        questionTextView.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }
        updateQuestion()
        quizViewModel.initCheatProtection()


    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.cheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false

            if (quizViewModel.cheated) {
                quizViewModel.setCheatedQuestion(quizViewModel.currentIndex)
            }
        }
        
    }

    private fun checkAnswer(userAnswer: Boolean){

        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.cheatedQuestions[quizViewModel.currentIndex] -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (messageResId == R.string.correct_toast)
            results+=1
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putBoolean(CHEAT_INDEX,quizViewModel.isCheater)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


}
