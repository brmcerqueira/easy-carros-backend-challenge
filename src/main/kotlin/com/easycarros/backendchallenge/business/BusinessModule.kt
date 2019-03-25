package com.easycarros.backendchallenge.business

import com.easycarros.backendchallenge.common.Auditor
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class BusinessModule {
    @Provides
    @Singleton
    @Named("business")
    fun provideAuditor() = Auditor("business")
}
