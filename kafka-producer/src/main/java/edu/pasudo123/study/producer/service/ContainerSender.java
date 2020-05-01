package edu.pasudo123.study.producer.service;

import edu.pasudo123.study.producer.config.CustomKafkaProperties;
import edu.pasudo123.study.producer.pojo.AtomicNumber;
import edu.pasudo123.study.producer.pojo.NameGenerator;
import edu.pasudo123.study.common.container.Container;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerSender {

    private final NameGenerator nameGenerator;
    private final AtomicNumber number;
    private final CustomKafkaProperties customProps;
    private final KafkaTemplate<String, Container> kafkaTemplate;

    public void send(Container container) {

        if(number.isMultiple50()){
            log.info("현재 시간 : {}", LocalDateTime.now());
        }

        final Message<Container> message = MessageBuilder
                .withPayload(container)
                .setHeader(KafkaHeaders.TOPIC, customProps.getTopic())
                .build();

        kafkaTemplate.send(message);
    }

    @Scheduled(fixedRate = 300L)
    public void schedulingTask(){
        final LocalTime currentTime = LocalTime.now();

        final Container container = Container.builder()
                .currentNumber(number.getCurrentNumber())
                .name(nameGenerator.getName())
                .hh(currentTime.getHour())
                .mm(currentTime.getMinute())
                .ss(currentTime.getSecond())
                .build();

        send(container);
    }
}