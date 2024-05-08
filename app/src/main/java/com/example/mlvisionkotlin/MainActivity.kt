package com.example.mlvisionkotlin

// Importaciones necesarias para la funcionalidad de la app
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

    private lateinit var binding: ActivityMainBinding // Referencia para el binding de la vista
    private var imageUri: Uri? = null // URI de la imagen capturada o seleccionada
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String> // Lanzador para la selección de imágenes
    private lateinit var cameraCaptureLauncher: ActivityResultLauncher<Uri> // Lanzador para la captura de fotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Infla el layout con ViewBinding
        setContentView(binding.root) // Establece el contenido de la vista
        initializeImagePickerLauncher() // Inicializa el lanzador de selección de imágenes
        initializeCameraCaptureLauncher() // Inicializa el lanzador de captura de cámara
        setupReadTextButtonListener() // Configura el listener del botón para leer texto
        setupCaptureImageButtonListener() // Configura el listener del botón para capturar imágenes
        setupShareButtonListener() //Configura el listener del botón para compartir texto

        binding.iaButtonId.setOnClickListener {
            val scannedText = binding.resultText.text.toString()
            val intent = Intent(this, AssistantActivity::class.java)
            intent.putExtra("scannedText", scannedText)
            startActivity(intent)
        }


    }

    // Inicializa el lanzador para seleccionar imágenes desde el almacenamiento del dispositivo
    private fun initializeImagePickerLauncher() {
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                handleImagePickerResult(uri)
            }
    }

    // Inicializa el lanzador para capturar imágenes con la cámara
    private fun initializeCameraCaptureLauncher() {
        cameraCaptureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    imageUri?.let { uri ->
                        binding.imageViewId.setImageURI(uri) // Muestra la imagen capturada
                        recognizeTextFromImage(uri) // Inicia el reconocimiento de texto en la imagen
                    }
                } else {
                    showToast("Image capture failed. Please try again.") // Muestra error si la captura falla

                }
            }
    }

    // Configura el listener para el botón de lectura de texto
    private fun setupReadTextButtonListener() {
        binding.readTextButtonId.setOnClickListener {
            launchImagePicker() // Lanza el selector de imágenes
        }
    }

    // Configura el listener para el botón de captura de imagen
    private fun setupCaptureImageButtonListener() {
        binding.captureImageButtonId.setOnClickListener {
            checkCameraPermissionAndLaunchCamera() // Verifica permisos y lanza la cámara
        }
    }

    // Configura el listener para el boton de compartir texto
    private fun setupShareButtonListener() {
        binding.shareButtonId.setOnClickListener {
            launchShare()
        }
    }

    //Lanza la funcion compartir texto
    private fun launchShare() {
        val textToShare = binding.resultText.text.toString()
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "Text to Share")
        }
        val shareIntent = Intent.createChooser(intent, "Share Text")
        startActivity(shareIntent)
    }

    // Verifica el permiso de cámara y, si está concedido, lanza la cámara; de lo contrario, solicita el permiso
    private fun checkCameraPermissionAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            launchCamera() // Lanza la cámara si el permiso ya está concedido
        }
    }

    // Maneja el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera() // Lanza la cámara si el permiso es concedido
            } else {
                showToast("Camera permission is required to use this feature.") // Muestra error si el permiso es denegado
            }
        }
    }

    // Lanza el selector de imágenes
    private fun launchImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    // Crea una intención para capturar una imagen y lanza la cámara
    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)?.also {
            val photoFile: File? = try {
                createImageFile() // Crea un archivo para guardar la imagen
            } catch (ex: IOException) {
                null // Maneja la excepción si la creación del archivo falla
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.provider",
                    it
                )
                imageUri = photoURI
                cameraCaptureLauncher.launch(photoURI) // Lanza la cámara con el URI del archivo
            }
        }
    }

    // Crea un archivo de imagen en el almacenamiento externo privado
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imageUri = Uri.fromFile(this) // Guarda el URI del archivo creado
        }
    }

    // Maneja el resultado de la selección de imágenes
    private fun handleImagePickerResult(uri: Uri?) {
        uri ?: return showToast("Image selection failed. Please try again.")
        imageUri = uri
        binding.imageViewId.setImageURI(uri) // Muestra la imagen seleccionada
        recognizeTextFromImage(uri) // Inicia el reconocimiento de texto en la imagen seleccionada
    }

    // Inicia el proceso de reconocimiento de texto en la imagen
    private fun recognizeTextFromImage(uri: Uri) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromFilePath(this, uri)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                displayRecognizedText(visionText.text) // Muestra el texto reconocido
            }
            .addOnFailureListener { e ->
                handleTextRecognitionError(e) // Maneja el error en el reconocimiento de texto
            }
    }

    // Muestra el texto reconocido en la UI
    private fun displayRecognizedText(text: String) {
        if (text.isBlank()) {
            showToast("No text found in image.") // Notifica al usuario si no se encontró texto
        } else {
            binding.resultText.text = text // Muestra el texto reconocido
        }
    }

    // Maneja errores en el reconocimiento de texto
    private fun handleTextRecognitionError(e: Exception) {
        showToast("Text recognition failed: ${e.localizedMessage}") // Muestra mensaje de error
        e.printStackTrace()
    }

    // Muestra un mensaje Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE =
            101 // Código de solicitud de permiso de cámara
    }
}
