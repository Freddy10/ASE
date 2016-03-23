
public class checkCategory extends Exception{

	public checkCategory(String category){
		super("'" + category + "' is not a valid menu item category.");
	}
}