# Dinner-Service

# 디너 배달 주문 서비스

### 서비스 소개

 저녁만찬을 고객의 집으로 배달하는 서비스이다. 고객들은 미스터 대박 웹사이트에서 배달 시간, 장소 등의 정보와 함께 주문할 메뉴와 디너 스타일을 선택하고 주문하면 집으로 만찬이 배달된다.

### 특징

- 저녁만찬이 제공되는 스타일을 선택할 수 있음
- 디너를 주문할때 모든 음식 항목의 수량을 변경할 수 있고 새로운 음식 항목을 추가할 수 있음

### 주문 절차(고객 & 직원)

1. 로그인한 고객이 메인 페이지에서 주문할 메뉴와 스타일을 선택한 후 주문 버튼을 누른다.
2. 주문에 포함될 음식의 수량을 변경한다.
3. 배달 주소, 결제수단, 요청사항을 입력하면 주문이 접수됨
4. 매니저는 고객으로부터 접수된 주문을 승인한다.
5. 매니저가 주문을 승인하면 요리사는 차례로 주문에 맞춰 음식을 요리한다.
6. 조리가 완료되면 배달원이 차례로 완성된 음식을 배달한다.

### URL

```java
ec2-15-164-165-148.ap-northeast-2.compute.amazonaws.com
```

### 설치

- 코드 다운

```jsx
git clone https://github.com/Dormarble/Dinner-Service.git
```

- /dinner-service-web-server/src에 있는 Login.js와 API.js에 있는 api server URL 수정(api server의 포트는 8080으로 설정)

```jsx
baseURL: '<api-server URL>:8080/api'     // API.js
```

```jsx
fetch('<api-server URL>:8080/api/user', { ...     // Login.js
```

- /dinner-service-application-server/src/main/resouces에 application.properties에 데이터베이스 설정 입력

```jsx
# DB
spring.datasource.url= <fill here>
spring.datasource.username= <fill here>
spring.datasource.password= <fill here>
#spring.jpa.show-sql=true

spring.jackson.property-naming-strategy=SNAKE_CASE

# Encoding UTF-8
spring.http.encoding.charset=utf-8
spring.http.encoding.enabled=true
spring.http.encoding.force=true

# port
server.port=8080
```

- docker-compose 실행

```jsx
docker-compose up -d
```