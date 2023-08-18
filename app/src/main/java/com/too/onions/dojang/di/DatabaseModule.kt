package com.too.onions.dojang.di

import android.content.Context
import com.too.onions.dojang.db.DojangDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDojangDB(@ApplicationContext context: Context): DojangDB {
        return DojangDB.getDatabase(context)
    }
}