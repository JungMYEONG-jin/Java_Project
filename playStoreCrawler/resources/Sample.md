# Google Play Developer API 가이드

Google Play Developer API는 API key를 이용하는 방식과 OAuth 인증을 통해 권한을 획득하는 방식이 있습니다.
본 가이드라인은 OAuth 인증을 이용한 샘플입니다.
1. 사용자는 인증 코드를 획득합니다.
2. 인증 코드를 다시 google에 보내 유효한지 검증받습니다.
3. 유효한 사용자라면 google에서 redirect_uri에 code를 보내줍니다.
4. 해당 코드를 가지고 다시 google에 보내 access_token과 refresh_token을 획득합니다.
5. refresh_token을 얻게 되면 위와 같은 인증 과정을 거치지 않고 access_token을 계속 획득 가능합니다.
6. token을 api 요청시 보내 인증을 받고 값을 return 받으면 완료입니다.

사용자는 인증 코드를 획득하고 그 인증코드를 다시 보내 구글에서는 그 코드를 가지고 해당 유저의 정보가 유효한지 확인합니다. 그리고 유효하다면 redirect_uri뒤에 code=값 형태로 return 하게 됩니다.
인증 코드를 획득하려면 해당 주소에 parameter를 채워 GET 호출을 해야합니다. 만약 자체적으로 운영중인 로그인 페이지가 있다면 redirect_uri를 저에게 회신해주시면 계정 생성시 해당 uri를 넣어드리겠습니다.
scopee는 Google의 여러 API중 저희는 play store와 관련된 API만 호출할 것이므로 해당 scope{scope=https://www.googleapis.com/auth/androidpublisher}를 그대로 사용하시면 됩니다.

https://accounts.google.com/o/oauth2/auth?scope=https://www.googleapis.com/auth/androidpublisher&response_type=code&access_type=offline&redirect_uri={redirect_uri}&client_id={client_id}

GET 하는 방법은 java code로 구현해도 되고 처음 한번만 필요한 과정이기 때문에 PostMan을 이용해 획득해도 됩니다. 단 java code를 이용해 구현하려면 redirect_uri 페이지를 생성하고 연결해줘야 합니다.
이 과정이 귀찮다면 인터넷 PC에서 PostMan을 이용해 token을 얻었습니다. (refresh_token을 한번만 얻으면 더 이상 해당 과정은 필요 없기 때문에 구현은 자유입니다.)

위 주소에 client_json의 값을 채워서 GET 하면 아래와 같이 본인에게 맞는 Code가 생성됩니다.
제 경우 4/0AdQt8qhBtjt-Q4UWzEP2XH1GSnQx0aQm4fm5mTqAOUC-IsNBppln_rnrYj847zRbv6XP6A 입니다.

이제 이 code를 이용해 access_token을 얻어야 합니다.
