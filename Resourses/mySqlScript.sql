-- xóa database nếu chưa tồn tại
DROP DATABASE IF EXISTS notebook;
-- tạo database nếu chưa tồn tại
CREATE DATABASE IF NOT EXISTS notebook;
USE notebook;

-- -- tạo bảng
CREATE TABLE NoteType 
(
	TypeId INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    TypeName TEXT NOT NULL
);

CREATE TABLE Note 
(
	NoteId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Title text DEFAULT("NO TITLE"),
    DateCreate DATE NOT NULL DEFAULT(CURDATE()), 
    `Password` VARCHAR(18) CHARACTER SET UTF8MB4 DEFAULT(""),
    TypeId  INT,
    pin TINYINT DEFAULT(0),
	FOREIGN KEY (TypeId) REFERENCES NoteType(TypeId) -- khóa ngoại
);

CREATE TABLE Content 
(
	ContentId INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `Text` MEDIUMBLOB DEFAULT(""),
    NoteId  INT,
    FOREIGN KEY (NoteId) REFERENCES Note(NoteId) -- khóa ngoại
);

CREATE TABLE Photo
(
	PhotoId INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `Data` MEDIUMBLOB,
    NoteId  INT,
    FOREIGN KEY (NoteId) REFERENCES Note(NoteId) -- khóa ngoại
);

CREATE TABLE TodoList 
(
	TodoListId  INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Item TEXT,
    `Check` TINYINT,
    NoteId INT,
    FOREIGN KEY (NoteId) REFERENCES Note(NoteId) -- khóa ngoại
);

CREATE TABLE ThemeMode(
	id TINYINT PRIMARY KEY,
	dark_mode TINYINT
);
-- mode mặc định - 0: light mode
INSERT INTO ThemeMode VALUES(1,0);
-- notetype mặc định
INSERT INTO notetype VALUES (1,'DEFAULT');