package com.kystudio.permissiondemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_info).setOnClickListener {
            doSomething()
        }
    }

    private fun doSomething() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermission()
        } else {
            showToast()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndRequestPermission() {
        val lackedPermission: MutableList<String> = ArrayList()
        val needCheckPermissions = needCheckPermissions
        for (permission in needCheckPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                lackedPermission.add(permission)
            }
        }

        if (lackedPermission.size == 0) {
            showToast()
        } else {
            val requestPermissions = lackedPermission.toTypedArray()
            requestPermissions(requestPermissions, 1024)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1024) {
            showToast()
        }
    }

    private val needCheckPermissions: List<String>
        get() = listOf(
            Manifest.permission.READ_PHONE_STATE
        )

    private fun showToast() {
        Toast.makeText(this@MainActivity, "已获取权限", Toast.LENGTH_SHORT).show()
    }
}