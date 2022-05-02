package com.bignerdranch.android.criminalintent

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import java.util.*

private const val TAG = "CrimeFragment"

class CrimeFragment : Fragment() {
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private val crimeDetailViewModel by lazy {
        activity?.run {
            ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        val crimeId = arguments?.getSerializable(CrimeListFragment.ARG_CRIME_ID)
        if (crimeId !== null) {
            crimeDetailViewModel.loadCrime(crimeId as UUID)
        }
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        return view
    }

    override fun onStart() {
        super.onStart()
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) {
            it?.let {
                crime = it
                Log.d(TAG, "crime changed: ${crime.date}")
                updateUI()
            }
        }
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _,
                                         isChecked ->
                crime.isSolved = isChecked
            }
        }

        dateButton.apply {
            text = crime.date.toString()
            setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable(DatePickerFragment.DIALOG_DATE, crime.date)
                findNavController().navigate(R.id.to_datePickerFragment, bundle)
            }
        }

        titleField.addTextChangedListener(titleWatcher)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
        crimeDetailViewModel.saveCrime(crime)
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }
}