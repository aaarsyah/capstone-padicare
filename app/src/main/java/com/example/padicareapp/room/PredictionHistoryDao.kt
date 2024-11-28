package com.example.padicareapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.padicareapp.entity.PredictionHistory

@Dao
interface PredictionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(predictionHistory: PredictionHistory)

    @Query("SELECT * FROM prediction_history ORDER BY id DESC")
    suspend fun getAllHistory(): List<PredictionHistory>

    @Query("DELETE FROM prediction_history")
    suspend fun deleteAllHistory()
}