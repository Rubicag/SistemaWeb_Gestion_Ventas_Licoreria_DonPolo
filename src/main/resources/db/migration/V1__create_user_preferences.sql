-- Flyway migration: crea tabla user_preferences si no existe
CREATE TABLE IF NOT EXISTS user_preferences (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100),
  pref_key VARCHAR(150),
  pref_value TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
