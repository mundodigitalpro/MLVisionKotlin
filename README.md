# ML Vision Scanner

## Descripción

**ML Vision Scanner** es una aplicación Android profesional que combina el reconocimiento óptico de caracteres (OCR) con inteligencia artificial. Utilizando Google ML Kit para el reconocimiento de texto y Groq API para análisis inteligente, esta herramienta permite capturar, procesar y analizar texto desde imágenes de manera eficiente.

**Package Name**: `dev.josejordan.mlvisionscanner`  
**Versión**: 4.0  
**SDK Mínimo**: Android 7.0 (API 24)  
**SDK Objetivo**: Android 14 (API 36)

### Funcionalidades

- **Captura de imágenes con la cámara**: Permite tomar fotografías con la cámara del dispositivo y realizar el reconocimiento de texto sobre ellas.
- **Selección de imágenes desde el almacenamiento**: Permite seleccionar imágenes desde la galería para extraer el texto presente en ellas.
- **Reconocimiento de texto**: Utiliza Google ML Kit para identificar y extraer texto desde las imágenes capturadas o seleccionadas.
- **Asistente IA**: Integración con Groq API (llama-3.3-70b-versatile) para analizar y procesar el texto extraído con inteligencia artificial.
- **Gestión segura de API keys**: Sistema de configuración y almacenamiento seguro de claves API en el dispositivo.
- **Chat interactivo**: Interfaz de conversación con efecto de escritura automática (typewriter) para interacción natural con el asistente.
- **Interfaz moderna**: Diseño Material Design 3 con paleta de colores moderna, gradientes y elevaciones.
- **Experiencia optimizada**: Área de texto expandida, botones con iconos intuitivos y navegación fluida.
- **Icono personalizado**: Icono de app diseñado específicamente para ML Vision con elementos de cámara, texto OCR y procesamiento IA.
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
- **Material Design 3**: Framework moderno de diseño con componentes actualizados.
- **CardView**: Componentes de tarjeta con elevación y sombras para mejor organización visual.

### Requisitos del Sistema

- **Android**: 7.0 (API 24) o superior
- **Arquitectura**: ARM64, ARM32, x86, x86_64
- **Permisos**: Cámara, Internet
- **Almacenamiento**: ~50 MB
- **Conexión**: Internet requerida para funcionalidad IA
- **API Key**: Groq API (gratuita) - [Obtener aquí](https://console.groq.com/keys)

### Compatibilidad con Google Play Store

✅ **Listo para publicación**:
- Package name único y profesional: `dev.josejordan.mlvisionscanner`
- Cumple con las políticas de Google Play
- No contiene API keys hardcodeadas
- Gestión segura de datos del usuario
- Compatible con Android App Bundle

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

### Diseño y Experiencia de Usuario

#### Características de Diseño Moderno
- **Paleta de colores**: Índigo vibrante (#6366F1) como color primario, verde esmeralda (#10B981) como secundario
- **Gradientes y sombras**: Botones con efectos de profundidad y estados de presión interactivos  
- **Material Design 3**: Componentes actualizados con bordes redondeados (12dp-20dp) y elevaciones progresivas
- **Iconos intuitivos**: Emojis integrados en botones (📁 Upload, 📸 Capture, 📤 Share, 🤖 AI)
- **Icono de aplicación**: Diseño vectorial personalizado con cámara, líneas de texto OCR y elementos de IA

#### Optimizaciones de Interfaz
- **Área de texto expandida**: Espacio maximizado para visualización cómoda del texto reconocido
- **Cards con elevación**: Organización visual clara con sombras suaves y fondos diferenciados
- **Layout responsivo**: Diseño adaptado para diferentes tamaños de pantalla
- **Estados visuales**: Placeholder animado cuando no hay imagen cargada

#### Mejoras de Usabilidad
- **Texto seleccionable**: Capacidad de copiar texto reconocido directamente
- **Navegación fluida**: Transiciones suaves entre pantallas principales y asistente
- **Feedback visual**: Estados de botones y confirmaciones de acciones
- **Gestión intuitiva**: Acceso fácil a configuración de API key sin menús ocultos

### Configuración de Desarrollo

```bash
# Clonar el repositorio
git clone https://github.com/mundodigitalpro/MLVisionKotlin.git

# Cambiar al directorio del proyecto
cd MLVisionKotlin

# Cambiar a rama de desarrollo
git checkout develop

# Construir el proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Generar APK debug
./gradlew assembleDebug

# Generar APK release
./gradlew assembleRelease
```

### Estructura del Proyecto

```
app/src/main/java/dev/josejordan/mlvisionscanner/
├── MainActivity.kt          # Actividad principal con OCR
├── AssistantActivity.kt     # Interfaz del asistente IA
└── res/
    ├── layout/             # Layouts de Material Design 3
    ├── drawable/           # Iconos y drawables personalizados
    ├── values/             # Colores, strings, temas
    └── values-v27/         # Recursos específicos API 27+
```

### Notas Importantes

- **Privacidad**: Las imágenes no se almacenan permanentemente, solo temporalmente durante el procesamiento
- **Seguridad**: Las API keys se guardan localmente usando SharedPreferences cifrados
- **Precisión**: La calidad del OCR depende de la resolución y claridad de la imagen
- **Conectividad**: El asistente IA requiere conexión a internet activa
- **Compatibilidad**: Optimizado para dispositivos modernos con Android 7.0+

### Licencia

MIT License - Ver archivo [LICENSE](./LICENSE) para más detalles.

### Desarrollador

**Jose Jordan**  
- GitHub: [@mundodigitalpro](https://github.com/mundodigitalpro)
- Proyecto: [MLVisionKotlin](https://github.com/mundodigitalpro/MLVisionKotlin)

---

*ML Vision Scanner v4.0 - Desarrollado con ❤️ para la comunidad Android*
