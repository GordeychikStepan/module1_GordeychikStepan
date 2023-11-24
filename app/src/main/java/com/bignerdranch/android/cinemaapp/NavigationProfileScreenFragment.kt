package com.bignerdranch.android.cinemaapp

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.FileOutputStream

class NavigationProfileScreenFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.sharedPrefFile"
    private lateinit var selectedIconImageView: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_profile_screen, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "")
        val name = sharedPreferences.getString("name", "")
        val surname = sharedPreferences.getString("surname", "")

        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        selectedIconImageView = view.findViewById(R.id.selectedIconImageView)
        val exitButton = view.findViewById<Button>(R.id.exitButton)
        val discussionsLinearLayout = view.findViewById<LinearLayout>(R.id.discussionsLinearLayout)

        emailTextView.text = email
        nameTextView.text = "$name $surname"

        // Проверяем наличие сохранённого пути к изображению в SharedPreferences
        val imagePath = sharedPreferences.getString("imagePath", null)
        if (!imagePath.isNullOrEmpty()) {
            selectedImageUri = Uri.parse(imagePath)
            selectedIconImageView.setImageURI(selectedImageUri)
        }

        view.findViewById<TextView>(R.id.changeTextView).setOnClickListener {
            openGallery()
        }

        exitButton.setOnClickListener {
            val intent = Intent(requireContext(), SignInScreen::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        discussionsLinearLayout.setOnClickListener {
            val intent = Intent(requireContext(), ChatListScreen::class.java)

            val options = ActivityOptions.makeCustomAnimation(
                requireContext(),
                R.anim.slide_in_right,
                R.anim.fade_out
            )

            startActivity(intent, options.toBundle())
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

            // Используйте content resolver для открытия входного потока
            selectedImageUri?.let { uri ->
                val contentResolver = requireContext().contentResolver
                val inputStream = contentResolver.openInputStream(uri)

                // Создайте временный файл
                val tempFile = createTempFile("tempImage", null, requireContext().cacheDir)
                tempFile.deleteOnExit()

                // Используйте буфер для копирования контента во временный файл
                inputStream?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        val buffer = ByteArray(4 * 1024) // размер буфера
                        while (true) {
                            val byteCount = input.read(buffer)
                            if (byteCount < 0) break
                            output.write(buffer, 0, byteCount)
                        }
                        output.flush()
                    }
                }

                // Сохраните путь временного файла в SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("imagePath", tempFile.absolutePath)
                editor.apply()
            }
        }
    }

}