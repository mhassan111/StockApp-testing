package com.plcoding.stockmarketapp.di

import com.plcoding.stockmarketapp.data.repository.FakeStockRepositoryImpl
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TestRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        fakeStockRepositoryImpl: FakeStockRepositoryImpl
    ): StockRepository
}