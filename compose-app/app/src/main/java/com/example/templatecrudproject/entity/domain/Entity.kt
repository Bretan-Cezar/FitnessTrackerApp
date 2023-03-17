package com.example.templatecrudproject.entity.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.templatecrudproject.entity.dto.EntityDTO
import com.example.templatecrudproject.utils.Converters
import com.example.templatecrudproject.utils.toUnixTimestamp

// TODO change entity table name
@androidx.room.Entity(tableName = "entity")
@TypeConverters(Converters::class)
// TODO add entity fields
data class Entity(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long? = null,
                  @ColumnInfo(name = "date") val date: String,
                  @ColumnInfo(name = "type") val type: String,
                  @ColumnInfo(name = "duration") val duration: Int,
                  @ColumnInfo(name = "distance") val distance: Int,
                  @ColumnInfo(name = "calories") val calories: Int,
                  @ColumnInfo(name = "rate") val rate: Int,
                  @ColumnInfo(name = "timestamp") val timestamp: Int = date.toUnixTimestamp(),
                  @ColumnInfo(name = "added_offline") var addedOffline: Boolean = false) {

    // TODO define toDTO function
    fun toDTO(): EntityDTO {

        return EntityDTO(
            id = id ?: 0L,
            date = date,
            type = type,
            duration = duration,
            distance = distance,
            calories = calories,
            rate = rate
        )
    }

    // TODO define fromDTO function
    companion object {
        fun fromDTO(entityDTO: EntityDTO): Entity {

            return Entity(
                id = entityDTO.id,
                date = entityDTO.date,
                type = entityDTO.type,
                duration = entityDTO.duration,
                distance = entityDTO.distance,
                calories = entityDTO.calories,
                rate = entityDTO.rate,
                addedOffline = false
            )
        }
    }
}