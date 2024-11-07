package com.example.team25.di

import com.example.team25.data.repository.DefaultAccompanyRepository
import com.example.team25.data.repository.DefaultCoordinatesRepository
import com.example.team25.data.repository.DefaultManagerRepository
import com.example.team25.data.repository.DefaultReservationRepository
import com.example.team25.data.repository.DefaultS3Repository
import com.example.team25.data.repository.DefaultUserRepository
import com.example.team25.domain.repository.AccompanyRepository
import com.example.team25.domain.repository.CoordinatesRepository
import com.example.team25.domain.repository.ManagerRepository
import com.example.team25.domain.repository.ReservationRepository
import com.example.team25.domain.repository.S3Repository
import com.example.team25.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewmodelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindManagerRepository(defaultManagerRepository: DefaultManagerRepository): ManagerRepository

    @Binds
    @ViewModelScoped
    abstract fun bindS3Repository(defaultS3Repository: DefaultS3Repository): S3Repository

    @Binds
    @ViewModelScoped
    abstract fun bindUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository

    @Binds
    @ViewModelScoped
    abstract fun bindAccompanyRepository(defaultAccompanyRepository: DefaultAccompanyRepository): AccompanyRepository

    @Binds
    @ViewModelScoped
    abstract fun bindCoordinatesRepository(defaultCoordinatesRepository: DefaultCoordinatesRepository): CoordinatesRepository

    @Binds
    @ViewModelScoped
    abstract fun bindReservationRepository(defaultReservationRepository: DefaultReservationRepository): ReservationRepository

}

