package cn.corner.web.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
@Slf4j
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MyQueue queue;

    @Autowired
    private DeferredResultHolder holder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(() -> {
            while(true){
                if(StringUtils.isNotBlank(queue.getCompleteOrder())){
                    String order = queue.getCompleteOrder();
                    log.info("返回订单处理结果,"+order);
                    DeferredResult<String> result = holder.getMap().get(order);
                    result.setResult("place order success");
                    queue.setCompleteOrder(null);
                    holder.getMap().remove(order);
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
