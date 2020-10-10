/**
 * 
 */
package unknow.log.format;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * Class responsible of formatting an argument
 * 
 * @author unknow
 */
public interface Formatter {
	/**
	 * format the value into out
	 * 
	 * @param out   output
	 * @param style the format style (can be null)
	 * @param value the value to format
	 * @throws IOException on error
	 */
	public void format(Appendable out, String style, Object value) throws IOException;

	/**
	 * @return the handlers type
	 */
	public String getType();

	public static class FormatDefault implements Formatter {
		@Override
		public void format(Appendable out, String style, Object value) throws IOException {
			out.append(String.valueOf(value));
		}

		@Override
		public String getType() {
			return null;
		}
	}

	public static class FormatDate extends FormatDateTime implements Formatter {
		public FormatDate() {
			super(DateTimeFormatter.ISO_DATE);
		}

		@Override
		public String getType() {
			return "date";
		}
	}

	public static class FormatTime extends FormatDateTime implements Formatter {
		public FormatTime() {
			super(DateTimeFormatter.ISO_TIME);
		}

		@Override
		public String getType() {
			return "time";
		}
	}

	public static class FormatDateTime implements Formatter {
		private final DateTimeFormatter defaultFormatter;

		public FormatDateTime() {
			this(DateTimeFormatter.ISO_DATE_TIME);
		}

		public FormatDateTime(DateTimeFormatter defaultFormatter) {
			this.defaultFormatter = defaultFormatter;
		}

		@Override
		public final void format(Appendable out, String style, Object value) throws IOException {
			TemporalAccessor t;
			if (value instanceof TemporalAccessor) {
				t = (TemporalAccessor) value;
			} else if (value instanceof Calendar) {
				Calendar c = (Calendar) value;
				t = ZonedDateTime.ofInstant(c.toInstant(), c.getTimeZone().toZoneId());
			} else if (value instanceof Date) {
				t = LocalDateTime.ofInstant(((Date) value).toInstant(), ZoneId.systemDefault());
			} else if (value instanceof Number) {
				t = LocalDateTime.ofInstant(Instant.ofEpochMilli(((Number) value).longValue()), ZoneId.systemDefault());
			} else
				throw new IOException("unsuported value type '" + value.getClass() + "'");

			DateTimeFormatter fmt = defaultFormatter;
			if (style != null)
				fmt = DateTimeFormatter.ofPattern(style);
			fmt.formatTo(t, out);
		}

		@Override
		public String getType() {
			return "datetime";
		}
	}

	public static class FormatDuration implements Formatter {
		private static final long[] DURATION = new long[] { 24 * 3600000, 3600000, 60000, 1000, 1 };
		private static final String[] UNIT = new String[] { "d", "h", "m", "s", "ms" };

		@Override
		public void format(Appendable out, String style, Object value) throws IOException {
			long ms;
			if (value instanceof Number)
				ms = ((Number) value).longValue();
			else if (value instanceof Duration) {
				Duration d = (Duration) value;
				ms = d.get(ChronoUnit.SECONDS) * 1000 + d.get(ChronoUnit.NANOS) / 1000000;
			} else
				throw new IOException("unsuported value type '" + value.getClass() + "'");

			for (int i = 0; i < DURATION.length; i++) {
				long d = DURATION[i];
				if (ms > d) {
					long v = ms / d;
					out.append(Long.toString(v));
					out.append(UNIT[i]);
					ms -= v * d;
				}
			}
		}

		@Override
		public String getType() {
			return "duration";
		}
	}

	public static class FormatSize implements Formatter {
		private static final String[] UNITS = new String[] { " o", " Ko", "Mo", "Go", "To", "Eo" };

		@SuppressWarnings("resource")
		@Override
		public void format(Appendable out, String style, Object value) throws IOException {
			if (!(value instanceof Number))
				throw new IOException("unsuported value type '" + value.getClass() + "'");
			double s = ((Number) value).doubleValue();
			int i = 0;
			while (s > 1024 && i < UNITS.length) {
				s /= 1024;
				i++;
			}
			if (style == null || style.isEmpty())
				style = "%.2f";
			new java.util.Formatter(out).format(style, s).flush(); // TODO replace
			out.append(UNITS[i]);
		}

		@Override
		public String getType() {
			return "size";
		}
	}
}