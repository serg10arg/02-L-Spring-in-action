El texto explica cómo habilitar y configurar **Spring Data Cassandra** en un proyecto Spring Boot, así como algunos conceptos básicos sobre Cassandra y su configuración. A continuación, te lo explico de manera clara y detallada:

---

### 1. **Habilitar Spring Data Cassandra**

Para usar Spring Data Cassandra en un proyecto Spring Boot, debes agregar la dependencia correspondiente en tu archivo de configuración de Maven o Gradle. Spring Data Cassandra ofrece dos tipos de dependencias:

- **No reactiva**: Para aplicaciones tradicionales (no reactivas).
- **Reactiva**: Para aplicaciones reactivas (se cubrirá en el capítulo 15).

En este caso, se utiliza la dependencia no reactiva.

#### Dependencia Maven:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-cassandra</artifactId>
</dependency>
```

#### Dependencia Gradle:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-cassandra'
```

---

### 2. **Eliminar dependencias de bases de datos relacionales**

Si anteriormente estabas usando Spring Data JPA o Spring Data JDBC para trabajar con una base de datos relacional, debes eliminar esas dependencias, ya que ahora usarás Cassandra. Por ejemplo, elimina las dependencias de `spring-boot-starter-data-jpa`, `spring-boot-starter-jdbc`, y cualquier driver de base de datos relacional (como H2, MySQL, etc.).

---

### 3. **Configurar un clúster de Cassandra**

Cassandra funciona como un clúster de nodos que trabajan juntos para almacenar datos. Si no tienes un clúster de Cassandra disponible, puedes configurar uno localmente usando Docker.

#### Ejemplo de configuración con Docker:
```bash
# Crear una red para Cassandra
docker network create cassandra-net

# Iniciar un nodo de Cassandra
docker run --name my-cassandra \
    --network cassandra-net \
    -p 9042:9042 \
    -d cassandra:latest
```

- **`--name my-cassandra`**: Asigna un nombre al contenedor.
- **`--network cassandra-net`**: Conecta el contenedor a la red creada.
- **`-p 9042:9042`**: Expone el puerto 9042 (puerto predeterminado de Cassandra) en el host.
- **`cassandra:latest`**: Usa la imagen oficial de Cassandra.

---

### 4. **Crear un keyspace en Cassandra**

En Cassandra, un **keyspace** es un espacio de nombres que agrupa tablas, similar a una base de datos en sistemas relacionales. Para crear un keyspace, puedes usar el shell de CQL (Cassandra Query Language).

#### Acceder al shell de CQL con Docker:
```bash
docker run -it --network cassandra-net --rm cassandra cqlsh my-cassandra
```

#### Crear un keyspace:
```sql
cqlsh> CREATE KEYSPACE tacocloud
       WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}
       AND durable_writes = true;
```

- **`tacocloud`**: Nombre del keyspace.
- **`SimpleStrategy`**: Estrategia de replicación para un solo centro de datos.
- **`replication_factor: 1`**: Indica que se almacenará una copia de cada fila.
- **`durable_writes: true`**: Asegura que las escrituras sean duraderas.

---

### 5. **Configurar Spring Data Cassandra**

Una vez creado el keyspace, debes configurar Spring Data Cassandra en tu aplicación. Esto se hace en el archivo `application.yml` o `application.properties`.

#### Ejemplo de configuración en `application.yml`:
```yaml
spring:
  data:
    cassandra:
      keyspace-name: tacocloud
      schema-action: recreate
      local-datacenter: datacenter1
```

- **`keyspace-name`**: Especifica el keyspace que se utilizará (`tacocloud` en este caso).
- **`schema-action`**: Define acciones sobre el esquema de la base de datos. En desarrollo, `recreate` es útil porque elimina y recrea las tablas al iniciar la aplicación. En producción, usa `none`.
- **`local-datacenter`**: Especifica el centro de datos local. Para un solo nodo, usa `datacenter1`.

#### Configuración adicional (opcional):
Si Cassandra no está en `localhost` o usa un puerto diferente, puedes configurar los puntos de contacto y el puerto:
```yaml
spring:
  data:
    cassandra:
      contact-points:
        - casshost-1.tacocloud.com
        - casshost-2.tacocloud.com
        - casshost-3.tacocloud.com
      port: 9043
```

- **`contact-points`**: Lista de nodos de Cassandra a los que la aplicación intentará conectarse.
- **`port`**: Puerto en el que Cassandra escucha (9042 por defecto).

#### Autenticación:
Si Cassandra requiere autenticación, configura el nombre de usuario y la contraseña:
```yaml
spring:
  data:
    cassandra:
      username: tacocloud
      password: s3cr3tP455w0rd
```

---

### 6. **Resumen de la configuración**

1. **Agrega la dependencia** `spring-boot-starter-data-cassandra`.
2. **Elimina dependencias** de bases de datos relacionales si las tenías.
3. **Configura un clúster de Cassandra** (puedes usar Docker para desarrollo).
4. **Crea un keyspace** en Cassandra usando el shell de CQL.
5. **Configura Spring Data Cassandra** en `application.yml` o `application.properties`.

---

### 7. **Próximos pasos**

Una vez configurado Spring Data Cassandra, puedes:
1. **Mapear entidades**: Usar anotaciones como `@Table` y `@PrimaryKey` para mapear clases Java a tablas de Cassandra.
2. **Crear repositorios**: Extender `CassandraRepository` para interactuar con la base de datos.
3. **Explorar consultas personalizadas**: Usar métodos de repositorio o la anotación `@Query` para consultas específicas.

---

### 8. **Consideraciones adicionales**

- **Replicación**: En un entorno de producción, considera usar `NetworkTopologyStrategy` para la replicación si tu clúster está distribuido en múltiples centros de datos.
- **Documentación oficial**: Para más detalles sobre Cassandra, consulta la [documentación oficial de Apache Cassandra](http://cassandra.apache.org/doc/latest/).

---

Con esto, tu aplicación Spring Boot está lista para usar Spring Data Cassandra y persistir datos en una base de datos NoSQL distribuida y escalable.