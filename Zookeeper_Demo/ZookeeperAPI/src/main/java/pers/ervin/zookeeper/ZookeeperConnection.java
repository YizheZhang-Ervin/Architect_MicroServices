package pers.ervin.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 标题：连接zookeeper
 */
public class ZookeeperConnection {
	public static void main(String[] args) {
		ZooKeeper zooKeeper = null;
		try {
			// 计数器对象
			CountDownLatch countDownLatch = new CountDownLatch(1);
			/**
			 * 第一个参数：服务器的ip和端口
			 * 第二个参数：客户端与服务器之间的会话超时时间，以毫秒为单位
			 * 第三个参数：监视器对象
			 */
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
			// 打印会话编号
			System.out.println(zooKeeper.getSessionId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zooKeeper != null) {
					// 关闭连接
					zooKeeper.close();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
