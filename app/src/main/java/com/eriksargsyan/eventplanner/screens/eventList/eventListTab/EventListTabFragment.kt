package com.eriksargsyan.eventplanner.screens.eventList.eventListTab

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.databinding.FragmentEventListTabBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class EventListTabFragment: BaseFragment<FragmentEventListTabBinding>({
    inflate, container -> FragmentEventListTabBinding.inflate(inflate, container, false)
}) {

    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabLayout
        viewPager = binding.pager
        tabLayoutAdapter = TabLayoutAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = tabLayoutAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.coming_label)
                    tab.icon =  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_alarm_black_24dp)
                }
                1 -> {

                    tab.text = getString(R.string.visited_label)
                    tab.icon =  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_done_black_24dp)
                }
                2 -> {
                    tab.text = getString(R.string.missed_label)
                    tab.icon =  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_close_black_24dp)
                }
            }
        }.attach()
    }

}

