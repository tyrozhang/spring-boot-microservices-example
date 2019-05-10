package gds;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("myServiceProvider") //被调用的服务的服务名
public interface FeignInterface {

//该方法与被调用的服务中的方法一致
 @RequestMapping("/")
 public String service1();

}
