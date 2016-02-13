/**
 * Class Item - an item in an adventure game.
 *
 * This class is part of the "Cave explorer" game. 
 * "Cave explorer" is a very simple, text based adventure game.  
 *
 * A "Item" represents one item in the game. Every item has a
 * description and a weight.
 * 
 * @author  Jacob Adlers
 * @version 2015-11-11
 */
public class Item {
	private String description;
	private int weight;

	/**
	 * Creates an item
	 * 
	 * @param description a one-word description of the item
	 * @param weight the weight of the item in grams
	 */
	public Item(String description, int weight) {
		this.description = description;
		this.weight = weight;
	}

	/**
	 * Returns the description of an item
	 * @return the description of an item
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the weight of an item
	 * @return the weight of an item
	 */
	public int getWeight() {
		return weight;
	}
}