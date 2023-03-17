package com.example.templatecrudproject.entity.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.templatecrudproject.entity.db.EntityRoomDatabase
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.domain.PopularType
import com.example.templatecrudproject.entity.repository.EntityRepositoryLocal
import com.example.templatecrudproject.entity.repository.EntityRepositoryLocalImpl
import com.example.templatecrudproject.entity.repository.EntityRepositoryNetwork
import com.example.templatecrudproject.entity.repository.EntityRepositoryNetworkImpl
import com.example.templatecrudproject.entity.service.RetrofitService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntityViewModel @Inject constructor(application: Application) : ViewModel() {

    private var localRepo: EntityRepositoryLocal?

    private var networkRepo: EntityRepositoryNetwork?

    private var retrofit: RetrofitService?

    private val _selectedDate: MutableStateFlow<String?> = MutableStateFlow(null)

    private var _datesList: MutableStateFlow<LiveData<List<String>>> = MutableStateFlow(MutableLiveData(listOf()))

    private var _filteredList: MutableStateFlow<LiveData<List<Entity>>> = MutableStateFlow(MutableLiveData(listOf()))

    private var _topList: MutableStateFlow<LiveData<List<PopularType>>> = MutableStateFlow(MutableLiveData(listOf()))

    val datesList: StateFlow<LiveData<List<String>>> = _datesList

    val selectedDate: StateFlow<String?> = _selectedDate

    val filteredList: StateFlow<LiveData<List<Entity>>> = _filteredList

    val topList: StateFlow<LiveData<List<PopularType>>> = _topList

    private var _serverAvailability: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val serverAvailability: StateFlow<Boolean> = _serverAvailability

    private val dbInstance = EntityRoomDatabase.getDatabase(application.applicationContext)
    private val dao = dbInstance.entityDao()

    init {

        localRepo = EntityRepositoryLocalImpl(dao)
        networkRepo = EntityRepositoryNetworkImpl(dao)
        retrofit = null
        _serverAvailability.value = false

        viewModelScope.launch(Dispatchers.IO) {

            retrofit = RetrofitService.getInstance(networkRepo!!, viewModelScope)

            _serverAvailability.value = retrofit != null

            if (retrofit != null) {
                localRepo = null
            }
            else {
                networkRepo = null
            }

            _datesList.value = if (retrofit != null) networkRepo!!.findDates(retrofit!!) else localRepo!!.findDates()
        }
    }

    fun setEntityListByDate(category: String?) {

        _selectedDate.value = category

        viewModelScope.launch(Dispatchers.IO) {

            if (retrofit != null && category != null) {

                networkRepo!!.findByDate(retrofit!!, category)
                _filteredList.value = dao.findByDate(category)
            }
            else if (category != null) {

                _filteredList.value = dao.findByDate(category)
            }
            else {

                _filteredList.value = MutableLiveData(listOf())
            }
        }
    }

    fun setNewEntityAction(action: (String) -> Unit) {

        viewModelScope.launch(Dispatchers.Main) {

            if (retrofit != null) {
                networkRepo!!.setOnNewEntityAction(action)
            }
        }

    }

    fun setTopList() {

        viewModelScope.launch(Dispatchers.IO) {

            if (retrofit != null) {

                networkRepo!!.findTop(retrofit!!)
                _topList.value = dao.findTop3Types()
            }
        }
    }

    fun retryConnection() {

        localRepo = EntityRepositoryLocalImpl(dao)
        networkRepo = EntityRepositoryNetworkImpl(dao)
        retrofit = null
        _serverAvailability.value = false

        viewModelScope.launch(Dispatchers.IO) {

            retrofit = RetrofitService.getInstance(networkRepo!!, viewModelScope)

            _serverAvailability.value = retrofit != null

            if (retrofit != null) {
                localRepo = null
            }
            else {
                networkRepo = null
            }

            _datesList.value = if (retrofit != null) networkRepo!!.findDates(retrofit!!) else localRepo!!.findDates()

        }
    }

    fun deleteEntity(id: Long) {

        viewModelScope.launch(Dispatchers.IO) {

            networkRepo!!.delete(retrofit!!, id)
        }
    }

    fun addEntity(entity: Entity) {

        viewModelScope.launch(Dispatchers.IO) {

            if (retrofit != null) {
                networkRepo!!.save(retrofit!!, entity)
            }
            else {
                entity.addedOffline = true
                localRepo!!.save(entity)
            }
        }
    }
}