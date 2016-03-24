
public class CheckMenuList extends Exception {
	
	public CheckMenuList(String same_item){
		super("Same menu item: " + same_item);
	}
}
