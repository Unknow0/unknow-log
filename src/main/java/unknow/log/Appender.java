/**
 * 
 */
package unknow.log;

import java.io.Flushable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import unknow.log.format.Out;

/**
 * @author unknow
 */
public class Appender extends Thread {
	private final String format;
	private final Appendable out;

	private final BlockingQueue<LogEvent> pending = new LinkedBlockingQueue<>();

	public Appender(String format, Appendable out) {
		super("Appender");
		setDaemon(true);
		this.format = format;
		this.out = out;

		Runtime.getRuntime().addShutdownHook(new Close());
		start();
	}

	public void append(LogEvent e) {
		pending.offer(e);
	}

	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				LogEvent event = pending.take();
				try {
					Out.format(out, format, event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) { // OK
		}
	}

	private class Close extends Thread {
		@Override
		public void run() {
			Appender.this.interrupt();
			try {
				Appender.this.join();
			} catch (InterruptedException e) {
			}
			if (out instanceof Flushable) {
				try {
					((Flushable) out).flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
