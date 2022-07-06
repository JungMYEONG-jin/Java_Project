> MongoDB 설치하기
```shell
# 설치
brew install mongodb-community
# 서비스 시작
brew services start mongodb-community
# 서비스 중지
brew services stop mongodb-community

# 모든 설치가 끝나고 mongo 정상 실행시 설치 완료
mongo
```

> 스택 구조
- Frontend : Angular FrameWork
- Backend : SpringBoot
- DB : MongoDB


> Angular CLI install

```shell
# intellij terminal 
npm install -g @angular/cli

ng new youtube-clone-ui --directory

# 실행
ng serve
```