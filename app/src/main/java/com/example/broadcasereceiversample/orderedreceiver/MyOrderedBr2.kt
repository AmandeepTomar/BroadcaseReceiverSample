package com.example.broadcasereceiversample.orderedreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyOrderedBr2 : BroadcastReceiver() {
    private val TAG: String = MyOrderedBr2::class.java.simpleName
    private val BREAD_CRUMB = "Breadcrumb"
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = getResultExtras(true)
        var trail = bundle.getString(BREAD_CRUMB)
        trail = if (trail == null) "Start->$TAG" else "$trail->$TAG"
        bundle.putString(BREAD_CRUMB, trail)
        Log.e(TAG, "MyOrderedBr2 triggered: $trail")
    }
}