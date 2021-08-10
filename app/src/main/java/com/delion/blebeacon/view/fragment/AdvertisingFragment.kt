package com.delion.blebeacon.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.delion.blebeacon.R
import com.delion.blebeacon.databinding.FragmentAdvertisingBinding
import com.delion.blebeacon.viewmodel.MainViewModel

class AdvertisingFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentAdvertisingBinding.inflate(inflater, container, false)
        binding.uuid.text = "%04d".format(viewModel.advertiser!!.randomIdentifier)
        return binding.root
    }
}