/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.androidjetpackgithub.viewmodels

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vjezba.androidjetpackgithub.ui.activities.LoginError
import com.vjezba.androidjetpackgithub.ui.activities.LoginSuccess
import com.vjezba.androidjetpackgithub.ui.activities.LoginViewState
import com.vjezba.androidjetpackgithub.ui.utilities.FingerPrintUtils
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * The ViewModel for [SavedLanguagesFragment].
 */
class FingerprintViewModel @ViewModelInject internal constructor(
    private val fingerPrintUtils: FingerPrintUtils,
    @ActivityContext private val context: Context,
    private val activity: FragmentActivity
) : ViewModel() {


    private val _fingerPrintState = MutableLiveData<Boolean>().apply {
        false
    }
    val fingerPrintState: LiveData<Boolean>
        get() = _fingerPrintState


    fun getBiometricManager() : BiometricManager {
        return fingerPrintUtils.getBiometricManager()
    }

    fun checkIfFingerprinteIsEnabled() : Int {
        return fingerPrintUtils.checkIfUserCanAuthenticate()
    }


    fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricError", "Authentication $errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // loginWithPassword() // Because in this app, the negative button allows the user to enter an account password. This is completely optional and your app doesn’t have to do it.
                    createPromptInfo()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricFailed", "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BiometricSuccess", "Authentication was successful")
                getBiometricPromtAuthentificationCallBackSuccess()
                // Proceed with viewing the private encrypted message.
                //showEncryptedMessage(result.cryptoObject)
            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        return biometricPrompt
    }

    fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("getString(R.string.action_settings)")
            .setSubtitle("getString(R.string.app_name)")
            .setDescription("getString(R.string.created_at)")
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText("getString(R.string.created_by)")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
        return promptInfo
    }

    fun getBiometricPromtAuthentificationCallBackSuccess() {
        _fingerPrintState.value = true
    }

}
