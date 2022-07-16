package com.tugasakhir.onandcafe.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tugasakhir.onandcafe.db.converter.DateConverter
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user")
@TypeConverters(DateConverter::class)
data class User(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean,
    val createdDate: Date = Date(),
    val updatedDate: Date = Date()
) : Parcelable