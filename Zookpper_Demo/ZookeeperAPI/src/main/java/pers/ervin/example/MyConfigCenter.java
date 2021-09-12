package pers.ervin.example;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 标题：配置中心案例
 */
public class MyConfigCenter implements Watcher {
	// zk连接对象
	ZooKeeper zooKeeper;
	// 计数器对象
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	// zk的连接串
	private String ip = "39.98.67.88:2181";
	// 用于本地化配置存储信息
	private String url;
	private String username;
	private String password;

	// 构造方法
	public MyConfigCenter() {
		initValue();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
			} else if (event.getType() == Event.EventType.NodeDataChanged) { // 当配置信息发生变化，就重新去加载配置信息
				initValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接zookeeper服务器，读取配置信息
	 */
	private void initValue() {
		//创建连对象
		try {
			// 创建链接
			if (zooKeeper == null) {
				zooKeeper = new ZooKeeper(ip, 5000, this);
			}
			// 阻塞线程，等待连接成功
			countDownLatch.await();
			// 读取配置信息
			this.url = new String(zooKeeper.getData("/config/url", true, null));
			this.username = new String(zooKeeper.getData("/config/username", true, null));
			this.password = new String(zooKeeper.getData("/config/password", true, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		try {
			MyConfigCenter myConfigCenter = new MyConfigCenter();
			for (int i = 0; i < 10; i++) {
				TimeUnit.SECONDS.sleep(5);
				System.out.println(myConfigCenter);
				System.out.println("==================================================");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "MyConfigCenter{" +
				"url='" + url + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
