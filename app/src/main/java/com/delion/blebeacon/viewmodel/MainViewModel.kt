package com.delion.blebeacon.viewmodel

import androidx.lifecycle.ViewModel
import com.delion.blebeacon.Advertiser

class MainViewModel : ViewModel() {
    var advertiser: Advertiser? = null

    override fun onCleared() {
        super.onCleared()
        advertiser?.dispose()
    }
}