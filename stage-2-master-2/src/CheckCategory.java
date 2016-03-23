
public class CheckCategory extends Exception{

	public CheckCategory(String category){
		super("'" + category + "' is not a valid menu item category.");
	}
}