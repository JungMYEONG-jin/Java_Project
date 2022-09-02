> Selenium 서버 환경에서 구동하기

우선 크롬을 설치해야한다.

``` shell
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo apt install ./google-chrome-stable_current_amd64.deb

-- version check
google-chrome --version

```
크롬을 설치후 해당 버전에 맞는 chromedriver를 찾아서 설치해주자.

```shell
sudo mv chromedriver /usr/bin/chromedriver 
sudo chown root:root /usr/bin/chromedriver 
sudo chmod +x /usr/bin/chromedriver
```
chromedriver를 받은 다음 해당 권한 부여.

크롬 설치가 끝난 후 selenium jar 파일을 받아야 한다. 공식 사이트 경로 wget으로 다운받기.

설치가 종료된후 크롬을 selenium server를 통해 구동하기 위해

```shell
xvfb-run java -Dwebdriver.chrome.driver=/usr/bin/chromedriver -jar selenium-server-standalone.jar
```

이제 컴파일 후 실행하면 된다. 하지만 CLASSPATH에 jar 경로를 추가적으로 등록해줘야한다.

```shell
sudo vim ~/.bashrc

-- 해당 문구 추가

export CLASSPATH = ".:$JAVA_HOME/lib:/usr/home/ubuntu/driver/selenium.jar"

-- 그리고 적용시켜주기

source ~/.bashrc

javac a.java
java a


-- kill all chromedriver

kill $(ps aux | grep chromedriver | grep -v grep | awk '{print $2}')
```



## 구글 API 사용하기
> review api 사용하는 글들이 없어 하루내내 삽질을 했다.. 사실 가이드 문서를 들어가도 뭐 어떻게 하라는건지 감이 잘 안잡히기도 했고...
> 나중에 또 삽질하기전에 글을 기록해놓습니다. 우선 구글 API를 사용하려면 OAUTH 클라이언트 계정을 생성해야한다. 
> 주소 https://play.google.com/apps/publish/#ApiAccessPlace
> 계정 생성시 redirect_uri를 쓰라고 한다. 만약 스프링 부트에 스프링 시큐리티를 쓴다면 https://localhost:8080/login/oauth2/code 이거나 http://localhost:8080/login/oauth2/code로 잡히므로 둘다 넣거나 하나만 넣어준다.
> 나의 경우 오직 자바에서 api review만 사용하기 위해 주소는 대충 했음. 
> 이렇게 다 완료하면 계정 정보를 json으로 다운 받을 수 있다. 해당 파일을 다운로드 받자.
> 한 30퍼정도 다 완료되었다. api를 사용하려면 access_token을 얻어야 한다. 하지만 그전에 코드를 얻어야 토큰을 넘겨받을 수 있다.

https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/androidpublisher&access_type=offline&redirect_uri={redirect_uri}&response_type=code&client_id={client_id}

해당 url에 존재하는 redirect_uri, client_id 값에 사용자 계정을 생성해서 얻은 json에서 값을 읽어와 채워야 한다.

채운 값을 browser 주소창에 옮기고 이동하거나 만약 서버 환경을 운영중이라면 controller를 이용해 해당 주소로 가게 하자. 그러면 oauth 인증 화면이 나온다.
인증을 다하고 나하면 redirect_uri에 넣었던 주소로 돌아오며 redirect_uri?code=xxxxxxxxxxxxxxxxxxx 이런식으로 주소창에 값이 나온다. 이 code가 access_token을 얻기 위해 필요한 값이다.
```shell
POST https://www.googleapis.com/oauth2/v4/token
code=xxxxxxxxxxxxxxxxxxx
client_id=your_client_id
client_secret=your_client_secret
redirect_uri=redirect_uri
grant_type=authorization_code
```
해당 주소에 아래 값들을 form-data로 넣어 전송하면 access_token과 refresh_token을 반환한다.
토큰이 만료돼도 refresh_token(구글 개발팀에서 해당 값은 만료가 없다고함.) 값만 알고 있으면 귀찮게 다시 브라우저에서 로그인 인증을 하지 않고 token 재생성이 가능하다.
아래와 같이 사용하면 된다. 필자는 폐쇄된 서버 환경에서 사용해야 해서 refresh_token으로 재발급하고 api 사용을 하고 있다.
```shell
POST https://www.googleapis.com/oauth2/v4/token
refresh_token=${refresh_token}
client_id=your_client_id
client_secret=your_client_secret
redirect_uri=redirect_uri
grant_type=refresh_token
```


