package edu.pasudo123.study.consumer.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka.consumer.props")
public class CustomKafkaProperties {
    private String topic;
    private List<String> bootstrapServers;
    private String groupId;
    private String offset;
    private Integer maxPollRecords;
}
