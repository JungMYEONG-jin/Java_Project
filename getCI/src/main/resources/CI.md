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





>Apple App store Connect API 사용하기



Apple App store Connect API를 사용하려면 private key를 발급 받아야 한다.
이는 1번 다운로드가 가능하여 잘 보관해야함..
이 키를 이용해 JWT를 생성해 API 호출마다 같이 보내줘야한다. 그래야
올바른 사용자로 인식하여 정상적 호출이 가능하다.
키 발급 방법은 app store connect 사이트 접속후
user > key > generate key 해서 키 생성하면 됨.

이제 JWT Header를 만들어 보자.
Header Field는 총 3개로 나뉜다. 
- Encryption Algorithm : 무슨 알고리즘 쓸건지. apple에서 무조건 ES256으로 사용하라함.
- Key ID : 키 발급 받으면 개인키 ID 확인 가능.
- Token Type : type JWT 로 세팅

이제 토큰에 실어보낼 payload 정보를 만들어야한다.
payload는 다음과 같은 필드로 구성된다.
- Issuer ID : 이것도 key 발급 받은 페이지에서 확인 가능. issuer id라고 나타남. xxxxxxxx-xxx-xxx-xxxx이런 형식
- Issued at Time : token 생성 시간. now 로 해주면 됨.
- Expiration Time : apple에서 토큰 유효시간은 최대 20분이라고 지정함. 실제로 20분 이상 세팅했더니 응답 거부 당함.
- Audience : appstoreconnect-v1으로 세팅
- Token Scope : token 범위 지정 기능임. Optional 이라 안해도 됨.

```json
{
    "iss": "57246542-96fe-1a63-e053-0824d011072a",
    "iat": 1528407600,
    "exp": 1528408800,
    "aud": "appstoreconnect-v1",
    "scope": [
        "GET /v1/apps?filter[platform]=IOS"
    ]
}
```
apple 공식 사이트에서 확인하면 대략 이런식으로 payload를 작성하면 된다함.
아래는 실제 작성한 JWT 토큰 작성법.

```java
public String createJWT( ){
        JWSHeader header=new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).type(JOSEObjectType.JWT).build();

        JWTClaimsSet claimsSet=new JWTClaimsSet();
        Date now=new Date();
        claimsSet.setIssuer(issuer_Id);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime()+900000)); // exp 15 minutes
        claimsSet.setAudience("appstoreconnect-v1");

        SignedJWT jwt=new SignedJWT(header,claimsSet);

        try{
        ECPrivateKey ecPrivateKey=new ECPrivateKeyImpl(readPrivateKey(keyPath));
        JWSSigner jwsSigner=new ECDSASigner(ecPrivateKey.getS());
        jwt.sign(jwsSigner);

        }catch(InvalidKeyException e)
        {
        e.printStackTrace();
        }catch(JOSEException e)
        {
        e.printStackTrace();
        }
        
        return jwt.serialize();
}



private byte[] readPrivateKey(String keyPath){
        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try(FileReader keyReader = new FileReader(resource.getFile());
        PemReader pemReader = new PemReader(keyReader))
        {
        PemObject pemObject = pemReader.readPemObject();
        content = pemObject.getContent();

        }catch(IOException e)
        {
        e.printStackTrace();
        }
        return content;
}
```

필요한 라이브러리

```java
implementation 'com.nimbusds:nimbus-jose-jwt:3.10'
```

실제로 API 호출 부분도 필요하다면 댓글 남겨주세요.
