package com.kystudio.permissiondemo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val requestDataLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra("data") ?: "empty"
                Log.e(TAG, data)
                showToast(data)
            }
        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                showToast("已获取电话权限")
            } else {
                showToast("已拒绝电话权限")
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.READ_EXTERNAL_STORAGE]!!) {
                showToast("已获取读取权限")
            } else {
                showToast("已拒绝读取权限")
            }
            if (it[Manifest.permission.ACCESS_FINE_LOCATION]!!) {
                showToast("已获取定位权限")
            } else {
                showToast("已拒绝定位权限")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 内置ActivityResultContracts
//        ActivityResultContracts.StartActivityForResult()
//        ActivityResultContracts.StartIntentSenderForResult()
//        ActivityResultContracts.RequestMultiplePermissions()
//        ActivityResultContracts.RequestPermission()
//        ActivityResultContracts.TakePicturePreview()
//        ActivityResultContracts.TakePicture()
//        ActivityResultContracts.TakeVideo()
//        ActivityResultContracts.PickContact()
//        ActivityResultContracts.GetContent()
//        ActivityResultContracts.GetMultipleContents()
//        ActivityResultContracts.OpenDocument()
//        ActivityResultContracts.OpenMultipleDocuments()
//        ActivityResultContracts.OpenDocumentTree()
//        ActivityResultContracts.CreateDocument()

        findViewById<TextView>(R.id.tv_info1).setOnClickListener {
            requestPermission.launch(Manifest.permission.READ_PHONE_STATE)
        }

        findViewById<TextView>(R.id.tv_info2).setOnClickListener {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }

        findViewById<TextView>(R.id.tv_info3).setOnClickListener {
            requestDataLauncher.launch(Intent(this@MainActivity, SecondActivity::class.java))
        }

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setIconifiedByDefault(false)
        searchView.imeOptions = 6
//        searchView.isIconified = false
//        searchView.onActionViewExpanded()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}