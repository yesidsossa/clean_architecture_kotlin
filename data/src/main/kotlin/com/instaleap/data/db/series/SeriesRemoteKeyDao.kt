package com.instaleap.data.db.series

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.instaleap.data.entities.SerieRemoteKeyDbData

/**
 * @author by Yesid Hernandez 02/09/2024
 */
@Dao
interface SeriesRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRemoteKey(keys: SerieRemoteKeyDbData)

    @Query("SELECT * FROM series_remote_keys WHERE id=:id")
    suspend fun getRemoteKeyByMovieId(id: Int): SerieRemoteKeyDbData?

    @Query("DELETE FROM series_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM series_remote_keys WHERE id = (SELECT MAX(id) FROM series_remote_keys)")
    suspend fun getLastRemoteKey(): SerieRemoteKeyDbData?
}