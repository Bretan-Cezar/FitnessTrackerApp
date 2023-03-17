package com.example.templatecrudproject.entity.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.templatecrudproject.entity.domain.Date
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.domain.PopularType
import com.example.templatecrudproject.entity.domain.WeekCalories
import com.example.templatecrudproject.utils.Converters

@Dao
@TypeConverters( Converters::class )
interface EntityDAO {

    /*
      " LiveData is a data holder class that can be observed within a given lifecycle.
        Always holds/caches latest version of data. Notifies its active observers when the
        data has changed. Since we are getting all the contents of the database,
        we are notified whenever any of the database contents have changed. "

        ~ Google
     */

    // TODO fill in the dots

    @Query("SELECT * FROM entity WHERE id = :id")
    fun findById(id: Long): Entity?

    @Query("SELECT * FROM entity WHERE date = :date")
    fun findByDate(date: String): LiveData<List<Entity>>

    @Query("SELECT type, COUNT(*) AS count FROM entity GROUP BY type ORDER BY count DESC LIMIT 3")
    fun findTop3Types(): LiveData<List<PopularType>>

    @Query("SELECT date FROM dates")
    fun findDates(): LiveData<List<String>>

    @Query("SELECT timestamp/604800 AS week, SUM(calories) AS calories FROM entity GROUP BY timestamp/604800 ORDER BY date DESC")
    fun getCaloriesForEachWeek(): LiveData<List<WeekCalories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: Entity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDate(date: Date)

    @Query("DELETE FROM dates")
    fun deleteAllDates()

    @Query("DELETE FROM entity WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM entity")
    fun deleteAll()

    @Query("SELECT * FROM entity WHERE added_offline = 1")
    fun findAddedOffline(): List<Entity>
}