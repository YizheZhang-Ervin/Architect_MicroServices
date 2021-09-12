package pers.ervin.zookeeper;

import org.apache.zookeeper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKGetChildren {
	ZooKeeper zooKeeper = null;

	/**
	 * 获取连接
	 */
	@Before
	public void befor() throws Exception {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		zooKeeper = new ZooKeeper("39.98.67.88:2181", 5000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == Event.KeeperState.SyncConnected) {
					System.out.println("连接创建成功");
					countDownLatch.countDown();
				}
			}
		});
		// 主线程阻塞等待连接对象的创建成功
		countDownLatch.await();
	}

	/**
	 * @功能: 同步查询子节点
	 */
	@Test
	public void getChildren1() throws KeeperException, InterruptedException {
		/**
		 * 第一个参数：节点的路径
		 * 第二个参数：是否启用监控 这里为false即可
		 */
		List<String> children = zooKeeper.getChildren("/hello", false);
		children.forEach(System.out::println);
	}

	/**
	 * @功能: 异步查询子节点
	 */
	@Test
	public void getChildren2() throws InterruptedException {
		/**
		 * 第一个参数：节点的路径
		 * 第二个参数：是否启用监控 这里为false即可
		 * 第三个参数：异步回调接口
		 * 第四个参数：上下文参数
		 */
		zooKeeper.getChildren("/hello", false, new AsyncCallback.ChildrenCallback() {
			@Override
			public void processResult(int rc, String path, Object ctx, List<String> children) {
				// 0代表读取成功
				System.out.println(rc);
				// 节点的路径
				System.out.println(path);
				// 上下文参数
				System.out.println(ctx);
				// 子节点集合
				children.forEach(System.out::println);
			}
		}, "我是上下文参数");
		TimeUnit.SECONDS.sleep(1);
	}


	/**
	 * 关闭连接
	 */
	@After
	public void after() throws InterruptedException {
		if (zooKeeper != null) {
			System.out.println("关闭成功");
			zooKeeper.close();
		}
	}
}
