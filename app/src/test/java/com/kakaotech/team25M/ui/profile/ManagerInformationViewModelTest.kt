package com.kakaotech.team25M.ui.profile

import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kakaotech.team25M.data.network.dto.ProfileData
import com.kakaotech.team25M.data.network.dto.ProfileDto
import com.kakaotech.team25M.data.network.dto.WorkingHour
import com.kakaotech.team25M.domain.model.WorkTimeDomain
import com.kakaotech.team25M.domain.usecase.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ManagerInformationViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ManagerInformationViewModel
    private val getProfileUseCase: GetProfileUseCase = mockk()
    private val getImageUriUseCase: GetImageUriUseCase = mockk()
    private val s3UploadUseCase: S3UploadUseCase = mockk()
    private val patchImageUseCase: PatchImageUseCase = mockk()
    private val patchLocationUseCase: PatchLocationUseCase = mockk()
    private val patchTimeUseCase: PatchTimeUseCase = mockk()
    private val patchCommentUseCase: PatchCommentUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ManagerInformationViewModel(
            getProfileUseCase,
            getImageUriUseCase,
            s3UploadUseCase,
            patchImageUseCase,
            patchLocationUseCase,
            patchTimeUseCase,
            patchCommentUseCase
        )
        mockLogClass()
    }

    @Test
    fun `프로필 불러오기 테스트`() = runTest {
        // Given
        mockkStatic(Uri::class)
        val profileDto = ProfileDto(
            status = true,
            message = "프로필 조회를 성공했습니다",
            data = ProfileData(
                name = "산지니",
                profileImage = "/images/profile/kim_jisoo",
                career = "2012~2020: 부산대학병원 간호사 근무",
                comment = "친부모처럼 따뜻하게 모시겠습니다 *^__^*",
                workingRegion = "부산광역시 남구",
                gender = "여성",
                workingHour = WorkingHour(
                    monStartTime = "09:00",
                    monEndTime = "18:00",
                    tueStartTime = "10:00",
                    tueEndTime = "18:00",
                    wedStartTime = "00:00",
                    wedEndTime = "00:00",
                    thuStartTime = "00:00",
                    thuEndTime = "00:00",
                    friStartTime = "00:00",
                    friEndTime = "00:00",
                    satStartTime = "00:00",
                    satEndTime = "00:00",
                    sunStartTime = "00:00",
                    sunEndTime = "00:00"
                )
            )
        )

        coEvery { getImageUriUseCase.invoke("/images/profile/kim_jisoo") } returns mockk()
        coEvery { getProfileUseCase.invoke() } returns profileDto

        // When
        viewModel.getProfile()
        advanceUntilIdle()

        // Then
        assertEquals(ManagerInformationViewModel.ProfileLoadStatus.SUCCESS, viewModel.profileLoadStatus.first())
    }

    @Test
    fun `근무 지역 수정 테스트`() = runTest {
        // Given
        val sido = "서울"
        val sigungu = "강남구"
        coEvery { patchLocationUseCase.invoke(sido, sigungu) } returns Result.success("근무 지역을 성공적으로 변경했습니다.")

        // When
        viewModel.patchLocation("서울", "강남구")
        advanceUntilIdle()

        // Then
        assertEquals(PatchStatus.SUCCESS, viewModel.locationPatched.first())
    }

    @Test
    fun `근무 시간 수정 테스트`() = runTest {
        // Given
        val workTime = WorkTimeDomain(
            monStartTime = "09:00", monEndTime = "18:00",
            tueStartTime = "09:00", tueEndTime = "18:00",
            wedStartTime = "09:00", wedEndTime = "18:00",
            thuStartTime = "09:00", thuEndTime = "18:00",
            friStartTime = "09:00", friEndTime = "18:00",
            satStartTime = "09:00", satEndTime = "18:00",
            sunStartTime = "09:00", sunEndTime = "18:00",
        )
        coEvery { patchTimeUseCase.invoke(workTime) } returns Result.success("근무 시간을 성공적으로 변경했습니다.")
        // When
        viewModel.patchTime(workTime)
        advanceUntilIdle()

        // Then
        assertEquals(PatchStatus.SUCCESS, viewModel.timePatched.first())
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
