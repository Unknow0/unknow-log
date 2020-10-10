/**
 * 
 */
package unknow.log.format;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;

import unknow.log.format.Formatter.FormatDateTime;

/**
 * @author unknow
 */
public class DateTimeFormatTest {
	private static final FormatDateTime formatDateTime = new FormatDateTime();

	@Test
	public void testLocalDateTime() throws IOException {
		StringBuilder sb = new StringBuilder();
		formatDateTime.format(sb, null, LocalDateTime.of(2020, 2, 11, 12, 30));
		assertEquals("2020-02-11T12:30:00", sb.toString());
	}
}
