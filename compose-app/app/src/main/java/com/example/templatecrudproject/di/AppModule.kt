package com.example.templatecrudproject.di

import android.app.Application
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule(private val application: Application) {

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

    }
}