## [게시판 프로젝트]

- 유튜브 코딩레시피 스프링부트 게시판 프로젝트 클론코딩
- 개발환경
    - IDE - IntelliJ IDEA
    - Spring Boot (3.4.1)
    - JDK 17
    - MySQL
    - Spring Data JPA
    - Thymeleaf
- 주요기능
    1. 글쓰기 (/board/save)
    2. 글 목록 (/board/)
    3. 글 조회 (/board/{id})
    4. 글 수정 (/board/update/{id})
        - 상세화면에서 수정버튼 클릭
        - 서버에서 해당 게시글의 정보를 가지고 수정 화면 출력
        - 제목, 내용 수정 입력 받아서 서버로 요청
        - 수정 처리
    5. 글 삭제 (/board/delete/{id})
    6. 페이징 처리 (/board/paging)
        - /board/paging?page=2
        - /board/paging/2
    7. 파일 첨부
        - 단일 파일 첨부
        - 다중 파일 첨부
        - 파일 첨부 관련 추가 부분
            - save.html
            - BoardDTO
            - BoardService.save()
            - BoardEntity
            - BoardFileEntity(파일자체를 db에 저장하는게 아닌 파일의 이름을 저장할 entity), BoardFileRepository 추가
            - detail.html
        1. 댓글 처리하기
            - 글 상세 페이지에서 댓글 입력 (ajax)
            - 상세조회 할 때 기존에 작성된 댓글 목록이 보임
            - 댓글을 입력하면 기존 댓글 목록에 새로 작성한 댓글 추가
            - 댓글용 테이블 필요
- application.yml
    
    ```java
    # 서버 포트 설정
    server:
      port: 8080
    
    # database 연동 설정
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/min?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
        username: root
        password: personal
      thymeleaf:
        cache: false
    
      # spring data jpa 설정
      jpa:
        database-platform: org.hibernate.dialect.MySQLDialect
        open-in-view: false
        show-sql: true
        hibernate:
          ddl-auto: update
    ```
    
    ```java
    # 서버 포트 설정
    server:
      port: 8080
      
    # 하나의 pc에서 같은 포트번호를 동시에 사용할 수 없다
    # 포트는 pc상의 만들어진 통로
    # springboot 내장서버인 tomcat을 사용
    # database(MySQL) 포트는 개별적으로 사용 (3306)
    ```
    
    ```java
    # database 연동 설정
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        # 3306 뒤에 min은 database의 이름을 의미한다.
        url: jdbc:mysql://localhost:3306/min?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
        username: root 
        password: personal
      thymeleaf:
        cache: false
    ```
