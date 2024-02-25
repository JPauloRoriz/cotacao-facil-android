package com.example.cotacaofacil.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.DialogCameraGalleryBinding

class CameraGalleryDialog : DialogFragment() {
    private lateinit var binding: DialogCameraGalleryBinding
    var optionPhoto: ((OptionPhoto) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedCornersThemeDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCameraGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewCamera.setOnClickListener {
            optionPhoto?.invoke(OptionPhoto.CAMERA)
            dismiss()
        }

        binding.imageViewGallery.setOnClickListener {
            optionPhoto?.invoke(OptionPhoto.GALLERY)
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}

enum class OptionPhoto {
    CAMERA,
    GALLERY
}
