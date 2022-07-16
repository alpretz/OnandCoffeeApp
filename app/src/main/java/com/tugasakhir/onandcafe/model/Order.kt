package com.tugasakhir.onandcafe.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tugasakhir.onandcafe.db.converter.DateConverter
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "order")
@TypeConverters(DateConverter::class)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = -1L,
    val id: String = "",
    val buyerName: String = "",
    val paymentType: String = "",
    val orderDate: Date,
    val totalPrice: Long = -1L,
    val tableNo: String = "",
    val isPaid: Int = 0
) : Parcelable {
    @Ignore
    var type: Int = 0
}