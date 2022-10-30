package com.eriksargsyan.eventplanner.screens.eventAddAndEdit

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.appComponent
import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.databinding.FragmentEventAddAndEditBinding
import com.eriksargsyan.eventplanner.hideKeyboard
import com.eriksargsyan.eventplanner.screens.base.BaseFragment
import com.eriksargsyan.eventplanner.util.Constants.ARG_DATE
import com.eriksargsyan.eventplanner.util.Constants.DIALOG_DATE
import com.eriksargsyan.eventplanner.util.Constants.FIELD_IS_EMPTY
import com.eriksargsyan.eventplanner.util.Constants.REQUEST_KEY
import java.util.*
import javax.inject.Inject


class EventAddAndEditFragment : BaseFragment<FragmentEventAddAndEditBinding>(
    { inflater, container -> FragmentEventAddAndEditBinding.inflate(inflater, container, false) }
) {

    private var eventId: Int = 0
    private lateinit var eventDate: Date
    private lateinit var cityName: CityName



    @Inject
    lateinit var viewModelFactory: EventAddingFragmentViewModel.EventAddingFragmentViewModelFactory.Factory

    private var searchResult: List<CityName> = emptyList()
    private var searchItemPosition: Int = 0

    private val eventAddingViewModel: EventAddingFragmentViewModel by viewModels {
        viewModelFactory.create()
    }

    private val cursorAdapter by lazy {
        with(binding) {

            val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
            val to = intArrayOf(R.id.search_result)
            val cursorAdapter = SimpleCursorAdapter(
                context,
                R.layout.item_search_recycler,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            )

            eventCityName.suggestionsAdapter = cursorAdapter
            cursorAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuLoading()
        searchCreate()

        with(binding) {

            dateButton.setOnClickListener {
                DatePickerFragment().show(
                    this@EventAddAndEditFragment.parentFragmentManager,
                    DIALOG_DATE
                )
                setFragmentResultListener(REQUEST_KEY) { _, bundle ->
                    eventDate = bundle.get(ARG_DATE) as Date
                    dateButton.text = eventDate.toString()
                }
            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                eventAddingViewModel.state.collect { searchState ->
                    when (searchState) {
                        is SearchListState.Loading -> {
                        }
                        is SearchListState.Searching -> {
                            searchUpdate(searchState.cityList.map { "${it.name}, ${it.country}" })
                            searchResult = searchState.cityList
                        }
                        is SearchListState.Result -> {
                            findNavController().navigate(
                                EventAddAndEditFragmentDirections.actionEventAddAndEditFragmentToEventListFragment()
                            )
                        }
                        is SearchListState.Error -> {}
                    }
                }

            }

        }


    }

    private fun menuLoading() {
        val menuHost: MenuHost = requireActivity()
        (activity as AppCompatActivity?)!!.supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_close_black_24dp)
        }

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.event_create_and_edit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.done -> {
                        checkField()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun checkField() {
        with(binding) {

            eventNameLayout.error = if (eventNameEditText.text.toString().isEmpty())
                FIELD_IS_EMPTY
            else null
            dateButtonLayout.error =
                if (dateButton.text.toString() == resources.getString(R.string.pick_date))
                    FIELD_IS_EMPTY
                else null
            eventCityNameLayout.error = if (searchResult.isEmpty()) FIELD_IS_EMPTY
            else null

            if (eventNameLayout.error == null && dateButtonLayout.error == null && eventCityNameLayout.error == null)
                eventAddingViewModel.saveResult(
                    eventId,
                    eventNameEditText.text.toString(),
                    eventDate,
                    searchResult[searchItemPosition],
                    eventAddressLine.editText?.text.toString(),
                    eventDescriptionLayout.editText?.text.toString()
                )

        }
    }

    private fun searchCreate() {
        with(binding) {

            eventCityName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard()
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    eventAddingViewModel.fetchEventGeo(cityName = query ?: "")
                    return true
                }
            })

            eventCityName.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    hideKeyboard()
                    val cursor = eventCityName.suggestionsAdapter.getItem(position) as Cursor
                    val selection =
                        cursor.getString(
                            cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1) ?: 0
                        )
                    eventCityName.setQuery(selection, false)

                    searchItemPosition = position

                    return true
                }
            })
        }
    }

    private fun searchUpdate(suggestions: List<String>) {
        val cursor =
            MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

        suggestions.forEachIndexed { index, suggestion ->
            cursor.addRow(arrayOf(index, suggestion))
        }

        cursorAdapter.changeCursor(cursor)
    }


}