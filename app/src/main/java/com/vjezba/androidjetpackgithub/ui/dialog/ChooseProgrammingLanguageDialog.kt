package com.vjezba.androidjetpackgithub.ui.dialog

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.fragments.PaggingWithNetworkAndDbFragmentDirections
import kotlinx.android.synthetic.main.dialog_choose_programing_language.*
import kotlinx.coroutines.Job

class ChooseProgrammingLanguageDialog constructor( val automaticIncreaseNumberByOne: Job?) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dialog_choose_programing_language, container, false)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCanceledOnTouchOutside(false)

            val displayRectangle = Rect()
            val window = activity?.window
            window?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)

            view.minimumWidth = (displayRectangle.width() * 1.0f).toInt()
            view.minimumHeight = (displayRectangle.height() * 0.7f).toInt()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kotlin.isChecked = true

        next.setOnClickListener {
            automaticIncreaseNumberByOne?.cancel()
            val selectedLanguage = when {
                resources.getResourceEntryName(radioGroupLanguage.checkedRadioButtonId) == "kotlin" -> "Kotlin"
                resources.getResourceEntryName(radioGroupLanguage.checkedRadioButtonId) == "swift" -> "Swift"
                resources.getResourceEntryName(radioGroupLanguage.checkedRadioButtonId) == "java" -> "Java"
                else -> "PHP"
            }

            val direction =
                PaggingWithNetworkAndDbFragmentDirections.pagginWithNetworkAndDbFragmentToDetailsPaggingWithNetworkAndDbFragment(selectedLanguage)
            findNavController().navigate(direction)
            dismiss()
        }

        cancel.setOnClickListener {
            dismiss()
        }
    }

}