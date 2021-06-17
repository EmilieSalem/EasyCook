package com.example.easycook.ui.generateQRCode

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class GenerateQRCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qrcode)

        val qrcodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
        val bundle = this.intent.extras
        val recipeId = bundle?.getString("recipeId") ?: "Pas de recipeId trouvé"

        val recipe = DataManager.getRecipeById(recipeId, this)
        val recipeJson = Gson().toJson(recipe)

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(recipeJson, BarcodeFormat.QR_CODE, 512, 512)

        qrcodeImageView.setImageBitmap(bitmap)

        // Toast.makeText(this@GenerateQRCodeActivity, recipeJson, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun navigateToGenerateQRCode(context : Context, recipeId : String) {
            val toGenerateQRCode = Intent(context, GenerateQRCodeActivity::class.java)
            val bdl = Bundle()
            bdl.putString("recipeId", recipeId)
            toGenerateQRCode.putExtras(bdl)
            ContextCompat.startActivity(context, toGenerateQRCode, null)
        }
    }
}