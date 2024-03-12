package com.example.mlvisionkotlin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mlvisionkotlin.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeImagePickerLauncher()
        setupReadTextButtonListener()
    }

    private fun initializeImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleImagePickerResult(uri)
        }
    }

    private fun setupReadTextButtonListener() {
        binding.readTextButtonId.setOnClickListener {
            launchImagePicker()
        }
    }

    private fun launchImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    private fun handleImagePickerResult(uri: Uri?) {
        if (uri == null) {
            showToast("Image selection failed. Please try again.")
            return
        }

        imageUri = uri
        binding.imageViewId.setImageURI(uri)
        recognizeTextFromImage(uri)
    }

    private fun recognizeTextFromImage(uri: Uri) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromFilePath(this, uri)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                displayRecognizedText(visionText.text)
            }
            .addOnFailureListener { e ->
                handleTextRecognitionError(e)
            }
    }

    private fun displayRecognizedText(text: String) {
        if (text.isBlank()) {
            showToast("No text found in image.")
        } else {
            binding.resultText.text = text
        }
    }

    private fun handleTextRecognitionError(e: Exception) {
        showToast("Text recognition failed: ${e.localizedMessage}")
        e.printStackTrace()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
