
---

## **1.3.3 Probando el Controlador**

Probar aplicaciones web puede ser complicado, especialmente cuando se trata de verificar el contenido de una página HTML. Sin embargo, Spring ofrece un soporte de pruebas muy potente que facilita la tarea. En este caso, se escribe una prueba para el controlador de la página de inicio (`HomeController`) que verifica lo siguiente:

1. **Realiza una solicitud HTTP GET** a la ruta raíz (`/`).
2. **Espera una respuesta exitosa** (código HTTP 200).
3. **Verifica que la vista devuelta** tenga el nombre lógico `home`.
4. **Comprueba que el contenido de la vista** contenga el texto `"Welcome to..."`.

Aquí está el código de la prueba:

```java
package tacos;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)   
public class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;    

  @Test
  public void testHomePage() throws Exception {
    mockMvc.perform(get("/"))          
           .andExpect(status().isOk())    
           .andExpect(view().name("home"))       
           .andExpect(content().string(containsString("Welcome to...")));
  }
}
```

---

### **Explicación del Código**

1. **Anotación `@WebMvcTest`**:
    - Esta anotación es específica para pruebas de controladores en Spring MVC.
    - Configura el entorno de prueba para que se enfoque únicamente en los componentes de Spring MVC, sin cargar todo el contexto de la aplicación.
    - En este caso, se especifica que la prueba es para `HomeController.class`, lo que permite que Spring registre este controlador en el contexto de prueba.

2. **Inyección de `MockMvc`**:
    - `MockMvc` es una clase proporcionada por Spring para simular solicitudes HTTP y verificar respuestas en un entorno de prueba.
    - Se inyecta automáticamente en la prueba gracias a la anotación `@Autowired`.

3. **Método `testHomePage()`**:
    - Este método define la prueba para la página de inicio.
    - **`mockMvc.perform(get("/"))`**:
        - Simula una solicitud HTTP GET a la ruta raíz (`/`).
    - **`.andExpect(status().isOk())`**:
        - Verifica que la respuesta tenga un código HTTP 200 (OK).
    - **`.andExpect(view().name("home"))`**:
        - Verifica que el nombre de la vista devuelta sea `home`.
    - **`.andExpect(content().string(containsString("Welcome to...")))`**:
        - Verifica que el contenido de la respuesta contenga el texto `"Welcome to..."`.

---

### **¿Por qué Usar `@WebMvcTest`?**

- **Enfoque en Spring MVC**:
    - `@WebMvcTest` configura un entorno de prueba ligero que se enfoca únicamente en los componentes de Spring MVC, como controladores.
    - Esto hace que las pruebas sean más rápidas y específicas, ya que no se carga todo el contexto de la aplicación.

- **Mocking de Spring MVC**:
    - En lugar de iniciar un servidor real, `MockMvc` simula las solicitudes y respuestas HTTP, lo que es suficiente para probar el comportamiento del controlador.

---

### **Ejecución de la Prueba**

La prueba se puede ejecutar de dos maneras:

1. **En un IDE**:
    - Puedes ejecutar la prueba directamente en tu IDE (como IntelliJ IDEA o Eclipse) haciendo clic derecho en la clase o método y seleccionando "Run".

2. **Con Maven**:
    - También puedes ejecutar la prueba desde la línea de comandos usando Maven:
      ```bash
      $ mvnw test
      ```

---

### **Resultado de la Prueba**

- Si el controlador y la vista están correctamente implementados, la prueba pasará y verás un mensaje de éxito (generalmente en verde).
- Si alguna de las expectativas no se cumple (por ejemplo, si la vista no se llama `home` o si el contenido no contiene `"Welcome to..."`), la prueba fallará.

---

### **Verificación en el Navegador**

Aunque la prueba pasa, es satisfactorio ver el resultado en un navegador para asegurarte de que todo funciona como se espera. Para hacer esto:

1. **Construye la aplicación**:
    - Usa Maven para construir la aplicación:
      ```bash
      $ mvnw clean package
      ```

2. **Ejecuta la aplicación**:
    - Una vez construida, ejecuta la aplicación:
      ```bash
      $ java -jar target/nombre-del-archivo.jar
      ```

3. **Abre el navegador**:
    - Visita `http://localhost:8080/` en tu navegador.
    - Deberías ver la página de inicio con el mensaje `"Welcome to..."` y el logo de Taco Cloud.

---

### **Resumen**

- **Pruebas con `MockMvc`**:
    - Spring facilita las pruebas de controladores utilizando `MockMvc`, que simula solicitudes HTTP y verifica respuestas.
    - La anotación `@WebMvcTest` configura un entorno de prueba ligero para Spring MVC.

- **Expectativas de la Prueba**:
    - La prueba verifica que la solicitud a la ruta raíz (`/`) devuelva un código HTTP 200, una vista llamada `home` y un contenido que incluya `"Welcome to..."`.

- **Verificación en el Navegador**:
    - Después de pasar la prueba, es recomendable ejecutar la aplicación y verificar manualmente el resultado en el navegador.

---

### **Conclusión**

En esta sección, has aprendido:

- Cómo escribir una prueba para un controlador Spring MVC utilizando `MockMvc`.
- Cómo usar la anotación `@WebMvcTest` para configurar un entorno de prueba ligero.
- Cómo verificar el comportamiento del controlador y la vista.

