FROM mysql:8.0

# MySQL 환경 변수 설정
ENV MYSQL_ROOT_PASSWORD=1234 \
    MYSQL_DATABASE=sucpi-db

# MySQL 기본 포트 노출
EXPOSE 3306

COPY my.cnf /etc/mysql/conf.d/my.cnf

# 초기화 SQL 스크립트 복사
COPY init.sql /docker-entrypoint-initdb.d/
COPY student_dummy.sql /docker-entrypoint-initdb.d/