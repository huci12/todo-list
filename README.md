# todo-list (2019 서버 개발자 경력 kakaopay 과제)

### 과제
할일 목록(todo-list) 웹 어플리케이션 구현

### 기능
* 사용자는 텍스트로 된 할일을 추가할 수 있다.
  * 할일 추가 시 다른 할일들을 참조 걸 수 있다.
  * 참조는 다른 할일의 id를 명시하는 형태로 구현한다. (예시 참고)
* 사용자는 할일을 수정할 수 있다.
* 사용자는 할일 목록을 조회할 수 있다.
  * 조회시 작성일, 최종수정일, 내용이 조회 가능하다.
  * 할일 목록은 페이징 기능이 있다.
* 사용자는 할일을 완료처리 할 수 있다.
  * 완료처리 시 참조가 걸린 완료되지 않은 할일이 있다면 완료처리할 수 없다. (예시 참고)
  
### 과제 요구사항
* 웹 어플리케이션으로 개발 
* 웹어플리케이션 개발언어는 Java, Scala, Golang 중 선택을 권장함 (단, 다른 언어에 특별히 자신있는 경우 선택에 제한을 두지 않음)
* 서버는 REST API로 구현
* 프론트엔드 구현방법은 제약 없음
* 데이타베이스는 사용에 제약 없음 (가능하면 In-memory db 사용)
* 단위테스트 필수, 통합테스트는 선택
* README.md 파일에 문제해결 전략 및 프로젝트 빌드, 실행 방법 명시

### Frontend Dependencies
| Dependence  |    Version    | 
|-------------|---------------|
| jquery      | 3.2.1         |
| handlebars  | 3.0.1         |
| bootstrap   | 3.3.2         |

### Backend Dependencies
| Dependence  |    Version    | 
|-------------|---------------|
| java        | 1.8           |
| spring-boot | 2.1.2.RELEASE |
| mybatis     |               |
| h2 databaase|               |
| lombok      |               |
                  

### Run Spring-boot Project 
``` 
# linux, mac os
./mvnw spring-boot:run

# windows
./mvnw.cmd spring-boot:run
```

### 프로젝트 구조 
```
project
│   README.md   
└───src
│   │---main
|   |   |─---java 
│   │   |    └----com.kakaopay.todo.todolist 
|   |   |         |  TodoListApplication.java
|   |   |         |─app
|   |   |         |  |─dao
|   |   |         |  |   TodoMapper.java 
|   |   |         |  |-service   
│   │   |         |  |   TodoService.java
|   |   |         |  |-vo
|   |   |         |  |   TodoJobVo.java
|   |   |         |  └─web
|   |   |         |      TodoController.java
|   |   |         |      TodoRestController.java
|   |   |         |
|   |   |         └─common
|   |   |            |-config
|   |   |            |   DataSourceConfig.java
|   |   |            |-exception
|   |   |            |   BusinessException.java
|   |   |            |   ExceptionController.java
|   |   |            |   ExceptionRestController.java
|   |   |            |-validation
|   |   |            |   Create.java
|   |   |            |   Delete.java
|   |   |            |   Update.java
|   |   |            └-vo
|   |   |                ErrorVo.java 
|   |   |                ListVo.java
|   |   |
|   |   |-----resource
|   |   |     |  application.properties
|   |   |     |  logback-spring.xml
|   |   |     |  messages.properties
|   |   |     |  schema.sql
|   |   |     └----mapper
|   |   |            todoMapper.xml
|   |   |-----webapp
|               |-static
|               |  └-js
|               |      pagination.js
|               |      todo.js
|               └-WEB-INF
|---pom.xml        └-views
|                      list.jsp
```

### 문제 해결 전략 
```
1. 할일간의 관계를 위해 2개의 TABLE을 사용 
  1) JOB 
     -> 할일에 대한 정보를 등록  
  2) JOB_REF
     -> 할일 간의 관계를 등록. 할일 등록/수정과 동시에 참조 할일을 등록
        
2. 할일 완료 처리 시 JOB_REF를 조회하여 참조된 일감의 완료 여부를 확인함 
   -> 완료 여부는 참조 관계(JOB_REF) 조회 후 나온 JOB의 ID 를 통해 JOB TABLE의 COMPLETE_YN 
      조건을 넣어 조회하여 확인 
        
3. 할일 완료 해제 시 2번과 같이 JOB_REF를 조회하여 처리함. 
   -> 해당 할일을 참조한 일감이 이미 완료 처리가 된 경우 해당 일감이 완료 해제가 될 경우 논리적으로 맞지 않으므로 
      완료 해제가 되지 않도록 추가 처리를 함 
```       

### DB 스키마
```
CREATE TABLE JOB
(
    ID CHAR(4),
    CONTENT VARCHAR2 (100),
    REG_DATE TIMESTAMP,
    UPD_DATE TIMESTAMP,
    COMPLETE_YN CHAR(1)
);


CREATE TABLE JOB_REF
(
    ID CHAR(4),
    REF_ID CHAR(4),
    REG_DATE TIMESTAMP
);

CREATE SEQUENCE JOB_SEQ START WITH 1 INCREMENT BY 1;     
```

### 동작 동영상 링크
https://github.com/rmsja10/todo-list/blob/master/todo-list.mp4




