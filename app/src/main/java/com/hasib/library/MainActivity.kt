package com.hasib.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.library.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tapProgress?.setTapProgressCompletedListener(object : TapProgress.TapProgressCompletedListener{
            override fun onTapProgressCompleted() {
                //write your code
            }

        })
    }
}
