
---

## **1.3.1 Manejo de Solicitudes Web**

Spring incluye un poderoso framework web conocido como **Spring MVC** (Model-View-Controller). En el centro de Spring MVC está el concepto de un **controlador**, que es una clase que maneja solicitudes y responde con información. En el caso de una aplicación web, un controlador responde opcionalmente llenando datos en el modelo y pasando la solicitud a una vista para producir HTML que se devuelve al navegador.

---

### **Controlador Simple para la Página de Inicio**

El texto presenta un ejemplo de un controlador simple que maneja solicitudes para la ruta raíz (`/`) y devuelve una vista llamada `home`. Aquí está el código del controlador:

```java
package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller           
public class HomeController {

  @GetMapping("/")     
  public String home() {
    return "home";    
  }
}
```

---

### **Explicación del Código**

1. **`@Controller`**:
    - Esta anotación identifica la clase como un **controlador** en Spring MVC.
    - Su principal propósito es marcar la clase como un componente para que Spring lo detecte durante el escaneo de componentes. Cuando Spring encuentra esta anotación, crea una instancia de `HomeController` y la registra como un bean en el contexto de la aplicación.
    - Aunque otras anotaciones como `@Component`, `@Service` y `@Repository` también podrían usarse, `@Controller` es más descriptiva y específica para este rol.

2. **`@GetMapping("/")`**:
    - Esta anotación indica que el método `home()` manejará solicitudes HTTP GET a la ruta raíz (`/`).
    - Cuando un usuario accede a la ruta raíz de la aplicación (por ejemplo, `http://localhost:8080/`), este método se ejecutará.

3. **Método `home()`**:
    - Este método es muy simple: devuelve un `String` con el valor `"home"`.
    - Este valor representa el **nombre lógico de la vista** que se debe renderizar. En este caso, la vista se llama `home`.

---

### **¿Cómo se Resuelve la Vista?**

El texto explica que el nombre de la vista (`home`) se interpreta de la siguiente manera:

- Spring busca un archivo de plantilla (template) que coincida con el nombre lógico de la vista.
- Dado que **Thymeleaf** está en el classpath (es decir, está incluido como dependencia en el proyecto), Spring asume que la plantilla está escrita en Thymeleaf.
- El nombre de la plantilla se deriva del nombre lógico de la vista de la siguiente manera:
    - Se le agrega el prefijo `/templates/`.
    - Se le agrega el sufijo `.html`.
    - Por lo tanto, la ruta completa de la plantilla será: `/src/main/resources/templates/home.html`.

---

### **¿Por qué Thymeleaf?**

El texto menciona que Thymeleaf fue elegido como motor de plantillas para este ejemplo, pero no es la única opción. Aquí hay algunas razones por las que se eligió Thymeleaf:

1. **Preferencia personal**:
    - El autor prefiere Thymeleaf sobre otras opciones como JSP (JavaServer Pages) o FreeMarker.

2. **Desafíos con JSP**:
    - Aunque JSP es una opción popular, puede haber complicaciones al usarlo con Spring Boot, especialmente en términos de configuración y compatibilidad.

3. **Flexibilidad**:
    - Thymeleaf es moderno, fácil de usar y se integra bien con Spring Boot. Además, permite trabajar con HTML estándar, lo que facilita la colaboración con diseñadores front-end.

---

### **Creación de la Plantilla `home.html`**

Para que este controlador funcione, necesitas crear la plantilla `home.html` en la siguiente ubicación:

```
/src/main/resources/templates/home.html
```

Aquí hay un ejemplo de cómo podría verse esta plantilla:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Taco Cloud</title>
</head>
<body>
    <h1>Welcome to Taco Cloud!</h1>
    <p>This is the home page.</p>
</body>
</html>
```

- **`xmlns:th`**: Este atributo es necesario para usar las funcionalidades de Thymeleaf en el HTML.
- **Contenido**: Puedes personalizar el contenido de la página según las necesidades de tu aplicación.

---

### **Resumen**

- **Spring MVC** es el framework web de Spring que se basa en el patrón Model-View-Controller.
- Un **controlador** es una clase que maneja solicitudes HTTP y devuelve una respuesta, generalmente una vista.
- La anotación `@Controller` marca una clase como un controlador.
- La anotación `@GetMapping` se usa para manejar solicitudes GET a una ruta específica.
- El nombre de la vista devuelto por el controlador (`home`) se resuelve en una plantilla Thymeleaf ubicada en `/src/main/resources/templates/home.html`.
- Thymeleaf fue elegido como motor de plantillas por su facilidad de uso y compatibilidad con Spring Boot.

---

### **Próximos pasos**

El texto sugiere que en el capítulo 2 se explorará más a fondo Spring MVC, incluyendo otras opciones de plantillas como JSP y FreeMarker. Esto te dará una visión más completa de las alternativas disponibles para construir vistas en aplicaciones Spring Boot.

