package com.delion.blebeacon

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.os.ParcelUuid
import android.util.Log
import java.nio.ByteBuffer
import java.util.*

class Advertiser(val adapter: BluetoothAdapter) {
    private val serviceUuid = ParcelUuid.fromString("9703e04c-991f-40e5-bff2-2234cc25788b")
    val randomIdentifier = (0..9999).random().toShort()
    private var advertising = false
    private lateinit var advertiser: BluetoothLeAdvertiser
    private val advertisingSetCallback = object : AdvertisingSetCallback() {
        override fun onAdvertisingSetStarted(advertisingSet: AdvertisingSet?, txPower: Int, status: Int) {
            super.onAdvertisingSetStarted(advertisingSet, txPower, status)
        }
    }

    init {
        if(adapter.isEnabled) {
            startAdvertising()
        }
    }

    fun onStateChanged(state: Int) {
        when(state) {
            BluetoothAdapter.STATE_ON -> {
                startAdvertising()
            }
            BluetoothAdapter.STATE_TURNING_OFF -> {
                stopAdvertising()
            }
        }
    }

    private fun startAdvertising() {
        if (!adapter.isEnabled) return
        if(advertising) {
            stopAdvertising()
        }

        val parameters = AdvertisingSetParameters.Builder()
            .setLegacyMode(true)
            .setConnectable(false)
            .setInterval(AdvertisingSetParameters.INTERVAL_MIN)
            .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
            .build()
        val data = AdvertiseData.Builder()
            .setIncludeTxPowerLevel(true)
            .addServiceData(serviceUuid, ByteBuffer.wrap(ByteArray(2)).putShort(randomIdentifier).array())
            .build()
        adapter.bluetoothLeAdvertiser.startAdvertisingSet(parameters, data, null, null, null, advertisingSetCallback)
        advertising = true
    }

    private fun stopAdvertising() {
        if (!adapter.isEnabled) return
        adapter.bluetoothLeAdvertiser.stopAdvertisingSet(advertisingSetCallback)
        advertising = false
    }

    fun dispose() {
        if(advertising) {
            stopAdvertising()
        }
    }
}