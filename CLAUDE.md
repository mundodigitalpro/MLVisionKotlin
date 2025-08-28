# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MLVisionKotlin is an Android application that combines Google ML Kit text recognition with AI assistant capabilities. The app captures or selects images to extract text, then allows users to interact with an AI assistant about the scanned content. Features secure API key management and an intuitive chat interface with typewriter effects.

## Architecture

### Core Components

- **MainActivity.kt**: Main screen handling image capture/selection and text recognition using Google ML Kit
- **AssistantActivity.kt**: AI assistant interface with secure API key management, chat functionality, and Groq API integration
- **ViewBinding**: Used throughout for efficient view management
- **Navigation**: Uses Navigation Components with nav_graph.xml
- **SharedPreferences**: Secure local storage for API key management

### Key Technologies

- **Google ML Kit**: Text recognition from images (com.google.mlkit:text-recognition:16.0.0)
- **Groq API**: AI text analysis using llama-3.3-70b-versatile model
- **CameraX**: Camera functionality (androidx.camera:*)
- **OkHttp**: HTTP client for API communication (5.0.0-alpha.12)
- **Gson**: JSON parsing for API responses (2.9.1)
- **SharedPreferences**: Secure local storage for user configuration
- **AlertDialog**: User-friendly API key management interface
- **File Provider**: Secure file sharing for camera captured images

## Build Configuration

### Gradle Commands

```bash
# Build the project
./gradlew build

# Run debug build
./gradlew assembleDebug

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### SDK Configuration

- **compileSdk**: 34
- **minSdk**: 24
- **targetSdk**: 34
- **Kotlin**: 1.9.23
- **AGP**: 8.3.2

## Key Features

### Text Recognition Flow

1. User captures image via camera or selects from gallery
2. Image processed through Google ML Kit Text Recognition
3. Extracted text displayed in UI
4. Text can be shared or sent to AI assistant

### AI Assistant Integration

- Uses Groq API (llama-3.3-70b-versatile model) for text analysis
- Secure API key management with user-friendly dialogs
- Maintains conversation history
- Typewriter effect for response display with word-by-word animation
- Automatic initial query with scanned text
- Visible "⚙️ Gestionar API Key" button for easy access to API key management

### Security Features

- **No hardcoded API keys**: All API keys are stored securely using SharedPreferences
- **User-controlled configuration**: API keys are entered by users through secure dialogs
- **Local storage**: API keys are stored only on the user's device
- **Easy management**: Users can change, view, or delete their API keys anytime

## Development Notes

### Permissions

The app requires:
- `CAMERA`: For image capture functionality
- Camera permissions are handled with runtime permission requests

### File Management

- Uses FileProvider for secure camera image storage
- Temporary files created in `getExternalFilesDir(Environment.DIRECTORY_PICTURES)`
- Images stored with timestamp naming: `JPEG_yyyyMMdd_HHmmss_*.jpg`

### API Integration

The AssistantActivity implements secure API configuration:
- **API Key Storage**: Securely stored using SharedPreferences with key "groq_api_key"
- **Model**: "llama-3.3-70b-versatile" (upgraded from llama3-8b-8192)
- **Endpoint**: "https://api.groq.com/openai/v1/chat/completions"
- **User Management**: Interactive dialogs for API key configuration, modification, and deletion

### API Key Management Features

- **First-time setup**: Automatic prompt when no API key is configured
- **Secure input**: Password-masked input field for API key entry
- **Visual management button**: "⚙️ Gestionar API Key" button always visible in AssistantActivity
- **Flexible options**: Change existing key, delete stored key, or cancel operations
- **Validation**: Input validation with user feedback
- **Confirmation dialogs**: Safe deletion with confirmation prompts

✅ **Security Improvement**: API keys are no longer hardcoded and are managed securely by end users.