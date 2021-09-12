package pers.ervin;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

// 事件监听机制
public class CuratorWatcher {
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
				// 构建连接对象
				.build();
		// 打开连接
		client.start();
	}

	@Test
	public void watcher1() throws Exception {
		// 参数1：连接对象
		// 参数2：监视的节点路径
		final NodeCache nodeCache = new NodeCache(client, "/watcher1");
		// 启动解释器对象
		nodeCache.start();
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			// 节点变化时回调的方法
			@Override
			public void nodeChanged() throws Exception {
				System.out.println(nodeCache.getCurrentData().getPath());
				System.out.println(new String(nodeCache.getCurrentData().getData()));
				System.out.println(nodeCache.getCurrentData().getStat());
			}
		});
		TimeUnit.SECONDS.sleep(30);
		// 关闭监视器对象
		nodeCache.close();
	}

	@Test
	public void watcher2() throws Exception {
		// 参数1：连接对象
		// 参数2：监视的节点路径
		// 参数3：事件中是否可以获取节点的数据
		final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/watcher1", true);
		// 启动解释器对象
		pathChildrenCache.start();
		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			// 当子节点方法变化时回调的方法
			@Override
			public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
				// 节点的事件类型
				System.out.println(pathChildrenCacheEvent.getType());
				// 节点的路径
				System.out.println(pathChildrenCacheEvent.getData().getPath());
				// 节点的数据
				System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
			}
		});
		TimeUnit.SECONDS.sleep(30);
		// 关闭监视器对象
		pathChildrenCache.close();
	}

	@After
	public void after() {
		if (client != null) {
			client.close();
		}
	}
}
