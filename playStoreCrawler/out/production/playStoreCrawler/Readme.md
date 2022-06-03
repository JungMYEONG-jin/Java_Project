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
```

