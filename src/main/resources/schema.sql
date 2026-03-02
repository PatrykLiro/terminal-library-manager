-- users
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL
);

-- books
CREATE TABLE IF NOT EXISTS books (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  isbn VARCHAR(50) UNIQUE NOT NULL,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  available BOOLEAN DEFAULT TRUE
);

-- categories
CREATE TABLE IF NOT EXISTS categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

-- join table book_categories
CREATE TABLE IF NOT EXISTS book_categories (
  book_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  PRIMARY KEY (book_id, category_id),
  FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- loans
CREATE TABLE IF NOT EXISTS loans (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  return_date TIMESTAMP NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Sample data (plaintext passwords will be hashed on init)
MERGE INTO users (username, password_hash, role) KEY(username) VALUES ('admin', 'admin', 'ADMIN');
MERGE INTO users (username, password_hash, role) KEY(username) VALUES ('user', 'user', 'USER');

MERGE INTO books (isbn, title, author, available) KEY(isbn) VALUES ('9780140444308', 'Pan Tadeusz', 'Adam Mickiewicz', TRUE);
MERGE INTO books (isbn, title, author, available) KEY(isbn) VALUES ('9788372320295', 'Lalka', 'Bolesław Prus', TRUE);
MERGE INTO books (isbn, title, author, available) KEY(isbn) VALUES ('9788373273894', 'Zbrodnia i kara', 'Fiodor Dostojewski', TRUE);

MERGE INTO categories (name) KEY(name) VALUES ('Fantasy');
MERGE INTO categories (name) KEY(name) VALUES ('Kryminał');
