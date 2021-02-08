package com.vjezba.androidjetpackgithub.ui.utilities

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class FingerPrintUtils(private val context: Context,
                       private val activity: FragmentActivity )
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
//                // Prompts the user to create credentials that your app accepts.
//                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                    putExtra(
//                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BiometricManager.Authenticators.BIOMETRIC_STRONG
//                    )
//                }
//                startActivityForResult(enrollIntent, BIOMETRICS_REQUEST_CODE)
            }
        }
        return -1
    }

//    fun createBiometricPrompt(): BiometricPrompt {
//        val executor = ContextCompat.getMainExecutor(context)
//
//        val callback = object : BiometricPrompt.AuthenticationCallback() {
//            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                super.onAuthenticationError(errorCode, errString)
//                Log.d("BiometricError", "Authentication $errorCode :: $errString")
//                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
//                    // loginWithPassword() // Because in this app, the negative button allows the user to enter an account password. This is completely optional and your app doesn’t have to do it.
//                    createPromptInfo()
//                }
//            }
//
//            override fun onAuthenticationFailed() {
//                super.onAuthenticationFailed()
//                Log.d("BiometricFailed", "Authentication failed for an unknown reason")
//            }
//
//            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                super.onAuthenticationSucceeded(result)
//                Log.d("BiometricSuccess", "Authentication was successful")
//                getBiometricPromtAuthentificationCallBackSuccess()
//                // Proceed with viewing the private encrypted message.
//                //showEncryptedMessage(result.cryptoObject)
//            }
//        }
//
//        val biometricPrompt = BiometricPrompt(activity, executor, callback)
//        return biometricPrompt
//    }
//
//    fun getBiometricPromtAuthentificationCallBackSuccess() : Boolean {
//        return true
//    }
//
//    fun createPromptInfo(): BiometricPrompt.PromptInfo {
//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("getString(R.string.action_settings)")
//            .setSubtitle("getString(R.string.app_name)")
//            .setDescription("getString(R.string.created_at)")
//            // Authenticate without requiring the user to press a "confirm"
//            // button after satisfying the biometric check
//            .setConfirmationRequired(false)
//            .setNegativeButtonText("getString(R.string.created_by)")
//            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
//            .build()
//        return promptInfo
//    }

}