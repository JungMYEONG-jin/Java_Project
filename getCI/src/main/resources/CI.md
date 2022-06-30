> kakao CI 얻기
> 
> CI 수집 인가를 받아도 sign up API를 통해 사용자 연결을 완료시켜야한다.
> 만약 연결을 완료시키지 않는다면 CI 값을 받을 수 없다.

sign up API 호출을 완료하지 않으면 기본적으로 제공하는 항목은 다음과 같다.

```shell
Name	Key
회원번호	id
고유 ID	uuid
프로필 정보(닉네임/프로필 사진)	kakao_account.profile
연결 여부	has_signed_up
연결 시각	connected_at
카카오계정(이메일)	kakao_account.email
유효 이메일 여부	is_email_valid
이메일 인증 여부	is_email_verified
```
따라서 CI 값을 얻기 위해서는 API 호출을 완료하거나 앱 설정 내에서 자동연결 true로 설정해주자.

```shell
POST /v1/user/signup HTTP/1.1
Host: kapi.kakao.com
Authorization: Bearer ${ACCESS_TOKEN}
Content-type: application/x-www-form-urlencoded;charset=utf-8
```

또는 내 애플리케이션 -> 카카오 로그인 -> 맨 아래 자동연결 설정여부 true로 해주기

