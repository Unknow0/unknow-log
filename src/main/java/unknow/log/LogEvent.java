/**
 * 
 */
package unknow.log;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author unknow
 */
public class LogEvent implements Map<String, Object> {
	private long timestamp;
	private String name;
	private Level level;
	private String thread;
	private String msg;

	/**
	 * create new LogEvent
	 * 
	 * @param timestamp
	 * @param name
	 * @param level
	 * @param thread
	 */
	public LogEvent(long timestamp, String name, Level level, String thread, String msg) {
		this.timestamp = timestamp;
		this.name = name;
		this.level = level;
		this.thread = thread;
		this.msg = msg;
	}

	@Override
	public int size() {
		return 4;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(Object key) {
		if ("timestamp".equals(key))
			return timestamp;
		if ("name".equals(key))
			return name;
		if ("level".equals(key))
			return level;
		if ("thread".equals(key))
			return thread;
		if ("msg".equals(key))
			return msg;
		return null;
	}

	@Override
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}
}
