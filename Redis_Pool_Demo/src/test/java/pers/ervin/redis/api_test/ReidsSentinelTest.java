package pers.ervin.redis.api_test;

import org.junit.After;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/*
 *	构建JedisPoolConfig配置对象
 *	创建一个HashSet，用来保存哨兵节点配置信息（记得一定要写端口号）
 *	构建JedisSentinelPool连接池
 *	使用sentinelPool连接池获取连接
 */
public class ReidsSentinelTest {

    private JedisSentinelPool jedisSentinelPool;

    @BeforeTest
    public void beforeTest() {
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

        HashSet<String> sentinelSet = new HashSet<>();
        sentinelSet.add("node1.xx.xx:26379");
        sentinelSet.add("node2.xx.xx:26379");
        sentinelSet.add("node3.xx.xx:26379");

        jedisSentinelPool = new JedisSentinelPool("mymaster", sentinelSet, config);
    }

    @Test
    public void keysTest() {
        // 1. 要操作Redis，肯定要获取Redis连接。现在通过哨兵连接池来获取连接
        Jedis jedis = jedisSentinelPool.getResource();

        // 2. 执行keys操作
        Set<String> keySet = jedis.keys("*");

        // 3. 遍历所有key
        for (String key : keySet) {
            System.out.println(key);
        }

        // 4. 再将连接返回到连接池
        jedis.close();
    }

    @AfterTest
    public void afterTest() {
        jedisSentinelPool.close();
    }
}
