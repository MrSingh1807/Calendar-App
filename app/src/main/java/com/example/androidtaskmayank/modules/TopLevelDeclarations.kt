package com.example.androidtaskmayank.modules

import android.content.Context
import com.example.androidtaskmayank.utils.NetworkConnectivity
import com.example.androidtaskmayank.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TopLevelDeclarations {


    @Singleton
    @Provides
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return NetworkUtil(context)
    }

}