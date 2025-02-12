## JPA 
- 가장 유명한 Client Connect라서 선택
- 영속성 컨텍스트를 포함한 개념은 잘 모르지만 기본적인 CRUD를 포함해서 Query는 nativeQuery를 활용해서 날릴 수 있어서 선택

## MySQL
- 최근에 Real MySQL을 읽음으로써 게시판을 만들면서 실습해보고 싶다는 생각을 했음

### SnowFlake
- 유니크하면서 정렬된 값을 보장하는 알고리즘이 필요했는데, MySQL에서 지원하는 AUTO_INCREMENT의 경우 table_lock을 발생시킨다.
- 데이터베이스 샤딩을 고려하여 AUTO_INCREMENT를 사용하지 않고 확장 가능한 서비스를 고려하기 위해 적당한 PK 알고리즘을 찾아보았는데 SnowFlake가 적당해보여서 선택하게 되었다.
- 추가로 B+ Tree 구조를 생각해보면 값이 정렬되어있다는 것은 결국 새로운 Node가 추가될 때 B+ Tree를 재정렬하는 비용이 발생하지 않음을 의미한다. 

# 인증/인가 고민
### 세션과 토큰 ?
- 단순히 세션 ID를 사용하면 세션 ID가 고정되어서 여러가지 문제를 할 수 있어서 Refresh Token을 Refresh Token을 DB에 저장하는 방식을 흔히 사용하는 것 같다.
- 그렇다면 세션 ID를 매번 갱신하는 방식으로 하면 어떠한 문제가 있을까 ?
- 그럼에도 보안 취약점이 존재한다. 
- 그래서 DB는 여러가지 이유를 고려해서 Redis를 선정하는 것 같다.
- Redis에 RDB, AOF 기능이 있어서 장애복구가 빠르다.
- 결국 JWT와 Session 저장소는 같이 사용하는 것이 여러모로 보안 측면을 고려했을 때 좋을 것 같다.
- Redis에서 TTL이 있으니 좋다.
Http Session을 공부해보고 Http Session을 관리하는 기법에 대해서 학습해봐야겠다.