CREATE TABLE profiles (
    idx        NUMBER PRIMARY KEY,
    image_data BLOB NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE hashtags (
    id   VARCHAR2(20) PRIMARY KEY,
    name VARCHAR2(20) NOT NULL
);

CREATE TABLE thumbnails (
    idx        NUMBER PRIMARY KEY,
    image_data BLOB NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE categories (
    idx         NUMBER PRIMARY KEY,
    name        VARCHAR2(20) NOT NULL,
    description VARCHAR2(100)
);

CREATE TABLE members (
    id           VARCHAR2(20) PRIMARY KEY,
    pwd          VARCHAR2(50) NOT NULL,
    name         VARCHAR2(20) NOT NULL,
    birth        CHAR(6),
    gender       CHAR(1), -- 'm'Àº ³²¼º 'f'´Â ¿©¼º
    email        VARCHAR2(80),
    tel          VARCHAR2(20),
    marketing    CHAR(1) DEFAULT '0', -- 0 ¶Ç´Â 1·Î Ç¥Çö
    role         CHAR(1) DEFAULT '1', -- 1 ÀÏ¹ÝÈ¸¿ø / 2 ±âÀÚ / 0 °ü¸®ÀÚ
    profiles_idx NUMBER,
    created_at   TIMESTAMP NOT NULL,
    CONSTRAINT members_profiles_fk FOREIGN KEY (profiles_idx) REFERENCES profiles (idx)
);

CREATE TABLE articles (
    idx           NUMBER PRIMARY KEY, -- °Ô½Ã±Û ¹øÈ£
    title         VARCHAR2(100) NOT NULL,
    content       CLOB NOT NULL,
    category      NUMBER NOT NULL, -- Ä«Å×°í¸® ID
    created_at    TIMESTAMP NOT NULL,
    visitcnt      NUMBER NOT NULL,
    members_id    VARCHAR2(20) NOT NULL,
    thumnails_idx NUMBER,
    CONSTRAINT articles_members_fk FOREIGN KEY (members_id) REFERENCES members (id),
    CONSTRAINT articles_categories_fk FOREIGN KEY (category) REFERENCES categories (idx),
    CONSTRAINT articles_thumbnails_fk FOREIGN KEY (thumnails_idx) REFERENCES thumbnails (idx)
);

CREATE TABLE comments (
    idx          NUMBER PRIMARY KEY,
    context      VARCHAR2(500) NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    members_id   VARCHAR2(20) NOT NULL,
    articles_idx NUMBER NOT NULL,
    CONSTRAINT comments_articles_fk FOREIGN KEY (articles_idx) REFERENCES articles (idx),
    CONSTRAINT comments_members_fk FOREIGN KEY (members_id) REFERENCES members (id)
);

CREATE TABLE hashtags_articles (
    hashtags_id  VARCHAR2(20) NOT NULL,
    articles_idx NUMBER NOT NULL,
    CONSTRAINT hashtags_articles_pk PRIMARY KEY (hashtags_id, articles_idx),
    CONSTRAINT hashtags_articles_articles_fk FOREIGN KEY (articles_idx) REFERENCES articles (idx),
    CONSTRAINT hashtags_articles_hashtags_fk FOREIGN KEY (hashtags_id) REFERENCES hashtags (id)
);

CREATE TABLE likes (
    members_id   VARCHAR2(20) NOT NULL,
    articles_idx NUMBER NOT NULL,
    CONSTRAINT likes_pk PRIMARY KEY (members_id, articles_idx),
    CONSTRAINT likes_articles_fk FOREIGN KEY (articles_idx) REFERENCES articles (idx),
    CONSTRAINT likes_members_fk FOREIGN KEY (members_id) REFERENCES members (id)
);

-- ½ÃÄö½º »ý¼º
CREATE SEQUENCE profiles_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE hashtags_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE thumbnails_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE members_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE articles_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE categories_seq START WITH 1 INCREMENT BY 1;

-- °ü°èÇü Å×ÀÌºí(º¹ÇÕÅ°) ½ÃÄö½º»ç¿ëX
--CREATE SEQUENCE hashtags_articles_seq START WITH 1 INCREMENT BY 1;
--CREATE SEQUENCE likes_seq START WITH 1 INCREMENT BY 1;

-- Å×ÀÌºí Á¦°Å
--DROP TABLE hashtags_articles CASCADE CONSTRAINTS;
--DROP TABLE likes CASCADE CONSTRAINTS;
--DROP TABLE comments CASCADE CONSTRAINTS;
--DROP TABLE articles CASCADE CONSTRAINTS;
--DROP TABLE profiles CASCADE CONSTRAINTS;
--DROP TABLE thumbnails CASCADE CONSTRAINTS;
--DROP TABLE categories CASCADE CONSTRAINTS;
--DROP TABLE members CASCADE CONSTRAINTS;
--DROP TABLE hashtags CASCADE CONSTRAINTS;

COMMIT;

-- Ä«Å×°í¸® µ¥ÀÌÅÍ »ðÀÔ
INSERT INTO categories (idx, name, description) VALUES (1, '¿©Çà', '¿©Çà °ü·Ã Á¤º¸');
INSERT INTO categories (idx, name, description) VALUES (2, '¸ÀÁý', '¸ÀÁý °ü·Ã Á¤º¸');

-- ÇÁ·ÎÇÊ µ¥ÀÌÅÍ »ðÀÔ (no preferences) - update¿¡ Ãß°¡
-- INSERT INTO profiles (idx, image_data, created_at) VALUES (profiles_seq.NEXTVAL, EMPTY_BLOB(), SYSTIMESTAMP);

-- ¸â¹ö µ¥ÀÌÅÍ »ðÀÔ (ÇÁ·ÎÇÊX)
INSERT ALL
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('admin', '1234', '°ü¸®ÀÚ', '111111', 'm', 'admin@email.com', '010-0000-0000', '0', '0', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user1', 'pass1', 'È«±æµ¿', '123456', 'm', 'user1@example.com', '010-1111-1111', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user2', 'pass2', '±èÃ¶¼ö', '234567', 'm', 'user2@example.com', '010-2222-2222', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user3', 'pass3', 'ÀÌ¿µÈñ', '345678', 'f', 'user3@example.com', '010-3333-3333', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user4', 'pass4', '¹ÚÁö¹Î', '456789', 'f', 'user4@example.com', '010-4444-4444', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user5', 'pass5', 'ÃÖ¹Î¼ö', '567890', 'm', 'user5@example.com', '010-5555-5555', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user6', 'pass6', 'Á¤¿ì¼º', '678901', 'm', 'user6@example.com', '010-6666-6666', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user7', 'pass7', 'ÇÑÁö¹Î', '789012', 'f', 'user7@example.com', '010-7777-7777', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user8', 'pass8', 'ÀÌ¹ÎÈ£', '890123', 'm', 'user8@example.com', '010-8888-8888', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user9', 'pass9', '½Å¹Î¾Æ', '901234', 'f', 'user9@example.com', '010-9999-9999', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user10', 'pass10', '±èÅÂÈñ', '012345', 'f', 'user10@example.com', '010-0000-0000', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('journalist1', 'passj1', 'ÀÌ±âÀÚ', '850101', 'm', 'journalist1@example.com', '010-1111-2222', '0', '2', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('journalist2', 'passj2', '¹Ú±âÀÚ', '860101', 'f', 'journalist2@example.com', '010-2222-3333', '0', '2', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('journalist3', 'passj3', 'ÃÖ±âÀÚ', '870101', 'm', 'journalist3@example.com', '010-3333-4444', '0', '2', SYSTIMESTAMP)
SELECT * FROM dual;

-- ½æ³×ÀÏ µ¥ÀÌÅÍ »ðÀÔ (no preferences) - no image·Î ÀÛ¾÷
-- INSERT INTO thumbnails (idx, image_data, created_at) VALUES (thumbnails_seq.NEXTVAL, EMPTY_BLOB(), SYSTIMESTAMP);

-- °Ô½Ã±Û µ¥ÀÌÅÍ »ðÀÔ (°ü¸®ÀÚ¿Í ±âÀÚ¸¸, ½æ³×ÀÏX)
INSERT ALL
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (1, '¸ÀÁý Å½¹æ 1', '¸ÀÁý Å½¹æ ³»¿ë 1', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (2, '¸ÀÁý Å½¹æ 2', '¸ÀÁý Å½¹æ ³»¿ë 2', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (3, '¸ÀÁý Å½¹æ 3', '¸ÀÁý Å½¹æ ³»¿ë 3', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (4, '¸ÀÁý Å½¹æ 4', '¸ÀÁý Å½¹æ ³»¿ë 4', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (5, '¸ÀÁý Å½¹æ 5', '¸ÀÁý Å½¹æ ³»¿ë 5', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (6, '¸ÀÁý Å½¹æ 6', '¸ÀÁý Å½¹æ ³»¿ë 6', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (7, '¸ÀÁý Å½¹æ 7', '¸ÀÁý Å½¹æ ³»¿ë 7', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (8, '¸ÀÁý Å½¹æ 8', '¸ÀÁý Å½¹æ ³»¿ë 8', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (9, '¸ÀÁý Å½¹æ 9', '¸ÀÁý Å½¹æ ³»¿ë 9', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (10, '¸ÀÁý Å½¹æ 10', '¸ÀÁý Å½¹æ ³»¿ë 10', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (11, '¸ÀÁý Å½¹æ 11', '¸ÀÁý Å½¹æ ³»¿ë 11', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (12, '¸ÀÁý Å½¹æ 12', '¸ÀÁý Å½¹æ ³»¿ë 12', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (13, '¸ÀÁý Å½¹æ 13', '¸ÀÁý Å½¹æ ³»¿ë 13', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (14, '¸ÀÁý Å½¹æ 14', '¸ÀÁý Å½¹æ ³»¿ë 14', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (15, '¸ÀÁý Å½¹æ 15', '¸ÀÁý Å½¹æ ³»¿ë 15', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (16, '¸ÀÁý Å½¹æ 16', '¸ÀÁý Å½¹æ ³»¿ë 16', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (17, '¸ÀÁý Å½¹æ 17', '¸ÀÁý Å½¹æ ³»¿ë 17', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (18, '¸ÀÁý Å½¹æ 18', '¸ÀÁý Å½¹æ ³»¿ë 18', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (19, '¸ÀÁý Å½¹æ 19', '¸ÀÁý Å½¹æ ³»¿ë 19', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (20, '¸ÀÁý Å½¹æ 20', '¸ÀÁý Å½¹æ ³»¿ë 20', 2, SYSTIMESTAMP, 0, 'journalist3')
SELECT * FROM dual;


-- ´ñ±Û µ¥ÀÌÅÍ »ðÀÔ
INSERT ALL
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (1, '´ñ±Û ³»¿ë 1 for article 1', SYSTIMESTAMP, 'user1', 1)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (2, '´ñ±Û ³»¿ë 2 for article 1', SYSTIMESTAMP, 'user2', 1)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (3, '´ñ±Û ³»¿ë 3 for article 1', SYSTIMESTAMP, 'user3', 1)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (4, '´ñ±Û ³»¿ë 1 for article 2', SYSTIMESTAMP, 'user4', 2)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (5, '´ñ±Û ³»¿ë 2 for article 2', SYSTIMESTAMP, 'user5', 2)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (6, '´ñ±Û ³»¿ë 3 for article 2', SYSTIMESTAMP, 'user6', 2)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (7, '´ñ±Û ³»¿ë 1 for article 3', SYSTIMESTAMP, 'user7', 3)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (8, '´ñ±Û ³»¿ë 2 for article 3', SYSTIMESTAMP, 'user8', 3)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (9, '´ñ±Û ³»¿ë 1 for article 4', SYSTIMESTAMP, 'user9', 4)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (10, '´ñ±Û ³»¿ë 1 for article 5', SYSTIMESTAMP, 'user10', 5)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (11, '´ñ±Û ³»¿ë 1 for article 6', SYSTIMESTAMP, 'user1', 6)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (12, '´ñ±Û ³»¿ë 2 for article 6', SYSTIMESTAMP, 'user2', 6)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (13, '´ñ±Û ³»¿ë 1 for article 7', SYSTIMESTAMP, 'user3', 7)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (14, '´ñ±Û ³»¿ë 1 for article 8', SYSTIMESTAMP, 'user4', 8)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (15, '´ñ±Û ³»¿ë 1 for article 9', SYSTIMESTAMP, 'user5', 9)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (16, '´ñ±Û ³»¿ë 1 for article 10', SYSTIMESTAMP, 'user6', 10)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (17, '´ñ±Û ³»¿ë 1 for article 11', SYSTIMESTAMP, 'user7', 11)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (18, '´ñ±Û ³»¿ë 1 for article 12', SYSTIMESTAMP, 'user8', 12)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (19, '´ñ±Û ³»¿ë 1 for article 13', SYSTIMESTAMP, 'user9', 13)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (20, '´ñ±Û ³»¿ë 1 for article 14', SYSTIMESTAMP, 'user10', 14)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (21, '´ñ±Û ³»¿ë 1 for article 15', SYSTIMESTAMP, 'user1', 15)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (22, '´ñ±Û ³»¿ë 1 for article 16', SYSTIMESTAMP, 'user2', 16)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (23, '´ñ±Û ³»¿ë 1 for article 17', SYSTIMESTAMP, 'user3', 17)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (24, '´ñ±Û ³»¿ë 1 for article 18', SYSTIMESTAMP, 'user4', 18)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (25, '´ñ±Û ³»¿ë 1 for article 19', SYSTIMESTAMP, 'user5', 19)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (26, '´ñ±Û ³»¿ë 1 for article 20', SYSTIMESTAMP, 'user6', 20)
SELECT * FROM dual;

-- ÇØ½ÃÅÂ±× µ¥ÀÌÅÍ »ðÀÔ
INSERT ALL
    INTO hashtags (id, name) VALUES 
    ('tag1', '#¸ÀÁý')
    INTO hashtags (id, name) VALUES 
    ('tag2', '#¿©Çà')
    INTO hashtags (id, name) VALUES 
    ('tag3', '#ÃßÃµ')
    INTO hashtags (id, name) VALUES 
    ('tag4', '#ºÐÀ§±â')
    INTO hashtags (id, name) VALUES 
    ('tag5', '#°¡Á·')
    INTO hashtags (id, name) VALUES 
    ('tag6', '#µ¥ÀÌÆ®')
    INTO hashtags (id, name) VALUES 
    ('tag7', '#¼Ò¼È')
    INTO hashtags (id, name) VALUES 
    ('tag8', '#Æ¯º°ÇÑ³¯')
    INTO hashtags (id, name) VALUES 
    ('tag9', '#¸ÀÀÖ´Â')
    INTO hashtags (id, name) VALUES 
    ('tag10', '#Áñ°Å¿î½Ã°£')
SELECT * FROM dual;

-- ÇØ½ÃÅÂ±×¿Í °Ô½Ã±Û °ü°è µ¥ÀÌÅÍ »ðÀÔ
INSERT ALL
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag1', 1)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag1', 2)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag2', 1)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag2', 3)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag3', 2)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag3', 4)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag4', 5)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag4', 6)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag5', 7)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag5', 8)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag6', 9)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag6', 10)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag7', 11)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag7', 12)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag8', 13)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag8', 14)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag9', 15)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag9', 16)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag10', 17)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag10', 18)
    INTO hashtags_articles (hashtags_id, articles_idx) VALUES 
    ('tag1', 19)
SELECT * FROM dual;


-- ÁÁ¾Æ¿ä µ¥ÀÌÅÍ »ðÀÔ
INSERT ALL
    INTO likes (members_id, articles_idx) VALUES 
    ('user1', 1)
    INTO likes (members_id, articles_idx) VALUES 
    ('user2', 1)
    INTO likes (members_id, articles_idx) VALUES 
    ('user3', 2)
    INTO likes (members_id, articles_idx) VALUES 
    ('user4', 2)
    INTO likes (members_id, articles_idx) VALUES 
    ('user5', 3)
    INTO likes (members_id, articles_idx) VALUES 
    ('user1', 3)
    INTO likes (members_id, articles_idx) VALUES 
    ('user6', 4)
    INTO likes (members_id, articles_idx) VALUES 
    ('user7', 4)
    INTO likes (members_id, articles_idx) VALUES 
    ('user8', 5)
    INTO likes (members_id, articles_idx) VALUES 
    ('user9', 5)
    INTO likes (members_id, articles_idx) VALUES 
    ('user10', 6)
    INTO likes (members_id, articles_idx) VALUES 
    ('user1', 6)
    INTO likes (members_id, articles_idx) VALUES 
    ('user2', 7)
    INTO likes (members_id, articles_idx) VALUES 
    ('user3', 7)
    INTO likes (members_id, articles_idx) VALUES 
    ('user4', 8)
    INTO likes (members_id, articles_idx) VALUES 
    ('user5', 8)
    INTO likes (members_id, articles_idx) VALUES 
    ('user6', 9)
    INTO likes (members_id, articles_idx) VALUES 
    ('user7', 9)
    INTO likes (members_id, articles_idx) VALUES 
    ('user8', 10)
    INTO likes (members_id, articles_idx) VALUES 
    ('user9', 10)
    INTO likes (members_id, articles_idx) VALUES 
    ('user10', 10)
SELECT * FROM dual;

COMMIT;