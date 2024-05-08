# ML Vision Kotlin App

## Descripción

Esta aplicación es una herramienta simple y poderosa que permite capturar o seleccionar imágenes para reconocer texto utilizando la tecnología de reconocimiento de texto de Google ML Kit. Desarrollada en Kotlin para la plataforma Android, proporciona funcionalidades fáciles de usar para capturar texto desde cualquier imagen y compartirlo con otras aplicaciones.

### Funcionalidades

- **Captura de imágenes con la cámara**: Permite tomar fotografías con la cámara del dispositivo y realizar el reconocimiento de texto sobre ellas.
- **Selección de imágenes desde el almacenamiento**: Permite seleccionar imágenes desde la galería para extraer el texto presente en ellas.
- **Reconocimiento de texto**: Utiliza Google ML Kit para identificar y extraer texto desde las imágenes capturadas o seleccionadas.
- **Compartir texto**: Facilita compartir el texto reconocido con otras aplicaciones a través de una sencilla opción de compartir.

### Tecnologías

- **Kotlin**: Lenguaje de programación utilizado para la aplicación.
- **Google ML Kit**: Kit de herramientas de aprendizaje automático para reconocimiento de texto.
- **ViewBinding**: Para una gestión más eficiente de las vistas y enlaces con la UI.
- **ActivityResultContracts**: Para una administración de resultados de actividades más eficaz.

### Requisitos

- Dispositivo Android con cámara y permisos de almacenamiento.
- Android SDK 21 (Lollipop) o superior.

### Uso

1. **Permisos**: Asegúrate de que la aplicación tenga permisos para acceder a la cámara.
2. **Captura/Selección**:
    - Captura una imagen utilizando el botón de captura de imagen.
    - O selecciona una imagen de la galería usando el botón de selección.
3. **Reconocer texto**: La aplicación iniciará automáticamente el proceso de reconocimiento de texto en la imagen seleccionada.
4. **Compartir texto**: Comparte el texto reconocido con otras aplicaciones.

### Observaciones

- La precisión del reconocimiento de texto puede variar según la calidad de la imagen y la claridad del texto.
- La aplicación no guarda imágenes de forma permanente, se almacenan temporalmente mientras se realiza el reconocimiento.
