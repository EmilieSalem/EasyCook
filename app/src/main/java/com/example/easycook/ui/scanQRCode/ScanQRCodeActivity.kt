package com.example.easycook.ui.scanQRCode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe
import com.example.easycook.ui.recipeList.RecipeListActivity
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator


class ScanQRCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode)

        val integrator = IntentIntegrator(this)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val value = result.contents
            if (value != null) {
                Toast.makeText(this@ScanQRCodeActivity, value, Toast.LENGTH_SHORT).show()
                val recipe = Gson().fromJson(value, Recipe::class.java)
                DataManager.saveRecipe(recipe,this)
                RecipeListActivity.navigateToRecipeList(this)
            } else {
                Toast.makeText(this@ScanQRCodeActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                RecipeListActivity.navigateToRecipeList(this)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToScanQRCode(context : Context) {
            val toScanQRCode = Intent(context, ScanQRCodeActivity::class.java)
            ContextCompat.startActivity(context, toScanQRCode, null)
        }
    }
}