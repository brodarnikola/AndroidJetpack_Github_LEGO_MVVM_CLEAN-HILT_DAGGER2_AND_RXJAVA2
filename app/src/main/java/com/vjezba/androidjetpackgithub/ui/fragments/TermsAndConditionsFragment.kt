package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.activities.RegistrationActivity
import com.vjezba.androidjetpackgithub.viewmodels.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TermsAndConditionsFragment : Fragment() {

    //private val registrationViewModel : RegistrationViewModel by viewModel()

    private var registrationViewModel : RegistrationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false)

        registrationViewModel = (activity as RegistrationActivity).registrationViewModel

        view.findViewById<Button>(R.id.next).setOnClickListener {
            registrationViewModel!!.acceptTCs()
            (activity as RegistrationActivity).onTermsAndConditionsAccepted()
        }

        return view
    }

}