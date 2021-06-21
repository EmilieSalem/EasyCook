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
import com.example.easycook.model.Ingredient
import com.example.easycook.model.Recipe
import com.example.easycook.model.Tag
import github.com.vikramezhil.dks.speech.Dks;
import github.com.vikramezhil.dks.speech.DksListener;


class RAGlassesActivity : AppCompatActivity() {
    private lateinit var dks:Dks

    var recipe:Recipe? = null
    var currentStep:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raglasses)

        val tv_step = findViewById<TextView>(R.id.tv_steps)
        val btn_speech = findViewById<Button>(R.id.btn_speech)
        val btn_next = findViewById<Button>(R.id.btn_step)
        val btn_last = findViewById<Button>(R.id.btn_step_last)

        val bundle = this.intent.extras
        val recipeId = bundle?.getString("recipeId") ?: "Pas de recipeId trouvé"
        Log.i("TEST_ID", DataManager.getRecipeById(recipeId, this).toString())

        /*
        get recipe by id from API ou preference
         */


        if(recipe == null) recipe = recipeExample2
        tv_step!!.text = recipe!!.steps.get(0)

        dks = Dks(application, supportFragmentManager, object: DksListener{
            override fun onDksFinalSpeechResult(speechResult: String) {
                Log.d(application.packageName, "Final Speech result - $speechResult")

                when(traitement(speechResult)){
                    "suivante"->GoToNextStep()
                    "précédente"->GoToLastStep()
                }

            }

            override fun onDksLanguagesAvailable(
                defaultLanguage: String?,
                supportedLanguages: ArrayList<String>?
            ) {
                Log.d(application.packageName, "defaultLanguage - $defaultLanguage")
                Log.d(application.packageName, "supportedLanguages - $supportedLanguages")
                if (supportedLanguages != null && supportedLanguages.contains("fr-FR")) {
                    Log.d(application.packageName, "Speech setLanguage")
                    // Setting the speech recognition language to english india if found
                    dks.currentSpeechLanguage = "fr-FR"
                }
            }

            override fun onDksLiveSpeechFrequency(frequency: Float) {
            }

            override fun onDksLiveSpeechResult(liveSpeechResult: String) {
                Log.d(application.packageName, "Speech result - $liveSpeechResult")

            }

            override fun onDksSpeechError(errMsg: String) {
                Toast.makeText(application, errMsg, Toast.LENGTH_SHORT).show()
            }

        })

//        dks.injectProgressView(R.layout.layout_pv_inject)
        //dks.oneStepResultVerify = true
        dks.continuousSpeechRecognition=true
        dks.startSpeechRecognition()

        btn_speech.setOnClickListener {

        }

        btn_last.setOnClickListener {
            GoToLastStep()
        }

        btn_next.setOnClickListener {
            GoToNextStep()
        }
    }


    //To implement the method vocal

    private fun startListeningCommand() {
    }

    private fun GoToNextStep(){

        var tv_step = findViewById<TextView>(R.id.tv_steps)
        if(currentStep+1< recipe?.steps?.size!!){
            currentStep++
            tv_step.text =  recipe!!.steps.get(currentStep)
        }else{
            Toast.makeText(this, "This is the last step", Toast.LENGTH_SHORT).show()
        }
    }

    private fun GoToLastStep(){
        var tv_step = findViewById<TextView>(R.id.tv_steps)
        if (currentStep>0){
            currentStep--
            tv_step.text =  recipe!!.steps.get(currentStep)
        }else{
            Toast.makeText(this, "This is the first step", Toast.LENGTH_SHORT).show()
        }
    }

    private fun traitement(str:String):String{
        val listsSubstirng = str.split(' ')
        Log.i("Traitement", "Number of words {${listsSubstirng.size}}")
        return when{
            listsSubstirng.contains("suivante")->"suivante"
            listsSubstirng.contains("précédente")->"précédente"
            else -> ""
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