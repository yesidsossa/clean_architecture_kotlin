package com.instaleap.data.db.series

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.instaleap.data.entities.SerieDbData

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@Dao
interface SerieDao {

    @Query("SELECT * FROM series")
    fun series(): PagingSource<Int, SerieDbData>

    /**
     * Get all Series from the Series table.
     *
     * @return all Series.
     */
    @Query("SELECT * FROM series")
    fun getSeries(): List<SerieDbData>

    /**
     * Get Serie by id.
     * **/
    @Query("SELECT * FROM series WHERE id = :serieId")
    suspend fun getSerie(serieId: Int): SerieDbData?

    /**
     * Insert all Series.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSeries(series: List<SerieDbData>)

    /**
     * Delete all series
     */
    @Query("DELETE FROM movies")
    suspend fun clearSeries()

}