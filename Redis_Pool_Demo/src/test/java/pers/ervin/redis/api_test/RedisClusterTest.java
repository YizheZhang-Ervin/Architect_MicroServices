package pers.ervin.redis.api_test;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;

/*
 * 创建一个HashSet<HostAndPort>，用于保存集群中所有节点的机器名和端口号
 * 创建JedisPoolConfig对象，用于配置Redis连接池配置
 * 创建JedisCluster对象
 * 使用JedisCluster对象设置一个key，然后获取key对应的值
 */
public class RedisClusterTest {
    private JedisCluster jedisCluster;

    @BeforeTest
    public void beforeTest() {
        HashSet<HostAndPort> hostAndPortSet = new HashSet<>();
        hostAndPortSet.add(new HostAndPort("node1.xx.xx", 7001));
        hostAndPortSet.add(new HostAndPort("node1.xx.xx", 7002));
        hostAndPortSet.add(new HostAndPort("node2.xx.xx", 7001));
        hostAndPortSet.add(new HostAndPort("node2.xx.xx", 7002));
        hostAndPortSet.add(new HostAndPort("node3.xx.xx", 7001));
        hostAndPortSet.add(new HostAndPort("node3.xx.xx", 7002));

        // JedisPoolConfig配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 指定最大空闲连接为10个
        config.setMaxIdle(10);
        // 最小空闲连接5个
        config.setMinIdle(5);
        // 最大等待时间为3000毫秒
        config.setMaxWaitMillis(3000);
        // 最大连接数为50
        config.setMaxTotal(50);

        jedisCluster = new JedisCluster(hostAndPortSet, config);
    }

    @Test
    public void setTest() {
        jedisCluster.set("k2", "v2");
        System.out.println(jedisCluster.get("k2"));
    }

    @AfterTest
    public void afterTest() throws IOException {
        jedisCluster.close();
    }
}
