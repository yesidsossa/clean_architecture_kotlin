package com.instaleap.data.repository.movie
import androidx.paging.PagingSource
import com.instaleap.data.db.movies.MovieDao
import com.instaleap.data.db.movies.MovieRemoteKeyDao
import com.instaleap.data.db.series.SerieDao
import com.instaleap.data.db.series.SeriesRemoteKeyDao
import com.instaleap.data.entities.*
import com.instaleap.data.exception.DataNotAvailableException
import com.instaleap.domain.util.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class StreamingLocalDataSourceTest {

    private lateinit var sut: StreamingLocalDataSource
    private val movieDao: MovieDao = mock()
    private val serieDao: SerieDao = mock()
    private val remoteKeyDao: MovieRemoteKeyDao = mock()
    private val serieRemoteKeyDao: SeriesRemoteKeyDao = mock()

    @Before
    fun setUp() {
        sut = StreamingLocalDataSource(movieDao, serieDao, remoteKeyDao, serieRemoteKeyDao)
    }

    // Test para verificar que se obtiene el PagingSource correcto de movies
    @Test
    fun `test movies returns correct PagingSource`() {
        val pagingSource: PagingSource<Int, MovieDbData> = mock()
        whenever(movieDao.movies()).thenReturn(pagingSource)

        val result = sut.movies()

        assertEquals(pagingSource, result)
    }

    // Test para verificar que se obtiene el PagingSource correcto de series
    @Test
    fun `test series returns correct PagingSource`() {
        val pagingSource: PagingSource<Int, SerieDbData> = mock()
        whenever(serieDao.series()).thenReturn(pagingSource)

        val result = sut.series()

        assertEquals(pagingSource, result)
    }

    // Test para verificar que se obtiene una lista de películas cuando hay datos
    @Test
    fun `test getMovies returns success when movies are available`() = runBlocking {
        val movieDbData = MovieDbData(1, "Title", "Description", "Image", "Action", "BackgroundUrl")
        whenever(movieDao.getMovies()).thenReturn(listOf(movieDbData))

        val result = sut.getMovies()

        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
        assertEquals(movieDbData.toDomain(), result.data[0])
    }

    // Test para verificar que se retorna error cuando no hay películas disponibles
    @Test
    fun `test getMovies returns error when no movies are available`() = runBlocking {
        whenever(movieDao.getMovies()).thenReturn(emptyList())

        val result = sut.getMovies()

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error is DataNotAvailableException)
    }

    // Test para verificar que se obtiene una lista de series cuando hay datos
    @Test
    fun `test getSeries returns success when series are available`() = runBlocking {
        val serieDbData = SerieDbData(1, "Title", "Description", "Image", "Drama", "BackgroundUrl")
        whenever(serieDao.getSeries()).thenReturn(listOf(serieDbData))

        val result = sut.getSeries()

        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
        assertEquals(serieDbData.toDomain(), result.data[0])
    }

    // Test para verificar que se retorna error cuando no hay series disponibles
    @Test
    fun `test getSeries returns error when no series are available`() = runBlocking {
        whenever(serieDao.getSeries()).thenReturn(emptyList())

        val result = sut.getSeries()

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error is DataNotAvailableException)
    }

    // Test para verificar que se retorna la película correcta cuando es encontrada
    @Test
    fun `test getMovie returns success when movie is found`() = runBlocking {
        val movieDbData = MovieDbData(1, "Title", "Description", "Image", "Action", "BackgroundUrl")
        whenever(movieDao.getMovie(any())).thenReturn(movieDbData)

        val result = sut.getMovie(1)

        assertTrue(result is Result.Success)
        assertEquals(movieDbData.toDomain(), (result as Result.Success).data)
    }

    // Test para verificar que se retorna error cuando la película no es encontrada
    @Test
    fun `test getMovie returns error when movie is not found`() = runBlocking {
        whenever(movieDao.getMovie(any())).thenReturn(null)

        val result = sut.getMovie(1)

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error is DataNotAvailableException)
    }

    // Test para verificar que se guardan correctamente las películas en el DAO
    @Test
    fun `test saveMovies calls saveMovies on movieDao`() = runBlocking {
        val movieEntities = listOf(MovieData(1, "Title", "Description", "Image", "Action", "BackgroundUrl"))
        val movieDbData = movieEntities.map { it.toDbData() }

        sut.saveMovies(movieEntities)

        verify(movieDao).saveMovies(movieDbData)
    }

    // Test para verificar que se limpia correctamente la tabla de películas
    @Test
    fun `test clearMovies calls clearMoviesExceptFavorites on movieDao`() = runBlocking {
        sut.clearMovies()

        verify(movieDao).clearMoviesExceptFavorites()
    }

    // Test para verificar que se limpian correctamente las claves remotas de películas
    @Test
    fun `test clearRemoteKeys calls clearRemoteKeys on remoteKeyDao`() = runBlocking {
        sut.clearRemoteKeys()

        verify(remoteKeyDao).clearRemoteKeys()
    }
}
