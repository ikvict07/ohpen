package com.ohpenl.midoffice.configurationtracker.problem.config;

import com.ohpenl.midoffice.configurationtracker.problem.advice.GeneralExceptionControllerAdvice;
import com.ohpenl.midoffice.configurationtracker.problem.exception.ServiceException;
import com.ohpenl.midoffice.configurationtracker.problem.processor.*;
import com.ohpenl.midoffice.configurationtracker.problem.registry.ProcessorsRegistry;
import jakarta.persistence.OptimisticLockException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Configuration
public class ExceptionHandlingConfiguration {

    @Bean
    public ProcessorsRegistry processorsRegistry() {
        return new ProcessorsRegistry();
    }

    @Bean
    public DefaultExceptionProcessor defaultExceptionProcessor(ProcessorsRegistry registry) {
        DefaultExceptionProcessor processor = new DefaultExceptionProcessor();
        registry.register(processor, Throwable.class);
        return processor;
    }

    @Bean
    public ServiceExceptionProcessor serviceExceptionProcessor(ProcessorsRegistry registry) {
        ServiceExceptionProcessor processor = new ServiceExceptionProcessor();
        registry.register(processor, ServiceException.class);
        return processor;
    }

    @Bean
    public IllegalArgumentExceptionProcessor illegalArgumentExceptionProcessor(ProcessorsRegistry registry) {
        IllegalArgumentExceptionProcessor processor = new IllegalArgumentExceptionProcessor();
        registry.register(processor, IllegalArgumentException.class);
        return processor;
    }

    @Bean
    public IllegalStateExceptionProcessor illegalStateExceptionProcessor(ProcessorsRegistry registry) {
        IllegalStateExceptionProcessor processor = new IllegalStateExceptionProcessor();
        registry.register(processor, IllegalStateException.class);
        return processor;
    }

    @Bean
    public MethodArgumentNotValidExceptionProcessor methodArgumentNotValidExceptionProcessor(ProcessorsRegistry registry) {
        MethodArgumentNotValidExceptionProcessor processor = new MethodArgumentNotValidExceptionProcessor();
        registry.register(processor, MethodArgumentNotValidException.class);
        return processor;
    }

    @Bean
    public HandlerMethodValidationExceptionProcessor handlerMethodValidationExceptionProcessor(ProcessorsRegistry registry) {
        HandlerMethodValidationExceptionProcessor processor = new HandlerMethodValidationExceptionProcessor();
        registry.register(processor, HandlerMethodValidationException.class);
        return processor;
    }

    @Bean
    public OptimisticLockExceptionProcessor optimisticLockExceptionProcessor(ProcessorsRegistry registry) {
        OptimisticLockExceptionProcessor processor = new OptimisticLockExceptionProcessor();
        registry.register(processor, OptimisticLockException.class);
        return processor;
    }

    @Bean
    public GeneralExceptionControllerAdvice generalExceptionControllerAdvice(ProcessorsRegistry registry) {
        return new GeneralExceptionControllerAdvice(registry);
    }
}
