
public class CheckMenuList extends Exception {
	
	public CheckMenuList(String duplicate_item){
		super("Duplicate menu item: " + duplicate_item);
	}
}
