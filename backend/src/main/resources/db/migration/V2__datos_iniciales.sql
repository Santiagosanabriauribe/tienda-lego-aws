INSERT INTO categorias (nombre, descripcion) VALUES
    ('Star Wars', 'Sets de la saga Star Wars'),
    ('City', 'Sets de LEGO City'),
    ('Technic', 'Sets de LEGO Technic'),
    ('Creator', 'Sets de LEGO Creator Expert'),
    ('Harry Potter', 'Sets del mundo magico de Harry Potter');

INSERT INTO productos (nombre, numero_set, descripcion, precio, stock, categoria_id) VALUES
    ('Millennium Falcon', '75375', 'La nave mas rapida de la galaxia', 849.99, 5, 1),
    ('X-Wing Starfighter', '75355', 'Caza estelar T-65B de Luke Skywalker', 239.99, 12, 1),
    ('AT-AT Walker', '75313', 'Transporte blindado todo terreno', 799.99, 3, 1),
    ('Estacion de Policia', '60316', 'Estacion de policia de LEGO City', 99.99, 20, 2),
    ('Camion de Bomberos', '60374', 'Camion de bomberos con escalera', 69.99, 15, 2),
    ('Bugatti Chiron', '42151', 'Replica detallada del Bugatti Chiron', 449.99, 8, 3),
    ('Porsche 911', '42096', 'Porsche 911 RSR escala 1:8', 149.99, 10, 3),
    ('Casa Modular', '10297', 'Boutique y floristeria modular', 199.99, 6, 4),
    ('Castillo de Hogwarts', '71043', 'El castillo completo con minifiguras', 469.99, 4, 5),
    ('Expreso de Hogwarts', '76405', 'El famoso tren escarlata', 499.99, 7, 5);
