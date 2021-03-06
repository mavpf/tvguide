package com.example.jobsity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jobsity.data.classes.Favorites
import com.example.jobsity.data.db.FavoritesDao
import com.example.jobsity.data.db.FavoritesDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.OrderWith
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RoomTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FavoritesDatabase
    private lateinit var favoritesDao: FavoritesDao

    @Before
    fun setup() {
        hiltRule.inject()
        favoritesDao = database.favoritesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }




    @Test
    @Config(manifest=Config.NONE)
    @Throws(Exception::class)
    fun testARoomInsert() {
        runBlocking(Dispatchers.Default) {
            val testData = Favorites(
                99999,
                "Test",
                "Action",
                ""
            )

            val count: Int

            favoritesDao.insertFavorite(testData)
            count = favoritesDao.getCountFavorites(99999)
            Assert.assertEquals(1, count)
        }
    }

        @Test
        @Config(manifest=Config.NONE)
        @Throws(Exception::class)
        fun testBRoomSelect() {
            runBlocking(Dispatchers.Default) {

                val idFavorites = favoritesDao.getIdFavorites().first()
                Assert.assertEquals(99999, idFavorites[0])

            }
        }

        @Test
        @Config(manifest=Config.NONE)
        @Throws(Exception::class)
        fun testCRoomDelete() {
            runBlocking(Dispatchers.Default) {
                val testData = Favorites(
                    99999,
                    "Test",
                    "Action",
                    ""
                )

                val count: Int

                favoritesDao.delete(testData)
                count = favoritesDao.getCountFavorites(99999)
                Assert.assertEquals(0, count)
            }
        }
   }

