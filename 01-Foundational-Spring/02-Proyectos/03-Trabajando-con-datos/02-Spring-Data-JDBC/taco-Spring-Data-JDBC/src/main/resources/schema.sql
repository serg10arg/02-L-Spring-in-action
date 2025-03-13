-- Tabla Taco_Order
CREATE TABLE IF NOT EXISTS Taco_Order (
    id IDENTITY PRIMARY KEY,
    delivery_name VARCHAR(50) NOT NULL,
    delivery_Street VARCHAR(50) NOT NULL,
    delivery_city VARCHAR(50) NOT NULL,
    delivery_State VARCHAR(2) NOT NULL,
    delivery_Zip VARCHAR(10) NOT NULL,
    cc_number VARCHAR(16) NOT NULL,
    cc_expiration VARCHAR(5) NOT NULL,
    cc_cvv VARCHAR(3) NOT NULL,
    placed_at TIMESTAMP NOT NULL
);

-- Tabla Taco
CREATE TABLE IF NOT EXISTS Taco (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    taco_order BIGINT NOT NULL,
    taco_order_key BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (taco_order) REFERENCES Taco_Order(id)
);

-- Tabla Ingredient
CREATE TABLE IF NOT EXISTS Ingredient (
    id VARCHAR(4) NOT NULL PRIMARY KEY,
    name VARCHAR(25) NOT NULL,
    type VARCHAR(10) NOT NULL
);

-- Tabla Ingredient_Ref
CREATE TABLE IF NOT EXISTS Ingredient_Ref (
    ingredient VARCHAR(4) NOT NULL,
    taco BIGINT NOT NULL,
    taco_key BIGINT NOT NULL,
    PRIMARY KEY (ingredient, taco), -- Clave primaria compuesta
    FOREIGN KEY (ingredient) REFERENCES Ingredient(id),
    FOREIGN KEY (taco) REFERENCES Taco(id)
);