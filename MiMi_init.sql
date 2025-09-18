-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS mimidaily DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mimidaily;

-- profiles 테이블
CREATE TABLE profiles (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    ofile VARCHAR(200), -- original file name
    sfile VARCHAR(200), -- saved file name
    file_path VARCHAR(300),
    file_size BIGINT,
    file_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- members 테이블
CREATE TABLE members (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    pwd VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    tel VARCHAR(20),
    birth VARCHAR(10),
    gender VARCHAR(10),
    role INT DEFAULT 0,
    marketing BOOLEAN DEFAULT FALSE,
    profiles_idx INT,
    visitcnt INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_email (email),
    FOREIGN KEY (profiles_idx) REFERENCES profiles(idx)
);

-- thumbnails 테이블
CREATE TABLE thumbnails (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    ofile VARCHAR(200),
    sfile VARCHAR(200),
    file_path VARCHAR(300),
    file_size BIGINT,
    file_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- articles 테이블
CREATE TABLE articles (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    category INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    visitcnt INT DEFAULT 0,
    members_id VARCHAR(50) NOT NULL,
    thumbnails_idx INT,
    FOREIGN KEY (members_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (thumbnails_idx) REFERENCES thumbnails(idx) 
);

-- comments 테이블
CREATE TABLE comments (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    context TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    members_id VARCHAR(50) NOT NULL,
    articles_idx INT NOT NULL,
    FOREIGN KEY (members_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (articles_idx) REFERENCES articles(idx) ON DELETE CASCADE
);

-- hashtags 테이블
CREATE TABLE hashtags (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- hashtags_articles(교차 테이블)
CREATE TABLE hashtags_articles (
    hashtags_idx INT NOT NULL,
    articles_idx INT NOT NULL,
    PRIMARY KEY (hashtags_idx, articles_idx),
    FOREIGN KEY (hashtags_idx) REFERENCES hashtags(idx) ON DELETE CASCADE,
    FOREIGN KEY (articles_idx) REFERENCES articles(idx) ON DELETE CASCADE
);

-- likes 테이블
CREATE TABLE likes (
    members_id VARCHAR(50) NOT NULL,
    articles_idx INT NOT NULL,
    PRIMARY KEY (members_id, articles_idx),
    FOREIGN KEY (members_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (articles_idx) REFERENCES articles(idx) ON DELETE CASCADE
);

-- 인덱스 추가 (자주 사용되는 검색 조건)
CREATE INDEX idx_articles_category ON articles(category);
CREATE INDEX idx_articles_members_id ON articles(members_id);
CREATE INDEX idx_comments_articles_idx ON comments(articles_idx);
CREATE INDEX idx_comments_members_id ON comments(members_id);
CREATE INDEX idx_likes_articles_idx ON likes(articles_idx);

-- 초기 관리자 계정 예시
INSERT INTO members (id, pwd, name, email, role, marketing) VALUES ('admin', 'adminpwd', '관리자', 'admin@mimidaily.com', 2, TRUE);