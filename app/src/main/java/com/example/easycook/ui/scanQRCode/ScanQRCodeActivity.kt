package com.example.easycook.ui.scanQRCode

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.easycook.R

class ScanQRCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode)
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToScanQRCode(context : Context) {
            val toScanQRCode = Intent(context, ScanQRCodeActivity::class.java)
            ContextCompat.startActivity(context, toScanQRCode, null)
        }
    }
}