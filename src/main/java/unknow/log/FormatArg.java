/**
 * 
 */
package unknow.log;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FormatArg implements Map<String, Object> {
	private Object[] a;
	private Log log;

	public void set(Log log, Object[] a) {
		this.log = log;
		this.a = a;
	}

	public void reset() {
		this.log = null;
		this.a = null;
	}

	@Override
	public int size() {
		return a.length + 4;
	}

	@Override
	public boolean isEmpty() {
		return a == null || log == null;
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
			return System.currentTimeMillis();
		if ("class".equals(key))
			return log.getName();
		if ("level".equals(key))
			return log.getLevel();
		if ("thread".equals(key))
			return Thread.currentThread().getName();
		try {
			int i = Integer.parseInt((String) key);
			return a[i];
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			return null;
		}
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
		return Arrays.asList(a);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}
}