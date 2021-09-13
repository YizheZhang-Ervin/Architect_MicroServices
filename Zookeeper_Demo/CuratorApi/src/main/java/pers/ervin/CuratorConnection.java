package pers.ervin;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorConnection {
	public static void main(String[] args) {
		// 创建连接对象
		CuratorFramework client = CuratorFrameworkFactory
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
				// 命名空间
				.namespace("create")
				// 构建连接对象
				.build();
		// 打开连接
		client.start();
		// 关闭连接
		client.close();
	}
}
