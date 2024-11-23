package com.example.actividadtema2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividadtema2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var confBinding : ActivityMainBinding
    private lateinit var sharedFich : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        confBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(confBinding.root)

        val nameSharedFich = getString(R.string.name_prefered_shared_fich)
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)

        initEvent()
    }

    private fun initEvent() {
        confBinding.settings.setOnClickListener {
            sharedFich.edit().putString(getString(R.string.name_shared_phone), null).apply()
            val confIntent = Intent(this, ConfigActivity::class.java)
            startActivity(confIntent)
        }
        confBinding.call.setOnClickListener {
            val phoneCallIntent = Intent(this, PhoneCallActivity::class.java)
            startActivity(phoneCallIntent)
        }
        confBinding.url.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/DanielSRomero/ActividadTema1"))
            startActivity(urlIntent)
        }
        confBinding.alarm.setOnClickListener {
            val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_HOUR, 0)
                putExtra(AlarmClock.EXTRA_MINUTES, 2)
            }
            startActivity(alarmIntent)
        }
        confBinding.email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("danielsrq2004@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Prueba de email")
            }
            startActivity(emailIntent)
        }
        confBinding.juego.setOnClickListener {
            val juegoIntent = Intent(this, DadosActivity::class.java)
            startActivity(juegoIntent)
        }
        confBinding.chistes.setOnClickListener {
            val chistesIntent = Intent(this, ChistesActivity::class.java)
            startActivity(chistesIntent)
        }
    }
}