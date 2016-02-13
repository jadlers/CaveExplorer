import java.util.ArrayList;

/**
 * Class Player - a player in an adventure game.
 *
 * This class is part of the "Cave explorer" game.
 * "Cave explorer" is a very simple, text based adventure game.
 *
 * A "Player" represents one player in the game. Every player has a name,
 * currentWeight, MAXLOAD, currentRoom and a ArrayList of the carried items.
 * 
 * @author  Jacob Adlers
 * @version 2015-11-11
 */

public class Player {
	private int currentWeight;
	private static int MAXLOAD = 9000;
    private Room currentRoom;
	private ArrayList<Item> heldItems;
	private int oxygenLeft;

	/**
	 * Creates the player equipped with clothes and
	 * an ArrayList with all the held items
	 */
	public Player() {
		heldItems = new ArrayList<>();
		// equip the player with clothes
		pickUpItem(new Item("clothes", 1500));
		oxygenLeft = 20;
	}

    /**
	 * Sets the currentRoom to the room passed to the method
	 * @param room the new current room of the player
	 */
	public void setCurrentRoom(Room room) {
		currentRoom = room;
	}

	/**
	 * Returns the Room the player is currently in
	 * @return the Room the player is currently in
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}

	/**
	 * Returns the weight of all items the player hold
	 * @return the weight of all items the player hold
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}

	/**
	 * Returns the maximum load the player can carry
	 * @return the maximum load the player can carry
	 */
	public int getMaxload() {
		return MAXLOAD;
	}
	
	public int getOxygenLeft() {
		return oxygenLeft;
	}

	/**
	 * Returns the ArrayList with the held items
	 * @return the ArrayList with the held items
	 */
	public ArrayList<Item> getItems() {
		return heldItems;
	}

	/**
	 * "Searches" through the held items to see if the player
	 * has an item matching the description and returns it.
	 * If the player doesn't hold the item return null.
	 *
	 * @param description a description of an item
	 * @return the Item matching the description
	 */
	public Item getItem(String description) {
		for (Item current: heldItems) {
			if (current.getDescription().equals(description)) {
				return current;
			}
		}
		return null;
	}

	/**
	 * Creates a String with the held items and returns is.
	 * If no held items return "nothing" instead.
	 *
	 * @return String with the held items
	 */
	public String currentlyHolding() {
		if (heldItems.isEmpty()) {
			return "nothing";
		}
		String holding = "";
		for (Item current : heldItems) {
			holding += current.getDescription() + " ";
		}
		return holding;
	}

	/**
	 * Makes the player get a player
	 *
	 * @param item the item to pick up
	 */
    public void pickUpItem(Item item) {
    	if (getCurrentWeight() + item.getWeight() <= MAXLOAD) {
    		heldItems.add(item);
    		updateWeight();
    	}
    }

	/**
	 * Method for player to drop an item which
	 * the player is carrying. It checks so that the player
	 * is carrying the item.
	 */
	public void dropItem(Item item) {
		if (item != null) {
			heldItems.remove(item);
			updateWeight();
		}
	}

	/**
	 * Calculates if the player can carry the given item without
	 * exceeding the MAXLOAD.
	 *
	 * @param an instance of an item
	 * @return true if player can carry the item, false otherwise
	 */
	public boolean canCarryItem(Item item) {
		return item.getWeight() + currentWeight <= MAXLOAD;
	}

	/**
	 * Calculates the total weight of all the held items
	 * and updates currentWeight with the value
	 */
	private void updateWeight() {
		int totalWeight = 0;
		for (Item current: heldItems) {
			totalWeight += current.getWeight();
		}
		currentWeight = totalWeight;
	}
	
	/**
	 * Decreases the players remaining oxygen left by one 
	 */
	public void decreaseOxygen() {
		oxygenLeft -= 1;
	}
	
	/**
	 * Increases the players remaining oxygen left by the amount given
	 * @param amount how many "rounds" of oxygen to add
	 */
	public void addOxygen(int amount) {
		oxygenLeft += amount;
	}
}