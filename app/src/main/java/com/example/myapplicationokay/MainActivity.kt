package com.example.myapplicationokay

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManagerExample()
        alarmManagerWithNotification()

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

    fun alarmManagerExample() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, MyReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            2 * 60 * 1000, pendingIntent
        )
    }

    fun alarmManagerWithNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, MyReceiver2::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            10* 1000, pendingIntent
        )
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
        Toast.makeText(context, "O Alarme foi executado!", Toast.LENGTH_LONG).show()
    }
}

class MyReceiver2 : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, MainActivity2::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context!!, "2")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Minha Notificação")
            .setContentText("Alarme agendado após 10 segundos")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val not = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                "2", "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Descricao"
            not.createNotificationChannel(channel)

        }
        with(NotificationManagerCompat.from(context))
        {
            notify(2, builder.build())
        }
    }
}


