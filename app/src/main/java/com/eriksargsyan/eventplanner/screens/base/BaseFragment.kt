package com.eriksargsyan.eventplanner.screens.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment<BINDING: ViewBinding>(
    val inflateFun: (
        inflate: LayoutInflater, container: ViewGroup?
    ) -> BINDING
) : Fragment() {

    private var _binding: BINDING? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateFun(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}