## 3.1.3 Definición del Esquema y Precarga de Datos

En esta sección, el texto explica cómo definir el esquema de la base de datos para la aplicación Taco Cloud y cómo precargar datos iniciales. Esto incluye la creación de tablas y la inserción de datos de ingredientes.

---

#### 1. **Esquema de la Base de Datos**

El esquema de la base de datos para Taco Cloud consta de cuatro tablas principales:

1. **`Taco_Order`**: Almacena los detalles de los pedidos.
2. **`Taco`**: Almacena información sobre los tacos diseñados por los usuarios.
3. **`Ingredient_Ref`**: Relaciona los tacos con sus ingredientes.
4. **`Ingredient`**: Almacena información sobre los ingredientes.

##### Estructura de las Tablas

- **`Taco_Order`**:
  ```sql
  create table if not exists Taco_Order (
      id identity,
      delivery_Name varchar(50) not null,
      delivery_Street varchar(50) not null,
      delivery_City varchar(50) not null,
      delivery_State varchar(2) not null,
      delivery_Zip varchar(10) not null,
      cc_number varchar(16) not null,
      cc_expiration varchar(5) not null,
      cc_cvv varchar(3) not null,
      placed_at timestamp not null
  );
  ```

- **`Taco`**:
  ```sql
  create table if not exists Taco (
      id identity,
      name varchar(50) not null,
      taco_order bigint not null,
      taco_order_key bigint not null,
      created_at timestamp not null
  );
  ```

- **`Ingredient_Ref`**:
  ```sql
  create table if not exists Ingredient_Ref (
      ingredient varchar(4) not null,
      taco bigint not null,
      taco_key bigint not null
  );
  ```

- **`Ingredient`**:
  ```sql
  create table if not exists Ingredient (
      id varchar(4) not null,
      name varchar(25) not null,
      type varchar(10) not null
  );
  ```

##### Relaciones entre Tablas

- **Clave foránea en `Taco`**:
  ```sql
  alter table Taco
      add foreign key (taco_order) references Taco_Order(id);
  ```

- **Clave foránea en `Ingredient_Ref`**:
  ```sql
  alter table Ingredient_Ref
      add foreign key (ingredient) references Ingredient(id);
  ```

---

#### 2. **Ubicación del Archivo `schema.sql`**

Spring Boot ejecuta automáticamente un archivo llamado `schema.sql` ubicado en la raíz del classpath (generalmente en `src/main/resources`) al iniciar la aplicación. Este archivo debe contener las sentencias SQL para crear las tablas.

##### Ejemplo de `schema.sql`:

```sql
create table if not exists Taco_Order (
    id identity,
    delivery_Name varchar(50) not null,
    delivery_Street varchar(50) not null,
    delivery_City varchar(50) not null,
    delivery_State varchar(2) not null,
    delivery_Zip varchar(10) not null,
    cc_number varchar(16) not null,
    cc_expiration varchar(5) not null,
    cc_cvv varchar(3) not null,
    placed_at timestamp not null
);

create table if not exists Taco (
    id identity,
    name varchar(50) not null,
    taco_order bigint not null,
    taco_order_key bigint not null,
    created_at timestamp not null
);

create table if not exists Ingredient_Ref (
    ingredient varchar(4) not null,
    taco bigint not null,
    taco_key bigint not null
);

create table if not exists Ingredient (
    id varchar(4) not null,
    name varchar(25) not null,
    type varchar(10) not null
);

alter table Taco
    add foreign key (taco_order) references Taco_Order(id);

alter table Ingredient_Ref
    add foreign key (ingredient) references Ingredient(id);
```

---

#### 3. **Precarga de Datos con `data.sql`**

Además de crear las tablas, es útil precargar la base de datos con datos iniciales. Spring Boot también ejecuta automáticamente un archivo llamado `data.sql` ubicado en la raíz del classpath.

##### Ejemplo de `data.sql`:

```sql
delete from Ingredient_Ref;
delete from Taco;
delete from Taco_Order;
delete from Ingredient;

insert into Ingredient (id, name, type) values ('FLTO', 'Flour Tortilla', 'WRAP');
insert into Ingredient (id, name, type) values ('COTO', 'Corn Tortilla', 'WRAP');
insert into Ingredient (id, name, type) values ('GRBF', 'Ground Beef', 'PROTEIN');
insert into Ingredient (id, name, type) values ('CARN', 'Carnitas', 'PROTEIN');
insert into Ingredient (id, name, type) values ('TMTO', 'Diced Tomatoes', 'VEGGIES');
insert into Ingredient (id, name, type) values ('LETC', 'Lettuce', 'VEGGIES');
insert into Ingredient (id, name, type) values ('CHED', 'Cheddar', 'CHEESE');
insert into Ingredient (id, name, type) values ('JACK', 'Monterrey Jack', 'CHEESE');
insert into Ingredient (id, name, type) values ('SLSA', 'Salsa', 'SAUCE');
insert into Ingredient (id, name, type) values ('SRCR', 'Sour Cream', 'SAUCE');
```

- **`delete from`**: Elimina cualquier dato existente en las tablas antes de insertar nuevos datos.
- **`insert into`**: Inserta datos de ingredientes en la tabla `Ingredient`.

---

#### 4. **Prueba de la Aplicación**

Una vez que el esquema y los datos iniciales están configurados, puedes ejecutar la aplicación y visitar la página de diseño (`/design`) para ver cómo `JdbcIngredientRepository` obtiene los ingredientes de la base de datos.

---

#### 5. **Próximos Pasos**

El texto menciona que, aunque solo se ha desarrollado un repositorio para los ingredientes, el siguiente paso es crear repositorios para las entidades `Taco` y `Taco_Order`. Estos repositorios permitirán persistir y recuperar información sobre los tacos y los pedidos.

---

### Resumen

1. **Esquema de la Base de Datos**:
    - Se definen cuatro tablas: `Taco_Order`, `Taco`, `Ingredient_Ref` e `Ingredient`.
    - Se establecen relaciones entre las tablas mediante claves foráneas.

2. **Archivo `schema.sql`**:
    - Contiene las sentencias SQL para crear las tablas.
    - Se coloca en `src/main/resources`.

3. **Archivo `data.sql`**:
    - Contiene las sentencias SQL para precargar datos iniciales.
    - Se coloca en `src/main/resources`.

4. **Prueba de la Aplicación**:
    - Al ejecutar la aplicación, las tablas se crean y los datos se cargan automáticamente.

Con esto, la base de datos está lista para ser utilizada por la aplicación Taco Cloud. 🎉