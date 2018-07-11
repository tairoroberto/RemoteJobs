package com.remotejobs.io.app.home.di

import android.content.Context
import com.remotejobs.io.app.CustomApplication
import com.remotejobs.io.app.data.AppDatabase
import com.remotejobs.io.app.data.dao.FavoritesDao
import com.remotejobs.io.app.data.dao.JobsDao
import com.remotejobs.io.app.di.PerUiScope
import com.remotejobs.io.app.home.data.FavoritesLocalDataStore
import com.remotejobs.io.app.home.data.HomeLocalDataStore
import com.remotejobs.io.app.home.data.HomeRemoteDataStore
import com.remotejobs.io.app.home.domain.FavoriteLocalRepository
import com.remotejobs.io.app.home.domain.HomeLocalRepository
import com.remotejobs.io.app.home.domain.HomeRemoteRepository
import com.remotejobs.io.app.home.domain.HomeUseCase
import com.remotejobs.io.app.home.viewmodel.HomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @PerUiScope
    @Provides
    internal fun provideContext(application: CustomApplication): Context {
        return application.applicationContext
    }

    @PerUiScope
    @Provides
    internal fun provideJobsDao(context: Context): JobsDao {
        return AppDatabase.getInstance(context).jobsDAO()
    }

    @PerUiScope
    @Provides
    internal fun provideFavoritesDao(context: Context): FavoritesDao {
        return AppDatabase.getInstance(context).favoritesDaoDao()
    }


    @PerUiScope
    @Provides
    internal fun provideHomeLocalRepository(jobsDao: JobsDao): HomeLocalRepository {
        return HomeLocalDataStore(jobsDao)
    }

    @PerUiScope
    @Provides
    internal fun provideFavoriteLocalRepository(favoritesDao: FavoritesDao): FavoriteLocalRepository {
        return FavoritesLocalDataStore(favoritesDao)
    }

    @PerUiScope
    @Provides
    internal fun provideHomeRemoteRepository(): HomeRemoteRepository {
        return HomeRemoteDataStore()
    }

    @PerUiScope
    @Provides
    internal fun provideHomeViewModelFactory(homeUseCase: HomeUseCase): HomeViewModelFactory {
        return HomeViewModelFactory(homeUseCase)
    }

    @PerUiScope
    @Provides
    internal fun provideHomeUseCase(homeLocalRepository: HomeLocalRepository,
                                    remoteRepository: HomeRemoteRepository,
                                    favoriteLocalRepository: FavoriteLocalRepository): HomeUseCase {
        return HomeUseCase(homeLocalRepository, remoteRepository, favoriteLocalRepository)
    }
}