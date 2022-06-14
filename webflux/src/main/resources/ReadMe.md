> Webflux란?

- tomcat은 servlet 기반인 반면 Webflux는 netty를 쓴다(servlet에 의존하지 않아 가벼움.)
- 비동기 방식으로 Transaction 단위가 가벼운 상황에 적당하다.


> Mono란?

- Mono는 Webflux의 대표적인 반환 타입이다.
- 파라미터를 한번에 받아오고, 전달한다.
- Publisher 는 Subscriber가 구독하지 않으면 데이터를 전송하지 않는다.
- Mono로 return하면 그 때 Spring 자체에서 Subscribe 동작 시작.