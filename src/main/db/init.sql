-- Category 테이블 삭제 및 생성
DROP TABLE IF EXISTS category;

CREATE TABLE category (
    category_id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name                 VARCHAR(100) NOT NULL,
    category_ratio                DOUBLE PRECISION,
    category_score_sum_m          DOUBLE PRECISION,
    category_score_sum_y          DOUBLE PRECISION,
    category_score_square_sum_m   DOUBLE PRECISION,
    category_score_square_sum_y   DOUBLE PRECISION,
    category_count_m              BIGINT,
    category_count_y              BIGINT
);

-- 기본 카테고리 데이터 삽입
INSERT INTO category (category_name, category_ratio, category_score_sum_m, category_score_sum_y, category_score_square_sum_m, category_score_square_sum_y, category_count_m, category_count_y)
VALUES ('LQ', 33.3, 0, 0, 0, 0, 0, 0),
       ('RQ', 33.3, 0, 0, 0, 0, 0, 0),
       ('CQ', 33.3, 0, 0, 0, 0, 0, 0);


-- Activity 테이블 삭제 및 생성
DROP TABLE IF EXISTS activity;

CREATE TABLE activity (
    activity_id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id                 BIGINT NOT NULL,
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
-- LQ
INSERT INTO activity (category_id, activity_class, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '교육활동', '교내외의의 교육 활동(초/중/고/대 멘토링, 동료 티칭 등 학과장 승인건)', 0.2, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '교육활동', '교육조교 활동(학부생 TA, 학기당)', 0.5, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '교육성취도', '학점 4.0이상 4.5이하', 3.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '교육성취도', '학점 3.5이상 4.0미만', 2.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '교육성취도', '학점 3.0이상 3.5미만', 1.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '오픈소스SW활동', 'OS커뮤니티 생성 및 활성도 - 운영위 심사', 5.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '오픈소스SW활동', 'OS커뮤니티 생성 및 활성도 - 운영위 심사', 4.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '오픈소스SW활동', 'OS커뮤니티 생성 및 활성도 - 운영위 심사', 3.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '오픈소스SW활동', '커미터로서의 활동 - 운영위 심사', 5.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '오픈소스SW활동', '커미터로서의 활동 - 운영위 심사', 4.0, 0),
         ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), '오픈소스SW활동', '커미터로서의 활동 - 운영위 심사', 3.0, 0);

-- RQ
INSERT INTO activity (category_id, activity_class, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '학술지 논문 개재','SCI, SSCI, A&HCI 급 학술지 - JCR 상위 5% 이내 학술지(주저)', 10.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '학술지 논문 개재','SCI, SSCI, A&HCI 급 학술지 - JCR 상위 5% 이내 학술지 (공저)', 5.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '학술지 논문 개재','KCI 우수등재 학술지 - JCR 상위 10% 이내 학술지 (주저)', 5.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '학술지 논문 개재','KCI 우수등재 학술지 - JCR 상위 10% 이내 학술지 (공저)', 2.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '학술지 논문 개재','KCI 등재 - JCR 상위 20% 이내 학술지 (주저)', 3.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '학술지 논문 개재','KCI 후보, 기타국제 - JCR 상위 20% 이내 학술지 (공저)', 1.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 4) - 학술대회 구두발표', 10.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 4) - 포스터 발표', 5.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 3) - 학술대회 구두발표', 8.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 3) - 포스터 발표', 4.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 2) - 학술대회 구두발표', 6.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 2) - 포스터 발표', 3.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 1) - 학술대회 구두발표', 4.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '저명','국제학술대회 발표(BK 기준 1) - 포스터 발표', 2.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '공모전/ICPC','국제/대규모 공모전(ICPC, 공개SW개발자대회) - 대상', 10.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '공모전/ICPC','국제/대규모 공모전(ICPC, 공개SW개발자대회) - 입상', 4.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '공모전/ICPC','국제/대규모 공모전(ICPC, 공개SW개발자대회) - 참여', 2.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '공모전/ICPC','교내/지역 공모전 - 대상', 3.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '공모전/ICPC','교내/지역 공모전 - 입상', 1.0, 0),
         ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), '공모전/ICPC','교내/지역 공모전 - 참여', 0.5, 0);

-- CQ
INSERT INTO activity (category_id, activity_class, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '산학프로젝트','수행 완료, 피평가점수(A)', 7.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '산학프로젝트','수행 완료, 피평가점수(B)', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '산학프로젝트','수행 완료, 피평가점수(C)', 3.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '산학프로젝트','수행 완료: 2점, 피평가점수(그 외)', 2.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '인턴십','수행 완료, 피평가점수(A)', 7.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '인턴십','수행 완료, 피평가점수(B)', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '인턴십','수행 완료, 피평가점수(C)', 3.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '인턴십','수행 완료, 피평가점수(그 외)', 2.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '창업','수행 완료', 30.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '해외봉사','수행 완료, 피평가점수(A)', 15.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '해외봉사','수행 완료, 피평가점수(B)', 13.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '해외봉사','수행 완료, 피평가점수(C)', 11.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '해외봉사','수행 완료, 피평가점수(그 외)', 10.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '화상강연/세미나 참여','수행 완료', 1.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '알리미','회장', 20.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '알리미','부회장', 15.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '알리미','임원진', 10.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '알리미','참여', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '학생회','회장', 20.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '학생회','부회장', 15.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '학생회','임원진', 10.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '학생회','참여', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'SCG','회장', 10.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'SCG','부회장', 8.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'SCG','임원진', 6.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'SCG','참여', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '미디어홍보','회장', 10.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '미디어홍보','부회장', 8.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '미디어홍보','임원진', 6.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '미디어홍보','참여', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '스튜디오기여','참여', 2.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '스터디그룹','회장', 5.0, 0),
        ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), '스터디그룹','참여', 2.0, 0);

-- User 테이블 삭제 및 생성
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    submit_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                  BIGINT NOT NULL,
    activity_id              BIGINT NOT NULL,
    submit_date              TIMESTAMP,
    submit_content           TEXT,
    submit_title             VARCHAR(100),
    submit_state             INT DEFAULT 0, -- 0 : 미승인, 1 : 승인, 2 : 거부
    submit_approved_date     TIMESTAMP,

    CONSTRAINT fk_submit_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_submit_activity
        FOREIGN KEY (activity_id)
            REFERENCES activity (activity_id)
            ON DELETE CASCADE
);

CREATE TABLE comment (
    comment_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    submit_id   BIGINT NOT NULL,
    comment_content TEXT,
    comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment_state INT DEFAULT 0, -- 0 : 미승인, 1 : 승인, 2 : 거부

    CONSTRAINT fk_comment_submit
        FOREIGN KEY (submit_id)
            REFERENCES submit (submit_id)
            ON DELETE CASCADE
);

-- Score 테이블 삭제 및 생성
DROP TABLE IF EXISTS score;

CREATE TABLE score (
    score_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                 BIGINT NOT NULL,
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

-- 파일 저장 경로 테이블 삭제 및 생성
DROP TABLE IF EXISTS file_storage;
CREATE TABLE file_storage (
    file_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    submit_id                 BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    
    file_type VARCHAR(100) NOT NULL, -- 파일 형식 (예: image/png, application/pdf)
    file_data LONGBLOB NOT NULL,

    CONSTRAINT fk_file_storage_submit
       FOREIGN KEY (submit_id)
           REFERENCES submit (submit_id)
           ON DELETE CASCADE
);