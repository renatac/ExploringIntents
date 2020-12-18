package com.example.myapplicationokay

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast

const val TAG = "Grupo1IntentService"

class Grupo1IntentService : IntentService(TAG) {
    override fun onHandleIntent(intent: Intent?) {

        try {
//          Thread.sleep(5000)
            val name = intent?.getStringExtra("name")
            val handler = Handler()
            handler.post {
                Toast.makeText(this, "$name", Toast.LENGTH_LONG).show()
            }
        }catch (e:Exception) {
            Log.d(TAG, "${e.message}",e)
        }

    }
}