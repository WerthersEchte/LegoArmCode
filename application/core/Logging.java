package application.core;

import com.kuka.task.ITaskLogger;

public class Logging {
	
	private static ITaskLogger sLogger;

	public static void setLogger(ITaskLogger logger) {
		sLogger = logger;
	}

	public static void log(String aMessage){
		log("Lego", aMessage);
	}
	public static void log(String aTag, String aMessage){
		sLogger.info("[" + aTag + "] " + aMessage);
	}

}
