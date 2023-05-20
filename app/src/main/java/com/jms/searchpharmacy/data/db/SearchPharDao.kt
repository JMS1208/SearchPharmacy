package com.jms.searchpharmacy.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.jms.searchpharmacy.data.model.server.PharmacyLocation

@Dao
interface SearchPharDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPL(pl: PharmacyLocation)

    @Delete
    suspend fun deletePL(pl: PharmacyLocation)



    //쿼리를 제외한 CUD 작업은 시간이 걸려서
    //코루틴 안에서 비동기적으로 작업해서 suspend 붙여줌
    @Query("SELECT * FROM pharLocation")
    fun getFavoritePharLocation(): LiveData<List<PharmacyLocation>>


    @Query("SELECT * FROM pharLocation WHERE `index` = :index")
    fun getPharLocation(index: Int): LiveData<PharmacyLocation?>


}