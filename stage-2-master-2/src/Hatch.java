
public class Hatch implements Runnable {

	private RestaurantModel kitchen;

	public Hatch(RestaurantModel k) {
		kitchen = k;
	}

	public void run() {

		int delayTime = 100;
		boolean orderReady = false;

		while ((!kitchen.isFinishedRun()) || (!kitchen.noOrdersInKitchen())) {
			if (kitchen.isThreadActive() && (!kitchen.noOrdersInKitchen())) {

				delayTime = kitchen.getMenuItemMap().findByName(kitchen.getFirstOrder().getItemName())
						.getPreparationTime() * 50;
				if (!orderReady)
					orderReady = true;
			} else if ((!kitchen.isFinishedRun()) && (kitchen.noOrdersInKitchen()))
				orderReady = false;
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
			}
			if (orderReady)
				kitchen.orderToHatch();
		}
	}
}
