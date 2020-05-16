package com.example.tapprogress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.library.TapProgress
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
