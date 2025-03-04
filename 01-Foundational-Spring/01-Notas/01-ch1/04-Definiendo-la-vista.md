
---

## **1.3.2 Definiendo la Vista**

El objetivo de la página de inicio es simple: dar la bienvenida a los usuarios al sitio. Para lograr esto, se utiliza una plantilla Thymeleaf que define el contenido de la página. Aquí está el código de la plantilla:

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:th="http://www.thymeleaf.org">
  <head>
    <title>Taco Cloud</title>
  </head>
  <body>
    <h1>Welcome to...</h1>
    <img th:src="@{/images/TacoCloud.png}"/>
  </body>
</html>
```

---

### **Explicación del Código**

1. **Estructura Básica del HTML**:
    - La plantilla es un archivo HTML estándar con una estructura básica: `<!DOCTYPE html>`, `<html>`, `<head>`, y `<body>`.
    - En el `<head>`, se define el título de la página (`<title>Taco Cloud</title>`), que aparecerá en la pestaña del navegador.

2. **Uso de Thymeleaf**:
    - Thymeleaf se integra con HTML a través de atributos especiales que comienzan con `th:`. En este caso, se usa el atributo `th:src` en la etiqueta `<img>`.
    - El atributo `xmlns:th="http://www.thymeleaf.org"` es necesario para que Thymeleaf funcione correctamente en el archivo HTML.

3. **Mostrar una Imagen**:
    - La imagen del logo de Taco Cloud se muestra utilizando la etiqueta `<img>`.
    - El atributo `th:src` utiliza una expresión Thymeleaf (`@{...}`) para referenciar la imagen. En este caso, la ruta de la imagen es `/images/TacoCloud.png`.
    - La expresión `@{...}` es una forma de Thymeleaf para manejar rutas relativas al contexto de la aplicación. Esto asegura que la ruta de la imagen sea correcta, independientemente de cómo se despliegue la aplicación.

---

### **Ubicación de la Imagen**

El texto explica que la imagen del logo (`TacoCloud.png`) debe colocarse en una ubicación específica dentro del proyecto para que pueda ser accedida correctamente:

- **Ruta de la Imagen**:
    - La imagen se referencia con la ruta `/images/TacoCloud.png`.
    - En una aplicación Spring Boot, el contenido estático (como imágenes, CSS y JavaScript) se coloca en la carpeta `/src/main/resources/static`.

- **Ubicación Correcta**:
    - Por lo tanto, la imagen debe estar ubicada en:
      ```
      /src/main/resources/static/images/TacoCloud.png
      ```

---

### **Resumen de la Vista**

- La vista es un archivo HTML simple que utiliza Thymeleaf para mostrar contenido dinámico.
- El logo de Taco Cloud se muestra utilizando una etiqueta `<img>` con el atributo `th:src`.
- La imagen debe colocarse en la carpeta de recursos estáticos (`/src/main/resources/static/images/`) para que Spring Boot pueda servirla correctamente.

---

### **Próximos Pasos**

Ahora que tienes:

1. Un controlador (`HomeController`) que maneja solicitudes para la ruta raíz (`/`).
2. Una vista (`home.html`) que muestra un mensaje de bienvenida y un logo.

El siguiente paso es **probar el controlador** para asegurarte de que funciona como se espera. El texto sugiere que se escribirá una prueba para el controlador, lo cual es una buena práctica en el desarrollo de software.

---

### **¿Por qué es Importante Probar el Controlador?**

- **Calidad del Código**:
    - Las pruebas aseguran que el controlador maneje las solicitudes correctamente y devuelva las respuestas esperadas.
- **Mantenibilidad**:
    - Si en el futuro cambias el código del controlador, las pruebas te ayudarán a detectar errores rápidamente.
- **Documentación**:
    - Las pruebas sirven como documentación viva del comportamiento esperado del controlador.

---

### **Conclusión**

En esta sección, has aprendido:

- Cómo definir una vista simple utilizando Thymeleaf.
- Cómo mostrar una imagen en la vista y dónde colocar los archivos estáticos en un proyecto Spring Boot.
- La importancia de escribir pruebas para los controladores.

