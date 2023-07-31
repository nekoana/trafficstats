package com.qr.kou.trafficstats

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qr.kou.trafficstats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGrantPermission.setOnClickListener {
            //float window permission
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)

            startActivity(intent)
        }

        binding.btnGrantTrafficPermission.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

        binding.btnStartService.setOnClickListener {
            startService(Intent(this, FloatTrafficViewService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }
}