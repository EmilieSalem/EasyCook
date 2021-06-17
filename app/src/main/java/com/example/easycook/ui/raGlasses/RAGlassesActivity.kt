package com.example.easycook.ui.raGlasses
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.easycook.MainActivity.Companion.recipeExample2
import com.example.easycook.R
import com.example.easycook.data.DataManager
import com.example.easycook.model.Recipe

class RAGlassesActivity : AppCompatActivity() {

    var recipe:Recipe? = null
    var currentStep:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raglasses)

        val tv_step = findViewById<TextView>(R.id.tv_steps)
        val btn_step = findViewById<Button>(R.id.btn_step)

        val bundle = this.intent.extras
        val recipeId = bundle?.getString("recipeId") ?: "Pas de recipeId trouvé"
        Log.i("TEST_ID", DataManager.getRecipeById(recipeId, this).toString())

        recipe = DataManager.getRecipeById(recipeId, this)

        if(recipe == null) recipe = recipeExample2
        tv_step!!.text = recipe!!.steps.get(0)

        btn_step!!.setOnClickListener {
            GoToNextStep()
            //startListeningCommand()
        }
    }

    //To implement the method vocal

    private fun startListeningCommand() {
        TODO("Not yet implemented")
    }

    private fun GoToNextStep(){

        val tv_step = findViewById<TextView>(R.id.tv_steps)
        if(currentStep+1< recipe?.steps?.size!!){
            currentStep++
            tv_step.text =  recipe!!.steps.get(currentStep)
        }else{
            Toast.makeText(this, "This is the last step", Toast.LENGTH_SHORT).show()
        }
    }

    private fun GoToLastStep(){
        val tv_step = findViewById<TextView>(R.id.tv_steps)
        if (currentStep>0){
            currentStep--
            tv_step.text =  recipe!!.steps.get(currentStep)
        }else{
            Toast.makeText(this, "This is the first step", Toast.LENGTH_SHORT).show()
        }
    }

    // TODO : A modifier si besoin
    companion object {
        fun navigateToRAGlasses(context : Context, recipeId : String) {
            val toRAGlasses = Intent(context, RAGlassesActivity::class.java)
            val bdl = Bundle()
            bdl.putString("recipeId", recipeId)
            toRAGlasses.putExtras(bdl)
            ContextCompat.startActivity(context, toRAGlasses, null)
        }
    }
}
