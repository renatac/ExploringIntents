package com.example.myapplicationokay

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManagerExample()

        setupPermissions()

        //Intent explícita
        button_1.setOnClickListener {
            val intent = Intent(this, ExplicitIntentActivity::class.java)
            startActivity(intent)
        }

        //Abre uma nova activity através de uma Intent implícita + Action
        button_2.setOnClickListener {
            val intent = Intent("ACTION_OPEN_ACTIVITY")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
            //Isso eu ponho no manifesto na activity que irá tratar essa intent
//            <action android:name="ACTION_OPEN_ACTIVITY" />
//            <category android:name="android.intent.category.DEFAULT" />

        }

        //Modo implícito (Intent filter + ACTION + Category) - Chamada de Uma activity de outro App
        // é + Category porque tem uma Category dentro do outro app, que no caso eu usei a default
        button_3.setOnClickListener {
            val intent = Intent("CUSTOM_CATEGORY")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        //Isso eu ponho no manifesto na activity que irá tratar essa intent, caso a action tivesse este nome
        /*<action android:name="ACTION_OPEN_ACTIVITY" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="CUSTOM_CATEGORY" />
        */

        //Abri página
        button_4.setOnClickListener {
            val intent = Intent(ACTION_VIEW).apply {
                setData(Uri.parse("https://www.google.com/"))
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        //Make a call
        button_5.setOnClickListener {
            val telNumber = "086995224983"
            val intent = Intent(ACTION_CALL).apply {
                setData(Uri.parse("tel: ${telNumber}"))
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        //Cria um alarme
        button_6.setOnClickListener {
            val intent = Intent().apply {
                setAction(AlarmClock.ACTION_SET_ALARM)
                putExtra(AlarmClock.EXTRA_MESSAGE, "Alarme")
                putExtra(AlarmClock.EXTRA_HOUR, 12)
                putExtra(AlarmClock.EXTRA_MINUTES, 30)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    fun alarmManagerExample(){
        val alarmManager =  getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, MyReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this,
            1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager?.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            2*60*1000, pendingIntent)
    }

    private fun setupPermissions() {
        val callPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        )

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i("noone", "Permission to Call has denied")
            makeCallPhoneRequest()
        }
    }

    private fun makeCallPhoneRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CALL_PHONE), 101
        )
    }
}

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"O Alarme foi executado!", Toast.LENGTH_LONG).show()
    }
}
