package com.instaleap.data.repository.movie
import android.util.Log
import androidx.paging.*
import com.instaleap.data.entities.SerieData
import com.instaleap.data.entities.SerieDbData
import com.instaleap.data.entities.SerieRemoteKeyDbData
import com.instaleap.data.exception.DataNotAvailableException
import com.instaleap.data.repository.Serie.SerieRemoteMediator
import com.instaleap.domain.util.Result
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.*

@OptIn(ExperimentalPagingApi::class)
class SerieRemoteMediatorTest {

    private val local: StreamingDataSource.Local = mock()
    private val remote: StreamingDataSource.Remote = mock()

    private lateinit var logMock: MockedStatic<Log>
    private lateinit var sut: SerieRemoteMediator

    @Before
    fun setUp() {
        logMock = mockStatic(Log::class.java).apply {
            `when`<Int> { Log.d(anyString(), anyString()) }.thenReturn(0)
        }
        sut = SerieRemoteMediator(local, remote)
    }

    @After
    fun tearDown() {
        logMock.close()
    }

    @Test
    fun `load refresh success when remote returns data`() = runBlocking {
        val serieData = SerieData(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(remote.getSeries(any(), any())).thenReturn(Result.Success(listOf(serieData)))
        whenever(local.clearSeries()).thenReturn(Unit)
        whenever(local.clearSeriesRemoteKeys()).thenReturn(Unit)
        whenever(local.saveSeries(any())).thenReturn(Unit)
        whenever(local.saveSerieRemoteKey(any())).thenReturn(Unit)

        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        verify(local).clearSeries()
        verify(local).clearSeriesRemoteKeys()
        verify(local).saveSeries(listOf(serieData))
        verify(local).saveSerieRemoteKey(any())
    }

    @Test
    fun `load refresh error when remote returns error`() = runBlocking {
        val error = DataNotAvailableException()
        whenever(remote.getSeries(any(), any())).thenReturn(Result.Error(error))

        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(error, (result as RemoteMediator.MediatorResult.Error).throwable)
    }

    @Test
    fun `load prepend should return endOfPaginationReached true`() = runBlocking {
        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `load append success when remote returns data`() = runBlocking {
        val serieData = SerieData(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getSerieLastRemoteKey()).thenReturn(SerieRemoteKeyDbData(1, null, 5))
        whenever(remote.getSeries(any(), any())).thenReturn(Result.Success(listOf(serieData)))
        whenever(local.saveSeries(any())).thenReturn(Unit)
        whenever(local.saveSerieRemoteKey(any())).thenReturn(Unit)

        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        verify(local).saveSeries(listOf(serieData))
        verify(local).saveSerieRemoteKey(any())
    }

    @Test
    fun `load append error when remote returns error`() = runBlocking {
        val error = DataNotAvailableException()
        whenever(local.getSerieLastRemoteKey()).thenReturn(SerieRemoteKeyDbData(1, null, 4))
        whenever(remote.getSeries(any(), any())).thenReturn(Result.Error(error))

        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(error, (result as RemoteMediator.MediatorResult.Error).throwable)
    }

    @Test
    fun `load append when no remote key return end of page`() = runBlocking {
        whenever(local.getSerieLastRemoteKey()).thenReturn(null)

        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `load append endOfPaginationReached true when no more pages`() = runBlocking {
        whenever(local.getSerieLastRemoteKey()).thenReturn(SerieRemoteKeyDbData(1, null, null))

        val pagingState = PagingState<Int, SerieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}
