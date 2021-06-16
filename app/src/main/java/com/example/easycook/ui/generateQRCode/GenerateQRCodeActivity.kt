package com.example.easycook.ui.generateQRCode

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class GenerateQRCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qrcode)

        /* Exemple : code pour récupérer la recette à partir de son id et l'afficher dans le Logcat"
        val bundle = this.intent.extras
        val recipeId = bundle?.getString("recipeId") ?: "Pas de recipeId trouvé"
        Log.i("TEST_ID", DataManager.getRecipeById(recipeId, this).toString())
         */

        val qrcodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)

        val content = "IdRecette"
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 512, 512)

        qrcodeImageView.setImageBitmap(bitmap)
    }

    // TODO : A modifier si besoin
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