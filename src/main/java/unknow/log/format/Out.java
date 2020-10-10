/**
 * 
 */
package unknow.log.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

import unknow.log.Pool;

/**
 * @author unknow
 */
public class Out {
	private static final int TEXT = 0;
	private static final int INDEX = 1;
	private static final int TYPE = 2;
	private static final int STYLE = 3;
	private static final int QUOTE = 4;

	private static final Map<String, Formatter> formats = new HashMap<>();
	static {
		for (Formatter f : ServiceLoader.load(Formatter.class))
			formats.put(f.getType(), f);
	}

	public static void format(Appendable out, CharSequence format) throws IOException {
		format(out, format, Collections.emptyMap());
	}

	public static void format(Appendable out, CharSequence format, Object... arg) throws IOException {
		format(out, format, new ArrayToMap(arg));
	}

	public static void format(Appendable out, CharSequence format, Map<String, ?> arg) throws IOException {
		StringBuilder sb = Pool.SB.get();
		try {
			int lastId = 0;

			String index = null;
			String type = null;
			int s = TEXT;
			int len = format.length();
			for (int i = 0; i < len; i++) {
				char c = format.charAt(i);
				if ((s == TEXT || s == QUOTE) && c == '\'') {
					// check duplicate quote
					if (i + 1 < len && format.charAt(i + 1) == '\'') {
						i++;
						sb.append('\'');
					} else
						s = s == QUOTE ? TEXT : QUOTE;
				} else if (s == TEXT && c == '{') {
					if (sb.length() > 0) {
						out.append(sb);
						sb.setLength(0);
					}
					s = INDEX;
				} else if (s == INDEX && (c == ',' || c == '}')) {
					if (sb.length() > 0) {
						index = sb.toString();
						sb.setLength(0);
					} else
						index = Integer.toString(lastId++);
					if (c == '}') {
						appendFormat(out, index, type, null, arg);
						s = TEXT;
					} else
						s = TYPE;
				} else if (s == TYPE && (c == ',' || c == '}')) {
					if (sb.length() > 0) {
						type = sb.toString();
						sb.setLength(0);
					}
					if (c == '}') {
						appendFormat(out, index, type, null, arg);
						type = null;
						s = TEXT;
					} else
						s = STYLE;
				} else if (s == STYLE && c == '}') {
					appendFormat(out, index, type, sb.toString(), arg);
					sb.setLength(0);
					type = null;
					s = TEXT;
				} else
					sb.append(c);
			}
			if (sb.length() > 0) {
				out.append(sb);
			}
		} finally {
			Pool.SB.put(sb);
		}
	}

	private static void appendFormat(Appendable out, String index, String type, String style, Map<String, ?> arg) throws IOException {
		Formatter format = formats.get(type);
		if (format == null)
			throw new IOException("format '" + type + "' doesn't exists");
		format.format(out, style, arg.get(index));
	}

	private static class ArrayToMap implements Map<String, Object> {
		private Object[] a;

		public ArrayToMap(Object[] a) {
			this.a = a;
		}

		@Override
		public int size() {
			return a.length;
		}

		@Override
		public boolean isEmpty() {
			return a.length > 0;
		}

		@Override
		public boolean containsKey(Object key) {
			if (!(key instanceof String))
				return false;
			try {
				int i = Integer.parseInt((String) key);
				return i >= 0 && i < a.length;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public boolean containsValue(Object value) {
			for (int i = 0; i < a.length; i++) {
				if (Objects.equals(a[i], value))
					return true;
			}
			return false;
		}

		@Override
		public Object get(Object key) {
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
}
