package com.example.templatecrudproject.entity.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.templatecrudproject.entity.db.EntityDAO
import com.example.templatecrudproject.entity.domain.Date
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.domain.PopularType
import com.example.templatecrudproject.entity.service.RetrofitService


interface EntityRepositoryNetwork {

    suspend fun findById(id: Long): Entity?

    suspend fun findByDate(retrofitService: RetrofitService, date: String): LiveData<List<Entity>>

    suspend fun findTop(retrofitService: RetrofitService): LiveData<List<PopularType>>

    suspend fun findDates(retrofitService: RetrofitService): LiveData<List<String>>

    suspend fun save(retrofitService: RetrofitService, entity: Entity)

    suspend fun saveLocally(entity: Entity)

    suspend fun delete(retrofitService: RetrofitService, id: Long)

    fun setOnNewEntityAction(action: (String) -> Unit)
}


class EntityRepositoryNetworkImpl(

    private val dao: EntityDAO,
    var newEntityAction: (String) -> Unit = {}

) : EntityRepositoryNetwork {

    @WorkerThread
    override suspend fun findById(id: Long): Entity? {

        val entity = dao.findById(id)
        Log.i("REPO", "Successfully retrieved entity of id $id from local DB: $entity")

        return entity
    }

    @WorkerThread
    override suspend fun findByDate(retrofitService: RetrofitService, date: String): LiveData<List<Entity>> {

        val ret = retrofitService.getItemsByDates(date).execute().body()!!

        ret.forEach {

            dao.save(Entity.fromDTO(it))
        }

        val entities = dao.findByDate(date = date)
        Log.i("REPO", "Successfully retrieved entities of category $date from local DB.")

        return entities
    }

    @WorkerThread
    override suspend fun findTop(retrofitService: RetrofitService): LiveData<List<PopularType>> {

        val ret = retrofitService.getAll().execute().body()!!

        dao.deleteAll()

        ret.forEach {

            dao.save(Entity.fromDTO(it))
        }

        val entities = dao.findTop3Types()
        Log.i("REPO", "Successfully retrieved easiest recipes.")

        return entities
    }

    @WorkerThread
    override suspend fun findDates(retrofitService: RetrofitService): LiveData<List<String>> {

        val ret = retrofitService.getDates().execute().body()!!

        dao.deleteAllDates()

        ret.forEach {

            dao.saveDate(Date(date = it))
        }

        return dao.findDates()
    }

    @WorkerThread
    override suspend fun save(retrofitService: RetrofitService, entity: Entity) {

        Log.d("REPO", "Saving entity to remote DB: $entity")

        val ret = retrofitService.addItem(entity.toDTO()).execute().body()!!

        Log.d("REPO", "Save to remote DB returned DTO: $ret")

        dao.save(Entity.fromDTO(ret))

        if (dao.findDates().value?.contains(ret.date) == false) {

            dao.saveDate(Date(date = ret.date))
        }

        Log.i("REPO", "Successfully saved entity to local DB: $ret")
    }

    @WorkerThread
    override suspend fun saveLocally(entity: Entity) {

        Log.d("REPO", "Saving received entity to local DB: $entity")

        dao.save(entity)

        // TODO handle new entity notification
        newEntityAction("New fitness exercise added: ${entity.type}, ${entity.date}, ${entity.duration} minutes")

        Log.i("REPO", "Successfully saved entity to local DB: $entity")
    }

    @WorkerThread
    override suspend fun delete(retrofitService: RetrofitService, id: Long) {

        Log.d("REPO", "Deleting entity with id $id from remote DB...")

        retrofitService.deleteItem(id).execute()

        Log.d("REPO", "Delete from remote DB exited.")

        dao.deleteById(id)

        Log.i("REPO", "Successfully deleted entity with id $id from local DB.")
    }

    override fun setOnNewEntityAction(action: (String) -> Unit) {

        newEntityAction = action
    }

}