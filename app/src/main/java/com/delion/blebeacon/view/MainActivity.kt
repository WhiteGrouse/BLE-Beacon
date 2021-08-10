package com.delion.blebeacon.view

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.delion.blebeacon.Advertiser
import com.delion.blebeacon.R
import com.delion.blebeacon.navigateTo
import com.delion.blebeacon.view.fragment.AdvertisingFragment
import com.delion.blebeacon.view.fragment.DisabledFragment
import com.delion.blebeacon.view.fragment.RequestPermissionsFragment
import com.delion.blebeacon.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent!!.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    when(state) {
                        BluetoothAdapter.STATE_ON -> {
                            navigateTo(supportFragmentManager, AdvertisingFragment())
                        }
                        BluetoothAdapter.STATE_OFF -> {
                            navigateTo(supportFragmentManager, DisabledFragment())
                        }
                    }
                    viewModel.advertiser?.onStateChanged(state)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigateTo(supportFragmentManager, RequestPermissionsFragment())
        }
        registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    fun onPermissionGranted() {
        viewModel.advertiser = Advertiser(BluetoothAdapter.getDefaultAdapter()!!)
        if (viewModel.advertiser!!.adapter.isEnabled) {
            navigateTo(supportFragmentManager, AdvertisingFragment())
        }
        else {
            navigateTo(supportFragmentManager, DisabledFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}