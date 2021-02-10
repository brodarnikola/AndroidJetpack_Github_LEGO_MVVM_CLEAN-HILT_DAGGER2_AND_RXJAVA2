package com.vjezba.androidjetpackgithub.ui.utilities

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext

class FingerPrintUtils( @ActivityContext private val context: Context )
    : RecyclerView.ItemDecoration() {

    fun getBiometricManager() : BiometricManager {
        val biometricManager = BiometricManager.from(context)
        return biometricManager
    }

    fun checkIfUserCanAuthenticate() : Int {
        when (getBiometricManager().canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                return BiometricManager.BIOMETRIC_SUCCESS
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                return BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                return BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("MY_APP_TAG", "Biometric has not one fingeprint added")
                return BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
            }
        }
        return -1
    }


}