package com.bignerdranch.android.criminalintent

import androidx.lifecycle.*
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            crimeLiveData.value?.let { crimeRepository.updateCrime(it) }
        }
    }


    fun addCrime(crime: Crime) {
        viewModelScope.launch {
            crimeRepository.addCrime(crime)
        }
    }
}