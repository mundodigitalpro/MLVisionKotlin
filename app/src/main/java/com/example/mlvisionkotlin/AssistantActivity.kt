package com.example.mlvisionkotlin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
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
    private val groqApiKey = "groqapikey"
    private val model = "llama3-8b-8192"
    private val url = "https://api.groq.com/openai/v1/chat/completions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)

        // Recuperar texto escaneado de la `MainActivity`
        val scannedText = intent.getStringExtra("scannedText") ?: ""

        conversationTextView = findViewById(R.id.conversationTextView)
        userInputEditText = findViewById(R.id.userInputEditText)
        scrollView = findViewById(R.id.scrollView)
        val sendButton: Button = findViewById(R.id.sendButton)

        // Mostrar texto escaneado
        updateConversation("Scanned Text", scannedText)

        // Si hay texto escaneado, inicia una consulta a la API automáticamente
        if (scannedText.isNotEmpty()) {
            queryGroqApi(scannedText)
        }

        // Listener del botón "Send"
        sendButton.setOnClickListener {
            val userInput = userInputEditText.text.toString().trim()
            if (userInput.isNotEmpty()) {
                updateConversation("You", userInput)
                queryGroqApi(userInput)
                userInputEditText.text.clear()
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
}
