package com.kystudio.permissiondemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        findViewById<TextView>(R.id.tv_info).setOnClickListener {
            val intent = Intent()
            intent.putExtra("data", "data from SecondActivity")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}