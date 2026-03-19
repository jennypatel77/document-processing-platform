package com.juniverse.docprocessor.messaging;

import com.juniverse.docprocessor.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JobProducer {

    private static final Logger log = LoggerFactory.getLogger(JobProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public JobProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJob(JobMessage jobMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.JOB_EXCHANGE,
                RabbitMQConfig.JOB_ROUTING_KEY,
                jobMessage
        );
        log.info("Published job to main queue jobId={} traceId={}", jobMessage.getJobId(), jobMessage.getTraceId());
    }

    public void sendToRetry(JobMessage jobMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RETRY_EXCHANGE,
                RabbitMQConfig.RETRY_ROUTING_KEY,
                jobMessage
        );
        log.info("Published job to retry queue jobId={} retryCount={}", jobMessage.getJobId(), jobMessage.getRetryCount());
    }

    public void sendToDlq(JobMessage jobMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DLQ_EXCHANGE,
                RabbitMQConfig.DLQ_ROUTING_KEY,
                jobMessage
        );
        log.warn("Published job to DLQ jobId={} retryCount={}", jobMessage.getJobId(), jobMessage.getRetryCount());
    }
}