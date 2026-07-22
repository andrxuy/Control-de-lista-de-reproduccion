
INSERT INTO canciones (nombre_cancion, duracion, genero) VALUES
('Bohemian Rhapsody', '00:05:54', 'Rock'),
('Shape of You', '00:03:53', 'Pop'),
('Despacito', '00:03:48', 'Reggaeton'),
('Blinding Lights', '00:03:20', 'Synth Pop'),
('Hotel California', '00:06:30', 'Rock'),
('La Bicicleta', '00:03:18', 'Reggaeton'),
('Billie Jean', '00:04:54', 'Pop'),
('Smells Like Teen Spirit', '00:05:01', 'Grunge'),
('Vivir Mi Vida', '00:04:22', 'Salsa'),
('Despacito (Remix)', '00:04:22', 'Reggaeton');


ALTER TABLE canciones ADD COLUMN artista VARCHAR(255) AFTER nombre_cancion;
ALTER TABLE canciones ADD COLUMN album VARCHAR(255) AFTER artista;
ALTER TABLE canciones ADD COLUMN url_audio VARCHAR(500) AFTER duracion;
ALTER TABLE canciones ADD COLUMN url_portada VARCHAR(500) AFTER url_audio;
ALTER TABLE canciones ADD COLUMN usuario_creador VARCHAR(255) AFTER genero;
ALTER TABLE canciones ADD COLUMN fecha_agregado DATE AFTER usuario_creador;

UPDATE canciones SET artista = 'Queen', album = 'A Night at the Opera' WHERE nombre_cancion = 'Bohemian Rhapsody';
UPDATE canciones SET artista = 'Ed Sheeran', album = 'Divide' WHERE nombre_cancion = 'Shape of You';
UPDATE canciones SET artista = 'Luis Fonsi', album = 'Vida' WHERE nombre_cancion = 'Despacito';
UPDATE canciones SET artista = 'The Weeknd', album = 'After Hours' WHERE nombre_cancion = 'Blinding Lights';
UPDATE canciones SET artista = 'Eagles', album = 'Hotel California' WHERE nombre_cancion = 'Hotel California';
UPDATE canciones SET artista = 'Carlos Vives', album = 'La Bicicleta' WHERE nombre_cancion = 'La Bicicleta';
UPDATE canciones SET artista = 'Michael Jackson', album = 'Thriller' WHERE nombre_cancion = 'Billie Jean';
UPDATE canciones SET artista = 'Nirvana', album = 'Nevermind' WHERE nombre_cancion = 'Smells Like Teen Spirit';
UPDATE canciones SET artista = 'Marc Anthony', album = 'Opusion' WHERE nombre_cancion = 'Vivir Mi Vida';
UPDATE canciones SET artista = 'Luis Fonsi ft. Daddy Yankee', album = 'Vida' WHERE nombre_cancion = 'Despacito (Remix)';

