package com.jms.searchpharmacy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jms.searchpharmacy.data.model.server.PharmacyLocation

@Database(
    entities = [PharmacyLocation::class],
    version = 1,
    exportSchema = false
)
abstract class SearchPharDatabase: RoomDatabase() {
    abstract fun searchPharDao(): SearchPharDao

    companion object {
        @Volatile
        private var INSTANCE: SearchPharDatabase? = null

        private fun buildDatabase(context: Context): SearchPharDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                SearchPharDatabase::class.java,
                "favorite-pharmacyLocation"
            ).build()

        fun getInstance(context: Context): SearchPharDatabase =
            INSTANCE ?: synchronized(SearchPharDatabase::class.java) {
                INSTANCE ?: buildDatabase(context).also{
                    INSTANCE = it
                }
            }
    }
}