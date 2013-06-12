package org.powerbot.script.internal;

import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScriptExecutor extends ThreadPoolExecutor {
	private ScriptHandler handler;

	public ScriptExecutor(ScriptHandler handler) {
		super(0, Integer.MAX_VALUE, 60l, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		allowCoreThreadTimeOut(true);
		setThreadFactory(new GroupedThreadFactory(new ThreadGroup(getClass().getName() + "@" + Integer.toHexString(hashCode()))));
		this.handler = handler;
	}

	@Override
	public void shutdown() {
		if (!handler.isStopping()) {
			handler.stop();
		}
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		if (!handler.isStopping()) {
			handler.stop();
		}
		return super.shutdownNow();
	}

	private final class GroupedThreadFactory implements ThreadFactory {
		private final String id;
		private final ThreadGroup threadGroup;
		private final AtomicInteger c;

		private GroupedThreadFactory(final ThreadGroup threadGroup) {
			this.id = getClass().getName() + "@" + Integer.toHexString(hashCode());
			this.threadGroup = threadGroup;
			this.c = new AtomicInteger(0);
		}

		@Override
		public Thread newThread(final Runnable r) {
			return new Thread(threadGroup, r, id + c.incrementAndGet());
		}
	}
}
