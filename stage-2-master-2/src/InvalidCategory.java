
public class InvalidCategory extends Exception{

	public InvalidCategory(String category){
		super("'" + category + "' is not a valid menu item category.");
	}
}