package com.example.mlvisionkotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mlvisionkotlin.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var cameraCaptureLauncher: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeImagePickerLauncher()
        initializeCameraCaptureLauncher()
        setupReadTextButtonListener()
        setupCaptureImageButtonListener()
    }

    private fun initializeImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleImagePickerResult(uri)
        }
    }

    private fun initializeCameraCaptureLauncher() {
        cameraCaptureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    binding.imageViewId.setImageURI(uri)
                    recognizeTextFromImage(uri)
                }
            } else {
                showToast("Image capture failed. Please try again.")
            }
        }
    }

    private fun setupReadTextButtonListener() {
        binding.readTextButtonId.setOnClickListener {
            launchImagePicker()
        }
    }

    private fun setupCaptureImageButtonListener() {
        binding.captureImageButtonId.setOnClickListener {
            checkCameraPermissionAndLaunchCamera()
        }
    }

    private fun checkCameraPermissionAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            launchCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                showToast("Camera permission is required to use this feature.")
            }
        }
    }

    private fun launchImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)?.also {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.provider",
                    it
                )
                imageUri = photoURI
                cameraCaptureLauncher.launch(photoURI)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imageUri = Uri.fromFile(this)
        }
    }

    private fun handleImagePickerResult(uri: Uri?) {
        uri ?: return showToast("Image selection failed. Please try again.")

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

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}
