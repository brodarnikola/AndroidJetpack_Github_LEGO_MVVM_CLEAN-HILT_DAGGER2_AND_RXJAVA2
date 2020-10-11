package com.vjezba.androidjetpackgithub.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vjezba.androidjetpackgithub.App
import com.vjezba.androidjetpackgithub.R
import com.vjezba.domain.repository.UserManager
import com.vjezba.domain.user.UserManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    //val userManager: UserManager

    //val userManager = (application as App).appComponent.userManager()

    @Inject
    lateinit var userManager: UserManager

    //val userManager = UserManagerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if( !userManager.isUserLoggedIn() ) {
            if( !userManager.isUserRegistered() ) {
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        else {
            startActivity(Intent(this, LanguagesActivity::class.java))
            finish()
        }

    }

}