package com.qr.kou.trafficstats

import android.app.Service
import android.app.usage.NetworkStatsManager
import android.content.Intent
import android.graphics.PixelFormat
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.qr.kou.trafficstats.databinding.TrafficLayoutBinding

class FloatTrafficViewService : Service(), Runnable {
    private lateinit var binding: TrafficLayoutBinding
    private lateinit var windowManager: WindowManager
    private lateinit var networkStatsManager: NetworkStatsManager
    private lateinit var handler: Handler

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        networkStatsManager = getSystemService(NETWORK_STATS_SERVICE) as NetworkStatsManager
        binding = TrafficLayoutBinding.inflate(LayoutInflater.from(this))

        showFloatWindow()

        handler.postDelayed(this, 1000)
    }


    private fun showFloatWindow() {
        val layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

            gravity = Gravity.CENTER or Gravity.TOP

            format = PixelFormat.RGBA_8888

            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }

        windowManager.addView(binding.root, layoutParams)

    }

    override fun onDestroy() {
        super.onDestroy()

        windowManager.removeView(binding.root)
    }

    private fun updateTraffic() {
        val bucket = networkStatsManager.querySummaryForDevice(
            ConnectivityManager.TYPE_WIFI,
            "",
            0,
            System.currentTimeMillis()
        )

        val detail = "rx: ${bucket.rxBytes} tx: ${bucket.txBytes}"
        binding.tvTraffic.text = detail

        handler.postDelayed(this, 1000)

    }

    override fun run() {
        updateTraffic()
    }

}