package com.capstone.trashtotreasure.model.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.trashtotreasure.model.data.local.entitiy.NewsEntity
import com.capstone.trashtotreasure.model.data.local.entitiy.RemoteKeys

@Database(
    entities = [NewsEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    abstract fun remoteKeysDao(): RemoteKeysDao

}