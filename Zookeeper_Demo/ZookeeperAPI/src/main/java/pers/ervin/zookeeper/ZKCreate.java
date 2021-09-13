package pers.ervin.zookeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKCreate {
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
	 * 同步创建节点
	 */
	@Test
	public void create1() throws KeeperException, InterruptedException {
		/**
		 * 第一个参数：节点的路径
		 * 第二个参数：节点的数据
		 * 第三个参数：权限列表 ZooDefs.Ids.OPEN_ACL_UNSAFE:world:anyone:cdrwa /
		 * 第四个参数：节点的类子持久化节点
		 */
		zooKeeper.create("/hello/gzh", "helloworld".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	/**
	 * world授权模式
	 */
	@Test
	public void als1() throws KeeperException, InterruptedException {
		// world授权模式
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("world", "anyone");
		// 权限设置
		acls.add(new ACL(ZooDefs.Perms.READ, id));
		acls.add(new ACL(ZooDefs.Perms.WRITE, id));
		zooKeeper.create("/hello/cl", "cl".getBytes(), acls, CreateMode.PERSISTENT);
	}

	/**
	 * IP授权模式
	 */
	@Test
	public void als2() throws Exception {
		// IP授权模式
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("ip", "0.0.0.0");
		// 权限设置
		acls.add(new ACL(ZooDefs.Perms.ALL, id));
		zooKeeper.create("/hello/alsj", "cl".getBytes(), acls, CreateMode.PERSISTENT);
	}

	/**
	 * auth授权模式
	 */
	@Test
	public void als3() throws Exception {
		// auth授权模式
		// 添加授权用户
		zooKeeper.addAuthInfo("digest", "root:070313".getBytes());
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("auth", "root");
		// 权限设置
		acls.add(new ACL(ZooDefs.Perms.ALL, id));
		zooKeeper.create("/hello/eh", "eel".getBytes(), acls, CreateMode.PERSISTENT);
	}

	/**
	 * digest授权模式
	 */
	@Test
	public void als4() throws Exception {
		// digest授权模式
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("digest", "root:4d9PWRXHtxrRSgCIGCixNUZdTPQ=");
		// 权限设置
		acls.add(new ACL(ZooDefs.Perms.ALL, id));
		zooKeeper.create("/hello/cjk", "cls".getBytes(), acls, CreateMode.PERSISTENT);
	}

	/**
	 * 异步创建节点
	 */
	@Test
	public void create2() throws KeeperException, InterruptedException {
		/**
		 * 第一个参数：节点的路径
		 * 第二个参数：节点的数据
		 * 第三个参数：权限列表 ZooDefs.Ids.OPEN_ACL_UNSAFE:world:anyone:cdrwa /
		 * 第四个参数：节点的类子持久化节点
		 * 第五个参数：异步回调接口
		 * 第六个参数：上下文参数
		 */
		zooKeeper.create("/hello/xjp", "zhuxi".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
			@Override
			public void processResult(int rc, String path, Object ctx, String name) {
				// 0 代表创建成功
				System.out.println(rc);
				// 节点的路径
				System.out.println(path);
				// 上下文的参数
				System.out.println(ctx);
				// 节点的路径
				System.out.println(name);
			}
		}, "i am context");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("创建完成");
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
