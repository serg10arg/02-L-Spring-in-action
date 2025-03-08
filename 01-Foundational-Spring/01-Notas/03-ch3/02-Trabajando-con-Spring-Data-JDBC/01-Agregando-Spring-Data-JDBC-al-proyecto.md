### 3.2.1 Agregando Spring Data JDBC al Proyecto

En esta sección, el texto explica cómo agregar Spring Data JDBC a un proyecto Spring Boot. Spring Data JDBC es una extensión de Spring Data que simplifica el trabajo con bases de datos relacionales, ofreciendo una abstracción más alta que `JdbcTemplate`.

---

#### 1. **Dependencia de Spring Data JDBC**

Para usar Spring Data JDBC, debes agregar la dependencia `spring-boot-starter-data-jdbc` al archivo `pom.xml` de tu proyecto Maven.

##### Código del `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

- **`spring-boot-starter-data-jdbc`**: Esta dependencia incluye Spring Data JDBC y todas las bibliotecas necesarias para trabajar con bases de datos relacionales.

---

#### 2. **Eliminar la Dependencia de `JdbcTemplate`**

Dado que Spring Data JDBC proporciona una abstracción más alta que `JdbcTemplate`, ya no necesitas la dependencia `spring-boot-starter-jdbc`. Puedes eliminarla del `pom.xml`.

##### Dependencia a Eliminar:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

- **Razón**: Spring Data JDBC ya incluye las funcionalidades de `JdbcTemplate`, por lo que no es necesario mantener ambas dependencias.

---

#### 3. **Mantener la Dependencia de H2**

Aunque ya no necesitas `spring-boot-starter-jdbc`, aún necesitas una base de datos para el desarrollo. Si estás utilizando H2 como base de datos embebida, debes mantener su dependencia en el `pom.xml`.

##### Dependencia de H2:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

- **`h2`**: Esta dependencia proporciona la base de datos H2, que es liviana y fácil de configurar para desarrollo y pruebas.

---

#### 4. **Estructura Final del `pom.xml`**

Después de realizar estos cambios, tu archivo `pom.xml` debería verse similar a esto:

```xml
<dependencies>
    <!-- Otras dependencias del proyecto -->

    <!-- Spring Data JDBC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jdbc</artifactId>
    </dependency>

    <!-- Base de datos H2 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Otras dependencias del proyecto -->
</dependencies>
```

---

### Resumen

1. **Agregar Spring Data JDBC**:
    - Añade la dependencia `spring-boot-starter-data-jdbc` al `pom.xml`.

2. **Eliminar `JdbcTemplate`**:
    - Elimina la dependencia `spring-boot-starter-jdbc`, ya que Spring Data JDBC la reemplaza.

3. **Mantener H2**:
    - Conserva la dependencia de H2 para usar una base de datos embebida durante el desarrollo.

Con estos cambios, tu proyecto está listo para utilizar Spring Data JDBC, lo que simplificará la interacción con la base de datos. 🚀