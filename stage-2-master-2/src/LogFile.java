import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class LogFile {

	private static final LogFile instance = new LogFile();
	private String logText;

	// private constructor
	private LogFile() {
		logText = "LOG UPDATE\r\n";
	}

	public static LogFile getInstance() {
		return instance;
	}

	public void addEntry(String log_entry) {
		logText += log_entry;
	}

	public String getLogEntries() {
		return logText;
	}

	public void outputLog() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("updateLog.txt", "UTF-8");
		writer.print(logText);
		writer.close();
	}
}
