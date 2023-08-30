package com.too.onions.gguggugi.di

import com.too.onions.gguggugi.db.DojangDB
import com.too.onions.gguggugi.db.repo.DojangRepository
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