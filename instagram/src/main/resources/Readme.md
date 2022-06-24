> 처음 세팅하기

- h2 연결할때 처음에 로컬에 만들고 그다음 remote 연결 가능.

```shell
jdbc:h2:~/instagram 으로 연결
그 후 jdbc:h2:tcp://localhost/~/instagram 으로 접속
```

- spring data jpa는 application.properties에서 접속 정보 세팅 가능.
```shell
spring.datasource.url=jdbc:h2:tcp://localhost/~/datajpa
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.show_sql=true
spring.jpa.properties.format_sql=true

logging.level.org.hibernate.SQL=debug
```