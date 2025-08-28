# ML Vision Kotlin App

## Descripción

Esta aplicación es una herramienta simple y poderosa que permite capturar o seleccionar imágenes para reconocer texto utilizando la tecnología de reconocimiento de texto de Google ML Kit. Desarrollada en Kotlin para la plataforma Android, proporciona funcionalidades fáciles de usar para capturar texto desde cualquier imagen, analizarlo con IA y compartirlo con otras aplicaciones.

### Funcionalidades

- **Captura de imágenes con la cámara**: Permite tomar fotografías con la cámara del dispositivo y realizar el reconocimiento de texto sobre ellas.
- **Selección de imágenes desde el almacenamiento**: Permite seleccionar imágenes desde la galería para extraer el texto presente en ellas.
- **Reconocimiento de texto**: Utiliza Google ML Kit para identificar y extraer texto desde las imágenes capturadas o seleccionadas.
- **Asistente IA**: Integración con Groq API (llama-3.3-70b-versatile) para analizar y procesar el texto extraído con inteligencia artificial.
- **Gestión segura de API keys**: Sistema de configuración y almacenamiento seguro de claves API en el dispositivo.
- **Chat interactivo**: Interfaz de conversación con efecto de escritura automática (typewriter) para interacción natural con el asistente.
- **Compartir texto**: Facilita compartir el texto reconocido con otras aplicaciones a través de una sencilla opción de compartir.

### Tecnologías

- **Kotlin**: Lenguaje de programación utilizado para la aplicación.
- **Google ML Kit**: Kit de herramientas de aprendizaje automático para reconocimiento de texto.
- **Groq API**: Servicio de IA para procesamiento de texto con modelo llama-3.3-70b-versatile.
- **OkHttp**: Cliente HTTP para comunicación con APIs (v5.0.0-alpha.12).
- **Gson**: Biblioteca para parsing de JSON (v2.9.1).
- **ViewBinding**: Para una gestión más eficiente de las vistas y enlaces con la UI.
- **CameraX**: Framework moderno para funcionalidades de cámara.
- **SharedPreferences**: Almacenamiento seguro local para configuración del usuario.
- **Navigation Components**: Sistema de navegación entre activities.

### Requisitos

- Dispositivo Android con cámara y permisos de almacenamiento.
- Android SDK 24 (Nougat) o superior.
- Conexión a internet para el funcionamiento del asistente IA.
- API key de Groq (gratuita) para usar el asistente - obtener en: https://console.groq.com/keys

### Uso

#### Reconocimiento de Texto
1. **Permisos**: Asegúrate de que la aplicación tenga permisos para acceder a la cámara.
2. **Captura/Selección**:
    - Captura una imagen utilizando el botón de captura de imagen.
    - O selecciona una imagen de la galería usando el botón de selección.
3. **Reconocer texto**: La aplicación iniciará automáticamente el proceso de reconocimiento de texto.
4. **Compartir texto**: Comparte el texto reconocido con otras aplicaciones.

#### Asistente IA
1. **Configurar API Key**: La primera vez que uses el asistente, se te pedirá configurar tu API key de Groq.
2. **Interacción**: El asistente analizará automáticamente el texto extraído de las imágenes.
3. **Chat**: Puedes hacer preguntas adicionales o solicitar análisis específicos del texto.
4. **Gestión de API Key**: Usa el botón "⚙️ Gestionar API Key" para cambiar o eliminar tu clave API.

### Configuración de Desarrollo

```bash
# Clonar el repositorio
git clone <repository-url>

# Construir el proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Generar APK debug
./gradlew assembleDebug
```

### Observaciones

- La precisión del reconocimiento de texto puede variar según la calidad de la imagen y la claridad del texto.
- La aplicación no guarda imágenes de forma permanente, se almacenan temporalmente mientras se realiza el reconocimiento.
- Las API keys se almacenan de forma segura en el dispositivo usando SharedPreferences.
- El asistente IA requiere conexión a internet para funcionar correctamente.
