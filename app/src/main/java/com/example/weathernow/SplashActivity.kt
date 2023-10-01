package com.example.weathernow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()//so that after we press back of main activity we dont get the
            //splash screen again
        },3000//3000 milli second
        )
    }
}