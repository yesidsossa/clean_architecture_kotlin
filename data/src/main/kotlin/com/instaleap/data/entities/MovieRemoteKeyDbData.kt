package com.instaleap.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author by Yesid Hernandez 02/09/2024
 */
@Entity(tableName = "movies_remote_keys")
data class MovieRemoteKeyDbData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prevPage: Int?,
    val nextPage: Int?
)