package com.example.posts

import android.app.Application
import com.example.posts.di.ApplicationComponent
import com.example.posts.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class PostsApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}