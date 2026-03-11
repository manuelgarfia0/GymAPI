-- Script de inicialización de datos para testing
-- Este archivo se ejecuta automáticamente al iniciar la aplicación

-- Insertar usuario de prueba si no existe
-- Contraseña: mypassword123 (hasheada con BCrypt)
INSERT INTO users (username, email, password, is_premium, language_preference, public_profile, created_at)
SELECT 'manuel', 'manuel@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', false, 'en', true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'manuel' OR email = 'manuel@example.com'
);

-- Insertar usuario adicional para testing
INSERT INTO users (username, email, password, is_premium, language_preference, public_profile, created_at)
SELECT 'testuser', 'test@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', false, 'en', true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'testuser' OR email = 'test@example.com'
);