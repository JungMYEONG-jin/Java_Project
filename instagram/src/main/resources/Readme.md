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

> @Transient
- 해당 어노테이션은 영속성 관리에서 제외하고 싶을때 사용한다.
- 메서드에 사용하지말고 필드에 사용하자.
- 영속성에서 제외되기 때문에 신중한 사용이 필요함.


> @NotBlank, @NotEmpty, @NotNull의 차이점
- @NotBlank : 빈 값, null, 빈 공백(스페이스)를 체크한다.
- @NotEmpty : 빈 값, null을 체크한다.
- @NotNull : null을 체크한다.


> @Around
- @Around는 Spring AOP중 하나다.
- 클라이언트 호출을 가로채서 실행이 된다. 따라서 종료후에 꼭 proceed를 실행해 넘겨줘야한다.
- 그렇지 않다면 클라이언트 호출을 가로챈 이후로 프로그램은 작동하지 않게 된다.

