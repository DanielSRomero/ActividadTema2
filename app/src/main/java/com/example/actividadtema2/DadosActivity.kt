package com.example.actividadtema2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.actividadtema2.databinding.ActivityDadosBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class DadosActivity : AppCompatActivity() {
    private lateinit var bindingDados : ActivityDadosBinding
    private var sumUser : Int = 0
    private var sumDealer : Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDados = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(bindingDados.root)
        initEvent()
    }



    private fun initEvent() {
        bindingDados.button.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
        bindingDados.imageButton.setOnClickListener{
            game()
        }
    }

    private fun game(){
        sheduleRun()
    }

    private fun sheduleRun() {

        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..5){
            schedulerExecutor.schedule(
                {
                    throwDadoInTime()

                },
                msc * i.toLong(), TimeUnit.MILLISECONDS)
        }

        schedulerExecutor.schedule({
            viewResult()
        },
            msc  * 7.toLong(), TimeUnit.MILLISECONDS)

        schedulerExecutor.shutdown()

    }
    private fun throwDadoInTime() {
        val miAnimacion = AnimationUtils.loadAnimation(this, R.anim.animacion)
        val numDadosUser = Array(3){Random.nextInt(1, 6)}
        val numDadosDealer = Array(3){Random.nextInt(1, 6)}
        val imagViews : Array<ImageView> = arrayOf<ImageView>(
            bindingDados.imagviewDado1,
            bindingDados.imagviewDado2,
            bindingDados.imagviewDado3,
            bindingDados.imagviewDado4,
            bindingDados.imagviewDado5,
            bindingDados.imagviewDado6)

        sumUser = numDadosUser.sum()
        sumDealer = numDadosDealer.sum()
        for (i in 0..3) {
            selectView(imagViews[i], numDadosUser[i])
            imagViews[i].startAnimation(miAnimacion)
            selectView(imagViews[i+3], numDadosDealer[i])
            imagViews[i+3].startAnimation(miAnimacion)
        }
    }

    private fun selectView(imgV: ImageView, v: Int) {
        when (v){
            1 -> imgV.setImageResource(R.drawable.dado1);
            2 -> imgV.setImageResource(R.drawable.dado2);
            3 -> imgV.setImageResource(R.drawable.dado3);
            4 -> imgV.setImageResource(R.drawable.dado4);
            5 -> imgV.setImageResource(R.drawable.dado5);
            6 -> imgV.setImageResource(R.drawable.dado6);
        }

    }

    private fun viewResult() {
        bindingDados.txtResultado.text = sumUser.toString()
        bindingDados.txtResultadoDealer.text = sumDealer.toString()
        if (bindingDados.txtResultado.toString() > bindingDados.txtResultadoDealer.toString())
            bindingDados.resultadoJuego.text = "Victoria"
        else{
            bindingDados.resultadoJuego.text = "Derrota"
        }

        println(sumUser)
        println(sumDealer)
    }

}