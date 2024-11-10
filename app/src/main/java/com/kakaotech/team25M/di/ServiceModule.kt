package com.kakaotech.team25M.di

import android.app.Service
import com.kakaotech.team25M.data.network.services.LocationUpdateLauncher
import com.kakaotech.team25M.ui.status.utils.LocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideLocationUpdateLauncher(
        service: Service,
        locationManager: LocationManager
    ): LocationUpdateLauncher {
        return LocationUpdateLauncher(service, locationManager)
    }
}