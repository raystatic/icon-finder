package com.raystatic.iconfinder.di

import android.content.Context
import androidx.room.Room
import com.raystatic.iconfinder.room.IconDB
import com.raystatic.iconfinder.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        IconDB::class.java,
        Constants.ROOM_DB_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesDownloadsDao(db:IconDB) = db.getDownloadsDao()

}