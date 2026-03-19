package com.juniverse.docprocessor.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String JOB_EXCHANGE = "job.exchange";
    public static final String JOB_ROUTING_KEY = "job.process";

    public static final String JOB_QUEUE = "job.queue";

    public static final String RETRY_EXCHANGE = "job.retry.exchange";
    public static final String RETRY_ROUTING_KEY = "job.retry";
    public static final String RETRY_QUEUE = "job.retry.queue";

    public static final String DLQ_EXCHANGE = "job.dlx";
    public static final String DLQ_ROUTING_KEY = "job.failed";
    public static final String DLQ_QUEUE = "job.dlq";

    @Bean
    public DirectExchange jobExchange() {
        return new DirectExchange(JOB_EXCHANGE);
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE);
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Queue jobQueue() {
        return new Queue(JOB_QUEUE, true);
    }

    @Bean
    public Queue retryQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        args.put("x-dead-letter-exchange", JOB_EXCHANGE);
        args.put("x-dead-letter-routing-key", JOB_ROUTING_KEY);

        return new Queue(RETRY_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(DLQ_QUEUE, true);
    }
    @Bean
    public Binding jobBinding() {
        return BindingBuilder.bind(jobQueue())
                .to(jobExchange())
                .with(JOB_ROUTING_KEY);
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryQueue())
                .to(retryExchange())
                .with(RETRY_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlqExchange())
                .with(DLQ_ROUTING_KEY);
    }
}