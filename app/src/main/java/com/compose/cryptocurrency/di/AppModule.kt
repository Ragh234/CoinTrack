package com.compose.cryptocurrency.di

import android.app.Application
import androidx.room.Room
import com.compose.cryptocurrency.common.AppConstants
import com.compose.cryptocurrency.data.local.CoinTrackDatabase
import com.compose.cryptocurrency.data.remote.CyptoCurrencyApi
import com.compose.cryptocurrency.data.repository.CoinRepositoryImpl
import com.compose.cryptocurrency.data.repository.PortfolioRepositoryImpl
import com.compose.cryptocurrency.data.repository.WatchlistRepositoryImpl
import com.compose.cryptocurrency.domain.repository.CoinRepository
import com.compose.cryptocurrency.domain.repository.PortfolioRepository
import com.compose.cryptocurrency.domain.repository.WatchlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePaprikaApi(): CyptoCurrencyApi {
        return Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CyptoCurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinTrackDatabase(application: Application): CoinTrackDatabase {
        return Room.databaseBuilder(
            application,
            CoinTrackDatabase::class.java,
            "cointrack.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CyptoCurrencyApi, database: CoinTrackDatabase): CoinRepository {
        return CoinRepositoryImpl(api, database.marketDao())
    }

    @Provides
    @Singleton
    fun provideWatchlistRepository(database: CoinTrackDatabase): WatchlistRepository {
        return WatchlistRepositoryImpl(database.watchlistDao())
    }

    @Provides
    @Singleton
    fun providePortfolioRepository(database: CoinTrackDatabase): PortfolioRepository {
        return PortfolioRepositoryImpl(database.portfolioDao())
    }
}
