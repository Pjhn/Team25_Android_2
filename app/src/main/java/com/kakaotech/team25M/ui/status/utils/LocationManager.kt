package com.kakaotech.team25M.ui.status.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val LATITUDE = doublePreferencesKey("LATITUDE")
        private val LONGITUDE = doublePreferencesKey("LONGITUDE")
    }

    suspend fun storeLocation(latitude: Double, longitude: Double) {
        dataStore.edit { preferences ->
            preferences[LATITUDE] = latitude
            preferences[LONGITUDE] = longitude
        }
    }

    val latitudeFlow: Flow<Double?> = dataStore.data.map { it[LATITUDE] }
    val longitudeFlow: Flow<Double?> = dataStore.data.map { it[LONGITUDE] }
}
