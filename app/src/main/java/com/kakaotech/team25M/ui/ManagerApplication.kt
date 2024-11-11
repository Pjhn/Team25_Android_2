package com.kakaotech.team25M.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.kakaotech.team25M.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ManagerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKakaoMapSdk()
        initializeKakaoSdk()
        initializeTransferNetworkLossHandler()
        setLightMode()
    }

    private fun setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initializeTransferNetworkLossHandler() {
        TransferNetworkLossHandler.getInstance(this)
    }

    private fun initializeKakaoMapSdk() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

    private fun initializeKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}
