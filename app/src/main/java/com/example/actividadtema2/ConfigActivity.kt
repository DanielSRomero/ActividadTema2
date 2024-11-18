package com.example.actividadtema2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadtema2.databinding.ActivityConfigBinding


class ConfigActivity : AppCompatActivity() {
    private lateinit var confBinding : ActivityConfigBinding
    private lateinit var sharedFich : SharedPreferences
    private lateinit var nameSharedPhone : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        confBinding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(confBinding.root)

        initPreferedShared()
        initEvent()
    }

    private fun initPreferedShared() {
        val nameSharedFich = getString(R.string.name_prefered_shared_fich)
        this.nameSharedPhone = getString(R.string.name_shared_phone)
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
    }

    private fun initEvent(){
        val sharedPreferences : String? = sharedFich.getString(nameSharedPhone, null)
        sharedPreferences?.let { startMainActivity() }

        confBinding.guardar.setOnClickListener {
            if (confBinding.tel.text.isNotBlank()) {
                sharedFich.edit().putString(
                    nameSharedPhone, confBinding.tel.text.toString()).apply()
                startMainActivity()            }
            else {
                Toast.makeText(this, "Introduzca un numero", Toast.LENGTH_SHORT).show()}
        }
    }
    private fun startMainActivity() {
        val intent = Intent(this@ConfigActivity, MainActivity::class.java)
        startActivity(intent)
    }
}