package pers.ervin;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

// 标题：事务
public class CuratorTransaction {
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
				.namespace("create")
				// 构建连接对象
				.build();
		// 打开连接
		client.start();
	}

	@Test
	public void transaction1() throws Exception {
		// 开启事务
		Collection<CuratorTransactionResult> collection = client.inTransaction()
				.create()
				.withMode(CreateMode.PERSISTENT)
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
				.forPath("/node1", "node1".getBytes())
				.and()
				.create().forPath("/node2", "node2".getBytes())
				.and()
				// 提交事务
				.commit();
		System.out.println(collection);
	}

	@After
	public void after() {
		if (client != null) {
			client.close();
		}
	}
}
