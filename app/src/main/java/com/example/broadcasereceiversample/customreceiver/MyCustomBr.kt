package com.example.broadcasereceiversample.customreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyCustomBr : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Custom Broadcast Received ${intent?.categories}", Toast.LENGTH_LONG).show()
    }
}