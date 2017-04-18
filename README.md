line-bot-sdk-java-1.6.0 customize source
----
이 소스는 LINE API에서 제공하는 [line-bot-sdk-java-1.6.0 release](https://github.com/line/line-bot-sdk-java/releases/tag/1.6.0)을 기준으로 수정되었습니다.

특징
----
SpringBoot 기반으로 어노테이션 `@EventMapping`과 `LineMessagingClientFactory`클래스를 이용하여 다수의 API계정을 하나의 Bot에서 컨트롤 할 수 있습니다.   
상세 예제는 "sample-spring-boot-echo"예제를 참고하시기 바랍니다.

설정
----
Maven기반으로 작업할 경우 아래와 같이 pom.xml파일에 dependency를 추가합니다.

- pom.xml    
```xml
<dependencies>
    <dependency>
        <groupId>io.github.jistol</groupId>
        <artifactId>line-bot-spring-boot</artifactId>
        <version>1.6.0.JISTOL</version>
    </dependency>
</dependencies>
```

다중 채널 설정
----
본 라이브러리는 다수의 LINE계정을 하나의 Bot에서 관리하며 각 계정은 channel-secret키를 이용하여 관리합니다.   

설정파일에 아래와 같이 다수의 채널을 입력합니다.

- application.yml
```yaml
line.bot:
  handler.path: /callback
  channel-token: channel-token-1
  channel-secret: channel-secret-1
  list :
    - channel-token: channel-token-2
      channel-secret: channel-secret-2
    - channel-token: channel-token-3
      channel-secret: channel-secret-3
      ...
```

channel-secret키를 인자값으로 받기
----
기존 SDK는 아래와 같이 이벤트만 받게 되어 있습니다.   
아래 예제의 경우 `channel-secret-1`의 이벤트만 처리합니다.

```java
@EventMapping
public void handler(Event event){
  ...
}
```

하지만 다수의 계정을 사용할 경우 아래와 같이 channel-secret 키를 받아 처리 할 수 있습니다.

```java
@EventMapping
public void handler(Event event, String secretKey){
  ...
}
```

특정 계정의 이벤트만 처리하고 싶을 경우 아래와 같이 사용할 수 있습니다.

```java
@EventMapping(secretKey = "${channel-secret-1}")
public void handler1(Event event, String secretKey){
  ...
}

@EventMapping(secretKey = "${channel-secret-2}")
public void handler2(Event event){
  ...
}
```

특정 계정으로 메시지 보내기
----

`line.bot`하위에 있는 `channel-secret-1`계정의 경우 아래와 같이 기존 `LineMessagingClient` Bean을 이용하여 사용 가능합니다.    

```java
@Autowired
private LineMessagingClient lineMessagingClient;

@EventMapping
public void handler(Event event){
  lineMessagingClient.pushMessage( ... );
}
```

하지만 `LineMessagingClientFactory` Bean을 사용할 경우 모든 채널을 사용 할 수 있습니다.

```java
@Autowired
private LineMessagingClientFactory lineMessagingClientFactory;

@EventMapping
public void handler(Event event, String secretKey){
  lineMessagingClientFactory.get(secretKey).pushMessage( ... );
}
```    

참고
----
Line Bot SDK - Java 1.6.0 : [https://github.com/line/line-bot-sdk-java/releases/tag/1.6.0](https://github.com/line/line-bot-sdk-java/releases/tag/1.6.0)
