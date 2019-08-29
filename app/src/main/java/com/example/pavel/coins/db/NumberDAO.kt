package com.example.pavel.coins.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.Model.Response

@Dao
interface NumberDAO {

    @Insert
    fun insert(number: List<Number>)

    @Query("DELETE FROM NumberClass")
    fun deleteAll()

    @Query("SELECT * FROM NumberClass ORDER BY favor, cmc_rank ASC")
    fun getAll(): DataSource.Factory<Int, Number>

    @Query("SELECT * FROM NumberClass WHERE cmc_rank = :rank")
    fun getWithRank(rank:Int): LiveData<Number>

    @Query("SELECT * FROM NumberClass WHERE name LIKE :name")
    fun search(name:String): DataSource.Factory<Int, Number>

    @Query("SELECT * FROM NumberClass ORDER BY price DESC")
    fun sortByPrice(): DataSource.Factory<Int, Number>

    @Query("SELECT * FROM NumberClass ORDER BY name")
    fun sortByName(): DataSource.Factory<Int, Number>

    @Query("UPDATE NumberClass SET favor = 1 WHERE cmc_rank = :rank")
    fun addToFavourite(rank:Int)

    @Query("UPDATE NumberClass SET favor = 0 WHERE cmc_rank = :rank")
    fun deleteFavourite(rank:Int)

    @Query("SELECT * FROM NumberClass ORDER BY favor desc")
    fun getAny(): DataSource.Factory<Int, Number>

    /*@Transaction
    fun updateWithDeleteData(rank:Int): DataSource.Factory<Int, Number>{
        deleteFavourite(rank)
        return getAll()
    }


    @Transaction
    fun updateWithAddData(rank:Int): DataSource.Factory<Int, Number>{
        addToFavourite(rank)
        return getAll()
    }*/


}