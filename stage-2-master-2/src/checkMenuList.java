
public class checkMenuList extends Exception {
	
	public checkMenuList(String duplicate_item){
		super("Duplicate menu item: " + duplicate_item);
	}
}
