package com.tugasakhir.onandcafe.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stock")
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var stockName: String,
    var stockMetric: String,
    var stockInitialValue: Int,
    var stockIn: Int,
    var stockOut: Int,
    var stockTotal: Int,
) : Parcelable