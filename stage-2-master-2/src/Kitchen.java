import java.io.IOException;

public class Kitchen implements Runnable {

	private RestaurantModel kitchen;

	private int kitchOpenTime;

	private int line;

	public Kitchen(RestaurantModel k) {
		kitchen = k;
		line = 0;
		kitchOpenTime = 0;
	}

	@Override
	public void run() {

		kitchOpenTime = kitchen.getKitchOpenTime();

		long start = System.currentTimeMillis();

		long end = start + kitchOpenTime * 1000;

		while (System.currentTimeMillis() < end) {
			if (kitchen.getPopulateMethod().equals("from a textfile")) {
				try {
					kitchen.populateWithFile(this.line);
				} catch (IOException e) {
					e.printStackTrace();
				}
				line++;
			}

			if (!kitchen.isThreadActive())
				kitchen.setStartThread();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("The kitchen is closing.");
		kitchen.setFinishedRun();
	}
}
