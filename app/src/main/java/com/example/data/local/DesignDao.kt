package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.DesignProject
import kotlinx.coroutines.flow.Flow

@Dao
interface DesignDao {
    @Query("SELECT * FROM design_projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<DesignProject>>

    @Query("SELECT * FROM design_projects WHERE category = :category ORDER BY createdAt DESC")
    fun getProjectsByCategory(category: String): Flow<List<DesignProject>>

    @Query("SELECT * FROM design_projects WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteProjects(): Flow<List<DesignProject>>

    @Query("SELECT * FROM design_projects WHERE id = :id")
    suspend fun getProjectById(id: Int): DesignProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: DesignProject): Long

    @Update
    suspend fun updateProject(project: DesignProject)

    @Query("DELETE FROM design_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Int)

    @Query("UPDATE design_projects SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFav: Boolean)
}
