# 1概述

本文主要实践通过服务名调用微服务。

##1.1内容结构如下：

| 模块                    | 说明                           | 端口 | 服务名            |
| ----------------------- | ------------------------------ | ---- | ----------------- |
| server_one_provider     | 服务提供者1                    | 8000 | myServiceProvider |
| server_one_provider_two | 服务提供者2，与服务提供者1相同 | 8001 | myServiceProvider |
| client_consume          |                                | 8002 | consumer          |

##1.2 项目目录结构

```
myexample  
    ├─client_consume
    │  │  pom.xml
    │  │  
    │  └─src
    │      └─main
    │          ├─java
    │          │  └─gds
    │          │          Example.java
    │          │          FeignInterface.java  
    │          └─resources
    │                  application.yml
    │                  
    ├─server_one_provider
    │  │  pom.xml
    │  │  tree.txt
    │  │  
    │  └─src
    │      └─main
    │          ├─java
    │          │  └─gds
    │          │          Example.java
    │          └─resources
    │                  application.yml
    │                  
    └─server_one_provider_two
        │  pom.xml
        │  
        └─src
            └─main
                ├─java
                │  └─gds
                │          Example.java 
                └─resources
                        application.yml
```

# 3服务注册中心

服务注册中心使用的是eureka，如何架设网上讲的很多。

#4主要代码

##.4.1服务提供者代码

启动类（方便起见，服务接口也写在其中）

```java
package gds;
import org.springframework.boot.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
@RestController
public class Example {
    @RequestMapping("/")
    String service1() {
        return "Hello World!,I am service one";
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
}
```

application.yml:

```yaml
spring:
  application:
    name: myServiceProvider
server:
  port: 8000

eureka:
  instance:
    prefer-ip-address: true
  client:
    registerWithEureka: true                           
    fetchRegistry: true                                
    serviceUrl:                                        
      defaultZone: http://localhost:8761/eureka/
```

重点注意 “myServiceProvider”，这是服务消费者要调用的服务名。

##4.2消费者代码

启动类：

```java
package gds;
import org.springframework.boot.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@RestController
public class Example {
    @Autowired
    private FeignInterface feign;
    
    @RequestMapping("/")
    String test() {
        return "recieved message:"+ feign.service1(); //调用其他微服务
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
}
```

注解@EnableFeignClients是重点，接下类就可以通过Feign客户端访问其他服务api。

FeignInterface为访问接口，相当于代理了对消费者服务的访问，代码如下：

```java
package gds;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("myServiceProvider") //被调用的服务的服务名，重点。。。。
public interface FeignInterface {

//该方法与被调用的服务中的方法一致，也是重点
 @RequestMapping("/")
 public String service1();

}

```

好了，代码基本就这样，很简单

# 5运行

简单起见，本例并没有建立父pom.xml，而是在每个应用下生成了pom.xml,因此需要分别在每个应用下进行打包，例：

```
cd client_consume
mvn clean install
```

分别打包完成之后，即可分别运行：

```
java -jar my  myXXXX.jar
```

如启动正常，服务注册中心应看到如下界面：

![](C:\Users\zhangyp\Desktop\军信通Q0lz63ccHp.png)

# 6访问测试

浏览器中输入<http://localhost:8002/>，返回消息如下：

Hello World!,iI am service two或Hello World!,iI am service one
