package com.example.myapplicationokay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_intent_service.*

/*  1. Crie um projeto que envia para um IntentService o texto de um TextView.
    2. onHandleIntent deve exibir o texto recebido em um Toast. */

class IntentServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_service)

        btn_ok.setOnClickListener {
            goToMainActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        callIntentService()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun callIntentService() {
        val intent = Intent(this, Grupo1IntentService::class.java)
        intent.putExtra("name", edt.text.toString())
        startService(intent)
    }

}