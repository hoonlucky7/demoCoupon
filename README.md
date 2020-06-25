1) mysql 세팅
$ sudo mysql
mysql> create database COUPON_WEB;

mysql> create user 'hoon'@'%' identified by '1q2w3e';

mysql> grant all on COUPON_WEB.* to 'hoon'@'%'; 

mysql> create database COUPON_WEB_TEST;

mysql> grant all on COUPON_WEB_TEST.* to 'hoon'@'%'; 

2) 실행 방법
./gradlew bootRun

3) swagger로 api 테스트
http://localhost:8080/swagger-ui.html#/
