package pers.ervin;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

// 修改节点

public class CuratorSet {
	CuratorFramework client;
	@Before
	public void before() {
		// 创建连接对象
		client = CuratorFrameworkFactory
				.builder()
				// IP地址端口号
				.connectString("39.98.67.88:2181,39.98.67.88:2182,39.98.67.88:2183")
				// 会话超时时间
				.sessionTimeoutMs(5000)
				// 重连机制
				// new RetryOneTime(3000)：每三秒重连一次，只重连一次
				// new RetryNTimes(3, 3000)：每每三秒重连一次，共重连3次
				// new RetryUntilElapsed(10000, 3000)：每三秒重连一次，10秒后停止重连
				// new ExponentialBackoffRetry(1000, 3)：重连3次，每次重连的间隔会越来越长
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				// 命名空间
				.namespace("set")
				// 构建连接对象
				.build();
		// 打开连接
		client.start();
	}

	@Test
	public void set1() throws Exception {
		Stat stat = client.setData()
				// 第一个参数：节点路径
				// 第二个参数：节点的数据
				.forPath("/node1", "hello".getBytes());
		System.out.println(stat);
	}


	@Test
	public void set2() throws Exception {
		Stat stat = client.setData()
				// 版本号 -1 标识忽略版本号
				.withVersion(1)
				.forPath("/node1", "world".getBytes());
		System.out.println(stat);
	}

	@Test
	public void set3() throws Exception {
		client.setData()
				.withVersion(2)
				.inBackground(new BackgroundCallback() {
					@Override
					public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
						System.out.println(curatorEvent.getPath());
						System.out.println(curatorEvent.getType());
					}
				})
				.forPath("/node1", "哈哈哈".getBytes());
		TimeUnit.SECONDS.sleep(1);
	}

	@After
	public void after() {
		if (client != null) {
			client.close();
		}
	}
}
