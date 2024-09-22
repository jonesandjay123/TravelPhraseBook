package com.jonesandjay123.travelphrasebook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val sentenceDao: SentenceDao,
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(sentenceDao, application) as T
    }
}