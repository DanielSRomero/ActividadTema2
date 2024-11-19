package com.example.actividadtema2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadtema2.databinding.ActivityConfigBinding


class ConfigActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var confBinding : ActivityConfigBinding
    private lateinit var sharedFich : SharedPreferences
    private lateinit var nameSharedPhone : String
    private lateinit var confChistes : String
    private lateinit var idiomaChistes : String


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
        this.confChistes = getString(R.string.conf_chistes)
        this.idiomaChistes = getString(R.string.idioma_chistes)
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
    }

    private fun initEvent(){
        confBinding.textView.visibility = View.INVISIBLE
        confBinding.spinner.visibility = View.INVISIBLE
        confBinding.gustos.visibility = View.INVISIBLE

        val sharedPreferences : String? = sharedFich.getString(nameSharedPhone, null)
        sharedPreferences?.let { startMainActivity() }

        //HABILITAR CONFIGURACIÓN AVANZADA:
        confBinding.toggleButton.setOnClickListener {
            if (confBinding.toggleButton.isChecked) {
                confBinding.checkBox.isChecked = false
                confBinding.spinner.visibility = View.VISIBLE
                confBinding.gustos.visibility = View.VISIBLE
                confBinding.checkBox.visibility = View.VISIBLE
                confBinding.radioGroup.visibility = View.INVISIBLE


                //CONFIGURACION SPINNER
                ArrayAdapter.createFromResource(
                    this,
                    R.array.idiomas,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    confBinding.spinner.adapter = adapter
                }
                confBinding.spinner.onItemSelectedListener = this

                //CONFIGURACION CHECKBOX
                confBinding.checkBox.setOnClickListener{
                    if (confBinding.checkBox.isChecked){
                        confBinding.radioGroup.visibility = View.VISIBLE

                    }
                    else {
                        confBinding.radioGroup.visibility = View.INVISIBLE
                    }
                }
            } else {
                confBinding.textView.visibility = View.INVISIBLE
                confBinding.spinner.visibility = View.INVISIBLE
                confBinding.checkBox.visibility = View.INVISIBLE
                confBinding.radioGroup.visibility = View.INVISIBLE
            }
        }
            confBinding.guardar.setOnClickListener {
                if (confBinding.tel.text.isNotBlank()) {
                    sharedFich.edit().putString(
                        nameSharedPhone, confBinding.tel.text.toString()).apply()
                sharedFich.edit().putString(
                    confChistes, findViewById<RadioButton>(
                        confBinding.radioGroup.checkedRadioButtonId).text.toString()).apply()
                    startMainActivity()            }
                else {
                    Toast.makeText(this, "Introduzca un numero", Toast.LENGTH_SHORT).show()}
            }
    }
    private fun startMainActivity() {
        val intent = Intent(this@ConfigActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sharedFich.edit().putString(
            idiomaChistes, confBinding.spinner.getItemAtPosition(position).toString()).apply()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        sharedFich.edit().putString(
            idiomaChistes, "Español").apply()
    }
}