package gds;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("myServiceProvider") //�����õķ���ķ�����
public interface FeignInterface {

//�÷����뱻���õķ����еķ���һ��
 @RequestMapping("/")
 public String service1();

}
