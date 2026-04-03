package com.ohpenl.midoffice.configurationtracker.problem.registry;

import com.ohpenl.midoffice.configurationtracker.problem.processor.ExceptionProcessor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ProcessorsRegistry {
    private final Map<Class<?>, ExceptionProcessor<?>> processors = new LinkedHashMap<>();

    public void register(ExceptionProcessor<?> processor, Class<?> exceptionClass) {
        processors.put(exceptionClass, processor);
    }

    @SuppressWarnings("unchecked")
    public <T extends Throwable> ExceptionProcessor<Throwable> getProcessor(Class<T> exceptionClass) {
        Class<?> matching = processors.keySet().stream()
            .filter(key -> key.isAssignableFrom(exceptionClass) && !key.equals(Throwable.class))
            .findFirst()
            .orElse(null);
        return (ExceptionProcessor<Throwable>) processors.get(Objects.requireNonNullElse(matching, Throwable.class));
    }
}
