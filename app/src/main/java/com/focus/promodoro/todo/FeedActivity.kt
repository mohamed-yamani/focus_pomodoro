package com.focus.promodoro.todo

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.focus.promodoro.todo.databinding.ActivityFeedBinding
import com.focus.promodoro.todo.databinding.ActivityMainBinding
import java.lang.Exception
import kotlin.math.round

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    private var focusMinutes :Int? = null
    private var breakMinutes :Int? = null
    private var roundCount :Int? = null

    private var focusTimer : CountDownTimer? = null
    private var restTimer : CountDownTimer? = null
    private var breakTimer : CountDownTimer? = null

    private var mRound = 0
    private var isFocus = true
    private var isStop = false

    private var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        focusMinutes = intent.getIntExtra("focus", 0) * 60 * 1000
        breakMinutes = intent.getIntExtra("break", 0) * 60 * 1000
        roundCount = intent.getIntExtra("round", 0)

        binding.tvRound.text = "$mRound/$roundCount"
        setRestTimer()
        binding.ivStop.setOnClickListener {
            resetOrStart()
        }
    }

    private fun setRestTimer() {
        playSound()
        mRound++
        binding.tvStatus.text = "Get Ready"
        binding.progressBar.progress = 0
        binding.progressBar.max = 10
        restTimer = object : CountDownTimer(10500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.progress = (millisUntilFinished / 1000).toInt()
                binding.tvTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                mp?.reset()
                if (isFocus) {
                   setUpFocusView()
                } else {
                    setUpBreakView()
                }
            }
        }.start()
    }

    private fun setUpBreakView() {
        binding.tvStatus.text = "Break Time"
        binding.progressBar.max = breakMinutes!!/1000

        if (breakTimer != null)
            breakTimer = null

        setBreakTimer()
    }

    private fun setBreakTimer() {
        breakTimer = object : CountDownTimer(breakMinutes!!.toLong() + 500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.progress = (millisUntilFinished/1000).toInt()
                binding.tvTimer.text = createTimerLabel(millisUntilFinished.toInt())
            }

            override fun onFinish() {
                isFocus = true
                setRestTimer()
            }
        }.start()
    }

    private fun setUpFocusView() {
        binding.tvRound.text = "$mRound/$roundCount"
        binding.tvStatus.text = "Focus Time"
        binding.progressBar.max = focusMinutes!!/1000

        if (focusTimer != null)
            focusTimer = null

        setFocusTimer()
    }

    private fun playSound() {
        try {
            val soundUrl = Uri.parse("android.resource://com.focus.promodoro.todo/" + R.raw.alert)
            mp = MediaPlayer.create(this, soundUrl)
            mp?.isLooping = false
            mp?.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setFocusTimer() {
        focusTimer = object : CountDownTimer(focusMinutes!!.toLong() + 500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.progress = (millisUntilFinished/ 1000).toInt()
                binding.tvTimer.text = createTimerLabel(millisUntilFinished.toInt())
            }

            override fun onFinish() {
                if (mRound < roundCount!!) {
                    isFocus = false
                    setRestTimer()
                    mRound++
                } else {
                    clearAttribute()
                    binding.tvStatus.text = "You have finsh your rounds"
                }
            }
        }.start()
    }

    private fun clearAttribute() {
        binding.tvStatus.text = "Press Play Button to Restart"
        binding.ivStop.setImageResource(R.drawable.ic_play)
        binding.progressBar.progress = 0
        binding.tvTimer.text = "0"
        mRound = 1
        binding.tvRound.text = "$mRound/$roundCount"
        restTimer?.cancel()
        focusTimer?.cancel()
        mp?.reset()
        isStop = true
    }

    private fun createTimerLabel(time: Int): String {

        var timeLabel = ""
        val minutes = time / 1000 / 60
        val seconds = time / 1000 % 60

        if (minutes < 10) timeLabel += "0"
        timeLabel += "$minutes:"

        if (seconds < 10) timeLabel+= "0"
        timeLabel += "$seconds"

        return timeLabel
    }

    private fun resetOrStart() {
        if (isStop) {
            binding.ivStop.setImageResource(R.drawable.ic_stop)
            setRestTimer()
            isStop = false
        } else {
            clearAttribute()
        }
    }
}



// "https://youtu.be/5Iltdqhho5o?t=1067"