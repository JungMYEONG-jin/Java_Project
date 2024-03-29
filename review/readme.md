# 프로젝트 계획 이유
>기존 QA 부서 분들이 자사 앱 리뷰를 조치할때 인터넷망에서 리뷰를 수집해와 사내망에서 작업중인 사실을 알게 되었다. 이는 너무 비효율적이란 생각이 들었습니다.
히스토리를 들어보니 기존에 개발 시도가 있었지만 Google, Apple API 적용법을 몰라 Jsoup으로 리뷰를 수집하려고 하니 유지보수에 들어가는 시간이 더 
들것이란 예상이 나와 실패했다고 한다. 이를 해결하고자 Google, Apple API를 사용해 웹 서버를 구축후 사내망에 올리려고 한다.

## 개발 환경
1. Spring boot
2. Spring Data JPA
3. Thymeleaf
4. Apache POI
5. Java 8
> 리뷰 웹서버 구축에 사용한 기술은 위 4개와 같습니다. 리뷰 웹서버를 띄우기 위해 Spring boot를 선택했고 서버 사이드에서 동적으로 렌더링 하기 위해
> Spring에서 밀고 있는 Thymeleaf를 사용해 프론트를 구축했습니다. JPA를 사용한 이유는 추후 어떤 칼럼들이 추가 될지 몰라 확장에 열린 구조로 개발하기 위해
> 사용했습니다. Apache POI는 QA 부서 분들에게 엑셀 다운로드 기능을 제공하기 위해 사용했습니다. 금융권 특성상 Java8이 가장 최신으로 사용할 수 있는 버전이라
> 해당 버전을 사용했습니다.(사실 lambda를 쓰고 싶어서...)

## 구조
<img src="./review.jpeg" width="500" height="270">

> 간단한 구조는 위 그림과 같다. 리뷰 페이지의 대부분 작업은 Review Controller에서 처리한다.
> 엑셀 다운로드 같은 경우는 Excel Controller에서 처리한다.
> 크롤러는 매일 정해진 시간에 스프링 스케줄러를 통해 리뷰를 수집해 온다. 만약 특정 앱을 수동으로 크롤링 해오고 싶다면 수동 크롤링 기능을 사용하면 된다.


## Prerequisite
>Java 8이상 설치된 곳 어디에서나 사용 가능합니다.

## Files
핵심 파일은 아래와 같다.

- GoogleApi.java, AppleApi.java - API 호출해 리뷰를 수집하는 코드
- MultiSheetExcelFile.java, SingleSheetExcelFile.java, SXSSFExcelFile - 엑셀 양식 코드

## 프로젝트를 통해 공부한것들
>리뷰 웹서버를 구축하면서 lambda 를 공부할 수 있어서 재밌는 시간이었다. 기존에 작성했던 사용자의 검색 조건에 따라 원하는 결과를 반환하기 위해
> 이를 if else 문으로 모두 작성했었다... 보기만 해도 끔찍하다..
```java
@Transactional
    public Page<ReviewDto> searchByCondition(Pageable pageable, SearchForm form){
        if (form.getStart()==null && form.getEnd()==null)
        {
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return findAll(pageable);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByOsType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByOsTypeAndAppPkg(pageable, form);
        }

        ... 생략 ...

        // 시작, 종료 지정 했을때
        if (form.getStart()!=null && form.getEnd()!=null) {
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByDate(pageable, form);
            else if(form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByDateAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByDateAndOsType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByDateAndOsTypeAndAppPkg(pageable, form);
        }
    }
```
>이는 정말 말도 안되게 코드가 길어지는 단점이 있다. 다행이 조건을 4개만 사용해서 총 16가지 경우에 대해 if else 를 작성했지만...
> 만약 요구사항이 늘어난다면...? 검새 조건이 n 개면 2 n제곱이 되는 사태가 발생한다...
> 이를 어떻게 깔끔하게 해결할까 고민하던중 lambda filter가 생각이 났고 이를 적용해 코드 라인을 획기적으로 줄일 수 있었다.
```java
@Transactional
    public List<ReviewExcelDto> getExcelByCondition(SearchForm form){
        List<ReviewExcelDto> reviewExcelDtos = listToReviewExcel(findAll());
        List<ReviewExcelDto> result = reviewExcelDtos.stream().filter(review -> {
            if (form.getOs() == null)
                return true;
            return review.getOsType().equals(form.getOs().getNumber());
        }).filter(review -> {
            if (isEmpty(form.getAppPkg()))
                return true;
            return review.getAppPkg().equals(form.getAppPkg());
        }).filter(review -> {
            String createdDate = review.getCreatedDate(); // yyyy-MM-dd
            LocalDate localDate = LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
            if (form.getStart() == null)
                return true;
            else
                return (localDate.isAfter(form.getStart()) || localDate.isEqual(form.getStart()));
        }).filter(review -> {
            String createdDate = review.getCreatedDate(); // yyyyMMddHHmmss
            LocalDate localDate = LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
            if (form.getEnd() == null)
                return true;
            return localDate.isBefore(form.getEnd());
        }).collect(Collectors.toList());
        // for 순번...
        Long cnt = 1L;
        for (ReviewExcelDto reviewExcelDto : result) {
            reviewExcelDto.setId(cnt++);
            reviewExcelDto.upateAppPkgToExcelTemplate(); // excel 에 한글로 이름 보여주려고...
        }
        return result;
    }
```




## War 배포하기

>JSP를 사용한 Spring boot프로젝트에서 gradle + 내/외장 톰캣을 이용하여 서비스 배포하는 과정을 정리하고자 한다. WAR를 이용하여 배포하는 방법은 간단하다고 했지만, 사실 그것 외에도 부가적인 작업들이 필요하다. 스프링 부트 애플리케이션을 로컬 - 내장톰캣 - 외장 톰캣순으로 모두 실행시켜 보면서 각각 필요한 설정이 무엇인지 정리할 것이다.

1. build.gradle 수정
   war로 빌드하기 위하여 build.gradle 파일을 수정한다. 아래 두 가지 방법 중 아무거나 사용하여도 war로 빌드하는 데에 문제는 없지만, 스프링 문서에서 소개하는 방법은 apply plugin: 'war' 를 추가하는 것으로, 다른 이슈가 없다면 이 방법을 사용하자.
   plugins { id 'org.springframework.boot' version '2.3.4.RELEASE' id 'io.spring.dependency-management' version '1.0.10.RELEASE'    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"    id 'java'    id 'war'}   ORgroup = 'com.example'version = '0.0.1-SNAPSHOT'apply plugin: 'war'

   위 라인을 추가하고 사용 중인 IDE 내 gradle task리스트를 확인해 보면 bootWar와 war 가 추가된 것을 확인할 수 있다. (bootWar, war의 차이점은 외장 톰캣에서 실행시킬 때 함께 정리함.)


2. 로컬에서 실행시키기
   어플리케이션을 실행시키면(run) 정상 작동되는 것을 확인할 수 있다.
   간혹 IntelliJ 를 이용할 시에 오류가 발생하는데, 이 경우 Run/Debug Configurations 설정에 Working directory: $MODULE_WORKING_DIR$ 을 추가하면 해결된다.

- Run/Debug Configurations 설정 추가(IntelliJ)

- 어플리케이션 실행 확인

3. WAR파일 생성 및 확인 (내장 톰캣)
   IDE에서는 gradle tasks를 통해 바로 bootWar를 실행할 수 있으며, 커맨드 라인으로도 호출이 가능하다.
   $ ./gradlew bootWar

   빌드 결과 생성되는 WAR File은 /build/libs/프로젝트명-버전-SNAPSHOT.war의 형태로 생성된다. 아래 이미지에서는 jar 파일도 존재하는데, 이전에 따로 확인해 봤던 것으로 무시한다. 자동 설정된 이름이 너무 길거나 마음에 들지 않는다면 원하는 이름으로 지정할 수도 있다.

- build.gradle 수정
  dependencies {        …}bootWar {        archiveName("demo.war")}

  이렇게 생성된 war파일은 Windows/Linux 환경에 상관없이 스프링 부트 웹 애플리케이션으로써 단독 실행이 가능하다. 즉, 내장 톰캣을 이용하여 서비스하기 위해서는 더 이상 부가적인 절차가 필요하지 않다는 것이다. 반면에, 이 war파일을 따로 설치한 Tomcat 서블릿컨테이너(외장 톰캣)에 넣게되면 404에러와 함께 서비스가 정상적으로 시작되지 않는 것을 확인할 수 있다. 외장톰캣에서의 정상 서비스를 위해서는 아래 조치를 더 취하여야 한다.

- WAR File 실행
  $ java -jar demo-0.0.1-SNAPSHOT.war

4. SpringBootServletInitializer 상속 (외장톰캣)
   외장 톰캣으로 서비스를 제공하기 위해서는 SpringBootServletInitializer상속(configure 메서드 오버 라이딩)이 따로 필요하다. 기본 클래스에 SpringBootServletInitializer상속 시 SpringFramework의 Servlet 3.0 자원을 사용하고, 서블릿 컨테이너에 의해 시작될 때 애플리케이션이 구성된다.

- DemoApplication.java
  @SpringBootApplication
  public class DemoApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
  return application.sources(DemoApplication.class);
  }

  public static void main(String[] args) {
  SpringApplication.run(DemoApplication.class, args);
  }
  }

5. build.gradle 수정
   애플리케이션의 내장 서블릿 컨테이너(내장톰캣)가 외부 서블릿컨테이너 작동에 영향을 주지 않도록 providedRuntime으로 명시해야 한다. 빌드 실행 시 providedRuntime로 지정된 의존성(dependency)은 의존성 jar파일이 lib-provided 디렉터리에 패키지 되어 WAR파일이 생성되므로, 내/외장 서블릿 컨테이너 모두에 사용 가능한 WAR파일이 될 수 있다.

- build.gradle
  group = 'com.example'version = '0.0.1-SNAPSHOT'apply plugin: 'war'dependencies {    ...    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'    ...}

6. WAR파일 재생성 및 확인 (외장 톰캣)
   bootWar로 빌드한 결과 WEB-INF/lib-provided 폴더를 포함한 war파일을 확인할 수 있다. 이 파일은 이전과 동일하게 java -jar 명령을 통해 내장 톰캣 애플리케이션으로 실행이 가능하다. bootWar는 실행 가능한 war파일 생성을 위한 task로, 내장 톰캣 엔진이 포함돼서 만들어지기 때문에 단독 실행이 가능하기 때문이다.

- bootWar 결과 파일 구조


gradle task 중에 bootWar 말고 war라는 것도 있는데, war는 외장 톰캣 서버에 배포할 목적으로 war파일을 생성하는 task로, 단독 실행이 불가능하다는 것이 둘의 차이점이다. war를 사용하고 싶다면 두 태스크 간의 충돌을 막기 위해 build.gradle 파일 수정이 추가적으로 필요하다.

- build.gradle 수정
  bootWar.enabled = falsewar.enabled = truedependencies {        …}

- bootWar / war 결과 구조 비교

7. WAR파일 배포 (외장 톰캣)
   따로 설치한 tomcat 폴더에 war 파일을 복사한 후 서비스를 시작할 것이다. Linux Tomcat 설치는 다른 포스팅을 참고하여 설치 가능하다.
   hye0-log.tistory.com/33

[LINUX]Tomcat 설치하기
LINUX환경(CentOS)에서 Tomcat 설치하는 과정을 정리한다. 1. 원하는 톰캣 설치 버전 확인 아래 사이트에서 원하는 버전의 다운로드 경로를 확인할 수 있다. 최종 다운로드 경로는 http://archive.apache.org/d
hye0-log.tistory.com


war 파일을 아래와 같이 webapps 폴더에 복사하게 되면 demo폴더로 war파일이 압축 해제되어 서비스될 준비가 된다.  테스트는 리눅스 환경에서 할 것이지만 파일 경로 확인을 위해 Windows경로로 이미지를 대체한다.

- 배포 위치: Tomcat설치 디렉터리/webapps/생성된 war파일

- 서비스 시작: Tomcat설치 디렉터리/bin/startup.sh 실행
#1. Tomcat설치 폴더로 이동[user@ /]$ cd /home/tomcat/tomcat9 #2. webapps 배포 파일 확인[user@ tomcat9]$ cd webapps/[user@ tomcat9]$ ls -altotal 87804 drwxr-x---. 8 mro mro 109 Dec 23 02:27 . drwxrwxr-x. 9 user user 220 Nov 25 00:38 .. drwxr-x---. 5 user user 48 Dec 23 02:27 demo -rw-rw-r--. 1 user user 89902845 Dec 23 02:22 demo.war drwxr-x---. 15 user user 4096 Nov 25 00:38 docs drwxr-x---. 6 user user 83 Nov 25 00:38 examples drwxr-x---. 5 user user 87 Nov 25 00:38 host-manager drwxr-x---. 6 user user 114 Nov 25 00:38 manager drwxr-x---. 3 user user 4096 Nov 25 00:38 ROOT#3. Tomcat 서비스 시작[user@ tomcat9]$ cd ..[user@ tomcat9]$ ./bin/startup.sh Using CATALINA_BASE: /home/tomcat/tomcat9 Using CATALINA_HOME: /home/tomcat/tomcat9 Using CATALINA_TMPDIR: /home/tomcat/tomcat9/temp Using JRE_HOME: /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64 Using CLASSPATH: /home/tomcat/tomcat9/bin/bootstrap.jar:/home/tomcat/tomcat9/bin/tomcat-juli.jar Using CATALINA_OPTS: Tomcat started. #4. 콘솔 로그 확인[user@ tomcat9]$ tail -f ./logs/catalina.out

- 서비스 시작 확인: http://Linux서버IP:8080/demo
  설치된 Tomcat에 아무런 작업을 하지 않은 상태로 서비스 시작을 하면 기본 포트는 8080으로 지정되며, 내가 배포한 프로젝트를 확인하기 위해서는 접속 주소 뒤에 war 파일명을 입력해 주어야 한다.


이렇게 작성한 프로젝트를 외장 Tomcat에 배포하는 것 까지 실습을 완료하였다. 포트 지정 및 접속 URL지정, 로깅 설정들을 부가적으로 진행할 필요가 있기 때문에 차근차근 정리해 볼 것이다.

## excel template 작성하기
java reflection을 이용하면 필드값을 가져올 수 있다.
필드명과 동일한 enum 타입을 만들고 ReviewDto class의 field.get을 통해 해당 변수의 값을 가져와 엑셀에 저장한다.
기존은 일일이 수동으로 작성해야 했지만 template을 작성하고 나서 잘못 타이핑해서 실패하거나 그럴일이 없음.

```java
package com.shinhan.review.web.controller;

import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.excel.ReviewColumnInfo;
import com.shinhan.review.excel.template.SimpleExcelFile;
import com.shinhan.review.web.service.ReviewService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    ReviewService reviewService;

    @GetMapping("/api/v1/excel/review")
    public void downloadReviewInfo(HttpServletResponse response) throws IOException{

        response.setContentType("application/vnd.ms-excel; charset=euc-kr"); // 한글 깨짐
        // get review list to transfer excel file
        List<ReviewDto> reviewsForExcel = reviewService.getReviewsForExcel();
        // create excel file
        Workbook workbook = new SXSSFWorkbook();
        // create a sheet in excel file
        Sheet sheet = workbook.createSheet();

        // create header
        int rowIdx = 0;
        Row headerRow = sheet.createRow(rowIdx++);

        Map<Integer, List<ReviewColumnInfo>> allColumns = ReviewColumnInfo.getAllColumns();
        List<ReviewColumnInfo> headerColumns = allColumns.get(0); // get header column
        // set header
        headerColumns.forEach(reviewColumnInfo -> {
            Cell cell = headerRow.createCell(reviewColumnInfo.getCol());
            cell.setCellValue(reviewColumnInfo.getText());
        });


        for (ReviewDto reviewDto : reviewsForExcel) {
            Row bodyRow = sheet.createRow(rowIdx++);
            Cell bodyCell = bodyRow.createCell(0);
            bodyCell.setCellValue(reviewDto.getAppPkg());
            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(reviewDto.getAppVersion());
            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(reviewService.getMatchedName(reviewDto.getOsType()));
            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(reviewDto.getDevice());
            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(reviewDto.getNickname());
            Cell bodyCell6 = bodyRow.createCell(5);
            bodyCell6.setCellValue(reviewDto.getCreatedDate());
            Cell bodyCell7 = bodyRow.createCell(6);
            bodyCell7.setCellValue(reviewDto.getRating());
            Cell bodyCell8 = bodyRow.createCell(7);
            bodyCell8.setCellValue(reviewDto.getBody());
            Cell bodyCell9 = bodyRow.createCell(8);
            bodyCell9.setCellValue(reviewDto.getAnsweredDate());
            Cell bodyCell10 = bodyRow.createCell(9);
            bodyCell10.setCellValue(reviewDto.getResponseBody());
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @GetMapping("/api/v2/excel/review")
    public void downloadReviewInfo2(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.ms-excel; charset=euc-kr");
        List<ReviewDto> reviews = reviewService.getReviewsForExcel();
        SimpleExcelFile<ReviewDto> excelFile = new SimpleExcelFile<>(reviews, ReviewDto.class);
        excelFile.write(response.getOutputStream());
    }
}
```

template은 header, body, 작성 그리고 excel 파일을 write 하는 크게 3부분으로 나눌수 있다.
이렇게 template을 만들어 놓음으로써 하드코딩을 피할 수 있다는 장점이 생겼다.

```java
package com.shinhan.review.excel.template;

import com.shinhan.review.excel.ReviewColumnInfo;
import com.shinhan.review.exception.ExcelInternalException;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class SimpleExcelFile<T> {

    private static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;
    private static final int ROW_START_IDX = 0;
    private static final int COL_START_IDX = 0;

    private SXSSFWorkbook wb;
    private Sheet sheet;
//    private SimpleExcelMetaData excelMetaData;

    public SimpleExcelFile(List<T> data, Class<T> type) {
        validateMaxRow(data);
        this.wb = new SXSSFWorkbook();
        renderExcel(data);
    }

    private void validateMaxRow(List<T> data) {
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows)
            throw new IllegalArgumentException(String.format("This Excel Version does not support over %s rows", maxRows));
    }

    private void renderExcel(List<T> data) {
        // Create sheet and render headers
        sheet = wb.createSheet();
        renderHeaders(sheet, ROW_START_IDX);

        if (data.isEmpty())
            return;

        // render body
        int rowIdx = ROW_START_IDX + 1;
        for (Object renderData : data) {
            renderBody(renderData, rowIdx++, COL_START_IDX);
        }
    }

    private void renderHeaders(Sheet sheet, int rowIdx) {
        Row row = sheet.createRow(rowIdx);
        Map<Integer, List<ReviewColumnInfo>> allColumns = ReviewColumnInfo.getAllColumns();
        List<ReviewColumnInfo> headerColumns = allColumns.get(0); // get header column
        // set header
        headerColumns.forEach(reviewColumnInfo -> {
            Cell cell = row.createCell(reviewColumnInfo.getCol());
            cell.setCellValue(reviewColumnInfo.getText());
        });
    }

    private void renderBody(Object data, int rowIdx, int colStartIdx) {
        Row row = sheet.createRow(rowIdx);
        int colIdx = colStartIdx;
        // 순서대로 enum type 이라 idx ++ 로 가능
        ReviewColumnInfo[] values = ReviewColumnInfo.values();
        for (ReviewColumnInfo value : values) {
            Cell cell = row.createCell(colIdx++);
            try {
                Field field = getField(data.getClass(), value.name());
                field.setAccessible(true);
                renderCellValue(cell, field.get(data));
            } catch (Exception e) {
                throw new ExcelInternalException(e.getMessage(), e);
            }
        }
    }

    private void renderCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

    public void write(OutputStream stream) throws IOException {
        wb.write(stream);
        wb.close();
        wb.dispose();
        stream.close();
    }

    private Field getField(Class<?> object, String fieldName) {
        Field field = null;
        try {
            field = object.getField(fieldName);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

## Join VS Fetch Join
Join은 관심 entity만 영속화 시키고 그외 연관 관계에 있는 애들이 Lazy 타입이면 가져오지 않는다. 따라서 만약 fetch type lazy 일때 연관관계는 proxy 를 통해 가져오게 되는데 이미 영속화가 종료된 상태이기 때문에
LazyInitializationException이 발생하게 된다. 이를 해결하기 위해 fetch join을 사용할 수 있다. fetch join 사용시 연관관계에 있는 엔터티도 동시에 영속화 하기 때문에 영속화 관련 오류가 발생하지 않게 된다.

## Spring MVC
Model의 생명주기는 하나의 요청마다 한번이다. 즉 한 메소드에서 요청했을때 Model이 생성되고 이게 끝이란 의미이다. 다른 메소드에서 model.getAttribute 시 null 이 나오는 이유는 해당 메소드의 model은 set했던 모델과 전혀 
다른 Model 이기 때문이다. 즉 model에 값을 넣으면 그 메소드에 해당하는 view에만 값이 전달됨.
