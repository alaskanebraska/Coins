package com.example.pavel.coins.Repository

import android.support.annotation.WorkerThread
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.db.NumberDAO

class DbRepository (private val numberDao: NumberDAO) {

    //val allWords: LiveData<List<Number>> = numberDao.getAll()

    @WorkerThread
     fun insert(number: List<Number>) {
        numberDao.insert(number)
    }
    @WorkerThread
    fun deleteAll() {
       numberDao.deleteAll()
    }
}