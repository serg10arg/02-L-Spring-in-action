
---

## **Preguntas de Opción Múltiple**

1. **¿Qué anotación se utiliza para marcar una clase como un controlador en Spring MVC?**
    - A) `@Component`
    - B) `@Service`
    - C) `@Controller`
    - D) `@Repository`

   **Respuesta Correcta: C) `@Controller`**
    - **Justificación:** La anotación `@Controller` se utiliza específicamente para marcar una clase como un controlador en Spring MVC. Las otras opciones (`@Component`, `@Service`, `@Repository`) tienen propósitos diferentes.

2. **¿Qué hace la anotación `@GetMapping("/")` en un controlador Spring MVC?**
    - A) Maneja solicitudes POST a la ruta raíz.
    - B) Maneja solicitudes GET a la ruta raíz.
    - C) Define una ruta para redireccionar a otra página.
    - D) Configura una vista predeterminada.

   **Respuesta Correcta: B) Maneja solicitudes GET a la ruta raíz.**
    - **Justificación:** La anotación `@GetMapping` se utiliza para manejar solicitudes HTTP GET a la ruta especificada, en este caso, la ruta raíz (`/`).

3. **¿Dónde se deben colocar los archivos estáticos (como imágenes) en un proyecto Spring Boot?**
    - A) `/src/main/resources/static/`
    - B) `/src/main/resources/templates/`
    - C) `/src/main/java/`
    - D) `/src/main/webapp/`

   **Respuesta Correcta: A) `/src/main/resources/static/`**
    - **Justificación:** En Spring Boot, los archivos estáticos (imágenes, CSS, JavaScript) deben colocarse en la carpeta `/src/main/resources/static/` para que puedan ser servidos correctamente.

4. **¿Qué método se utiliza en `MockMvc` para simular una solicitud HTTP GET?**
    - A) `post()`
    - B) `put()`
    - C) `delete()`
    - D) `get()`

   **Respuesta Correcta: D) `get()`**
    - **Justificación:** El método `get()` de `MockMvc` se utiliza para simular una solicitud HTTP GET a una ruta específica.

5. **¿Qué anotación se utiliza para configurar un entorno de prueba ligero enfocado en Spring MVC?**
    - A) `@SpringBootTest`
    - B) `@WebMvcTest`
    - C) `@DataJpaTest`
    - D) `@RestClientTest`

   **Respuesta Correcta: B) `@WebMvcTest`**
    - **Justificación:** La anotación `@WebMvcTest` configura un entorno de prueba ligero que se enfoca únicamente en los componentes de Spring MVC, como controladores.

6. **¿Qué verifica la expresión `.andExpect(content().string(containsString("Welcome to...")))` en una prueba con `MockMvc`?**
    - A) Que la respuesta tenga un código HTTP 200.
    - B) Que la vista se llame `home`.
    - C) Que el contenido de la respuesta contenga el texto `"Welcome to..."`.
    - D) Que la solicitud sea redirigida a otra página.

   **Respuesta Correcta: C) Que el contenido de la respuesta contenga el texto `"Welcome to..."`.**
    - **Justificación:** La expresión `content().string(containsString("..."))` verifica que el contenido de la respuesta contenga el texto especificado.

---

### **Conclusión**

En esta sección, has aprendido:

- Cómo escribir una prueba para un controlador Spring MVC utilizando `MockMvc`.
- Cómo usar la anotación `@WebMvcTest` para configurar un entorno de prueba ligero.
- Cómo verificar el comportamiento del controlador y la vista.

Si tienes más preguntas o necesitas más detalles, ¡no dudes en preguntar! 😊

---



