package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.ManagerRegisterDto
import com.kakaotech.team25M.data.network.dto.NameData
import com.kakaotech.team25M.data.network.dto.NameDto
import com.kakaotech.team25M.data.network.response.RegisterManagerResponse
import com.kakaotech.team25M.data.network.services.ManagerService
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class DefaultManagerRepositoryTest {

    private lateinit var managerService: ManagerService
    private lateinit var managerRepository: DefaultManagerRepository

    @Before
    fun setUp() {
        managerService = mockk()
        managerRepository = DefaultManagerRepository(managerService)
        mockLogClass()
    }

    @Test
    fun `매니저 등록 성공 시 성공 메시지를 반환한다`() = runBlocking {
        // Given
        val managerRegisterDto = ManagerRegisterDto(
            name = "테스트",
            profileImage = "images/profile/테스트",
            gender = "남성",
            career = "없음",
            comment = "테스트",
            certificateImage = "images/certificate/테스트"
        )
        val response = RegisterManagerResponse(
            status = true,
            message = "매니저 등록을 성공했습니다.",
            data = emptyMap()
        )

        coEvery { managerService.registerManager(managerRegisterDto) } returns Response.success(response)

        // When
        val result = managerRepository.registerManager(managerRegisterDto)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("매니저 등록을 성공했습니다.", result.getOrNull())
    }

    @Test
    fun `매니저 등록 실패 시 예외를 반환한다`() = runBlocking {
        // Given
        val managerRegisterDto = ManagerRegisterDto(
            name = "테스트",
            profileImage = "images/profile/테스트",
            gender = "남성",
            career = "없음",
            comment = "테스트",
            certificateImage = "images/certificate/테스트"
        )
        coEvery { managerService.registerManager(managerRegisterDto) } returns Response.error(
            400, "".toResponseBody()
        )

        // When
        val result = managerRepository.registerManager(managerRegisterDto)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `응답 본문이 null이거나 상태가 false일 경우 예외를 반환한다`() = runBlocking {
        // Given
        val managerRegisterDto = ManagerRegisterDto(
            name = "테스트",
            profileImage = "images/profile/테스트",
            gender = "남성",
            career = "없음",
            comment = "테스트",
            certificateImage = "images/certificate/테스트"
        )
        val response = RegisterManagerResponse(status = false, message = "Failed to register", data = emptyMap())

        coEvery { managerService.registerManager(managerRegisterDto) } returns Response.success(response)

        // When
        val result = managerRepository.registerManager(managerRegisterDto)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `이름 조회 성공 시 이름을 반환한다`() = runBlocking {
        // Given
        val nameDto =
            NameDto(status = true, message = "Success", data = NameData("Manager Name"))

        coEvery { managerService.getName() } returns Response.success(nameDto)

        // When
        val name = managerRepository.getName()

        // Then
        assertEquals("Manager Name", name)
    }

    @Test
    fun `이름 조회 실패 시 null을 반환한다`() = runBlocking {
        // Given
        coEvery { managerService.getName() } returns Response.error(400, "".toResponseBody())

        // When
        val name = managerRepository.getName()

        // Then
        assertNull(name)
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
