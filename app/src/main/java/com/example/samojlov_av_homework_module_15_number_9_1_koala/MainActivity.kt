package com.example.samojlov_av_homework_module_15_number_9_1_koala

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samojlov_av_homework_module_15_number_9_1_koala.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageStartIV: ImageView
    private lateinit var startButtonBT: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        animation()
    }

    private fun animation() {
        val animationOne = AnimationUtils.loadAnimation(applicationContext, R.anim.combine_down)
        val animationTwo = AnimationUtils.loadAnimation(applicationContext, R.anim.combine_up)

        startButtonBT.startAnimation(animationOne)
        imageStartIV.startAnimation(animationTwo)

    }

    private fun init() {
        imageStartIV = binding.imageStartIV
        startButtonBT = binding.startButtonBT

        startButtonBT.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}