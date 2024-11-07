package com.example.team25.di

import android.app.Service
import com.example.team25.data.network.services.LocationUpdateLauncher
import com.example.team25.ui.status.utils.LocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
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
