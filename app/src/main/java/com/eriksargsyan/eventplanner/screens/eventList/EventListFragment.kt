package com.eriksargsyan.eventplanner.screens.eventList

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eriksargsyan.eventplanner.databinding.FragmentEventListBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment


class EventListFragment : BaseFragment<FragmentEventListBinding>({ inflate, container ->
    FragmentEventListBinding.inflate(inflate, container, false)
}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            eventAdderFAB.setOnClickListener{
                findNavController().navigate(
                    EventListFragmentDirections.actionEventListFragmentToEventAddAndEditFragment()
                )
            }

            recyclerEventList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        // Scroll Down
                        if (eventAdderFAB.isShown) {
                            eventAdderFAB.hide()
                        }
                    } else if (dy < 0) {
                        // Scroll Up
                        if (!eventAdderFAB.isShown) {
                            eventAdderFAB.show()
                        }
                    }
                }
            })

        }


    }
}