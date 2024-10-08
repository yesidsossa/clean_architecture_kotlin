package com.instaleap.data.repository.movie
import android.util.Log
import androidx.paging.*
import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.MovieDbData
import com.instaleap.data.entities.MovieRemoteKeyDbData
import com.instaleap.data.exception.DataNotAvailableException
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
class MovieRemoteMediatorTest {

    private val local: StreamingDataSource.Local = mock()
    private val remote: StreamingDataSource.Remote = mock()
    private lateinit var logMock: MockedStatic<Log>
    private lateinit var sut: MovieRemoteMediator

    @Before
    fun setUp() {
        logMock = mockStatic(Log::class.java).apply {
            `when`<Int> { Log.d(anyString(), anyString()) }.thenReturn(0)
        }
        sut = MovieRemoteMediator(local, remote)
    }

    @After
    fun tearDown() {
        logMock.close()
    }

    @Test
    fun `load refresh success when remote returns data`() = runBlocking {
        val movieData = MovieData(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(remote.getMovies(any(), any())).thenReturn(Result.Success(listOf(movieData)))
        whenever(local.clearMovies()).thenReturn(Unit)
        whenever(local.clearRemoteKeys()).thenReturn(Unit)
        whenever(local.saveMovies(any())).thenReturn(Unit)
        whenever(local.saveRemoteKey(any())).thenReturn(Unit)

        val pagingState = PagingState<Int, MovieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        verify(local).clearMovies()
        verify(local).clearRemoteKeys()
        verify(local).saveMovies(listOf(movieData))
        verify(local).saveRemoteKey(any())
    }

    @Test
    fun `load refresh error when remote returns error`() = runBlocking {
        val error = DataNotAvailableException()
        whenever(remote.getMovies(any(), any())).thenReturn(Result.Error(error))

        val pagingState = PagingState<Int, MovieDbData>(
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
        val pagingState = PagingState<Int, MovieDbData>(
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
        val movieData = MovieData(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getLastRemoteKey()).thenReturn(MovieRemoteKeyDbData(1, null, 5))
        whenever(remote.getMovies(any(), any())).thenReturn(Result.Success(listOf(movieData)))
        whenever(local.saveMovies(any())).thenReturn(Unit)
        whenever(local.saveRemoteKey(any())).thenReturn(Unit)

        val pagingState = PagingState<Int, MovieDbData>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        val result = sut.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        verify(local).saveMovies(listOf(movieData))
        verify(local).saveRemoteKey(any())
    }

    @Test
    fun `load append error when remote returns error`() = runBlocking {
        val error = DataNotAvailableException()
        whenever(local.getLastRemoteKey()).thenReturn(MovieRemoteKeyDbData(1, null, 4))
        whenever(remote.getMovies(any(), any())).thenReturn(Result.Error(error))

        val pagingState = PagingState<Int, MovieDbData>(
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
        whenever(local.getLastRemoteKey()).thenReturn(null)

        val pagingState = PagingState<Int, MovieDbData>(
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
        whenever(local.getLastRemoteKey()).thenReturn(MovieRemoteKeyDbData(1, null, null))

        val pagingState = PagingState<Int, MovieDbData>(
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
