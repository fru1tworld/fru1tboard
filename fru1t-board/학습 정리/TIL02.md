# Java 버전 선택

- Java는 8, 11 ,17, 21에 큰 변화가 있는 것 같다.

### Java 8

- LTS 2030.12까지 지원
- Lambda, Stream API 제공
- Optional, 시간과 날짜 API 제공

## Java 11

- String과 File 기능이 향상되었다고 하는데 이 내용은 잘 모르겠다.

## Java 17

- Spring Boot 3.x.x 버전은 JDK 17부터 가능하다고 한다.
- Switch에 대한 패턴 매칭, recode class 도입 등이 있다고 하는데 이 내용도 잘 모르겠다.

## Java 21

- Spring Boot 3.2 버전부터 지원이 된다고 한다.
- 가상 스레드를 지원하고 UTF-8이 기본값으로 사용된다고 한다.

## Java에서 LTS

- Jaava에서 LTS는 8년의 보안 및 버그 업데이트를 지원한다고 한다. 일반적으로 그렇지만
- 특이사항으로 8과 11은 지원 기간을 연장했다고 한다.

## 스프링과 스프링부트

- Spring 6부터 JDK 17 이상을 요구한다. 현재 학습하는 입장에서 굳이 옛날 버전을 사용할 필요는 없다고 생각이 된다.
- Virtual Thread라는 흥미로운 기술이 JDK 21부터 추가됐다.
- 관련내용을 학습해보고 프로젝트에 적용할지 여부를 고민해봐야겠다.
- 이후에 도입할 수 있도록 현재 프로젝트 버전은 Java 21로 하였다.
### 결론 
현재 학습하는 입장에서 굳이 옛날 버전을 사용할 필요는 없는 것 같고 LTS 기준으로 많이 사용되는 Java 21을 골랐다. 

이 과정에서 다양한 구현체들이 존재하는데 큰 의미는 없는 것 같아서 아무거나 골라보았다.

# Gradle
Gradle은 빌드 자동화 시스템이라고 한다.
주요 사용 목적은 복잡한 의존성 관리와 여러 개발 환경 세팅 및 테스트 자동화 등 유용한 기능을 많이 제공한다.

Maven과 차이가 있는데 Gradle은 XML이 아닌 그루비 기반의 DSL을 사용하는게 매력적으로 느꼈던 것 같다.
Groovy는 여러 예제를 살펴봤을 때 사용법이 그렇게 어려워 보이지 않아보인다는 인상이 있었다.


## 인젤리제이에서 스프링부트를 생성하면 생기는 일
- 인텔리제이에서 스프링부트프 프로젝트를 생성하면
```
.gradle/
.idea/
build/
gradle/
src/
.gitignore
build.gradle
gradlew
gradlew.bat
settings.gradle
```

이렇게 생성이 된다. 
### .gradle/
- Gradle의 캐시 및 빌드 정보 저장소라고 한다.
- 프로젝트를 빌드할 때 Gradle이 다운로드한 의존성, 실행 기록, 빌드 결과 등이 저장된다고 한다.
- 삭제 가능

### .idea/
- IntelliJ IDEA 프로젝트 설정 폴더
- git에 올라갈 필요는 없는 것 같아서 .gitignore로 삭제헀다. 
### build/
- 빌드 결과물이 저장되는 폴더
- gradle build 실행 시 생성 
- 컴파일된 클래스, JAR 파일, 리소스 파일이 포함된다. 
### gradle/
- Gradle Wrapper 관련 파일이 들어있는 폴더
- 프로젝트에서 Gradle 버전을 관리할 때 사용됨 
- gradlew 실행 시 필요한 스크립트 및 설정 파일 포함 
### src/
- 소스 코드가 들어있는 폴더
### build.gradle
- Gradle의 빌드 스크립트 파일
- 프로젝트의 의존성, 플러그인, 태스크, 빌드 설정을 정의한다.
### gradlew
- Gradle Wrapper 실행 파일
- 
### gradlew.bat
### settings.gradle

### MVC 패턴과 Annotation
`@Controller`, `@Repository`, `@Service`와 같은 Annotation이 있는데 현재 프로젝트가 계층적 아키텍트를 사용하고 있다.
계층형 아키텍트를 사용하는 이유는 계층적으로 구조를 관리해서 역할을 명화히 하고 의존성을 제어하기 위함이다.
신기한 부분은 @Component 라는 애너테이션을 포함하고 있는데 사실 메타정보와 같은 의미로 사용되고 있는 것이다.
스프링 프레임워크에서 클래스를 Bean으로 등록하는데 컴포넌트 스캔을 통해 자동으로 Bean 클래스를 생성하는 역할을 하고 있다.


### main이 public static void main인 이유 
Java에서 public static void main(String[] args)로 시작하는 이유 
1. public: main JVM이 직접 호출하는 메서드이므로 public으로 해야한다.
2. static Java에서 객체를 생성하지 않고도 실행할 수 있도록 static을 선언해야 한다.
3. void: main은 JVM이 호출한 후 종료되는 것이 목적이므로 리턴값이 필요하지 않다. 
4. main: JVM은 main이라는 이름을 찾아서 실행한다.
5. String[] args: 프로그램 실행 시 명령줄에서 전달된 값을 받을 수 있도록 String[] args를 사용한다. 


### Entity 애너테이션 
`@Entity`: JPA 엔티티
`@Table`: DB 테이블 매핑 설정
`@Getter`: 롬복의 Getter 생성 
`@NoArgsConstructor`: JPA는 내부적으로 리플렉션 기법을 사용하여 엔티티 객체를 생성하는데 기본 생성자가 없으면 이를 실행할 수 없다.
`@ToString`: 디버깅 용이 - 만약 연관관계를 매핑해야하는 경우 순환 참조에 유의해야함

### 영속성 컨텍스트 
- `@Transactional` 을 쓰면 findById를 해서 갖고와도 영속성 컨텍스트에 의해 자동으로 저장이 된다.
- 
