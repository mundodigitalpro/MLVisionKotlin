# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MLVisionKotlin is an Android application that combines Google ML Kit text recognition with AI assistant capabilities. The app captures or selects images to extract text, then allows users to interact with an AI assistant about the scanned content. Features secure API key management, an intuitive chat interface with typewriter effects, and a modern Material Design 3 interface optimized for user experience.

## Architecture

### Core Components

- **MainActivity.kt**: Main screen with modern Material Design 3 interface, handling image capture/selection and text recognition using Google ML Kit
- **AssistantActivity.kt**: AI assistant interface with secure API key management, chat functionality, and Groq API integration
- **ViewBinding**: Used throughout for efficient view management
- **Navigation**: Uses Navigation Components with nav_graph.xml
- **SharedPreferences**: Secure local storage for API key management
- **UI Components**: Custom drawables, modern color palette, and responsive layout system

### Key Technologies

- **Google ML Kit**: Text recognition from images (com.google.mlkit:text-recognition:16.0.0)
- **Groq API**: AI text analysis using llama-3.3-70b-versatile model
- **CameraX**: Camera functionality (androidx.camera:*)
- **OkHttp**: HTTP client for API communication (5.0.0-alpha.12)
- **Gson**: JSON parsing for API responses (2.9.1)
- **SharedPreferences**: Secure local storage for user configuration
- **AlertDialog**: User-friendly API key management interface
- **File Provider**: Secure file sharing for camera captured images
- **Material Design 3**: Modern design system with updated components and theming
- **CardView**: Elevated cards with shadows for visual organization and hierarchy

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

## UI/UX Design System

### Modern Material Design Implementation

The application implements a comprehensive Material Design 3 interface with the following characteristics:

#### Color System
- **Primary Colors**: Indigo (#6366F1) with darker variant (#4F46E5) and light variant (#818CF8)
- **Secondary Colors**: Emerald green (#10B981) with variants for different states
- **Accent Colors**: Orange/yellow (#F59E0B) for special actions and warnings
- **Background Colors**: Light grays (#F8FAFC, #F1F5F9) with white surfaces
- **Text Hierarchy**: Dark slate (#1E293B) for primary text, lighter variants for secondary content

#### Visual Components
- **Custom Drawables**: 5 specialized drawable files for buttons and backgrounds
  - `button_primary.xml`: Gradient buttons with shadow effects
  - `button_secondary.xml`: Alternative button styling
  - `button_accent.xml`: Special action button styling
  - `card_background.xml`: Elevated card backgrounds with shadows
  - `modern_imageview_background.xml`: Image container with placeholder state

#### Layout Structure
- **MainActivity**: LinearLayout-based design with card-based organization
  - Header section with app branding and description
  - Image card (220dp height) with placeholder overlay system
  - Expanded text result card (2x weight for maximum text visibility)
  - Two-row button layout organized by action priority
- **AssistantActivity**: Professional chat interface with header context
  - Elevated conversation card with chat header
  - Modern input field with rounded container
  - Visible API key management button

#### Design Principles
- **Spacing System**: Consistent 16dp-20dp margins with optimized vertical distribution
- **Elevation Hierarchy**: 4dp-8dp elevations for depth and visual organization
- **Border Radius**: 12dp-20dp rounded corners for modern appearance
- **Typography**: Structured text sizes from 14sp-28sp with proper line spacing

#### User Experience Enhancements
- **Visual Feedback**: Button press states and interactive elements
- **Empty States**: Placeholder content with helpful instructions
- **Text Selection**: Selectable text areas for copy functionality
- **Intuitive Icons**: Emoji-based button icons for immediate recognition
- **Optimized Flow**: Automatic overlay hiding when images are loaded