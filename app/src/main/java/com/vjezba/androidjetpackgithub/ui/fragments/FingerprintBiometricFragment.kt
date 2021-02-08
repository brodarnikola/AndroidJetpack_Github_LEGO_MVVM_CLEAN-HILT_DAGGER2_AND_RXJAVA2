package com.vjezba.androidjetpackgithub.ui.fragments

import android.content.Intent
import android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vjezba.androidjetpackgithub.databinding.FragmentFingerprintBinding
import com.vjezba.androidjetpackgithub.viewmodels.FingerprintViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_fingerprint.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        //biometricPrompt = createBiometricPrompt()

        activity?.speedDial?.visibility = View.VISIBLE


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == BIOMETRICS_REQUEST_CODE ) {
            Toast.makeText(requireContext(), "Juhu pokazat cemo slijedeci put sliku", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()

        val inttest5 = viewModel.checkIfFingerprinteIsEnabled()
        if( BIOMETRIC_ERROR_NONE_ENROLLED == inttest5 ) {
            Log.i("FingerprintAvailable", "Da li ce uci za fingerprint Check if fingerprint is availabe: ${inttest5}")
                            // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                    )
                }
                startActivityForResult(enrollIntent, BIOMETRICS_REQUEST_CODE)
        }

        biometricPrompt = viewModel.createBiometricPrompt()
        Log.i("FingerprintAvailable", "Check if fingerprint is availabe: ${inttest5}")

        btnFingerPrint.setOnClickListener {
            val promptInfo = viewModel.createPromptInfo()
            if (viewModel.getBiometricManager().canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
//                    showSnackbarSync(resources.getString(R.string.normal_login_not_fingerprint), true, rootElement)
//                    delay(2000)
//                    val intent = Intent(this@LoginActivity, WeatherActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }
            }
        }

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