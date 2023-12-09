package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileOutputStream
import java.io.IOException
import android.Manifest

class NavigationProfileScreenFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.sharedPrefFile"
    private lateinit var selectedIconImageView: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var apiService: ApiService

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile_screen, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/focus.lvlup2021/LEVEL_UP_MOBILE/1.0.0/") // Измените на ваш базовый URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        sharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        selectedIconImageView = view.findViewById(R.id.selectedIconImageView)
        val exitButton = view.findViewById<Button>(R.id.exitButton)
        val discussionsLinearLayout = view.findViewById<LinearLayout>(R.id.discussionsLinearLayout)

        accessToken?.let {
            apiService.getUserProfile(it).enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val username = response.body()?.user?.username
                        val location = response.body()?.user?.location

                        // Обновление SharedPreferences и UI
                        val editor = sharedPreferences.edit()
                        editor.putString("name", username)
                        editor.apply()

                        nameTextView.text = username
                        emailTextView.text = location
                    } else {
                        handleApiError(response.code())
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    handleApiFailure(t)
                }
            })
        }

        val imagePath = sharedPreferences.getString("imagePath", null)
        imagePath?.let {
            selectedImageUri = Uri.parse(it)
            selectedIconImageView.setImageURI(selectedImageUri)
        }

        view.findViewById<TextView>(R.id.changeTextView).setOnClickListener {
            showImagePickerOptions()
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

    private fun showImagePickerOptions() {
        val pictureDialog = context?.let { AlertDialog.Builder(it) }
        pictureDialog?.setTitle("Выберите действие")
        val pictureDialogItems = arrayOf("Выбрать фото из галереи", "Сделать снимок камерой")
        pictureDialog?.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> openGallery()
                1 -> openCamera()
            }
        }
        pictureDialog?.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun openCamera() {
        if (hasCameraPermission()) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 2)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> { // Галерея
                    val selectedImageUri: Uri? = data?.data
                    selectedIconImageView.setImageURI(selectedImageUri)
                    selectedImageUri?.let { uri -> saveImageUri(uri) }
                }
                2 -> { // Камера
                    val photo = data?.extras?.get("data") as Bitmap
                    val photoUri = saveImageFromCamera(photo)
                    selectedIconImageView.setImageURI(photoUri)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    showToast("Требуется разрешение камеры")
                }
                return
            }
        }
    }


    private fun saveImageUri(uri: Uri) {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        val tempFile = createTempFile("tempImage", null, requireContext().cacheDir)
        tempFile.deleteOnExit()

        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                val buffer = ByteArray(4 * 1024)
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        val editor = sharedPreferences.edit()
        editor.putString("imagePath", tempFile.absolutePath)
        editor.apply()

        Glide.with(this)
            .load(tempFile)
            .into(selectedIconImageView)
    }

    private fun saveImageFromCamera(photo: Bitmap): Uri? {
        val tempFile = createTempFile("cameraImage", ".jpg", requireContext().cacheDir)
        tempFile.deleteOnExit()

        try {
            FileOutputStream(tempFile).use { out ->
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        val editor = sharedPreferences.edit()
        editor.putString("imagePath", tempFile.absolutePath)
        editor.apply()

        return Uri.fromFile(tempFile)
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }



    private fun handleApiFailure(t: Throwable) {
        showToast("Ошибка соединения: ${t.localizedMessage}")
    }

    private fun handleApiError(errorCode: Int) {
        val message = when (errorCode) {
            404 -> "Профиль не найден"
            else -> "Ошибка запроса. Код: $errorCode"
        }
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 100
    }
}