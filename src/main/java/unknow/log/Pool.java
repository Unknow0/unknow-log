/**
 * 
 */
package unknow.log;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

/**
 * @author unknow
 */
public abstract class Pool<T> {
	private final Queue<T> queue = new LinkedBlockingQueue<>();

	public T get() {
		T t = queue.poll();
		if (t == null)
			t = create();
		return t;
	}

	public void put(T t) {
		reset(t);
		queue.offer(t);
	}

	protected abstract T create();

	protected abstract void reset(T t);

	public static final Pool<StringBuilder> SB = new Pool<StringBuilder>() {
		@Override
		protected StringBuilder create() {
			return new StringBuilder();
		}

		@Override
		protected void reset(StringBuilder t) {
			t.setLength(0);
		}
	};
}
