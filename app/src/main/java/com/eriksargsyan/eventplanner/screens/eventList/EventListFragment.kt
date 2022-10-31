package com.eriksargsyan.eventplanner.screens.eventList

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eriksargsyan.eventplanner.appComponent
import com.eriksargsyan.eventplanner.databinding.FragmentEventListBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import javax.inject.Inject


class EventListFragment : BaseFragment<FragmentEventListBinding>({ inflate, container ->
    FragmentEventListBinding.inflate(inflate, container, false)
}) {

    @Inject
    lateinit var viewModelFactory: EventListFragmentViewModel.EventListFragmentViewModelFactory.Factory

    private val eventListViewModel: EventListFragmentViewModel by viewModels {
        viewModelFactory.create()
    }

    private val eventAdapter: EventListAdapter by lazy { EventListAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            recyclerEventList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = eventAdapter
            }

            eventAdderFAB.setOnClickListener{
                findNavController().navigate(
                    EventListFragmentDirections.actionEventListFragmentToEventAddAndEditFragment()
                )
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
                            eventListViewModel.fetchEvents()
                        }
                        is EventListState.Success -> {
                            eventAdapter.submitList(eventState.eventList)
                        }
                        is EventListState.Error -> {}
                    }
                }

            }

        }


    }
}