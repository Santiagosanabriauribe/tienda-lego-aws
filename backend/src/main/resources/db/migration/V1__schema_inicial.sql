CREATE TABLE categorias (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL,
    descripcion VARCHAR(500),
    activa      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE productos (
    id            BIGSERIAL PRIMARY KEY,
    nombre        VARCHAR(255) NOT NULL,
    numero_set    VARCHAR(50),
    descripcion   VARCHAR(1000),
    precio        NUMERIC(10,2) NOT NULL,
    stock         INTEGER NOT NULL,
    imagen_url    VARCHAR(500),
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    categoria_id  BIGINT REFERENCES categorias(id)
);

CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_activo ON productos(activo);

CREATE TABLE usuarios (
    id       BIGSERIAL PRIMARY KEY,
    nombre   VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol      VARCHAR(50) NOT NULL DEFAULT 'USUARIO'
);

CREATE INDEX idx_usuarios_email ON usuarios(email);

CREATE TABLE pedidos (
    id                    BIGSERIAL PRIMARY KEY,
    usuario_id            BIGINT NOT NULL,
    usuario_nombre        VARCHAR(255),
    total                 NUMERIC(10,2) NOT NULL DEFAULT 0,
    metodo_pago           VARCHAR(50),
    estado                VARCHAR(50) NOT NULL DEFAULT 'CONFIRMADO',
    fecha                 TIMESTAMP NOT NULL DEFAULT NOW(),
    direccion_envio       VARCHAR(500),
    ciudad_envio          VARCHAR(100),
    departamento_envio    VARCHAR(100),
    telefono_envio        VARCHAR(50),
    nombre_destinatario   VARCHAR(255)
);

CREATE INDEX idx_pedidos_usuario ON pedidos(usuario_id);

CREATE TABLE items_pedido (
    id               BIGSERIAL PRIMARY KEY,
    pedido_id        BIGINT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
    producto_id      BIGINT NOT NULL,
    producto_nombre  VARCHAR(255) NOT NULL,
    cantidad         INTEGER NOT NULL,
    precio_unitario  NUMERIC(10,2) NOT NULL,
    subtotal         NUMERIC(10,2) NOT NULL
);

CREATE INDEX idx_items_pedido ON items_pedido(pedido_id);
