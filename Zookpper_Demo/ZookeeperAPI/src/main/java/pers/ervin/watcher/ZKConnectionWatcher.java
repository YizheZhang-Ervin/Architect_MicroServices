package pers.ervin.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZKConnectionWatcher implements Watcher {
	// 计数器对象
	private static CountDownLatch countDownLatch = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {
		try {
			// 时间类型
			if (event.getType() == Event.EventType.None) {
				if (event.getState() == Event.KeeperState.SyncConnected) {
					System.out.println("连接创建成功");
					countDownLatch.countDown();
				} else if (event.getState() == Event.KeeperState.Disconnected) {
					System.out.println("断开连接");
				} else if (event.getState() == Event.KeeperState.Expired) {
					System.out.println("会话超时");
				} else if (event.getState() == Event.KeeperState.AuthFailed) {
					System.out.println("认证失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// 连接对象
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper("39.98.67.88:2181", 5000, new ZKConnectionWatcher());
			// 线程阻塞等待连接的创建
			countDownLatch.await();
			System.out.println(zooKeeper.getSessionId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zooKeeper != null) {
				try {
					zooKeeper.close();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
