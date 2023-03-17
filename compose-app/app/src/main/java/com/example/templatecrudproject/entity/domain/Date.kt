package com.example.templatecrudproject.entity.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.templatecrudproject.utils.toUnixTimestamp

@androidx.room.Entity(tableName = "dates")
data class Date (@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long? = null,
                 @ColumnInfo(name = "date") val date: String
)