package com.vjezba.androidjetpackgithub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

  companion object {
    lateinit var instance: Application
      private set
  }

  // lazy znaci da je varijabla immutabilna i da se inicijalizira samo kada se treba
  // Instance of the AppComponent that will be used by all the Activities in the project
//  val appComponent: AppComponent by lazy {
//    initializeComponent()
//  }
  
  override fun onCreate() {
    super.onCreate()
    instance = this
  }

//  open fun initializeComponent() : AppComponent {
//    // Creates an instance of AppComponent using its Factory constructor
//    // We pass the applicationContext that will be used as Context in the graph
//    return DaggerAppComponent.factory().create(applicationContext)
//  }


}

