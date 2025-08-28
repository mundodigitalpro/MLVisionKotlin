package com.example.mlvisionkotlin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.concurrent.thread

class AssistantActivity : AppCompatActivity() {
    private lateinit var conversationTextView: TextView
    private lateinit var userInputEditText: EditText
    private lateinit var scrollView: ScrollView
    private val client = OkHttpClient()
    private val gson = Gson()
    private val conversation = mutableListOf<Map<String, String>>()

    // Variables para la API
    private var groqApiKey: String = ""
    private val model = "llama-3.3-70b-versatile"
    private val url = "https://api.groq.com/openai/v1/chat/completions"
    private lateinit var sharedPreferences: SharedPreferences
    private val API_KEY_PREF = "groq_api_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MLVisionPrefs", Context.MODE_PRIVATE)

        // Recuperar texto escaneado de la `MainActivity`
        val scannedText = intent.getStringExtra("scannedText") ?: ""

        conversationTextView = findViewById(R.id.conversationTextView)
        userInputEditText = findViewById(R.id.userInputEditText)
        scrollView = findViewById(R.id.scrollView)
        val sendButton: Button = findViewById(R.id.sendButton)
        val manageApiKeyButton: Button = findViewById(R.id.manageApiKeyButton)

        // Mostrar texto escaneado
        updateConversation("Scanned Text", scannedText)

        // Verificar y cargar API key
        loadApiKey { apiKeyLoaded ->
            if (apiKeyLoaded && scannedText.isNotEmpty()) {
                // Si hay API key y texto escaneado, inicia consulta automática
                queryGroqApi(scannedText)
            }
        }

        // Listener del botón "Gestionar API Key"
        manageApiKeyButton.setOnClickListener {
            showApiKeyManagementDialog()
        }

        // Listener del botón "Send"
        sendButton.setOnClickListener {
            val userInput = userInputEditText.text.toString().trim()
            if (userInput.isNotEmpty()) {
                loadApiKey { apiKeyLoaded ->
                    if (apiKeyLoaded) {
                        updateConversation("You", userInput)
                        queryGroqApi(userInput)
                        userInputEditText.text.clear()
                    }
                }
            }
        }
    }



    // Función para actualizar la conversación
/*    private fun updateConversation(sender: String, message: String) {
        val formattedMessage = "$sender: $message\n"
        conversationTextView.append(formattedMessage)

        // Hacer scroll hasta el final
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }*/

    private fun updateConversation(sender: String, message: String) {
        val formattedMessage = "$sender: $message\n"
        displayMessageTypewriterEffect(formattedMessage)

        // Hacer scroll hasta el final
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private fun displayMessageTypewriterEffect(text: String) {
        val words = text.split(" ") // Divide el texto en palabras
        var currentIndex = 0
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                if (currentIndex < words.size) {
                    conversationTextView.append(words[currentIndex] + " ")
                    currentIndex++
                    scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) } // Asegura que el ScrollView se desplace automáticamente
                    handler.postDelayed(this, 20) // Ajusta el tiempo para cada palabra
                }
            }
        }
        handler.post(runnable)
    }



    // Función para consultar la API de Groq
    private fun queryGroqApi(text: String) {
        conversation.add(mapOf("role" to "user", "content" to text))

        val requestBodyJson = gson.toJson(mapOf("model" to model, "messages" to conversation))
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer $groqApiKey")
            .post(requestBody)
            .build()

        thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val jsonResponse = JsonParser.parseString(it).asJsonObject
                        val assistantResponse = jsonResponse["choices"].asJsonArray[0].asJsonObject["message"].asJsonObject["content"].asString

                        conversation.add(mapOf("role" to "assistant", "content" to assistantResponse))
                        runOnUiThread { updateConversation("Assistant", assistantResponse) }
                    }
                } else {
                    runOnUiThread { updateConversation("Error", "Request failed with code ${response.code}") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { updateConversation("Error", "API request failed: ${e.message}") }
            }
        }
    }

    // Función para cargar la API key
    private fun loadApiKey(callback: (Boolean) -> Unit) {
        groqApiKey = sharedPreferences.getString(API_KEY_PREF, "") ?: ""
        
        if (groqApiKey.isEmpty()) {
            showApiKeyDialog(callback)
        } else {
            callback(true)
        }
    }

    // Función para mostrar el diálogo de entrada de API key
    private fun showApiKeyDialog(callback: (Boolean) -> Unit) {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.hint = "Ingresa tu API key de Groq"

        AlertDialog.Builder(this)
            .setTitle("Configurar API Key")
            .setMessage("Para usar el asistente AI, necesitas ingresar tu API key de Groq.\n\nPuedes obtenerla en: https://console.groq.com/keys")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val apiKey = input.text.toString().trim()
                if (apiKey.isNotEmpty()) {
                    saveApiKey(apiKey)
                    groqApiKey = apiKey
                    updateConversation("System", "API key configurada correctamente")
                    callback(true)
                } else {
                    updateConversation("Error", "API key no puede estar vacía")
                    callback(false)
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
                updateConversation("System", "Operación cancelada. El asistente AI no estará disponible.")
                callback(false)
            }
            .setNeutralButton("Eliminar API guardada") { _, _ ->
                removeApiKey()
                updateConversation("System", "API key eliminada del dispositivo")
                callback(false)
            }
            .setCancelable(false)
            .show()
    }

    // Función para guardar la API key de forma segura
    private fun saveApiKey(apiKey: String) {
        with(sharedPreferences.edit()) {
            putString(API_KEY_PREF, apiKey)
            apply()
        }
    }

    // Función para eliminar la API key guardada
    private fun removeApiKey() {
        with(sharedPreferences.edit()) {
            remove(API_KEY_PREF)
            apply()
        }
        groqApiKey = ""
    }


    // Función para mostrar el diálogo de gestión de API key
    private fun showApiKeyManagementDialog() {
        val hasApiKey = sharedPreferences.getString(API_KEY_PREF, "")?.isNotEmpty() == true
        
        val message = if (hasApiKey) {
            "API Key actual: ${groqApiKey.take(8)}...\n\n¿Qué deseas hacer?"
        } else {
            "No hay API Key configurada.\n\n¿Qué deseas hacer?"
        }

        val builder = AlertDialog.Builder(this)
            .setTitle("Gestionar API Key")
            .setMessage(message)

        if (hasApiKey) {
            builder.setPositiveButton("Cambiar API Key") { _, _ ->
                showApiKeyInputDialog()
            }
            .setNegativeButton("Eliminar API Key") { _, _ ->
                confirmRemoveApiKey()
            }
            .setNeutralButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        } else {
            builder.setPositiveButton("Configurar API Key") { _, _ ->
                showApiKeyInputDialog()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        }

        builder.show()
    }

    // Función para confirmar eliminación de API key
    private fun confirmRemoveApiKey() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar la API Key guardada?")
            .setPositiveButton("Eliminar") { _, _ ->
                removeApiKey()
                updateConversation("System", "API Key eliminada correctamente")
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Función para mostrar solo el input de API key (sin el botón de eliminar)
    private fun showApiKeyInputDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.hint = "Ingresa tu API key de Groq"
        
        // Si ya hay una API key, mostrarla parcialmente
        if (groqApiKey.isNotEmpty()) {
            input.setText(groqApiKey)
        }

        AlertDialog.Builder(this)
            .setTitle("Configurar API Key")
            .setMessage("Ingresa tu API key de Groq.\n\nPuedes obtenerla en: https://console.groq.com/keys")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val apiKey = input.text.toString().trim()
                if (apiKey.isNotEmpty()) {
                    saveApiKey(apiKey)
                    groqApiKey = apiKey
                    updateConversation("System", "API key configurada correctamente")
                } else {
                    updateConversation("Error", "API key no puede estar vacía")
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
