import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Tables implements Runnable {

	private RestaurantModel kitchen;

	public Tables(RestaurantModel k) {
		kitchen = k;
	}

	public void run() {

		while (!kitchen.hatchIsFinished() || !kitchen.isFinishedRun()) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			kitchen.orderToTable();
		}
		try {
			LogFile.getInstance().outputLog();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		kitchen.setThreadFinished();
	}
}
