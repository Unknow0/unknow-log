/**
 * 
 */
package unknow.log;

/**
 * @author unknow
 */
public class TestLog {
	public static void main(String[] arg) throws Throwable {
		Log logger = LogFactory.getLogger(TestLog.class);
		logger.trace("trace");
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error", new Exception());
//		Appender appender = new Appender(System.out);
//
//		appender.append("test\n");
//		appender.append("test {}\n", System.currentTimeMillis());
//		appender.append("test {0,date}\n", System.currentTimeMillis());
//		appender.append("test {0,time}\n", System.currentTimeMillis());
//		appender.append("test {0,datetime}\n", System.currentTimeMillis());
//
//		System.out.flush();
//
//		for (Method m : Log.class.getDeclaredMethods())
//			System.out.println(Type.getMethodDescriptor(m));
	}
}
