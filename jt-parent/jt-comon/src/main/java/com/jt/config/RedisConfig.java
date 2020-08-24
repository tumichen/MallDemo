package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration//配置类
@PropertySource(value = "classpath:/properties/redis.properties")
public class RedisConfig {

    @Value("${redis.nodes}")
    private String nodes;

    /**
     * xml配置文件,添加bean标签
     */
    @Bean
    public JedisCluster jedisCluster() {
        Set<HostAndPort> nodes = getNodes();
        return new JedisCluster(nodes);
    }

    private Set<HostAndPort> getNodes() {
        HashSet<HostAndPort> nodeSets = new HashSet<>();
        String[] strNode = nodes.split(",");
        for (String node : strNode) {
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort = new HostAndPort(host,port);
            nodeSets.add(hostAndPort);
        }
        return nodeSets;
    }

}
