-- Category 테이블 삭제 및 생성
DROP TABLE IF EXISTS category;

CREATE TABLE category (
    category_id                 INT AUTO_INCREMENT PRIMARY KEY,
    category_name               VARCHAR(100) NOT NULL,
    category_ratio              DOUBLE PRECISION,
    category_score_sum          DOUBLE PRECISION,
    category_score_deviation    DOUBLE PRECISION
);

-- 기본 카테고리 데이터 삽입
INSERT INTO category (category_name, category_ratio, category_score_sum, category_score_deviation)
VALUES ('LQ', 33.3, 0, 0),
       ('RQ', 33.3, 0, 0),
       ('CQ', 33.3, 0, 0);




-- Activity 테이블 삭제 및 생성
DROP TABLE IF EXISTS activity;

CREATE TABLE activity (
    activity_id                 INT AUTO_INCREMENT PRIMARY KEY,
    category_id                 INT NOT NULL,
    activity_class              VARCHAR(100) NOT NULL,
    activity_detail             TEXT,
    activity_weight             DOUBLE PRECISION,
    activity_domain             INT,

    CONSTRAINT fk_activity_category
        FOREIGN KEY (category_id)
            REFERENCES category (category_id)
            ON DELETE CASCADE
);

-- 기본 activity들 삽입
INSERT INTO activity (category_id, activity_class, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), 'education', 'campus', 0.2, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'education', 'TA', 0.5, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'education', 'gradeH', 3, 0);
       


-- User 테이블 삭제 및 생성
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id                   INT AUTO_INCREMENT PRIMARY KEY,
    user_name                 VARCHAR(100) NOT NULL,
    user_role                 VARCHAR(50) DEFAULT 'student',  -- 'student', 'admin', 'super_admin'
    user_hakbun               VARCHAR(20),
    user_hakgwa_cd            FLOAT DEFAULT 0,
    user_year                 DOUBLE PRECISION,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Submit 테이블 삭제 및 생성
DROP TABLE IF EXISTS submit;

CREATE TABLE submit (
    submit_id                INT AUTO_INCREMENT PRIMARY KEY,
    user_id                  INT NOT NULL,
    activity_id              INT NOT NULL,
    submit_date              DATE,
    submit_file              VARCHAR(100),
    submit_state             INT DEFAULT 0, -- 0 : 미승인, 1 : 승인, 2 : 거부
    submit_approved_date     TIMESTAMP,
    submit_content           TEXT,

    CONSTRAINT fk_submit_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_submit_activity
        FOREIGN KEY (activity_id)
            REFERENCES activity (activity_id)
            ON DELETE CASCADE
);

-- Score 테이블 삭제 및 생성
DROP TABLE IF EXISTS score;

CREATE TABLE score (
    score_id                INT AUTO_INCREMENT PRIMARY KEY,
    user_id                 INT NOT NULL,
    lq_score                DOUBLE PRECISION NOT NULL  DEFAULT 0,
    rq_score                DOUBLE PRECISION NOT NULL  DEFAULT 0,
    cq_score                DOUBLE PRECISION NOT NULL  DEFAULT 0,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_score_user
       FOREIGN KEY (user_id)
           REFERENCES users (user_id)
           ON DELETE CASCADE
);

CREATE TABLE test_entity (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     title VARCHAR(255) NOT NULL,
     content TEXT NOT NULL
);