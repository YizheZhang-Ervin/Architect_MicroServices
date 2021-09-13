package pers.ervin.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKWatcherGetData {
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
				System.out.println("path：" + event.getPath());
				System.out.println("evenType：" + event.getType());
				System.out.println("evenState：" + event.getState());
			}
		});
		// 主线程阻塞等待连接对象的创建成功
		countDownLatch.await();
	}

	/**
	 * 使用查询节点信息的方法为节点注册watcher，使用连接对象的watcher
	 */
	@Test
	public void watcherGetData1() throws KeeperException, InterruptedException {
		// 参数1：节点的路径
		// 参数2：使用连接对象的watcher
		zooKeeper.getData("/watcher2", true, null);
		TimeUnit.SECONDS.sleep(10);
	}

	/**
	 * 使用查询节点信息的方法为节点注册watcher，使用自定义的watcher
	 */
	@Test
	public void watcherGetData2() throws KeeperException, InterruptedException {
		// 参数1：节点的路径
		// 参数2：使用自定义的watcher
		zooKeeper.getData("/watcher2", new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义Watcher");
				System.out.println("path：" + event.getPath());
				System.out.println("evenType：" + event.getType());
				System.out.println("evenState：" + event.getState());
			}
		}, null);
		TimeUnit.SECONDS.sleep(10);
	}

	/**
	 * 使用查询节点信息的方法为节点注册watcher，并验证watcher是一次性的
	 * 略
	 */

	/**
	 * 使用查询节点信息的方法为节点注册多个watcher
	 * 略
	 */

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
