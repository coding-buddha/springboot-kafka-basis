package edu.pasudo123.study.consumer.config;


import edu.pasudo123.study.common.dto.Employee;
import edu.pasudo123.study.consumer.props.CustomKafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class EmployeeConsumerConfig {

    private final CustomKafkaProperties customKafkaProperties;

    @SuppressWarnings("Duplicates")
    @Bean
    public ConsumerFactory<String, Employee> employeeConsumerFactory() {
        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, customKafkaProperties.getBootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, customKafkaProperties.getGroupId());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, customKafkaProperties.getOffset());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // 컨슈머 측에서 poll() 단일호출하여 들고 올 수 있는 최대레코드 수 (디폴트 : 500)
        /** max.poll.records 값을 25으로 설정해두었다. **/
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, customKafkaProperties.getMaxPollRecords());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>(Employee.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Employee> employeeContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, Employee> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(employeeConsumerFactory());
        factory.setBatchListener(true);                             // max.poll.records 개수만큼 목록을 획득한다.
        return factory;
    }
}
