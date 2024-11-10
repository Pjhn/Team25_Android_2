package com.kakaotech.team25M.data.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    private val defaultInputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
    private val defaultOutputFormat = SimpleDateFormat("M월 d일 a h시", Locale.KOREAN)

    /**
     * 주어진 날짜 문자열을 입력 형식(inputFormat)에서 출력 형식(outputFormat)으로 변환
     * @param dateStr 변환할 날짜 문자열
     * @param inputFormat 입력 날짜 형식 (기본값: yyyy-MM-dd HH:mm:ss)
     * @param outputFormat 출력 날짜 형식 (기본값: M월 d일 a h시)
     * @return 변환된 날짜 문자열, 변환에 실패하면 "날짜 없음" 반환
     */
    fun formatDate(
        dateStr: String?,
        inputFormat: SimpleDateFormat = defaultInputFormat,
        outputFormat: SimpleDateFormat = defaultOutputFormat
    ): String {
        return try {
            val date = dateStr?.let { inputFormat.parse(it) }
            date?.let { outputFormat.format(it) } ?: "날짜 없음"
        } catch (e: ParseException) {
            "날짜 없음"
        }
    }
}
