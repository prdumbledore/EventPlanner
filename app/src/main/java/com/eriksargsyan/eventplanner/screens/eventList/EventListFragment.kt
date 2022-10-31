package com.eriksargsyan.eventplanner.screens.eventList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.appComponent
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.databinding.FragmentEventListBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import com.google.android.material.transition.MaterialElevationScale
import javax.inject.Inject


class EventListFragment : BaseFragment<FragmentEventListBinding>({ inflate, container ->
    FragmentEventListBinding.inflate(inflate, container, false)
}) {

    @Inject
    lateinit var viewModelFactory: EventListViewModel.EventListViewModelFactory.Factory

    private val eventListViewModel: EventListViewModel by viewModels {
        viewModelFactory.create()
    }

    private val eventAdapter: EventListAdapter by lazy { EventListAdapter{ event, view ->
            onCardClicked(view, event)
    } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }

            recyclerEventList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = eventAdapter
            }

            eventAdderFAB.setOnClickListener{
                onFabClicked()
            }

            recyclerEventList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                eventListViewModel.state.collect { eventState ->
                    when (eventState) {
                        is EventListState.Loading -> {
                            loadingField.progressBar.visibility = View.VISIBLE
                            eventListViewModel.fetchEvents()
                        }
                        is EventListState.Success -> {
                            loadingField.progressBar.visibility = View.GONE
                            eventAdapter.submitList(eventState.eventList)
                        }
                        is EventListState.Error -> {
                            eventListViewModel.setLoadingState()
                        }
                    }
                }

            }

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
        val directions = EventListFragmentDirections.actionEventListFragmentToEventViewingFragment(
            eventId = event.id, eventName = event.eventName
        )
            findNavController().navigate(directions, extras)
    }

    private fun onFabClicked() {
        exitTransition = null
        reenterTransition = null
        findNavController().navigate(
            EventListFragmentDirections.actionEventListFragmentToEventAddAndEditFragment(
                eventId = 0
            )
        )
    }

}