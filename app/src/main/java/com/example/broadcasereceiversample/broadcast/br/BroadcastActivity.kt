package com.example.broadcasereceiversample.broadcast.br

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.broadcasereceiversample.broadcast.br.customreceiver.MyCustomBr
import com.example.broadcasereceiversample.broadcast.br.orderedreceiver.MyOrderedBr1
import com.example.broadcasereceiversample.broadcast.br.orderedreceiver.MyOrderedBr2
import com.example.broadcasereceiversample.broadcast.br.orderedreceiver.MyOrderedBr3
import com.example.broadcasereceiversample.broadcast.br.receiver.MyBroadcastReceiver
import com.example.broadcasereceiversample.broadcast.databinding.ActivityBroadcastBinding

class BroadcastActivity : AppCompatActivity() {
    private var _binding: ActivityBroadcastBinding? = null
    private val binding get() = _binding

    private val broadcast = MyBroadcastReceiver()
    private val customBroadcast = MyCustomBr()

    private val orderedBr1 = MyOrderedBr1()
    private val orderedBr2 = MyOrderedBr2()
    private val orderedBr3 = MyOrderedBr3()

    val localBroadCastManager by lazy { LocalBroadcastManager.getInstance(this) }

    private val customIntent = Intent().also {
        it.addCategory(Intent.CATEGORY_DEFAULT)
        it.action = "com.mycustom.receiver"
    }
    private val customIntentFilter = IntentFilter("com.mycustom.receiver").also {
        it.addCategory(Intent.CATEGORY_DEFAULT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBroadcastBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSendCustomBroadcast?.setOnClickListener {
            sendBroadcast(customIntent)
        }

        binding?.btnSendUsingLocalBroadcast?.setOnClickListener {
            localBroadCastManager.sendBroadcast(customIntent)
        }

        binding?.btnSendOrderedBroadcast?.setOnClickListener {
            val customIntentForBr1 = Intent().also {
                it.action ="com.myorderedBr1"
                it.addCategory(Intent.CATEGORY_DEFAULT)
            }
            val customIntentForBr2 = Intent().also {
                it.action ="com.myorderedBr2"
                it.addCategory(Intent.CATEGORY_DEFAULT)
            }
            val customIntentForBr3 = Intent().also {
                it.action ="com.myorderedBr3"
                it.addCategory(Intent.CATEGORY_DEFAULT)
            }

            //sendOrderedBroadcast(customIntentForBr1,null,orderedBr2,null)
            sendOrderedBroadcast(customIntentForBr1,null)
//            sendOrderedBroadcast(customIntentForBr2,null)
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFiler = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(broadcast, intentFiler, receiverFlags)
        registerCustomBroadcast()
        registerOrderedBroadcast()
    }

    private fun registerCustomBroadcast(isRegister: Boolean = true) {
        if (isRegister) {
            registerReceiver(customBroadcast, customIntentFilter,receiverFlags)
        } else {
            unregisterReceiver(customBroadcast)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcast)
        registerCustomBroadcast(false)
        registerOrderedBroadcast(false)
    }


    val receiverFlags = if (true) {
        ContextCompat.RECEIVER_EXPORTED
    } else {
        ContextCompat.RECEIVER_NOT_EXPORTED
    }


    private fun registerOrderedBroadcast(isRegistered:Boolean=true){

        val customIntentForBr1 = IntentFilter("com.myorderedBr1").also {
            it.addCategory(Intent.CATEGORY_DEFAULT)
            it.priority = 1
        }
        val customIntentForBr2 = IntentFilter("com.myorderedBr2").also {
            it.addCategory(Intent.CATEGORY_DEFAULT)
            it.priority = 2
        }
        val customIntentForBr3 = IntentFilter("com.myorderedBr3").also {
            it.addCategory(Intent.CATEGORY_DEFAULT)
            it.priority = 3
        }

        if (isRegistered){
            registerReceiver(orderedBr1,customIntentForBr1,receiverFlags)
            registerReceiver(orderedBr2,customIntentForBr2,receiverFlags)
            registerReceiver(orderedBr3,customIntentForBr3,receiverFlags)
        }else{
            unregisterReceiver(orderedBr1)
            unregisterReceiver(orderedBr2)
            unregisterReceiver(orderedBr3)
        }
    }
}