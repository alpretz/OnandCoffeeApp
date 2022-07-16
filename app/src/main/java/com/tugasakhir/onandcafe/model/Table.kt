package com.tugasakhir.onandcafe.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table")
data class Table(
    @PrimaryKey
    @NonNull
    var id: Int,
    var tableName: String,
    var isOccupied: Boolean
)