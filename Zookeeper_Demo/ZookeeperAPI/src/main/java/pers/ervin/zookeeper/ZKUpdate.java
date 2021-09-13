package pers.ervin.zookeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKUpdate {
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
	 * @功能: 同步更新数据
	 */
	@Test
	public void update1() throws KeeperException, InterruptedException {
		/**
		 * 第一个参数：节点的路径
		 * 第二个参数：修改的数据
		 * 第三个参数：数据版本号 -1 代表版本号不参与更新
		 */
		Stat stat = zooKeeper.setData("/set/note1", "i love you".getBytes(), -1);
		System.out.println(stat);
	}

	/**
	 * @功能: 同步更新数据
	 */
	@Test
	public void update2() throws InterruptedException {
		/**
		 * 第一个参数：节点的路径
		 * 第二个参数：修改的数据
		 * 第三个参数：数据版本号 -1 代表版本号不参与更新
		 */
		zooKeeper.setData("/set/note1", "i love you2".getBytes(), -1, new AsyncCallback.StatCallback() {
			@Override
			public void processResult(int rc, String path, Object ctx, Stat stat) {
				// 0 代表创建成功
				System.out.println(rc);
				// 节点的路径
				System.out.println(path);
				// 上下文的参数
				System.out.println(ctx);
				// 当前节点的详细信息
				System.out.println(stat);
			}
		}, "i am context");
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
