package com.example.templatecrudproject.entity.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.templatecrudproject.entity.domain.Date
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.utils.Converters

@Database(entities = [Entity::class, Date::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EntityRoomDatabase: RoomDatabase() {

    abstract fun entityDao(): EntityDAO

    companion object {

        @Volatile
        private var INSTANCE: EntityRoomDatabase? = null

        fun getDatabase(
            context: Context
        ) : EntityRoomDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntityRoomDatabase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    // TODO Add datetime type converters if needed
                    // .addTypeConverter(Converters())
                    .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}