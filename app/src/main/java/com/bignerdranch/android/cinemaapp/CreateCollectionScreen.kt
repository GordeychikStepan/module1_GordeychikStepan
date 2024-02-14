package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

// окно для создания новой коллекции
class CreateCollectionScreen : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_collection_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val icons = listOf(
            R.drawable.ico1, R.drawable.ico2, R.drawable.ico3, R.drawable.ico4,
            R.drawable.ico5, R.drawable.ico6, R.drawable.ico7, R.drawable.ico8,
            R.drawable.ico9, R.drawable.ico10, R.drawable.ico11, R.drawable.ico12,
            R.drawable.ico13, R.drawable.ico14, R.drawable.ico15, R.drawable.ico16,
            R.drawable.ico17, R.drawable.ico18, R.drawable.ico19, R.drawable.ico20,
            R.drawable.ico21, R.drawable.ico22, R.drawable.ico23, R.drawable.ico24,
            R.drawable.ico25, R.drawable.ico26, R.drawable.ico27, R.drawable.ico28,
            R.drawable.ico29, R.drawable.ico30, R.drawable.ico31, R.drawable.ico32,
            R.drawable.ico33, R.drawable.ico34, R.drawable.ico35, R.drawable.ico36
        )

        val randomIcon = icons.random()

        val selectedIconImageView = findViewById<ImageView>(R.id.selectedIconImageView)
        selectedIconImageView.setImageResource(randomIcon)
        selectedIconImageView.tag = randomIcon

        val backButton = findViewById<ImageView>(R.id.backImage)
        backButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finishWithAnimation()
        }

        val selectIconButton = findViewById<Button>(R.id.selectIconButton)
        selectIconButton.setOnClickListener {
            val intent = Intent(this, IconSelectionScreen::class.java)
            startActivityForResult(intent, REQUEST_SELECT_ICON)
        }

        val createButton = findViewById<Button>(R.id.createButton)
        createButton.setOnClickListener {
            val collectionName = findViewById<EditText>(R.id.nameEditText).text.toString()
            val selectedIcon = findViewById<ImageView>(R.id.selectedIconImageView).tag as? Int
            if (collectionName.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите название коллекции", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                if (selectedIcon != null) {
                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_COLLECTION_NAME, collectionName)
                    resultIntent.putExtra(EXTRA_SELECTED_ICON, selectedIcon)
                    setResult(RESULT_OK, resultIntent)
                }
                else {
                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_COLLECTION_NAME, collectionName)
                    resultIntent.putExtra(EXTRA_SELECTED_ICON, R.drawable.ico1)
                    setResult(RESULT_OK, resultIntent)
                }
            }
            finish()
        }
    }

    private fun finishWithAnimation() {
        finish()
        overridePendingTransition(R.anim.slide_out_right, R.anim.fade_out)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_ICON && resultCode == RESULT_OK) {
            val selectedIcon = data?.getIntExtra(IconSelectionScreen.EXTRA_SELECTED_ICON, R.drawable.ico1)
            if (selectedIcon != null) {
                val selectedIconImageView = findViewById<ImageView>(R.id.selectedIconImageView)
                selectedIconImageView.setImageResource(selectedIcon)
                selectedIconImageView.tag = selectedIcon
            }
        }
    }

    companion object {
        const val EXTRA_COLLECTION_NAME = "collection_name"
        const val EXTRA_SELECTED_ICON = "selected_icon"
        private const val REQUEST_SELECT_ICON = 1
    }
}