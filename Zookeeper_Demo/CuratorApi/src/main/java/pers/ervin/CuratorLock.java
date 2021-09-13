package pers.ervin;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

// 分布式锁

public class CuratorLock {
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
	public void lock1() throws Exception {
		// 参数1：连接对象
		// 参数2：节点路径
		InterProcessLock interProcessLock = new InterProcessMutex(client, "/lock1");
		System.out.println("等待获取锁对象");
		// 获取锁
		interProcessLock.acquire();
		for (int i = 0; i < 5; i++) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println(i);
		}
		System.out.println("等待释放锁");
		// 释放锁
		interProcessLock.release();
	}

	@Test
	public void lock2() throws Exception {
		// 读写锁
		InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(client, "/lock1");
		// 获取读锁对象
		InterProcessMutex interProcessMutex = readWriteLock.readLock();
		System.out.println("等待获取锁对象");
		// 获取锁
		interProcessMutex.acquire();
		for (int i = 0; i < 10; i++) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println(i);
		}
		System.out.println("等待释放锁");
		// 释放锁
		interProcessMutex.release();
	}

	@Test
	public void lock3() throws Exception {
		// 读写锁
		InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(client, "/lock1");
		// 获取读锁对象
		InterProcessMutex interProcessMutex = readWriteLock.writeLock();
		System.out.println("等待获取锁对象");
		// 获取锁
		interProcessMutex.acquire();
		for (int i = 0; i < 10; i++) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println(i);
		}
		System.out.println("等待释放锁");
		// 释放锁
		interProcessMutex.release();
	}

	@After
	public void after() {
		if (client != null) {
			client.close();
		}
	}
}
