package com.too.onions.dojang.di

import com.too.onions.dojang.db.DojangDB
import com.too.onions.dojang.db.repo.DojangRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideDojangRepository(dojangDB: DojangDB): DojangRepository {
        return DojangRepository(dojangDB)
    }
}