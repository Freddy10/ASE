
/**
* <h1>Manager</h1>
* Manager class is responsible for creating each component of the project's
* MVC architecture: GUI as view, the different collections as model and a
* controller class which joins view and model.

public class Manager {
	
	/**
	 * Class constructor
	 */
	public Manager() {
	}

	/**
	 * Calls the population method for the different collections of orders and items.
	 * In addition, creates the user interface
	 */
	public void run(){
		RestaurantModel model = new RestaurantModel();
		MVCRestaurantView view = new MVCRestaurantView(model);
		MVCRestaurantController controller = new MVCRestaurantController(model, view);   
		view.setVisible(true);

	}	
}
