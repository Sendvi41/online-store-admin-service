package com.epam.druzhinin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue}")
    private String queue;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public TopicExchange getTopicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue getQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding declareBinding() {
        return BindingBuilder.bind(getQueue()).to(getTopicExchange())
                .with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter getMessageConverter(final ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory factory, final Jackson2JsonMessageConverter getMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(getMessageConverter);
        return rabbitTemplate;
    }
}