package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.CoFounderMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM cofounder_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<CoFounderMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: CoFounderMessage): Long

    @Query("DELETE FROM cofounder_messages")
    suspend fun clearAllMessages()
}
