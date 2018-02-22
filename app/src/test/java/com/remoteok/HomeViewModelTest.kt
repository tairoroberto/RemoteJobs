package com.remoteok

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.remoteok.io.app.data.AppDatabase
import com.remoteok.io.app.data.LocalDataStore
import com.remoteok.io.app.data.RemoteDataStore
import com.remoteok.io.app.domain.HomeUseCase
import com.remoteok.io.app.domain.LocalRepository
import com.remoteok.io.app.domain.RemoteRepository
import com.remoteok.io.app.model.Job
import com.remoteok.io.app.model.JobsResponse
import com.remoteok.io.app.viewmodel.home.HomeViewModel
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    lateinit var localRepositoty: LocalRepository

    @Mock
    lateinit var remoteRepository: RemoteRepository

    lateinit var homeUseCase: HomeUseCase

    lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var response: Observer<List<Job>>

    @Mock
    private lateinit var loadingStatus: Observer<Boolean>

    @Mock
    private lateinit var errorStatus: Observer<String>

    @Before
    fun setUp() {
        val db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()

        localRepositoty = LocalDataStore(db.jobsDAO())
        remoteRepository = RemoteDataStore()
        homeUseCase = HomeUseCase(localRepositoty, remoteRepository)
        homeViewModel = HomeViewModel(homeUseCase)

        homeViewModel.getResponse().observeForever(response)
        homeViewModel.getLoadingStatus().observeForever(loadingStatus)
        homeViewModel.getErrorStatus().observeForever(errorStatus)
    }

    @Test
    fun sholdShowLoadingStatusWhenFetch() {
        //given
        Mockito.`when`(remoteRepository.listAll()).thenReturn(Single.just(JobsResponse("", ArrayList())))

        //when
        homeViewModel.getAllJobs()

        //then
        loadingStatus.onChanged(homeViewModel.getLoadingStatus().value)

    }

    @Test
    fun sholdChangeErrorStatusWhenThrowAException() {
        //given
        Mockito.`when`(remoteRepository.listAll()).thenReturn(Single.error(Throwable()))

        //when
        homeViewModel.getAllJobs()

        //then
        errorStatus.onChanged(homeViewModel.getErrorStatus().value)
    }

    @Test
    fun sholdFetchJobs() {
        //given
        Mockito.`when`(remoteRepository.listAll()).thenReturn(Single.just(JobsResponse("", ArrayList())))

        //when
        homeViewModel.getAllJobs()

        //then
        kotlin.test.assertNotNull(homeViewModel.getResponse().value)
    }
}