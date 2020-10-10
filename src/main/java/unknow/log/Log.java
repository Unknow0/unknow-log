/**
 * 
 */
package unknow.log;

import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import unknow.log.format.Out;

/**
 * @author unknow
 */
public class Log {
	private static final Object[] EMPTY = new Object[0];
	private String name;
	private Level level;
	private Appender appender;

	Log(String name, Level level, Appender appender) {
		this.name = name;
		this.level = level;
		this.appender = appender;
	}

	private final void log(Level level, CharSequence format, Throwable t, Object[] arg) {
		if (level.ordinal() < this.level.ordinal())
			return;
		long start = System.currentTimeMillis();
		StringBuilder sb = Pool.SB.get();
		try {
			Out.format(sb, format, arg);
			if (t != null) {
				sb.append('\n');
				append(sb, t);
			}

			appender.append(new LogEvent(start, name, level, Thread.currentThread().getName(), sb.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Pool.SB.put(sb);
		}
	}

	public static final void append(StringBuilder sb, Throwable t) {
		Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
		dejaVu.add(t);

		// Print our stack trace
		sb.append(t.getClass().getName());
		String name = t.getLocalizedMessage();
		if (name != null)
			sb.append(": ").append(name);
		sb.append('\n');
		StackTraceElement[] trace = t.getStackTrace();
		for (StackTraceElement traceElement : trace)
			sb.append("\tat ").append(traceElement).append('\n');

		// Print suppressed exceptions, if any
		for (Throwable se : t.getSuppressed())
			printEnclosedStackTrace(sb, se, trace, "Suppressed: ", "\t", dejaVu);

		// Print cause, if any
		Throwable ourCause = t.getCause();
		if (ourCause != null)
			printEnclosedStackTrace(sb, ourCause, trace, "Caused by: ", "", dejaVu);
	}

	/**
	 * Print our stack trace as an enclosed exception for the specified stack trace.
	 */
	private static void printEnclosedStackTrace(StringBuilder sb, Throwable t, StackTraceElement[] enclosingTrace, String caption, String prefix, Set<Throwable> dejaVu) {
		if (dejaVu.contains(t)) {
			sb.append("\t[CIRCULAR REFERENCE:").append(t).append(']');
		} else {
			dejaVu.add(t);
			int framesInCommon = 0;
			// Compute number of frames in common between this and enclosing trace
			StackTraceElement[] trace = t.getStackTrace();
			int m = trace.length - 1;
			int n = enclosingTrace.length - 1;
			while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
				m--;
				n--;
			}
			framesInCommon = trace.length - 1 - m;

			// Print our stack trace
			sb.append(prefix).append(caption).append(t);
			for (int i = 0; i <= m; i++)
				sb.append(prefix).append("\tat ").append(trace[i]);
			if (framesInCommon != 0)
				sb.append(prefix).append("\t... ").append(framesInCommon).append(" more");

			// Print suppressed exceptions, if any
			for (Throwable se : t.getSuppressed())
				printEnclosedStackTrace(sb, se, trace, "Suppressed: ", prefix + "\t", dejaVu);

			// Print cause, if any
			Throwable ourCause = t.getCause();
			if (ourCause != null)
				printEnclosedStackTrace(sb, ourCause, trace, "Caused by: ", prefix, dejaVu);
		}
	}

	public void trace(CharSequence msg) {
		log(Level.TRACE, msg, null, EMPTY);
	}

	public void trace(CharSequence msg, Object... arg) {
		log(Level.TRACE, msg, null, arg);
	}

	public void trace(CharSequence msg, Throwable t) {
		log(Level.TRACE, msg, t, EMPTY);
	}

	public void debug(CharSequence msg) {
		log(Level.DEBUG, msg, null, EMPTY);
	}

	public void debug(CharSequence msg, Object... arg) {
		log(Level.DEBUG, msg, null, arg);
	}

	public void debug(CharSequence msg, Throwable t) {
		log(Level.DEBUG, msg, t, EMPTY);
	}

	public void info(CharSequence msg) {
		log(Level.INFO, msg, null, EMPTY);
	}

	public void info(CharSequence msg, Object... arg) {
		log(Level.INFO, msg, null, arg);
	}

	public void info(CharSequence msg, Throwable t) {
		log(Level.INFO, msg, t, EMPTY);
	}

	public void warn(CharSequence msg) {
		log(Level.WARN, msg, null, EMPTY);
	}

	public void warn(CharSequence msg, Object... arg) {
		log(Level.WARN, msg, null, arg);
	}

	public void warn(CharSequence msg, Throwable t) {
		log(Level.WARN, msg, t, EMPTY);
	}

	public void error(CharSequence msg) {
		log(Level.ERROR, msg, null, EMPTY);
	}

	public void error(CharSequence msg, Object... arg) {
		log(Level.ERROR, msg, null, arg);
	}

	public void error(CharSequence msg, Throwable t) {
		log(Level.ERROR, msg, t, EMPTY);
	}

	public String getName() {
		return name;
	}

	public Level getLevel() {
		return level;
	}
}
