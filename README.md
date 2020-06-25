### 환경
- Gradle 6.5
- openjdk version "1.8.0_252"
- spring boot
- jpa hibernate
- h2

## 실행 방법
``` bash
./gradlew bootRun
```
## swagger로 api 테스트
- http://localhost:8080/swagger-ui.html#/

## api

### user api
- 회원 가입 (/api/user/signup)
- ex) curl -X POST "http://localhost:8080/api/user/signup" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\", \"password\": \"123\"}"
### auth api
- jwt를 이용한 토큰 얻기 (/api/auth/create/token)
- ex) curl -X POST "http://localhost:8080/api/auth/create/token" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\", \"password\": \"123\"}"
### coupon api
- header에 accesstoken이 꼭 필요함
### 랜덤한 코드의 쿠폰을 N개 생성 (/api/coupon/create)
- ex) curl -X POST "http://localhost:8080/api/coupon/create" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A" -H "Content-Type: application/json" -d "{ \"count\": 10}"
### 생성된 쿠폰중 하나를 사용자에게 지급(사용자 email을 통해서 지급하게 구현) (/api/coupon/allocate)
- ex) curl -X POST "http://localhost:8080/api/coupon/allocate" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\"}"
### 사용자에게 지급된 쿠폰을 조회(사용자 email을 통해서 지급된 정보를 조회) (/api/coupon/user/coupon)
- ex) curl -X POST "http://localhost:8080/api/coupon/user/coupon" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\"}"
### 지급된 쿠폰중 하나를 사용 (/api/coupon/use/{code})
- ex) curl -X PUT "http://localhost:8080/api/coupon/use/725602046346592256" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A"
### 지급된 쿠폰중 하나를 사용 취소 (/api/coupon/cancel/{code})
- ex) curl -X PUT "http://localhost:8080/api/coupon/cancel/725602046350786563" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A"
### 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회 (/api/coupon/expired)
- ex) curl -X GET "http://localhost:8080/api/coupon/expired" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A"

### 성능 테스트
``` bash
sudo apt-get install apache2-utils
```
- 랜덤한 코드의 쿠폰을 N개 생성 (/api/coupon/create)
- n은 1로 설정
``` bash
ab -p post_create.txt -T application/json -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0QHQuY29tIiwiaWF0IjoxNTkzMDgwMjc1LCJleHAiOjE1OTMwODc0NzV9.nnRNCo307VjavsRCJjJUlka8DlYz6LR0o6boBNPlcOq9IIODNiS3eBEXIolw7boa1Kvcx4PHmoWR_MC9JOAoSQ' -c 10 -n 2000 http://localhost:8080/api/coupon/create
Requests per second:    3571.98 [#/sec] (mean)
Time per request:       2.800 [ms] (mean)
```
- 생성된 쿠폰중 하나를 사용자에게 지급(사용자 email을 통해서 지급하게 구현) (/api/coupon/allocate)
``` bash
ab -p post_email.txt -T application/json -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0QHQuY29tIiwiaWF0IjoxNTkzMDgwMjc1LCJleHAiOjE1OTMwODc0NzV9.nnRNCo307VjavsRCJjJUlka8DlYz6LR0o6boBNPlcOq9IIODNiS3eBEXIolw7boa1Kvcx4PHmoWR_MC9JOAoSQ' -c 10 -n 2000 http://localhost:8080/api/coupon/allocate
Requests per second:    1806.78 [#/sec] (mean)
Time per request:       5.535 [ms] (mean)
```
- 사용자에게 지급된 쿠폰을 조회(사용자 email을 통해서 지급된 정보를 조회) (/api/coupon/user/coupon)
``` bash
ab -p post_email.txt -T application/json -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0QHQuY29tIiwiaWF0IjoxNTkzMDgyMzg4LCJleHAiOjE1OTMwODk1ODh9.z5Q98Uunbod6bVrxS0Z6jteQ4KzITDvUt8vLXPYOF8KuFAoiiXUzMW_YPG-RQglfvp4Yhu0Fz1o4DnhpKMRI1w' -c 10 -n 2000 http://localhost:8080/api/coupon/user/coupon
Requests per second:    327.98 [#/sec] (mean)
Time per request:       30.489 [ms] (mean)
```
- 지급된 쿠폰중 하나를 사용 (/api/coupon/use/{code})
``` bash
하나를 사용하면 에러기 때문에 성능 테스트 힘듬
```
- 지급된 쿠폰중 하나를 사용 취소 (/api/coupon/cancel/{code})
``` bash
하나를 취소하면 에러기 때문에 성능 테스트 힘듬
```
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회 (/api/coupon/expired)
``` bash
ab -T application/json -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0QHQuY29tIiwiaWF0IjoxNTkzMDgyMzg4LCJleHAiOjE1OTMwODk1ODh9.z5Q98Uunbod6bVrxS0Z6jteQ4KzITDvUt8vLXPYOF8KuFAoiiXUzMW_YPG-RQglfvp4Yhu0Fz1o4DnhpKMRI1w' -c 10 -n 2000 http://localhost:8080/api/coupon/expired
Requests per second:    2624.32 [#/sec] (mean)
Time per request:       3.811 [ms] (mean)
```
### 매일 배치로 동작
- 매일 1 am 때 만료된 쿠폰이 있으면 그 쿠폰을 만료 상태로 바꿔줌
- 매일 8 am 때 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송

### 문제 해결 전략
- 쿠폰은 유니크 해야 하므로 uuid를 이용해서 생성했음
- 패스워드는 BCrypt로 암호화
- jwt를 통한 인증 구현
- 필터를 이용해서 token이 invalid하거나 만료됐는 지 체크
- 필요한 fail exception 처리 구현 

### 개선 할점
- spring boot reactive(webflux)를 이용해서 구현
- db 선택 : mongodb-reactive, mysql(현재 구조)
- twitter-archive snowflake 쿠폰 생성 할때 활용
- micro service 아키텍처로 전환
- ngrinder로 성능 측정