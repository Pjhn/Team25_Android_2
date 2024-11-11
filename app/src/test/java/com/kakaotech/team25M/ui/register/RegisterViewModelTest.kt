package com.kakaotech.team25M.ui.register

import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kakaotech.team25M.domain.model.ImageFolder
import com.kakaotech.team25M.domain.usecase.RegisterManagerUseCase
import com.kakaotech.team25M.domain.usecase.S3UploadUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var registerViewModel: RegisterViewModel

    private val registerManagerUseCase: RegisterManagerUseCase = mockk()
    private val s3UploadUseCase: S3UploadUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        registerViewModel = RegisterViewModel(registerManagerUseCase, s3UploadUseCase)
        mockLogClass()
    }

    @Test
    fun `매니저 등록 UseCase가 실행되는 지 테스트`() = runTest {
        // Given
        mockkStatic(Uri::class)
        val name = "테스트"
        val profileImageUri = "file:///android_asset/images/profile/test"
        val certificateImageUri = "file:///android_asset/images/certificate/test"

        // When
        coEvery { Uri.parse(profileImageUri) } returns mockk()
        coEvery { Uri.parse(certificateImageUri) } returns mockk()

        coEvery { s3UploadUseCase(name, Uri.parse(profileImageUri), ImageFolder.PROFILE.path) } returns "images/profile/test"
        coEvery { s3UploadUseCase(name, Uri.parse(certificateImageUri), ImageFolder.CERTIFICATE.path) } returns "images/certificate/test"

        // RegisterManagerUseCase mock
        coEvery { registerManagerUseCase.invoke(any()) } returns Result.success("매니저 등록 성공")


        registerViewModel.updateName(name)
        registerViewModel.updateProfileImage(profileImageUri)
        registerViewModel.updateCertificateImage(certificateImageUri)

        // uploadImage 실행
        registerViewModel.uploadImage()

        advanceUntilIdle()

        // Then
        coVerify { registerManagerUseCase.invoke(any()) }
    }


    private fun mockLogClass() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
