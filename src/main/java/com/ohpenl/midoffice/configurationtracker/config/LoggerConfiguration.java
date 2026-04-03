package com.ohpenl.midoffice.configurationtracker.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Method;

@Configuration
class LoggerConfiguration {

    @Bean
    public LoggerFactory loggerFactory() {
        return new LoggerFactory();
    }

    @Bean
    @Primary
    @Scope("prototype")
    @Qualifier("logger")
    public Logger logger(LoggerFactory loggerFactory, ObjectProvider<InjectionPoint> injectionPointProvider) {
        return loggerFactory.createLogger(targetingClass(injectionPointProvider));
    }

    private static Class<?> targetingClass(ObjectProvider<InjectionPoint> provider) {
        InjectionPoint injectionPoint;
        try {
            injectionPoint = provider.getIfAvailable();
        } catch (Exception e) {
            injectionPoint = null;
        }
        if (injectionPoint == null) {
            return LoggerConfiguration.class;
        }
        Method method = injectionPoint.getMethodParameter() != null
            ? injectionPoint.getMethodParameter().getMethod()
            : null;
        boolean isSetter = method != null && method.getName().startsWith("set");
        if (method == null || isSetter) {
            return injectionPoint.getMember().getDeclaringClass();
        }
        return method.getReturnType();
    }

    public static class LoggerFactory {
        public Logger createLogger(Class<?> clazz) {
            return LogManager.getLogger(clazz);
        }
    }
}
