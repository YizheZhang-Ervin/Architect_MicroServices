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

public class ZKWatcherExists {
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
			}
		});
		// 主线程阻塞等待连接对象的创建成功
		countDownLatch.await();
	}

	/**
	 * 使用检查节点是否存在的方法为节点注册watcher，使用连接对象的watcher
	 */
	@Test
	public void watcherExists1() throws KeeperException, InterruptedException {
		// 参数1：节点的路径
		// 参数2：使用连接对象的watcher
		zooKeeper.exists("/watcher1", true);
		TimeUnit.SECONDS.sleep(5);
	}

	/**
	 * 使用检查节点是否存在的方法为节点注册watcher，使用自定义的watcher
	 */
	@Test
	public void watcherExists2() throws KeeperException, InterruptedException {
		// 参数1：节点的路径
		// 参数2：使用自定义的watcher对象
		zooKeeper.exists("/watcher1", new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义watcher");
				System.out.println("path：" + event.getPath());
				System.out.println("evenType：" + event.getType());
				System.out.println("evenState：" + event.getState());
			}
		});
		TimeUnit.SECONDS.sleep(5);
	}

	/**
	 * 使用检查节点是否存在的方法为节点注册watcher，测试watcher的一次性
	 */
	@Test
	public void watcherExists3() throws KeeperException, InterruptedException {
		Watcher watcher = new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义watcher");
				System.out.println("path：" + event.getPath());
				System.out.println("evenType：" + event.getType());
				System.out.println("evenState：" + event.getState());
				try {
					// 在执行一次后再为该节点注册一次watcher，就可以实现循环注册
					zooKeeper.exists("/watcher1", this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		zooKeeper.exists("/watcher1", watcher);
		TimeUnit.SECONDS.sleep(25);
	}

	/**
	 * 使用检查节点是否存在的方法为节点注册多个watcher
	 */
	@Test
	public void watcherExists4() throws KeeperException, InterruptedException {
		zooKeeper.exists("/watcher1", new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义watcher 1");
				System.out.println("path：" + event.getPath());
				System.out.println("evenType：" + event.getType());
				System.out.println("evenState：" + event.getState());
			}
		});
		zooKeeper.exists("/watcher1", new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义watcher 2");
				System.out.println("path：" + event.getPath());
				System.out.println("evenType：" + event.getType());
				System.out.println("evenState：" + event.getState());
			}
		});
		TimeUnit.SECONDS.sleep(10);
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
