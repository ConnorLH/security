package cn.corner.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;

import java.util.concurrent.Callable;

@Slf4j
@Component
public class MyCallableInterceptor implements CallableProcessingInterceptor {
    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
        log.info("MyCallableInterceptor--beforeConcurrentHandling");
    }

    @Override
    public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
        log.info("MyCallableInterceptor--preProcess");
    }

    @Override
    public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
        log.info("MyCallableInterceptor--postProcess");
    }

    @Override
    public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
        log.info("MyCallableInterceptor--afterCompletion");
    }
}
