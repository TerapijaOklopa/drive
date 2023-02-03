package com.mobile.drive.screens.drive

import com.google.api.services.drive.model.File
import com.google.common.truth.Truth.assertThat
import com.mobile.BaseTest
import com.mobile.drive.data.drive.DriveRepository
import com.mobile.drive.mobile.ui.drive.DEFAULT_FOLDER_ID
import com.mobile.drive.mobile.ui.drive.DriveViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DriveViewModelTest : BaseTest() {

    private val files = listOf(File())
    private val searchFiles = listOf(File(), File())
    private var driveRepository: DriveRepository = MockDriveRepo()
    private lateinit var driveViewModel: DriveViewModel

    @Before
    fun setUp() {
        driveViewModel = DriveViewModel(
            driveRepository = driveRepository,
            dispatcherProvider = coroutineScope.dispatcherProvider
        )
    }

    @Test
    fun `DriveViewModel get files success`() = runTest {
        assertThat(driveViewModel.uiContent.value.state.data).isNotNull()
        assertThat(driveViewModel.uiContent.value.state.data?.size).isEqualTo(files.size)
    }

    @Test
    fun `DriveViewModel get files failed`() = runTest {
        val t = Throwable()
        val repository: DriveRepository = mockk(relaxed = true)
        driveViewModel = DriveViewModel(
            driveRepository = repository,
            dispatcherProvider = coroutineScope.dispatcherProvider
        )
        coEvery {
            repository.getFolderFiles(DEFAULT_FOLDER_ID)
        } returns Result.failure(t)
        assertThat(driveViewModel.uiContent.value.state.error?.message).isEqualTo(t.message)
    }

    @Test
    fun `DriveViewModel search success`() = runTest {
        val query = "query"
        driveViewModel.searchFiles(query)
        assertThat(driveViewModel.uiContent.value.state.data).isNotNull()
        assertThat(driveViewModel.uiContent.value.state.data?.size).isEqualTo(searchFiles.size)
    }

    @Test
    fun `DriveViewModel search failed`() = runTest {
        val query = "query"
        val t = Throwable()
        val repository: DriveRepository = mockk(relaxed = true)
        driveViewModel = DriveViewModel(
            driveRepository = repository,
            dispatcherProvider = coroutineScope.dispatcherProvider
        )
        coEvery {
            repository.searchFiles(query)
        } returns Result.failure(t)
        driveViewModel.searchFiles(query)
        assertThat(driveViewModel.uiContent.value.state.error?.message).isEqualTo(t.message)
    }

    inner class MockDriveRepo : DriveRepository {

        override suspend fun getFolderFiles(folderId: String): Result<List<File>> {
            return Result.success(files)
        }

        override suspend fun searchFiles(query: String): Result<List<File>> {
            return Result.success(searchFiles)
        }
    }
}
