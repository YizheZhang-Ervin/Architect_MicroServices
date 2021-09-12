package pers.ervin;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CuratorCreate {
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
	public void create1() throws Exception {
		String path = client.create()
				// 节点的类型
				.withMode(CreateMode.PERSISTENT)
				// 节点的权限列表 world:anyone:cdrwa
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
				// 第一个参数：节点的数据
				// 第二个参数：节点的数据
				.forPath("/node1", "node1".getBytes());
		System.out.println(path);
	}

	@Test
	public void create2() throws Exception {
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("ip", "39.98.67.88");
		acls.add(new ACL(ZooDefs.Perms.ALL, id));
		String path = client.create()
				.withMode(CreateMode.PERSISTENT)
				.withACL(acls)
				.forPath("/node2", "node2".getBytes());
		System.out.println(path);
	}

	@Test
	public void create3() throws Exception {
		String path = client.create()
				// 支持递归节点的创建
				.creatingParentsIfNeeded()
				.withMode(CreateMode.PERSISTENT)
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
				.forPath("/node3/node31", "node31".getBytes());
		System.out.println(path);
	}

	@Test
	public void create4() throws Exception {
		client.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.PERSISTENT)
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
				.inBackground(new BackgroundCallback() {
					@Override
					public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
						// 节点的路径
						System.out.println(curatorEvent.getPath());
						// 事件类型
						System.out.println(curatorEvent.getType());
					}
				})
				.forPath("/node4", "node4".getBytes());
		TimeUnit.SECONDS.sleep(1);
	}

	@After
	public void after() {
		if (client != null) {
			client.close();
		}
	}
}
