package cn.corner.web.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

@RestController
@Slf4j
public class AsyncController {

    @Autowired
    private DeferredResultHolder holder;

    @Autowired
    private MyQueue queue;

    @RequestMapping("/order")
    public Callable<String> order() throws InterruptedException {
        log.info("主线程开始");
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("副线程开始");
                Thread.sleep(1000);
                log.info("副线程返回");
                return "success";
            }
        };
        log.info("主线程返回");
        return result;
    }

    @RequestMapping("/order2")
    public DeferredResult<String> order2() throws InterruptedException {
        log.info("主线程开始");
        String orderNum = RandomStringUtils.randomNumeric(8);
        DeferredResult<String> result = new DeferredResult<>();
        queue.setPlaceOrder(orderNum);
        holder.getMap().put(orderNum,result);
        log.info("主线程返回");
        return result;
    }
}
