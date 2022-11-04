package com.eriksargsyan.eventplanner.screens.eventViewing

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.appComponent
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.data.model.domain.EventStatus
import com.eriksargsyan.eventplanner.databinding.FragmentEventViewingBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import com.eriksargsyan.eventplanner.util.EventTxtTransform.dateToDMY
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class EventViewingFragment : BaseFragment<FragmentEventViewingBinding>({ inflate, container ->
    FragmentEventViewingBinding.inflate(inflate, container, false)
}) {

    @Inject
    lateinit var viewModelFactory: EventViewingViewModel.EventViewingViewModelFactory.Factory

    private val eventViewingViewModel: EventViewingViewModel by viewModels {
        viewModelFactory.create()
    }

    private val args: EventViewingFragmentArgs by navArgs()
    private var eventId: Int = 0
    private var eventStatus: Int = 0
    private lateinit var event: Event


    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)

        eventId = args.eventId
        (activity as AppCompatActivity).supportActionBar?.title = args.eventName
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check status change
        onCheckedStateChanged()

        // load appbar menu
        menuLoading()

        // card motion
        enterTransition()

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                eventViewingViewModel.state.collect { eventState ->
                    when (eventState) {
                        is EventViewingState.Loading -> {
                            loadingField.progressBar.visibility = View.VISIBLE
                            chipGroup.visibility = View.GONE
                            eventViewingViewModel.fetchEventDetails(eventId)
                        }
                        is EventViewingState.Success -> {
                            loadingField.progressBar.visibility = View.GONE
                            event = eventState.event
                            chipGroup.visibility = View.VISIBLE
                            fillField(eventState.event)
                        }
                        is EventViewingState.Delete -> {
                            findNavController().navigate(
                                EventViewingFragmentDirections
                                    .actionEventViewingFragmentToEventListTabFragment()
                            )
                        }
                    }
                }
            }
        }

    }

    private fun fillField(event: Event) {
        binding.apply {
            eventDate.text = dateToDMY(event.date)
            weatherIcon.setImageResource(R.drawable.baseline_double_dash_24dp)
            if (event.weather.weatherTemp.isNotEmpty())
                weatherTemp.text = root
                    .resources.getString(R.string.weather_temp, event.weather.weatherTemp)
            eventPlace.text = if (event.addressLine.isEmpty()) event.cityName
            else " ${event.cityName}, ${event.addressLine}"
            eventDescription.text = event.description
            eventStatus = event.status.status
            chipGroup.check(event.status.id)
        }
    }

    private fun menuLoading() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.event_viewing_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete -> {
                        eventViewingViewModel.deleteEvent(eventId)
                        true
                    }
                    R.id.edit -> {
                        findNavController().navigate(
                            EventViewingFragmentDirections
                                .actionEventViewingFragmentToEventAddAndEditFragment(
                                    eventId = eventId,
                                    eventStatus = eventStatus,
                                )
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun onCheckedStateChanged() {
        binding.chipGroup.setOnCheckedStateChangeListener { _, _ ->
            eventViewingViewModel.setNewEventStatus(
                event.copy(status = EventStatus.fromId(binding.chipGroup.checkedChipId))
            )
        }
    }

    private fun enterTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_graph
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(com.google.android.material.R.attr.colorOnSurface)
        }
    }



}