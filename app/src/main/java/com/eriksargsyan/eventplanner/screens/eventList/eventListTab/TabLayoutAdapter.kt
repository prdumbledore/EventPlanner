package com.eriksargsyan.eventplanner.screens.eventList.eventListTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eriksargsyan.eventplanner.screens.eventList.EventListFragment
import com.eriksargsyan.eventplanner.util.Constants.ARG_OBJECT


class TabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = EventListFragment()

        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }

        return fragment
    }
}

