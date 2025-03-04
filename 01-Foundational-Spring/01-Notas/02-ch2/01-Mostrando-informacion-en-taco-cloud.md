## **Mostrando Información en Taco Cloud**

En esta sección, se describe cómo Taco Cloud, una aplicación web para ordenar tacos personalizados, necesita mostrar una lista de ingredientes disponibles para que los usuarios puedan diseñar sus propios tacos. La lista de ingredientes no debe estar codificada en el HTML, sino que debe ser dinámica, obtenida desde una base de datos y mostrada en la página web.

---

### **Componentes Necesarios**

Para implementar esta funcionalidad, se necesitan los siguientes componentes:

1. **Clase de Dominio (Ingrediente)**:
    - Define las propiedades de un ingrediente (por ejemplo, nombre, tipo, etc.).
    - Actúa como la representación de los datos en la aplicación.

2. **Controlador Spring MVC**:
    - Se encarga de obtener la lista de ingredientes y pasarla a la vista.
    - En este capítulo, el controlador proporcionará datos simulados, pero en el capítulo 3 se conectará a una base de datos.

3. **Vista (Template)**:
    - Renderiza la lista de ingredientes en HTML para que el usuario pueda verlos en el navegador.

---

### **Flujo de una Solicitud en Spring MVC**

El flujo típico de una solicitud en Spring MVC es el siguiente:

1. El navegador realiza una solicitud (por ejemplo, para ver la página de diseño de tacos).
2. El controlador recibe la solicitud, obtiene los datos necesarios (en este caso, la lista de ingredientes) y los pasa a la vista.
3. La vista genera el HTML con los datos proporcionados por el controlador.
4. El HTML se envía de vuelta al navegador para que el usuario lo vea.

---

### **Clase de Dominio: Ingrediente**

Antes de escribir el controlador y la vista, es necesario definir la clase de dominio que representa un ingrediente. Aquí está un ejemplo de cómo podría verse esta clase:

```java
package tacos;

public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public Ingredient(String id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

- **Atributos**:
    - `id`: Identificador único del ingrediente.
    - `name`: Nombre del ingrediente (por ejemplo, "Tortilla de Maíz").
    - `type`: Tipo de ingrediente (por ejemplo, WRAP, PROTEIN, etc.).
- **Enum `Type`**:
    - Define los tipos de ingredientes disponibles.

---

### **Controlador Spring MVC**

El controlador se encarga de obtener la lista de ingredientes y pasarla a la vista. Aquí está un ejemplo de cómo podría implementarse:

```java
package tacos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import java.util.Arrays;
import java.util.List;

@Controller
public class DesignTacoController {

    @GetMapping("/design")
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
            new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
            new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
            new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
            new Ingredient("CARN", "Carnitas", Type.PROTEIN),
            new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
            new Ingredient("LETC", "Lettuce", Type.VEGGIES),
            new Ingredient("CHED", "Cheddar", Type.CHEESE),
            new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
            new Ingredient("SLSA", "Salsa", Type.SAUCE),
            new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        model.addAttribute("ingredients", ingredients);
        return "design";
    }
}
```

- **Anotación `@Controller`**:
    - Marca la clase como un controlador Spring MVC.
- **Método `showDesignForm`**:
    - Maneja solicitudes GET a la ruta `/design`.
    - Crea una lista de ingredientes simulados y la agrega al modelo (`Model`).
    - Devuelve el nombre de la vista (`design`), que renderizará la lista de ingredientes.

---

### **Vista: Template `design.html`**

La vista se encarga de mostrar la lista de ingredientes en el navegador. Aquí está un ejemplo de cómo podría verse el template `design.html` usando Thymeleaf:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Taco Cloud</title>
</head>
<body>
    <h1>Design Your Taco!</h1>
    <h2>Choose your ingredients:</h2>
    <ul>
        <li th:each="ingredient : ${ingredients}">
            <span th:text="${ingredient.name}">Ingredient Name</span>
            (<span th:text="${ingredient.type}">Ingredient Type</span>)
        </li>
    </ul>
</body>
</html>
```

- **Thymeleaf**:
    - La expresión `th:each` itera sobre la lista de ingredientes.
    - La expresión `th:text` muestra el nombre y el tipo de cada ingrediente.

---

### **Resumen**

- **Clase de Dominio**: Define las propiedades de un ingrediente.
- **Controlador**: Obtiene la lista de ingredientes y la pasa a la vista.
- **Vista**: Renderiza la lista de ingredientes en HTML.

---

### **Preguntas de Opción Múltiple**

1. **¿Qué anotación se utiliza para marcar una clase como un controlador en Spring MVC?**
    - A) `@Component`
    - B) `@Service`
    - C) `@Controller`
    - D) `@Repository`

   **Respuesta Correcta: C) `@Controller`**
    - **Justificación:** La anotación `@Controller` se utiliza específicamente para marcar una clase como un controlador en Spring MVC.

2. **¿Qué método se utiliza en un controlador Spring MVC para manejar solicitudes GET?**
    - A) `@PostMapping`
    - B) `@GetMapping`
    - C) `@PutMapping`
    - D) `@DeleteMapping`

   **Respuesta Correcta: B) `@GetMapping`**
    - **Justificación:** La anotación `@GetMapping` se utiliza para manejar solicitudes HTTP GET.

3. **¿Qué atributo de Thymeleaf se utiliza para iterar sobre una lista en un template?**
    - A) `th:if`
    - B) `th:each`
    - C) `th:text`
    - D) `th:value`

   **Respuesta Correcta: B) `th:each`**
    - **Justificación:** La expresión `th:each` se utiliza para iterar sobre una colección en Thymeleaf.

4. **¿Qué objeto se utiliza en un controlador Spring MVC para pasar datos a la vista?**
    - A) `HttpServletRequest`
    - B) `Model`
    - C) `HttpServletResponse`
    - D) `Session`

   **Respuesta Correcta: B) `Model`**
    - **Justificación:** El objeto `Model` se utiliza para pasar datos desde el controlador a la vista.

5. **¿Dónde se define la lista de ingredientes en el controlador?**
    - A) En la base de datos.
    - B) En el archivo `application.properties`.
    - C) En el método del controlador.
    - D) En el template HTML.

   **Respuesta Correcta: C) En el método del controlador.**
    - **Justificación:** En este ejemplo, la lista de ingredientes se define directamente en el método del controlador. En un escenario real, se obtendría de una base de datos.

---

### **Conclusión**

En esta sección, has aprendido cómo mostrar información dinámica en una aplicación Spring MVC utilizando un controlador, una clase de dominio y una vista. Este es el primer paso para construir la funcionalidad de diseño de tacos en Taco Cloud.

