package com.remotejobs.io.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.companies.repository.CompaniesLocalDataStore
import com.remotejobs.io.app.companies.repository.CompaniesRemoteDataStore
import com.remotejobs.io.app.interfaces.CompaniesLocalRepository
import com.remotejobs.io.app.interfaces.CompaniesRemoteRepository
import com.remotejobs.io.app.companies.usecase.CompaniesUseCase
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModelFactory
import com.remotejobs.io.app.data.database.AppDatabase
import com.remotejobs.io.app.data.database.dao.CompaniesDao
import com.remotejobs.io.app.data.database.dao.FavoritesDao
import com.remotejobs.io.app.data.database.dao.HighestPaidDao
import com.remotejobs.io.app.data.database.dao.JobsDao
import com.remotejobs.io.app.highestpaid.repository.HighestPaidLocalDataStore
import com.remotejobs.io.app.highestpaid.repository.HighestPaidRemoteDataStore
import com.remotejobs.io.app.interfaces.HighestPaidLocalRepository
import com.remotejobs.io.app.interfaces.HighestPaidRemoteRepository
import com.remotejobs.io.app.highestpaid.usecase.HighestPaidUseCase
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModelFactory
import com.remotejobs.io.app.home.repository.FavoritesLocalDataStore
import com.remotejobs.io.app.home.repository.HomeLocalDataStore
import com.remotejobs.io.app.home.repository.HomeRemoteDataStore
import com.remotejobs.io.app.interfaces.FavoriteLocalRepository
import com.remotejobs.io.app.interfaces.HomeLocalRepository
import com.remotejobs.io.app.interfaces.HomeRemoteRepository
import com.remotejobs.io.app.home.usecase.HomeUseCase
import com.remotejobs.io.app.home.viewmodel.HomeViewModelFactory
import io.fabric.sdk.android.Fabric
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

/**
 * Created by tairo on 11/10/17 12:06 AM.
 */
open class CustomApplication : Application(), KodeinAware {

    override val kodein = ConfigurableKodein(mutable = true)

    init {
        kodein.addConfig {
            bind<FirebaseAnalytics>() with provider { FirebaseAnalytics.getInstance(this@CustomApplication) }
            bind<JobsDao>() with provider { AppDatabase.getInstance(this@CustomApplication).jobsDAO() }
            bind<CompaniesDao>() with provider { AppDatabase.getInstance(this@CustomApplication).companiesDAO() }
            bind<HighestPaidDao>() with provider { AppDatabase.getInstance(this@CustomApplication).highestPaidDao() }
            bind<FavoritesDao>() with provider { AppDatabase.getInstance(this@CustomApplication).favoritesDaoDao() }

            bind<HomeLocalRepository>() with provider { HomeLocalDataStore(instance()) }
            bind<FavoriteLocalRepository>() with provider { FavoritesLocalDataStore(instance()) }
            bind<HomeRemoteRepository>() with provider { HomeRemoteDataStore() }
            bind<HomeViewModelFactory>() with provider { HomeViewModelFactory(instance()) }
            bind<HomeUseCase>() with provider {
                HomeUseCase(
                    instance(),
                    instance(),
                    instance()
                )
            }

            bind<HighestPaidLocalRepository>() with provider { HighestPaidLocalDataStore(instance(), instance()) }
            bind<HighestPaidRemoteRepository>() with provider { HighestPaidRemoteDataStore() }
            bind<HighestPaidUseCase>() with provider { HighestPaidUseCase(instance(), instance()) }
            bind<HighestPaidViewModelFactory>() with provider { HighestPaidViewModelFactory(instance()) }

            bind<CompaniesViewModelFactory>() with provider { CompaniesViewModelFactory(instance()) }
            bind<CompaniesUseCase>() with provider { CompaniesUseCase(instance(), instance()) }
            bind<CompaniesLocalRepository>() with provider { CompaniesLocalDataStore(instance(), instance()) }
            bind<CompaniesRemoteRepository>() with provider { CompaniesRemoteDataStore() }

        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit)
    }
}
