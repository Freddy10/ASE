
public class Manager {

	public Manager() {
	}

	public void run() {
		RestaurantModel model = new RestaurantModel();
		RestaurantView view = new RestaurantView(model);
		RestaurantController controller = new RestaurantController(model, view);
		view.setVisible(true);

	}
}
