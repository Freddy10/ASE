
public class DuplicateMenuItem extends Exception {
	
	public DuplicateMenuItem(String duplicate_item){
		super("Duplicate menu item: " + duplicate_item);
	}
}
