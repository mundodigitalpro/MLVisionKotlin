# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**ML Vision Scanner** (`dev.josejordan.mlvisionscanner`) is a professional Android application that combines Google ML Kit optical character recognition (OCR) with AI assistant capabilities. The app captures or selects images to extract text, then allows users to interact with an AI assistant about the scanned content. Features secure API key management, an intuitive chat interface with typewriter effects, and a modern Material Design 3 interface optimized for Google Play Store publication.

**Current Version**: 4.0  
**Package Name**: `dev.josejordan.mlvisionscanner`  
**Target Audience**: Professional and personal document processing

## Architecture

### Core Components

- **MainActivity.kt**: Main screen with modern Material Design 3 interface, handling image capture/selection and text recognition using Google ML Kit
  - Location: `dev.josejordan.mlvisionscanner.MainActivity`
  - Features: Camera integration, gallery selection, OCR processing, modern UI
- **AssistantActivity.kt**: AI assistant interface with secure API key management, chat functionality, and Groq API integration
  - Location: `dev.josejordan.mlvisionscanner.AssistantActivity`  
  - Features: Groq API integration, secure key management, typewriter chat effects
- **ViewBinding**: Used throughout for efficient view management
- **Navigation**: Uses Navigation Components with nav_graph.xml
- **SharedPreferences**: Secure local storage for API key management
- **UI Components**: Custom drawables, modern color palette, responsive layout system, and personalized app icon
- **FileProvider**: Secure image sharing with authority `dev.josejordan.mlvisionscanner.provider`

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

- **compileSdk**: 36 (Android 14)
- **minSdk**: 24 (Android 7.0 Nougat)
- **targetSdk**: 36 (Android 14)
- **versionCode**: 4
- **versionName**: "4.0"
- **Kotlin**: 1.9.23
- **AGP**: 8.3.2
- **Google Play Ready**: ✅ Package name updated for store publication

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
- `INTERNET`: For AI assistant API communication  
- `android.hardware.camera.any`: Camera feature requirement
- Camera permissions are handled with runtime permission requests
- All permissions comply with Google Play Store policies

### File Management

- Uses FileProvider for secure camera image storage
- Authority: `dev.josejordan.mlvisionscanner.provider`
- Temporary files created in `getExternalFilesDir(Environment.DIRECTORY_PICTURES)`
- Images stored with timestamp naming: `JPEG_yyyyMMdd_HHmmss_*.jpg`
- Privacy compliant: Images not stored permanently

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
- **App Icon Design**: Custom vector-based launcher icon
  - `ic_launcher_foreground.xml`: Camera with OCR text lines and AI processing indicators
  - `ic_launcher_background.xml`: Gradient background matching app theme colors

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

### App Icon Design System

#### Icon Symbolism and Elements
The custom app icon represents the core functionality of ML Vision through carefully designed visual elements:

- **Camera Body**: White modern camera design representing image capture capability
- **Camera Lens**: Realistic multi-layered lens (light gray outer, dark gray inner) for professional appearance  
- **Viewfinder**: Small rectangular element indicating camera targeting functionality
- **OCR Text Lines**: Green lines (#10B981) on the left representing recognized text output
- **AI Processing Lines**: Lighter green lines (#34D399) on the right symbolizing intelligent analysis
- **ML Vision Brackets**: Corner focus indicators (L-shaped brackets) representing precision targeting

#### Technical Implementation
- **Format**: Vector XML drawables for perfect scalability across all Android densities
- **Structure**: Separate foreground and background for adaptive icon compatibility
- **Size**: Standard 108dp adaptive icon format supporting round and square launchers
- **Colors**: Consistent with app's Material Design 3 palette for brand coherence

#### Background Design
- **Primary Gradient**: Three-point gradient using app colors (#818CF8 → #6366F1 → #4F46E5)
- **Geometric Accents**: Subtle semi-transparent squares for visual texture
- **Corner Elements**: Green triangular accents matching secondary color theme
- **Professional Appearance**: Clean, modern design suitable for app store presentation

## Google Play Store Readiness

### Publication Status
✅ **Ready for Google Play Console**:
- **Package Name**: `dev.josejordan.mlvisionscanner` (unique and professional)
- **Version**: 4.0 (versionCode: 4)
- **Target API**: 36 (Android 14) - meets Google Play requirements
- **Minimum API**: 24 (Android 7.0) - broad device compatibility
- **Security**: No hardcoded secrets, secure API key management
- **Permissions**: Minimal and justified permissions only
- **Privacy**: User data handled securely, images not stored permanently

### Store Optimization
- **App Name**: "ML Vision Scanner"
- **Category**: Productivity / Business Tools
- **Target Keywords**: OCR, text recognition, document scanner, AI assistant
- **Description**: Professional OCR app with AI-powered text analysis
- **Icon**: Custom-designed adaptive icon optimized for all launcher types
- **Screenshots**: Material Design 3 interface, modern and professional

### Technical Compliance
- ✅ App Bundle compatible
- ✅ 64-bit architecture support
- ✅ ProGuard/R8 optimization ready
- ✅ No deprecated APIs
- ✅ Lint checks passed
- ✅ Security best practices implemented
- ✅ GDPR compliant (local data storage only)

## Development Workflow

### Branch Strategy
- **master**: Production-ready releases
- **develop**: Active development branch
- Always work on **develop** branch for new features

### Build Commands
```bash
# Debug build for testing
./gradlew assembleDebug

# Release build for store submission  
./gradlew assembleRelease

# Generate Android App Bundle (recommended for Play Store)
./gradlew bundleRelease

# Run all tests
./gradlew test connectedAndroidTest

# Code quality checks
./gradlew lint
```

### Release Checklist
- [ ] Update version code and name in build.gradle.kts
- [ ] Test on multiple devices and Android versions
- [ ] Verify all permissions work correctly
- [ ] Test API key management flow
- [ ] Check app icon and screenshots
- [ ] Run lint and fix any issues
- [ ] Generate signed release build
- [ ] Test release APK thoroughly