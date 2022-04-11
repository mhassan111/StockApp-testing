package com.plcoding.stockmarketapp.data.repository

import com.plcoding.stockmarketapp.domain.model.CompanyInfo
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.model.IntradayInfo
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeStockRepositoryImpl @Inject constructor() : StockRepository {

    private val companyListing = mutableListOf<CompanyListing>()

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {
            emit(Resource.Loading(isLoading = true))
            emit(Resource.Success(data = companyListing.filter {
                it.name.lowercase().contains(query)
            }))

            val localDbEmpty = companyListing.isEmpty() && query.isEmpty()
            val loadFromCache = !localDbEmpty && !fetchFromRemote
            if (loadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            ('a'..'z').forEach {
                companyListing.add(
                    CompanyListing(
                        name = "${it.toString()}_name",
                        symbol = it.toString(),
                        exchange = "${it.toString()}_exchange"
                    )
                )
            }
            emit(Resource.Success(data = companyListing))
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return Resource.Success(listOf())
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return Resource.Success(
            CompanyInfo()
        )
    }
}