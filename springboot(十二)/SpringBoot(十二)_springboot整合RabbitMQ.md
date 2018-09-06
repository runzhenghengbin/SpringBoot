



### RabbitMQ简介

AMQP:Advanced Message Queue，高级消息队列协议。它是应用层协议的一个开放标准，为面向消息的中间件设计，基于此协议的客户端与消息中间件可传递消息，并不受产品、开发语言等条件的限制。

RabbitMQ 是一个由Erlang语言开发的AMQP的开源实现。（PS:前几天有篇文章介绍了阿里P10的淘宝褚霸，就是erlang大神）。支持多种客户端。用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。具体特点如下：

1、可靠性

RabbitMQ 使用一些机制来保证可靠性，如持久化，传输确认、发布确认。

2、灵活的路由

在消息进入队列之前，通过Exchange来路由消息。对于典型的路由功能，RabbitMQ 提供了内置的Exchange来实现。针对更复杂的路由功能。可以将多个Exchange 绑定在一起，也通过插件机制实现自己的 Exchange。

3、消息集群

多个RabbitMQ 服务器可以组成一个集群，形成一个逻辑Broker.

4、高可用

队列可以在集群中的机器上进行镜像，使得在部分节点出问题的情况下队列任然可用。

5、多种协议

RabbitMQ支持多种消息队列协议。比如STOMP、MQTT等等

6、多语言客户端

RabbitMQ支持多种语言，比如java/Ruby等

7、管理界面

RabbitMQ提供了一个易用的用户界面。使得用户可有监控和管理Broker的许多方面

8、跟踪机制

如果消息异常，RabbitMQ 提供了消息跟踪机制，使用者可以找出

9、插件机制

RabbitMQ提供了许多插件，从多方面进行扩展，也可以编写自己的插件

### RabitMQ 概念模型

 ![](http://p7zk4x9pv.bkt.clouddn.com/111.png)

1、Message

消息，消息是不具名的，它由消息头和消息体组成，消息体是不透明的。而消息头则由一系列的可选属性组成。包括routing-key(路由键)、priority(相对于其他消息的优先权)、delivery-mode(指出该消息可能需要持久性存储)等

2、Publisher

消息的生产者，也是一个向交换器发布消息的客户端应用程序

3、Exchange

交换器，*用来接收生产者发送的消息并将这些消息路由给服务器中的队列。*

4、Binding

绑定，用于消息队列和交换器之间的关联。一个绑定就是基于路由键将交换器和消息队列连接起来的路由规则。所以可以将交换器理解成一个由绑定构成的路由表。

5、Queue

消息队列，用来保存消息直到发送给消费者。它是消息的容器，也是消息的终点。一个消息可投入一个或多个队列。消息一直在队列里面。等待消息消费者连接到这个队列将其取走。

6、Connection

网络连接

7、Channel

信道，多路复用连接中的一条独立的双向数据流通道。

8、Consumer

消息的消费者，表示一个从消息队列中取得消息的客户端应用程序

9、Virtual Host

虚拟主机、表示一批交换器、消息队列和相关对象。虚拟主机是共享相同的身份认证和加密环境的独立服务器域。每个vhost 本质上就是一个mini版的RabbitMQ服务器。拥有自己的队列、交换器、绑定、和权限机制。vhost是AMQP概念的基础。必须在连接时指定，RabbitMQ默认的Vhost是/.

10、Broker

表示消息队列服务器实体。

### Exchange 类型



Exchange 有四种类型：Direct、Topic、Headers 和 Fanout 。

- Direct：该类型的行为是“先匹配，再投送”，即在绑定时设定一个 **routing_key**，消息的**routing_key** 匹配时，才会被交换器投送到绑定的队列中去。
- Topic：按规则转发消息（最灵活）。
- Headers：设置 header attribute 参数类型的交换机。
- Fanout：转发消息到所有绑定队列。

> headers 交换器和 direct 交换器完全一致，但性能差很多，目前几乎用不到了,这里不再详细介绍

#### Driect

![](http://p7zk4x9pv.bkt.clouddn.com/direct.png)



Direct Exchange 是 RabbitMQ 默认的交换机模式，也是最简单的模式，根据 key 全文匹配去寻找队列。



消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配.它是完全匹配、单播的模式。

#### Topic

![](http://p7zk4x9pv.bkt.clouddn.com/topic.png)

Topic Exchange 转发消息主要是根据通配符。在这种交换机下，队列和交换机的绑定会定义一种路由模式，那么，通配符就要在这种路由模式和路由键之间匹配后交换机才能转发消息。

在这种交换机模式下：

- 路由键必须是一串字符，用句号（.）隔开，如 agreements.us，或者 agreements.eu.stockholm 等。

topic 和 direct 类似，只是匹配上支持了“模式”，在“点分”的 `routing_key` 形式中，可以使用两个通配符：

- `*`表示一个词。
- `#`表示零个或多个词。





#### Fanout

![](http://p7zk4x9pv.bkt.clouddn.com/fanout.png)

Fanout Exchange 消息广播的模式，不管路由键或者是路由模式，**会把消息发给绑定给它的全部队列**，如果配置了`routing_key`会被忽略。



### 代码实战



> 此部分代码基于springboot进行，全部代码放到github上

Spring Boot 集成 RabbitMQ 非常简单，仅需非常少的配置就可使用，Spring Boot 提供了 spring-boot-starter-amqp 组件对MQ消息支持。

#### 简单使用

（1）配置 pom 包，主要是添加 spring-boot-starter-amqp 的支持。

```
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

（2）配置文件，配置 rabbitmq 的安装地址、端口及账户信息。

```
spring.application.name=spirng-boot-rabbitmq

spring.rabbitmq.host= localhost
spring.rabbitmq.port= 5672
spring.rabbitmq.username=allen
spring.rabbitmq.password=123456

server.port=8080
```

（3）定义队列

```
@Configuration
public class RabbitConfig {

    @Bean
    public Queue Queue() {
        return new Queue("hello");
    }
}
```

（4）发送者

AmqpTemplate 是 Spring Boot 提供的默认实现。

```
@RestController
public class HelloSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping(value = "/sendString")
    public String sendString() {
        //发送消息 String routingKey, Object object
        amqpTemplate.convertAndSend("hello", "hello rabbitMQ");
        return "消息已发送";
    }

    @GetMapping(value = "/sendObject")
    public String sendObject() {
        //发送消息 String routingKey, Object object
        UserEntity userEntity = new UserEntity();
        userEntity.setName("allen");
        userEntity.setAddress("山东济南");
        amqpTemplate.convertAndSend("hello", userEntity);
        return "消息已发送";
    }
```

（5）接收者

注意使用注解`@RabbitListener`，使用 queues 指明队列名称，`@RabbitHandler`为具体接收的方法。

```
@Component
@RabbitListener(queues = "hello")
@Slf4j
public class HelloReceiver {

    @RabbitHandler
    public void process(String hello) {
        log.info("消息接受为：{}",hello);
    }

    @RabbitHandler
    public void process(UserEntity userEntity) {
        log.info("消息接受为：{}",userEntity);
    }
}

```

（5）访问结果

```
com.zhb.direct.HelloReceiver             : 消息接受为：hello rabbitMQ
com.zhb.direct.HelloReceiver             : 消息接受为：UserEntity(name=allen, address=山东济南)

```

#### Topic Exchange
（1）定义队列、交换机并绑定

```
/**
 * @author: curry
 * @Date: 2018/9/5
 */
@Configuration
public class TopicRabbitConfig {

    final static String message = "topic.message";
    final static String messages = "topic.messages";

    //定义队列
    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }

    //exchange
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    //将队列和交换机进行绑定
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }
}
```
（2）发送者

```
/**
 * @author: curry
 * @Date: 2018/9/5
 */
@RestController
public class Sender {
    @Resource
   private AmqpTemplate rabbitTemplate;

    @GetMapping(value = "send1")
    public void send1(){
        String context = "hi, i am message1";
        rabbitTemplate.convertAndSend("exchange","topic.message",context);
    }

    @GetMapping(value = "send2")
    public void send2(){
        String context = "hi, i am message2";
        rabbitTemplate.convertAndSend("exchange","topic.messages",context);
    }

```
(3) 接受者

```
/**
 * @author: curry
 * @Date: 2018/9/5
 */
@Slf4j
@Component
@RabbitListener(queues = "topic.message")
public class Receiver1 {

    @RabbitHandler
    public void process(String message) {
        log.info("topic Receive1:{}", message);
    }
}

/**
 * @author: curry
 * @Date: 2018/9/5
 */
@Slf4j
@Component
@RabbitListener(queues = "topic.messages")
public class Receiver2 {

    @RabbitHandler
    public void process(String message) {
        log.info("topic Receive2:{}", message);
    }
}

```

> 其余代码不再演示，完整代码见github

完整代码：[github](https://github.com/runzhenghengbin/SpringBoot)

好了，玩的开心！

