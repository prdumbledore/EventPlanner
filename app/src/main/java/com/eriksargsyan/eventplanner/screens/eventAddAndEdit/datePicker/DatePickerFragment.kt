package com.eriksargsyan.eventplanner.screens.eventAddAndEdit.datePicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.eriksargsyan.eventplanner.util.Constants.ARG_DATE
import com.eriksargsyan.eventplanner.util.Constants.REQUEST_KEY
import java.util.*


class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val resultDate: Date = GregorianCalendar(year, month, day).time
        this.parentFragmentManager.setFragmentResult(REQUEST_KEY, Bundle().apply {
            putSerializable(ARG_DATE, resultDate)
        })
    }



}

