import com.lagou.edu.factory.AnnotationBeanFactory;
import com.lagou.edu.service.TransferService;
import org.junit.Test;

/**
 * 后台模拟转账测试类
 * @author YanMei.Li
 */
public class AnnotationTest {
    @Test
    public void test() throws Exception {
        TransferService transferService = (TransferService) AnnotationBeanFactory.getBean("transferService");
        transferService.transfer("6029621011001","6029621011000",1);
    }
}
