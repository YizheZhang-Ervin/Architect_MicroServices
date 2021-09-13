package pers.ervin.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 标题：分布式锁创建节点
 */
public class MyLock {
	// zk连接对象
	public static ZooKeeper zooKeeper;
	// 计数器对象
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	// zk的连接串
	private static String ip = "39.98.67.88:2181";
	//  锁的节点名称
	public static final String LOCK_ROOT_PATH = "/Locks";
	public static final String LOCK_NODE_NAME = LOCK_ROOT_PATH + "/lock_";
	private String lockPath;

	private Watcher watcher = new Watcher() {
		@Override
		public void process(WatchedEvent event) {
			if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
				synchronized (this) {
					notifyAll();
				}
			}
		}
	};


	/**
	 * 构造方法
	 */
	public MyLock() {
		try {
			// 打开zookeeper连接
			zooKeeper = new ZooKeeper(ip, 5000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.None) {
						if (event.getState() == Event.KeeperState.SyncConnected) {
							System.out.println("连接成功");
							countDownLatch.countDown();
						}
					}
				}
			});
			countDownLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取锁
	 */
	public void acquireLock() {
		//创建锁节点
		createLock();
		//尝试获取锁
		attemptLock();
	}

	/**
	 * 创建锁节点
	 */
	private void createLock() {
		// 判断/Locks是否存在，不存在则创建
		try {
			Stat exists = zooKeeper.exists(LOCK_ROOT_PATH, false);
			if (exists == null) {
				zooKeeper.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			// 创建临时有序节点
			lockPath = zooKeeper.create(LOCK_NODE_NAME, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			System.out.println("节点创建成功：" + lockPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 尝试获取锁
	 */
	private void attemptLock() {
		try {
			// 获取/Locks节点下的所有子节点
			List<String> childrens = zooKeeper.getChildren(LOCK_ROOT_PATH, false);
			// 对子节点进行排序
			Collections.sort(childrens);
			int index = childrens.indexOf(lockPath.substring((LOCK_NODE_NAME.length() - LOCK_ROOT_PATH.length()) + 1));
			System.out.println(index);
			if (index == 0) {
				System.out.println("获取锁成功");
				return;
			} else {
				// 上一个节点的路径
				String path = childrens.get(index - 1);
				Stat exists = zooKeeper.exists(LOCK_ROOT_PATH + "/" + path, watcher);
				if (exists == null) {
					attemptLock();
				} else {
					synchronized (watcher) {
						watcher.wait();
					}
					attemptLock();
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放锁
	 */
	private void releaseLock() {
		try {
			// 删除临时有序节点
			zooKeeper.delete(this.lockPath, -1);
			zooKeeper.close();
			System.out.println("锁已释放：" + lockPath);
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		MyLock myLock = new MyLock();
		// 获取锁
		myLock.acquireLock();
		sall();
		// 释放锁
		myLock.releaseLock();
	}

	public static void sall() {
		System.out.println("售票开始");
		// 线程休眠，模拟现实中耗时的操作
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("售票结束");
	}
}