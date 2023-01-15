package com.focus.promodoro.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.focus.promodoro.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var TAG = "MAINACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvStart.setOnClickListener {
            val focusTime = binding.focusEditText.text.toString()
            val breakTime = binding.breakEditText.text.toString()
            val roundCount = binding.roundEditText.text.toString()

            if (focusTime.isNotEmpty() && breakTime.isNotEmpty() && roundCount.isNotEmpty())
            {
                Log.d(TAG, focusTime.toString())
                Log.d(TAG, breakTime.toString())
                Log.d(TAG, roundCount.toString())
                val intent = Intent(this,FeedActivity::class.java)
                intent.putExtra("focus", focusTime.toInt())
                intent.putExtra("break", breakTime.toInt())
                intent.putExtra("round", roundCount.toInt())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Fill fields above!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}