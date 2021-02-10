package com.vjezba.androidjetpackgithub.ui.fragments

import android.content.Intent
import android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentFingerprintBinding
import com.vjezba.androidjetpackgithub.viewmodels.FingerprintViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_fingerprint.*


/**
 * A simple [Fragment] subclass.
 * Use the [FingerprintBiometricFragment.newInstance] factory method to
 * create an instance of this fragments.
 */
@AndroidEntryPoint
class FingerprintBiometricFragment : Fragment() {

    private val BIOMETRICS_REQUEST_CODE = 1
    private lateinit var biometricPrompt: BiometricPrompt

    private val viewModel: FingerprintViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFingerprintBinding.inflate(inflater, container, false)

        activity?.speedDial?.visibility = View.GONE

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == BIOMETRICS_REQUEST_CODE ) {
            Log.i("FingerprintAvailable", "New fingerprint added to mobile phone")
        }
    }

    override fun onStart() {
        super.onStart()

        checkIfThereIsAtLeastOneFingerprintAddedToMobilePhone()
        setOnClickListeners()
        addLiveData()
    }

    private fun setOnClickListeners() {
        biometricPrompt = viewModel.createBiometricPrompt()

        val fingerPrintTitle = resources.getString(R.string.biometric_fingerprint_title)
        val fingerPrintDescription = resources.getString(R.string.biometric_fingerprint_description)
        val fingerPrintCancel = resources.getString(R.string.biometric_fingerprint_cancel)

        btnFingerPrint.setOnClickListener {
            val promptInfo = viewModel.createPromptInfo(fingerPrintTitle, fingerPrintDescription, fingerPrintCancel)
            if (viewModel.getBiometricManager().canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                detail_image.visibility = View.VISIBLE
            }
        }
    }

    private fun checkIfThereIsAtLeastOneFingerprintAddedToMobilePhone() {
        val notOneFingerExistOnMobilePhone = viewModel.checkIfFingerprinteIsEnabled()
        if( notOneFingerExistOnMobilePhone == BIOMETRIC_ERROR_NONE_ENROLLED ) {
            Log.i("FingerprintAvailable", "Da li ce uci za fingerprint Check if fingerprint is availabe: ${notOneFingerExistOnMobilePhone}")
            // Prompts the user to create credentials that your app accepts.
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
            }
            startActivityForResult(enrollIntent, BIOMETRICS_REQUEST_CODE)
        }
        Log.i("FingerprintAvailable", "Check if there is at least one fingerprint: ${notOneFingerExistOnMobilePhone}")
    }

    private fun addLiveData() {
        viewModel.fingerPrintState.observe(viewLifecycleOwner, Observer { state ->
            if( state ) {
                detail_image.visibility = View.VISIBLE
            }
            else {
                detail_image.visibility = View.INVISIBLE
            }
        })
    }

}