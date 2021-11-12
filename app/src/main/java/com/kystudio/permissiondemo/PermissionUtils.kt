package com.kystudio.permissiondemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

object PermissionUtils {
    fun jumpPermissionPage(context: Context) {
        when (Build.MANUFACTURER.lowercase(Locale.getDefault())) {
            "xiaomi" -> {
                goXiaoMiManager(context)
            }
            "huawei" -> {
//                goHuaWeiManager(context)
                goAppDetailSetting(context)
            }
            "oppo" -> {
                goOppoManager(context)
            }
            "meizu" -> {
                goMeizuManager(context)
            }
            "samsung" -> {
                goSamsungManager(context)
            }
            "sony" -> {
                goSonyManager(context)
            }
            "lg" -> {
                goLGManager(context)
            }
            "letv" -> {
                goLetvManager(context)
            }
            "qiku", "360" -> {
                go360Manager(context)
            }
//            "vivo" -> {}//vivo
//            "yulong" -> {}//酷派
//            "lenovo" -> {}//联想
//            "zte" -> {}//中兴
//            "google" -> {}//谷歌
            else -> {
                goAppDetailSetting(context)
            }
        }
    }

    private fun goIntentSetting(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
    }

    private fun goAppDetailSetting(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName(
                "com.android.settings",
                "com.android.settings.InstalledAppDetails"
            )
            localIntent.putExtra(
                "com.android.settings.ApplicationPkgName",
                BuildConfig.APPLICATION_ID
            )
        }
        try {
            context.startActivity(localIntent)
        } catch (e: Exception) {
//            e.printStackTrace()
//            AppUtils.topActivity?.showTip(context.resources.getString(R.string.permission_setting_error))
        }
    }

    private fun getMiuiVersion(): String? {
        val propName = "ro.miui.ui.version.name"
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(
                InputStreamReader(p.inputStream), 1024
            )
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } finally {
            input?.close()
        }
        return line
    }

    //小米系统有以下几个版本需要适配
    private fun goXiaoMiManager(context: Context) {
        val rom: String? = getMiuiVersion()
        try {
            var intent = Intent()
            if ("V5" == rom) {
                val packageURI = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
            } else if ("V6" == rom || "V7" == rom) {
                intent.action = "miui.intent.action.APP_PERM_EDITOR"
                intent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                )
                intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID)
            } else if ("V8" == rom || "V9" == rom) {
                intent.action = "miui.intent.action.APP_PERM_EDITOR"
                intent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity"
                )
                intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID)
            } else {
                goAppDetailSetting(context)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    private fun goHuaWeiManager(context: Context) {
        try {
            val intent = Intent(BuildConfig.APPLICATION_ID)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    private fun goOppoManager(context: Context) {
        try {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
//            val comp = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionAppAllPermissionActivity")//R9SK 6.0.1  os-v3.0
            val comp = ComponentName(
                "com.coloros.securitypermission",
                "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity"
            )//R11t 7.1.1 os-v3.2
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    private fun goMeizuManager(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    private fun goSamsungManager(context: Context) {
        //如果使用同样的跳转包名类名跳转方式，三星会报这个问题
        //java.lang.SecurityException: requires android.permission.GRANT_RUNTIME_PERMISSIONS
        //目前选择直接跳转应用信息界面
        goAppDetailSetting(context)
    }

    private fun goSonyManager(context: Context) {
        try {
            val intent = Intent(BuildConfig.APPLICATION_ID)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    private fun goLGManager(context: Context) {
        try {
            val intent = Intent("android.intent.action.MAIN")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
            val comp = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    private fun goLetvManager(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
            val comp = ComponentName(
                "com.letv.android.letvsafe",
                "com.letv.android.letvsafe.PermissionAndApps"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }

    //360只能打开到自带安全软件
    private fun go360Manager(context: Context) {
        try {
            val intent = Intent("android.intent.action.MAIN")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
            val comp = ComponentName(
                "com.qihoo360.mobilesafe",
                "com.qihoo360.mobilesafe.ui.index.AppEnterActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goAppDetailSetting(context)
        }
    }
}