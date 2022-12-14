package com.eriksargsyan.eventplanner.screens.eventList

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.appComponent
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.databinding.FragmentEventListBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import com.eriksargsyan.eventplanner.screens.eventList.eventListTab.EventListTabFragmentDirections
import com.eriksargsyan.eventplanner.util.Constants.ARG_OBJECT
import com.google.android.material.transition.MaterialElevationScale
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import javax.inject.Inject


class EventListFragment : BaseFragment<FragmentEventListBinding>({ inflate, container ->
    FragmentEventListBinding.inflate(inflate, container, false)
}) {

    @Inject
    lateinit var viewModelFactory: EventListViewModel.EventListViewModelFactory.Factory

    @Inject
    lateinit var picasso: Picasso


    private val eventListViewModel: EventListViewModel by viewModels {
        viewModelFactory.create()
    }

    private var eventStatus: Int = 0

    private val eventAdapter: EventListAdapter by lazy {
        EventListAdapter({ event, view ->
            onCardClicked(view, event)
        }, picasso)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadingField.progressBar.visibility = View.VISIBLE
        // Page change callback
        onPageChangeCallback()

        // hide FAB when scrolled
        recyclerEventListScrollListener()

        // Move to Create Event
        onFabClicked()

        // Get event status
        getEventStatus()

        // Waiting for recycler load
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        // Set cards to RecyclerView
        updateRecycler()

        with(binding) {

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                eventListViewModel.state.collect { eventState ->
                    when (eventState) {
                        is EventListState.Loading -> {
                            eventListViewModel.fetchEvents()
                        }
                        is EventListState.Success -> {
                            loadingField.progressBar.visibility = View.GONE
                            eventAdapter.submitList(eventState.eventList.filter {
                                it.status.status == eventStatus
                            }.sortedBy { it.date.time })
                        }
                        is EventListState.Error -> {
                            showMessage()
                            eventListViewModel.setLoadingState()
                        }
                    }
                }

            }

        }

    }

    private fun updateRecycler() {
        binding.recyclerEvent.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = eventAdapter
        }
    }

    private fun onCardClicked(cardView: View, event: Event) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(
                com.google.android.material.R.integer.material_motion_duration_medium_1
            ).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(
                com.google.android.material.R.integer.material_motion_duration_medium_1
            ).toLong()
        }
        val eventCardDetailTransitionName = getString(
            R.string.card_event_transition_name
        )
        val extras = FragmentNavigatorExtras(
            cardView to eventCardDetailTransitionName
        )
        val directions =
            EventListTabFragmentDirections.actionEventListTabFragmentToEventViewingFragment(
                eventId = event.id, eventName = event.eventName
            )
        findNavController().navigate(directions, extras)
    }

    private fun onFabClicked() {
        binding.eventAdderFAB.setOnClickListener {
            exitTransition = null
            reenterTransition = null
            findNavController().navigate(
                EventListTabFragmentDirections.actionEventListTabFragmentToEventAddAndEditFragment(
                    eventId = 0,
                    eventStatus = 0
                )
            )
        }

    }

    private fun recyclerEventListScrollListener() {
        with(binding) {
            recyclerEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        if (eventAdderFAB.isShown) {
                            eventAdderFAB.hide()
                        }
                    } else if (dy < 0) {
                        if (!eventAdderFAB.isShown) {
                            eventAdderFAB.show()
                        }
                    }
                }
            })
        }

    }

    private fun getEventStatus() {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            eventStatus = getInt(ARG_OBJECT)
        }
    }

    private fun onPageChangeCallback() {
        val myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                eventListViewModel.setLoadingState()
                hideFab()
                binding.eventAdderFAB.show()
            }
        }
        parentFragment
            ?.view
            ?.findViewById<ViewPager2>(R.id.pager)
            ?.registerOnPageChangeCallback(myPageChangeCallback)
    }

    private fun hideFab() {
        val lifecycle = viewLifecycleOwner.lifecycle
        lifecycle.coroutineScope.launchWhenCreated {
            delay(10000)
            if (eventAdapter.itemCount >= 6) binding.eventAdderFAB.hide()
        }
    }

}