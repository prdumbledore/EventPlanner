package com.eriksargsyan.eventplanner.screens.eventAdding

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.appComponent
import com.eriksargsyan.eventplanner.databinding.FragmentEventAddingBinding
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import com.eriksargsyan.eventplanner.util.Constants.DIALOG_DATE
import java.util.*
import javax.inject.Inject


class EventAddingFragment : BaseFragment<FragmentEventAddingBinding>(
    { inflater, container -> FragmentEventAddingBinding.inflate(inflater, container, false) }
) {

    @Inject
    lateinit var viewModelFactory: EventAddingFragmentViewModelFactory.Factory


    private val eventAddingViewModel: EventAddingFragmentViewModel by viewModels {
        viewModelFactory.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuLoading()

        eventAddingViewModel.fetchEventGeo()

        binding.apply {

            dateButton.setOnClickListener{
                DatePickerFragment().show(this@EventAddingFragment.parentFragmentManager, DIALOG_DATE)
            }

        }

        val loc = Locale("", "NL")
        loc.displayCountry


    }

    private fun menuLoading() {
        val menuHost: MenuHost = requireActivity()
        (activity as AppCompatActivity?)!!.supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_close_black_24dp);
        }

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.event_create_and_edit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.done -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}