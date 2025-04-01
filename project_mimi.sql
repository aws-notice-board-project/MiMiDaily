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
    gender       CHAR(1), -- 'm'�� ���� 'f'�� ����
    email        VARCHAR2(80),
    tel          VARCHAR2(20),
    marketing    CHAR(1) DEFAULT '0', -- 0 �Ǵ� 1�� ǥ��
    role         CHAR(1) DEFAULT '1', -- 1 �Ϲ�ȸ�� / 2 ���� / 0 ������
    profiles_idx NUMBER,
    created_at   TIMESTAMP NOT NULL,
    CONSTRAINT members_profiles_fk FOREIGN KEY (profiles_idx) REFERENCES profiles (idx)
);

CREATE TABLE articles (
    idx           NUMBER PRIMARY KEY, -- �Խñ� ��ȣ
    title         VARCHAR2(100) NOT NULL,
    content       CLOB NOT NULL,
    category      NUMBER NOT NULL, -- ī�װ� ID
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

-- ������ ����
CREATE SEQUENCE profiles_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE hashtags_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE thumbnails_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE members_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE articles_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE categories_seq START WITH 1 INCREMENT BY 1;

-- ������ ���̺�(����Ű) ���������X
--CREATE SEQUENCE hashtags_articles_seq START WITH 1 INCREMENT BY 1;
--CREATE SEQUENCE likes_seq START WITH 1 INCREMENT BY 1;

-- ���̺� ����
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

-- ī�װ� ������ ����
INSERT INTO categories (idx, name, description) VALUES (1, '����', '���� ���� ����');
INSERT INTO categories (idx, name, description) VALUES (2, '����', '���� ���� ����');

-- ������ ������ ���� (no preferences) - update�� �߰�
-- INSERT INTO profiles (idx, image_data, created_at) VALUES (profiles_seq.NEXTVAL, EMPTY_BLOB(), SYSTIMESTAMP);

-- ��� ������ ���� (������X)
INSERT ALL
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('admin', '1234', '������', '111111', 'm', 'admin@email.com', '010-0000-0000', '0', '0', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user1', 'pass1', 'ȫ�浿', '123456', 'm', 'user1@example.com', '010-1111-1111', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user2', 'pass2', '��ö��', '234567', 'm', 'user2@example.com', '010-2222-2222', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user3', 'pass3', '�̿���', '345678', 'f', 'user3@example.com', '010-3333-3333', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user4', 'pass4', '������', '456789', 'f', 'user4@example.com', '010-4444-4444', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user5', 'pass5', '�ֹμ�', '567890', 'm', 'user5@example.com', '010-5555-5555', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user6', 'pass6', '���켺', '678901', 'm', 'user6@example.com', '010-6666-6666', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user7', 'pass7', '������', '789012', 'f', 'user7@example.com', '010-7777-7777', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user8', 'pass8', '�̹�ȣ', '890123', 'm', 'user8@example.com', '010-8888-8888', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user9', 'pass9', '�Źξ�', '901234', 'f', 'user9@example.com', '010-9999-9999', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('user10', 'pass10', '������', '012345', 'f', 'user10@example.com', '010-0000-0000', '0', '1', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('journalist1', 'passj1', '�̱���', '850101', 'm', 'journalist1@example.com', '010-1111-2222', '0', '2', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('journalist2', 'passj2', '�ڱ���', '860101', 'f', 'journalist2@example.com', '010-2222-3333', '0', '2', SYSTIMESTAMP)
    INTO members (id, pwd, name, birth, gender, email, tel, marketing, role, created_at) VALUES 
    ('journalist3', 'passj3', '�ֱ���', '870101', 'm', 'journalist3@example.com', '010-3333-4444', '0', '2', SYSTIMESTAMP)
SELECT * FROM dual;

-- ����� ������ ���� (no preferences) - no image�� �۾�
-- INSERT INTO thumbnails (idx, image_data, created_at) VALUES (thumbnails_seq.NEXTVAL, EMPTY_BLOB(), SYSTIMESTAMP);

-- �Խñ� ������ ���� (�����ڿ� ���ڸ�, �����X)
INSERT ALL
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (1, '���� Ž�� 1', '���� Ž�� ���� 1', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (2, '���� Ž�� 2', '���� Ž�� ���� 2', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (3, '���� Ž�� 3', '���� Ž�� ���� 3', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (4, '���� Ž�� 4', '���� Ž�� ���� 4', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (5, '���� Ž�� 5', '���� Ž�� ���� 5', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (6, '���� Ž�� 6', '���� Ž�� ���� 6', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (7, '���� Ž�� 7', '���� Ž�� ���� 7', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (8, '���� Ž�� 8', '���� Ž�� ���� 8', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (9, '���� Ž�� 9', '���� Ž�� ���� 9', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (10, '���� Ž�� 10', '���� Ž�� ���� 10', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (11, '���� Ž�� 11', '���� Ž�� ���� 11', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (12, '���� Ž�� 12', '���� Ž�� ���� 12', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (13, '���� Ž�� 13', '���� Ž�� ���� 13', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (14, '���� Ž�� 14', '���� Ž�� ���� 14', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (15, '���� Ž�� 15', '���� Ž�� ���� 15', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (16, '���� Ž�� 16', '���� Ž�� ���� 16', 2, SYSTIMESTAMP, 0, 'journalist3')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (17, '���� Ž�� 17', '���� Ž�� ���� 17', 1, SYSTIMESTAMP, 0, 'admin')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (18, '���� Ž�� 18', '���� Ž�� ���� 18', 2, SYSTIMESTAMP, 0, 'journalist1')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (19, '���� Ž�� 19', '���� Ž�� ���� 19', 1, SYSTIMESTAMP, 0, 'journalist2')
    INTO articles (idx, title, content, category, created_at, visitcnt, members_id) VALUES 
    (20, '���� Ž�� 20', '���� Ž�� ���� 20', 2, SYSTIMESTAMP, 0, 'journalist3')
SELECT * FROM dual;


-- ��� ������ ����
INSERT ALL
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (1, '��� ���� 1 for article 1', SYSTIMESTAMP, 'user1', 1)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (2, '��� ���� 2 for article 1', SYSTIMESTAMP, 'user2', 1)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (3, '��� ���� 3 for article 1', SYSTIMESTAMP, 'user3', 1)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (4, '��� ���� 1 for article 2', SYSTIMESTAMP, 'user4', 2)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (5, '��� ���� 2 for article 2', SYSTIMESTAMP, 'user5', 2)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (6, '��� ���� 3 for article 2', SYSTIMESTAMP, 'user6', 2)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (7, '��� ���� 1 for article 3', SYSTIMESTAMP, 'user7', 3)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (8, '��� ���� 2 for article 3', SYSTIMESTAMP, 'user8', 3)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (9, '��� ���� 1 for article 4', SYSTIMESTAMP, 'user9', 4)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (10, '��� ���� 1 for article 5', SYSTIMESTAMP, 'user10', 5)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (11, '��� ���� 1 for article 6', SYSTIMESTAMP, 'user1', 6)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (12, '��� ���� 2 for article 6', SYSTIMESTAMP, 'user2', 6)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (13, '��� ���� 1 for article 7', SYSTIMESTAMP, 'user3', 7)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (14, '��� ���� 1 for article 8', SYSTIMESTAMP, 'user4', 8)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (15, '��� ���� 1 for article 9', SYSTIMESTAMP, 'user5', 9)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (16, '��� ���� 1 for article 10', SYSTIMESTAMP, 'user6', 10)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (17, '��� ���� 1 for article 11', SYSTIMESTAMP, 'user7', 11)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (18, '��� ���� 1 for article 12', SYSTIMESTAMP, 'user8', 12)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (19, '��� ���� 1 for article 13', SYSTIMESTAMP, 'user9', 13)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (20, '��� ���� 1 for article 14', SYSTIMESTAMP, 'user10', 14)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (21, '��� ���� 1 for article 15', SYSTIMESTAMP, 'user1', 15)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (22, '��� ���� 1 for article 16', SYSTIMESTAMP, 'user2', 16)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (23, '��� ���� 1 for article 17', SYSTIMESTAMP, 'user3', 17)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (24, '��� ���� 1 for article 18', SYSTIMESTAMP, 'user4', 18)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (25, '��� ���� 1 for article 19', SYSTIMESTAMP, 'user5', 19)
    INTO comments (idx, context, created_at, members_id, articles_idx) VALUES 
    (26, '��� ���� 1 for article 20', SYSTIMESTAMP, 'user6', 20)
SELECT * FROM dual;

-- �ؽ��±� ������ ����
INSERT ALL
    INTO hashtags (id, name) VALUES 
    ('tag1', '#����')
    INTO hashtags (id, name) VALUES 
    ('tag2', '#����')
    INTO hashtags (id, name) VALUES 
    ('tag3', '#��õ')
    INTO hashtags (id, name) VALUES 
    ('tag4', '#������')
    INTO hashtags (id, name) VALUES 
    ('tag5', '#����')
    INTO hashtags (id, name) VALUES 
    ('tag6', '#����Ʈ')
    INTO hashtags (id, name) VALUES 
    ('tag7', '#�Ҽ�')
    INTO hashtags (id, name) VALUES 
    ('tag8', '#Ư���ѳ�')
    INTO hashtags (id, name) VALUES 
    ('tag9', '#���ִ�')
    INTO hashtags (id, name) VALUES 
    ('tag10', '#��ſ�ð�')
SELECT * FROM dual;

-- �ؽ��±׿� �Խñ� ���� ������ ����
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


-- ���ƿ� ������ ����
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