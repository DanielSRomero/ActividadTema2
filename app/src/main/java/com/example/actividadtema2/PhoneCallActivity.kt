package com.example.actividadtema2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadtema2.databinding.ActivityPhoneCallBinding


class PhoneCallActivity : AppCompatActivity() {
    private lateinit var confBinding : ActivityPhoneCallBinding
    private lateinit var sharedFich : SharedPreferences
    private lateinit var nameSharedPhone : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        confBinding = ActivityPhoneCallBinding.inflate(layoutInflater)
        setContentView(confBinding.root)
        initPreferedShared()
        initEvent()
    }

    private fun initPreferedShared() {
        val nameSharedFich = getString(R.string.name_prefered_shared_fich)
        this.nameSharedPhone = getString(R.string.name_shared_phone)
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
    }

    private fun initEvent() {
        confBinding.editTextPhone.text.insert(confBinding.editTextPhone.selectionStart, sharedFich.getString(nameSharedPhone, null))
        confBinding.call.setOnClickListener {
            if (confBinding.editTextPhone.text.isNotBlank()) {
                makePhoneCall(confBinding.editTextPhone.text.toString())
            }
            else {Toast.makeText(this, "Introduzca un numero", Toast.LENGTH_SHORT).show()}
        }
        confBinding.back.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }
    }
    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
        } else {
            startActivity(intent)
        }
    }

}