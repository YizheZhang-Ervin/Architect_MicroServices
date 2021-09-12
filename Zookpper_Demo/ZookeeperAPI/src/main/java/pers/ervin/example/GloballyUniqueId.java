package pers.ervin.example;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式唯一Id案例
 */
public class GloballyUniqueId implements Watcher {
	// zk连接对象
	ZooKeeper zooKeeper;
	// 计数器对象
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	// zk的连接串
	private String ip = "39.98.67.88:2181";
	//  用户生成序号的节点
	String defaultPath = "/uniqueId";

	/**
	 * 构造方法
	 */
	public GloballyUniqueId() {
		try {
			// 打开连接对象
			zooKeeper = new ZooKeeper(ip, 5000, this);
			// 阻塞线程
			countDownLatch.await();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			// 时间类型
			if (event.getType() == Watcher.Event.EventType.None) {
				if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
					System.out.println("连接创建成功");
					countDownLatch.countDown();
				} else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
					System.out.println("断开连接");
				} else if (event.getState() == Watcher.Event.KeeperState.Expired) {
					System.out.println("会话超时");
					zooKeeper = new ZooKeeper(ip, 5000, this); // 重连
				} else if (event.getState() == Watcher.Event.KeeperState.AuthFailed) {
					System.out.println("验证失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成ID的方法
	 */
	public String getUnqiueId() {
		String path = "";
		try {
			path = zooKeeper.create(defaultPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path.substring(9);
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		GloballyUniqueId globallyUniqueId = new GloballyUniqueId();
		for (int i = 0; i < 5; i++) {
			String unqiueId = globallyUniqueId.getUnqiueId();
			System.out.println(unqiueId);
		}
	}
}
