/**
 * 
 */
package unknow.log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * @author unknow
 */
public class LogFactory {
	private static final Map<String, Appender> appender = new HashMap<>();
	private static final Map<String, LogConf> loggers;
	static {
		InputStream is = LogFactory.class.getClassLoader().getResourceAsStream("log-config.yml");
		Map<String, LogConf> l = null;
		if (is != null) {
			try {
				Conf conf = new ObjectMapper(new YAMLFactory()).readValue(is, Conf.class);
				for (Map.Entry<String, AppenderConf> e : conf.appenders.entrySet()) {
					appender.put(e.getKey(), new Appender(e.getValue().format, System.err)); // TODO appendable
				}

				l = conf.loggers;
			} catch (Exception e) {
				System.err.println("failed to parse conf");
				e.printStackTrace();
			}
		} else
			System.err.println("missing 'log-config.yml'");
		if (l == null)
			l = new HashMap<>();
		if (!l.containsKey("")) {
			System.err.println("Missing default LogConfig loading default");
			appender.put("default", new Appender("{timestamp,datetime} {level} ({thread}) [{name}] {msg}\n", System.err));
			LogConf logConf = new LogConf();
			logConf.appender = "default";
			logConf.level = Level.INFO;
			l.put("", logConf);
		}
		loggers = l;
	}

	public static Log getLogger(Class<?> cl) {
		String name = cl.getName();
		String best = null;
		for (String l : loggers.keySet()) {
			if (name.startsWith(l) && (best == null || l.length() > best.length()))
				best = l;
		}
		LogConf c = loggers.get(best);
		return new Log(name, c.level, appender.get(c.appender));
	}

	public static class Conf {
		public Map<String, AppenderConf> appenders;
		public Map<String, LogConf> loggers;
	}

	public static class AppenderConf {
		public String format;
		public String out;
		// TODO file rolling
	}

	public static class LogConf {
		public Level level;
		public String appender;
	}
}
