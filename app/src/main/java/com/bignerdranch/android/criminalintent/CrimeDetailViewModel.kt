package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeDetailViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    private val crimeIdLiveData: MutableLiveData<UUID> = MutableLiveData<UUID>()
    val crimeLiveData: LiveData<Crime> =
        Transformations.switchMap(crimeIdLiveData) {
            crimeRepository.getCrime(it)
        }

    private val _crimeDateLiveData: MutableLiveData<Date> =
        MutableLiveData<Date>().apply {
            value = crimeLiveData.value?.date
        }

    val crimeDateLiveData: LiveData<Date> = _crimeDateLiveData

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun updateDate(date: Date) {
        _crimeDateLiveData.value = date
        crimeLiveData.value?.date = date
    }

    fun saveCrime() {
        crimeLiveData.value?.let { crimeRepository.updateCrime(it) }
    }


    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
}