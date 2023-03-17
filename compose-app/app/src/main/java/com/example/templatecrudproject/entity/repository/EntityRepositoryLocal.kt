package com.example.templatecrudproject.entity.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.templatecrudproject.entity.db.EntityDAO
import com.example.templatecrudproject.entity.domain.Entity


interface EntityRepositoryLocal {

    suspend fun findById(id: Long): Entity?

    suspend fun findByDate(date: String): LiveData<List<Entity>>

    suspend fun findDates(): LiveData<List<String>>

    suspend fun save(entity: Entity)

    suspend fun delete(id: Long)
}

class EntityRepositoryLocalImpl(

    private val dao: EntityDAO

) : EntityRepositoryLocal {

    override suspend fun findById(id: Long): Entity? {
        val entity = dao.findById(id)
        Log.i("REPO", "Successfully retrieved entity of id $id from local DB: $entity")

        return entity
    }

    override suspend fun findByDate(date: String): LiveData<List<Entity>> {
        val entities = dao.findByDate(date)
        Log.i("REPO", "Successfully retrieved entities of category $date from local DB")

        return entities
    }

    override suspend fun findDates(): LiveData<List<String>> {
        return dao.findDates()
    }

    override suspend fun save(entity: Entity) {
        dao.save(entity)
        Log.i("REPO", "Successfully saved entity to local DB: $entity")
    }

    override suspend fun delete(id: Long) {
        Log.wtf("REPO", "Attempted Delete operation while offline")
        return
    }
}