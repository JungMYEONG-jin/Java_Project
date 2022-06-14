> Webflux란?

- tomcat은 servlet 기반인 반면 Webflux는 netty를 쓴다(servlet에 의존하지 않아 가벼움.)
- 비동기 방식으로 Transaction 단위가 가벼운 상황에 적당하다.


> Mono란?

- Mono는 Webflux의 대표적인 반환 타입이다.
- 파라미터를 한번에 받아오고, 전달한다.
- Publisher 는 Subscriber가 구독하지 않으면 데이터를 전송하지 않는다.
- Mono로 return하면 그 때 Spring 자체에서 Subscribe 동작 시작.


> Intellij Vue 띄우기

- intellij terminal 화면 접근
- 아래 명령어 입력

```shell
npm install -g @vue/cli

// vue 버전 확인
vue --version

// project 생성
vue create project명

cd 프로젝트명
npm run serve


// vue.config.js 설정 하는 경우

module.exports = {
  outputDir: "../src/main/resources/static",  // 빌드 타겟 디렉토리
  devServer: {
    proxy: {
      '/api': {
        // '/api' 로 들어오면 포트 8081(스프링 서버)로 보낸다
        target: 'http://localhost:8081',
        changeOrigin: true // cross origin 허용
      }
    }
  }
};




```