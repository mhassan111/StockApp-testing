package com.plcoding.stockmarketapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object TestAppModule {

//    @Provides
//    @Singleton
//    fun provideStockDatabase(app: Application): StockDatabase {
//        return Room.inMemoryDatabaseBuilder(
//            app,
//            StockDatabase::class.java
//        ).build()
//    }
}