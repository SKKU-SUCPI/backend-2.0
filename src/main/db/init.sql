-- Category 테이블 삭제 및 생성
DROP TABLE IF EXISTS category;

CREATE TABLE category (
    category_id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name                 VARCHAR(100) NOT NULL,
    category_ratio                DOUBLE PRECISION,
    category_score_sum_m          DOUBLE PRECISION,
    category_score_sum_y          DOUBLE PRECISION,
    category_score_variance_m    DOUBLE PRECISION,
    category_score_variance_y    DOUBLE PRECISION
);

-- 기본 카테고리 데이터 삽입
INSERT INTO category (category_name, category_ratio, category_score_sum_m, category_score_sum_y, category_score_variance_m, category_score_variance_y)
VALUES ('LQ', 33.3, 0, 0, 0, 0, 0),
       ('RQ', 33.3, 0, 0, 0, 0, 0),
       ('CQ', 33.3, 0, 0, 0, 0, 0);


-- Activity 테이블 삭제 및 생성
DROP TABLE IF EXISTS activity;

CREATE TABLE activity (
    activity_id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id                 BIGINT NOT NULL,
    activity_class              VARCHAR(100) NOT NULL,
    activity_name               TEXT,
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
INSERT INTO activity (category_id, activity_class, activity_name, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1), 'education', 'campus', '교내외의의 교육 활동', 0.2, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'education', 'TA','교육조교 활동(학부생 TA)', 0.5, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'achievement', 'grade40to45','학점 4.0 ~ 4.5', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'achievement', 'grade35to40','학점 3.5 ~ 4.0', 2.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'achievement', 'grade30to35','학점 3.0 ~ 3.5', 1.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'achievement', 'grade00to30', '학점 3.0 미만',0.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'opensourceContribute5','OS커뮤니티생성 및 활성도 : 5점', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'opensourceContribute4','OS커뮤니티생성 및 활성도 : 4점', 4.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'opensourceContribute3','OS커뮤니티생성 및 활성도 : 3점', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'opensourceContribute0','OS커뮤니티생성 및 활성도 : 3점미만', 0.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'commitStar5','커미터로서의 활동 : 5점', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'commitStar4','커미터로서의 활동 : 4점', 4.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'commitStar3','커미터로서의 활동 : 3점', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='LQ' LIMIT 1),'swActivity', 'commitStar0','커미터로서의 활동 : 0점', 0.0, 0);

-- RQ, 그 중에서 학술지 파트
INSERT INTO activity (category_id, activity_class, activity_name, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'jcr5Main','JCR 상위 5% 이내 학술지(주저)', 5.0, 1), -- 자연계열 학술지
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'jcr5Part','JCR 상위 5% 이내 학술지 (공저)', 4.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'jcr10Main','JCR 상위 10% 이내 학술지 (주저)', 4.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'jcr10Part','JCR 상위 10% 이내 학술지 (공저)', 3.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'jcr20Main','JCR 상위 20% 이내 학술지 (주저)', 3.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'jcr20Part','JCR 상위 20% 이내 학술지 (공저)', 2.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'kciOver','SCI, SSCI, A&HCI 급 학술지', 5.0, 2), -- 인문계 학술지
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'kciExcellent','KCI 우수 등재 학술지', 4.0, 2),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'kci','KCI 등재', 3.0, 2),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'journal', 'kciCandidate','KCI 후보, 기타 국제', 2.0, 2); 

-- RQ, 그 중에서 학술대회 파트
INSERT INTO activity (category_id, activity_class, activity_name, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'conference', 'knownSpeech1','저명 국제학술대회 구두 발표', 4.0, 1), -- 자연계열 학술대회
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'knownPoster','저명 국제학술대회 포스터 발표', 3.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'normalSpeech1','일반 국제학술대회 구두 발표', 3.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'normalPoster','일반 국제학술대회 포스터 발표', 2.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'nationalSpeech1','국내학술대회 구두 발표', 2.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'nationalPoster','국내학술대회 포스터 발표', 1.0, 1),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'knownSpeech2','저명 국제학술대회 발표(BK 기준)', 4.0, 2), -- 인문계 학술대회회
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'normalSpeech2','일반 국제학술대회 발표', 2.0, 2),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1),'conference', 'nationalSpeech2','국내학술대회 발표', 1.0, 2); 
       -- 자연계열과 인문계 중 항목이 비슷하게 중복되는 경우가 있어 knownSpeech1(자연이 domain 1), knownSpeech2(인문이 domain 2)이렇게 구분하긴 했으나, 너무 거추장스러운지추후 논의 필요.

-- RQ, 그 중에서 공모전/ICPC
INSERT INTO activity (category_id, activity_class, activity_name, activity_detail, activity_weight, activity_domain)
VALUES ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'contest', 'bigCompetitionTop','국제/대규모 공모전(ICPC, 공개SW개발자대회) 대상', 10.0, 0), -- 자연&인문계열 공모전/ICPC
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'contest', 'bigCompetitionWin','국제/대규모 공모전(ICPC, 공개SW개발자대회) 입상', 4.0, 0),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'contest', 'bigCompetitionPlay','국제/대규모 공모전(ICPC, 공개SW개발자대회) 참여', 2.0, 0),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'contest', 'nationalCompetitionTop','교내/지역 공모전 대상', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'contest', 'nationalCompetitionWin','교내/지역 공모전 입상', 1.0, 0),
       ((SELECT category_id FROM category WHERE category_name='RQ' LIMIT 1), 'contest', 'nationalCompetitionPlay','교내/지역 공모전 참여', 0.5, 0);

-- CQ
INSERT INTO activity (category_id, activity_class, activity_name, activity_detail, activity_weight, activity_domain) -- cq의 경우 activity name == activity detail 인 것들이 종종 있음
VALUES ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'coop', 'coop','산학프로젝트', 10.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'internship', 'internship','인턴십', 10.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'startup', 'startup','창업', 30.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'overseaVolunteer', 'overseaVolunteer','해외봉사', 10.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'seminar', 'seminar','화상강연 / 세미나 참여', 1.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'alimi', 'alimiLeader','알리미-회장', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'alimi', 'alimiVice','알리미-부회장', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'alimi', 'alimiMember','알리미-참여', 2.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'council', 'councilLeader','학생회-회장', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'council', 'councilVice','학생회-부회장', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'council', 'councilMember','학생회-참여', 1.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'reporter', 'reporterLeader','기자단-회장', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'reporter', 'reporterVice','기자단-부회장', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'reporter', 'reporterMember','기자단-참여', 1.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'studioContribution', 'studioContribution','스튜디오 기여 (ARS Electronica 작품 제공 등)', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'studyGroup', 'studyGroupLeader','SCG, MAV, 스꾸딩 등 - 회장', 5.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'studyGroup', 'studyGroupVice','SCG, MAV, 스꾸딩 등 - 부회장', 3.0, 0),
       ((SELECT category_id FROM category WHERE category_name='CQ' LIMIT 1), 'studyGroup', 'studyGroupMember','SCG, MAV, 스꾸딩 등 - 참여', 1.0, 0);

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
    submit_date              DATE,
    -- submit_file              VARCHAR(100), -- 야는 사라진 attribute인 것이여여
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

CREATE TABLE test_entity (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     title VARCHAR(255) NOT NULL,
     content TEXT NOT NULL
);