package com.rifki.kotlin.mynotesroom.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "notes")

@Parcelize
data class Note (
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id : Int = 0,
    @ColumnInfo(name = "title") var title : String = "",
    @ColumnInfo(name = "description") var description : String = "",
    @ColumnInfo(name = "date") var date : String = ""
) : Parcelable