> MySQL 설치법

```shell
brew install mysql

mysql.server start

# 기본 설정 시작
mysql_secure_installation

# 로그인
mysql -u {id} -p

# 서버 계속 기동
brew services start mysql

# 기본 명령어
show tables;
create database {name};
use {database_name};
desc {table_name};
```


## CascadeType.REMOVE VS onDelete
두 개의 차이점은 JPA냐 DDL 이냐의 차이인듯하다.
CascadeType.REMOVE를 사용하면
연관된 건수만큼 쿼리가 실행된다.
즉 n개면 쿼리가 n번 실행됨. 하지만 onDelete 쿼리의 경우 한번만 실행이됨.
그리고 onDelete는 스키마상 제약조건이 들어감.

