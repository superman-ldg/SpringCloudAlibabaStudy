import com.ldg.cloud.OrderApplication;
import com.ldg.cloud.pojo.Order;
import com.ldg.cloud.service.OrderServiceTx;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class Test {

    @Autowired
    private OrderServiceTx orderServiceTx;

    public void test(){
        Order order = new Order();
        order.setUsername("测试事务");
        order.setUid(20182131106L);
        int txId = orderServiceTx.createOrderBefore(order);
        orderServiceTx.createOrder(txId,order);
    }

}
