package com.bignerdranch.android.cinemaapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class NavigationProfileScreenFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.sharedPrefFile"
    private lateinit var selectedIconImageView: ImageView

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_profile_screen, container, false)

        // инициализация SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "")
        val name = sharedPreferences.getString("name", "")
        val surname = sharedPreferences.getString("surname", "")

        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        selectedIconImageView = view.findViewById(R.id.selectedIconImageView)

        emailTextView.text = email
        nameTextView.text = "$name $surname"

        view.findViewById<TextView>(R.id.changeTextView).setOnClickListener {
            openGallery()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedIconImageView.setImageURI(selectedImageUri)
        }
    }


}