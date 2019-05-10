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
        return "recieved message:"+ feign.home();
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
}