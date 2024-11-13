package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.ReportDto
import com.kakaotech.team25M.data.network.dto.ServiceResponse
import com.kakaotech.team25M.data.network.services.ReportService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class DefaultReportRepositoryTest {

    private lateinit var reportService: ReportService
    private lateinit var reportRepository: DefaultReportRepository

    @Before
    fun setUp() {
        reportService = mockk()
        reportRepository = DefaultReportRepository(reportService)
        mockLogClass()
    }

    @Test
    fun `보고서 전송 성공 시 성공 로그 출력`() = runTest {
        // Given
        val reservationId = "1"
        val reportDto = ReportDto(
            doctorSummary = "환자 상태 양호",
            frequency = 2,
            medicineTime = "식후 30분",
            timeOfDays = "아침 점심"
        )
        val response = ServiceResponse(data = reportDto, message = "성공", status = true)

        coEvery { reportService.postReportInfo(reservationId, reportDto) } returns Response.success(response)

        // When
        reportRepository.postReport(reservationId, reportDto)

        // Then
        coVerify { reportService.postReportInfo(reservationId, reportDto) }
        verify { Log.d("ReportRepository", response.toString()) }
    }

    @Test
    fun `보고서 전송 실패 시 오류 로그 출력`() = runTest {
        // Given
        val reservationId = "2"
        val reportDto = ReportDto(
            doctorSummary = "응급 상황 발생",
            frequency = 1,
            medicineTime = "식전 30분",
            timeOfDays = "저녁"
        )

        coEvery { reportService.postReportInfo(reservationId, reportDto) } returns Response.error(
            401,
            "등록되지 않은 회원입니다".toResponseBody()
        )

        // When
        reportRepository.postReport(reservationId, reportDto)

        // Then
        coVerify { reportService.postReportInfo(reservationId, reportDto) }
        verify { Log.e("ReportRepository", "401") }
    }

    private fun mockLogClass() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }
}
