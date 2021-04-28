package com.example.posts.di

import android.app.Application
import com.example.posts.PostsApplication
import com.example.posts.core.di.CoreModule
import com.example.posts.data.di.DataModule
import com.example.posts.framework.datasource.di.DataSourceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CoreModule::class, DataModule::class, DataSourceModule::class, AndroidSupportInjectionModule::class, AndroidInjectionModule::class,
        FragmentBuildersModule::class]
)
interface ApplicationComponent {
    fun inject(application: PostsApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}