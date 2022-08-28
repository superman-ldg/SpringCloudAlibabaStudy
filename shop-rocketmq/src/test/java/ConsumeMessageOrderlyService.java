import org.apache.rocketmq.client.impl.consumer.ConsumeMessageService;
import org.apache.rocketmq.client.impl.consumer.ProcessQueue;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.ConsumeMessageDirectlyResult;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ConsumeMessageOrderlyService implements ConsumeMessageService {

   private final ThreadPoolExecutor poolExecutor= (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    @Override
    public void start() {

    }

    @Override
    public void shutdown(long l) {

    }

    @Override
    public void updateCorePoolSize(int i) {

    }

    @Override
    public void incCorePoolSize() {

    }

    @Override
    public void decCorePoolSize() {

    }

    @Override
    public int getCorePoolSize() {
        return 0;
    }

    @Override
    public ConsumeMessageDirectlyResult consumeMessageDirectly(MessageExt messageExt, String s) {
        return null;
    }

    @Override
    public void submitConsumeRequest(List<MessageExt> list, ProcessQueue processQueue, MessageQueue messageQueue, boolean b) {

    }
}
