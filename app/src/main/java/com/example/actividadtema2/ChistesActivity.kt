package com.example.actividadtema2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadtema2.databinding.ActivityChistesBinding
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class ChistesActivity : AppCompatActivity() {

    private var isRunning = false
    private lateinit var confBinding : ActivityChistesBinding
    private lateinit var textToSpeech: TextToSpeech  //descriptor de voz
    private val TOUCH_MAX_TIME = 500 // en milisegundos
    private var touchLastTime: Long = 0  //para saber el tiempo entre toque.
    private var touchNumber = 0   //numero de toques dado (por si acaso). De momento no nos hace falta.
    private lateinit var handler: Handler
    val MYTAG = "LOGCAT"  //para mirar logs
    private lateinit var sharedFich : SharedPreferences
    private lateinit var idioma : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRunning = true
        confBinding = ActivityChistesBinding.inflate(layoutInflater)
        setContentView(confBinding.root)
        confBinding.btnExample.visibility = View.GONE  //ocultamos el botón.
        initPreferedShared()
        configureTextToSpeech()  //configuramos nuestro textToSpeech
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }
    private fun initPreferedShared() {
        val nameSharedFich = getString(R.string.name_prefered_shared_fich)
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
        this.idioma = sharedFich.getString("idioma_chistes", null).toString()
    }

    private fun configureTextToSpeech() {
        var chistes : Array<String>
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            val locSpanish = Locale("spa", "MEX")
            if (it == TextToSpeech.SUCCESS) {
                //conf idioma y chistes
                if (sharedFich.getString("conf_chistes", null).equals("Chistes normales")) {
                    if (idioma == "English") {
                        textToSpeech.language = Locale.ENGLISH
                        chistes = resources.getStringArray(R.array.jokes)
                    } else {
                        textToSpeech.language = locSpanish
                        chistes = resources.getStringArray(R.array.chistes)
                    }
                } else {
                    if (idioma == "English") {
                        textToSpeech.language = Locale.ENGLISH
                        chistes = resources.getStringArray(R.array.good_jokes)
                    } else {
                        textToSpeech.language = locSpanish
                        chistes = resources.getStringArray(R.array.buenos_chistes)
                    }
                }
                Log.i(MYTAG,"TextToSpeech configurado correctamente.")
                initHandler()    //lanzaremos un hilo para el progressBar. No es necesario un hilo.
                initEvent(chistes)     //Implementación del botón.
            }else {
                Log.e(MYTAG, "Error al inicializar TextToSpeech.")
            }
        })
    }



    private fun initHandler() {
        handler = Handler(Looper.getMainLooper())  //queremos que el tema de la IU, la llevemos al hilo principal.
        confBinding.progressBar.visibility = View.VISIBLE  //hacemos visible el progress

        Thread{
            Thread.sleep(3000)
            handler.post{
                confBinding.progressBar.visibility = View.GONE  //ocultamos el progress
                val description = getString(R.string.describe)
                speakMeDescription(description)  //que nos comente de qué va esto...
                Log.i(MYTAG,"Se ejecuta correctamente el hilo")
                confBinding.btnExample.visibility = View.VISIBLE

            }
        }.start()
    }

    private fun initEvent(chiste: Array<String>) {
        Thread{
            while (isRunning){
                while (textToSpeech.isSpeaking) {
                    runOnUiThread{
                    confBinding.progressBar.visibility = View.VISIBLE
                    }
                }
                runOnUiThread{
                    confBinding.progressBar.visibility = View.GONE
                }
            }
        }.start()
        confBinding.btnExample.setOnClickListener{
            //Sacamos el tiempo actual
            val currentTime = System.currentTimeMillis()
            //Comprobamos si el margen entre pulsación, da lugar a una doble pulsación.
            if (currentTime - touchLastTime < TOUCH_MAX_TIME){
                //  touchNumber=0
                executorDoubleTouch(chiste[Random.nextInt(0, 5)])  //hemos pulsado dos veces, por tanto lanzamos el chiste.
                Log.i(MYTAG,"Escuchamos el chiste")

            }
            else{
                //  touchNumber++
                Log.i(MYTAG,"Hemos pulsado 1 vez.")
                //Describimos el botón, 1 sóla pulsación
                speakMeDescription("Botón para escuchar un chiste")
            }

            touchLastTime = currentTime
        }
        confBinding.volver.setOnClickListener{
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
    private fun speakMeDescription(s: String) {
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null)

    }

    private fun executorDoubleTouch(chiste: String) {
        speakMeDescription(chiste)
    }

    override fun onDestroy() {
        //Si hemos inicializado la propiedad textToSpeech, es porque existe.
        if (::textToSpeech.isInitialized){
            textToSpeech.stop()
            textToSpeech.shutdown()

        }
        super.onDestroy()
    }



}