import java.text.DecimalFormat;

public class ListOfMenu {

	private String itemName;
	private double itemPrice;
	private String category;
	private boolean isVegetarian;
	private int preparationTime;
	DecimalFormat df = new DecimalFormat("#.00"); 
													

	public ListOfMenu(String name, double price, String category, boolean is_vegetarian, int time) {
		itemName = name;
		itemPrice = price;
		this.category = category;
		isVegetarian = is_vegetarian;
		preparationTime = time;
	}

	public String getName() {
		return itemName;
	}

	public double getPrice() {
		return itemPrice;
	}

	public String getCategory() {
		return category;
	}

	public int getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(int preparationTime) {
		this.preparationTime = preparationTime;
	}

	public void setName(String new_name) {
		itemName = new_name;
	}

	public void setPrice(String new_price) {
		itemName = new_price;
	}

	public void setCategory(String new_category) {
		category = new_category;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ListOfMenu) {
			ListOfMenu otherMenuItem = (ListOfMenu) other;
			return itemName.equals(otherMenuItem.getName());
		} else {
			return false;
		}
	}

	public int compareByNameTo(ListOfMenu otherMenuItem) {
		return itemName.compareTo(otherMenuItem.getName());
	}

	public int compareByCategoryTo(ListOfMenu otherMenuItem) {
		return itemName.compareTo(otherMenuItem.getCategory());
	}

	public String printItemSummary() {
		String padded = String.format("%-20s", this.itemName);
		return padded + " " + this.isVegetarianPrint() + " " + df.format(itemPrice);
	}

	public String isVegetarianPrint() {
		String is_veg = "   ";
		if (this.isVegetarian) {
			is_veg = "(V)";
		}
		return is_veg;
	}
}
