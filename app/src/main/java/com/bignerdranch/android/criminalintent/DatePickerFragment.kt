package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val TAG = "DatePickerFragment"

class DatePickerFragment : DialogFragment() {
    private val calendar: Calendar = Calendar.getInstance()
    private val crimeDetailViewModel by lazy {
        activity?.run {
            ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(DIALOG_DATE) as Date
        calendar.time = date

        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                Log.d(TAG, "$year - $month - $day")
                calendar.set(year, month, day)
                Log.d(TAG, "DatePickerDialog.OnDateSetListener: $calendar")
                crimeDetailViewModel.updateDate(calendar.time)
            }
        return DatePickerDialog(
            requireContext(),
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    companion object {
        const val DIALOG_DATE = "DialogDate"
    }
}