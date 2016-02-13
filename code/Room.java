import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Cave explorer" game.
 * "Cave explorer" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room. The room can also have
 * items stored in it.
 * 
 * @author  Michael Kölling, David J. Barnes and Jacob Adlers
 * @version 2015-11-11
 */

public class Room 
{
	private String description;
    private HashMap<String, Room> exits;	// stores exits of this room.
    private ArrayList<Item> items;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
    	this.description = description;
    	exits = new HashMap<String, Room>();
    	items = new ArrayList<Item>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
    	exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
    	return description;
    }

    /**
     * Return a description of the room in the form:
     * 		You are in the kitchen.
     *		Items in this room: apple lamp
     *		Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
    	String longDescription = "\n You are " + description + ".\n";
    	longDescription += itemsInThisRoom();
    	longDescription += getExitString();
    	return longDescription;
    }

    /**
     * Return a description of the items in the form:
     *		Items in this room: apple lamp
     * If no items return ""
     * @return A list of the items in the room
     */
    public String itemsInThisRoom() {
    	if (!items.isEmpty()) {
    		String itemsInRoom = "Items in this room:";
    		for (Item item: items) {
    			itemsInRoom += " " + item.getDescription();
    		}
    		itemsInRoom += "\n";
    		return itemsInRoom;
    	}
    	return "";
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
    	String returnString = "Exits:";
    	Set<String> keys = exits.keySet();
    	for(String exit : keys) {
    		returnString += " " + exit;
    	}
    	return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
    	return exits.get(direction);
    }

    /**
     * Adds an item to the ArrayList of items
     *
     * @param item item to add to the room
     */
    public Item addItem(Item item) {
    	items.add(item);
    	return item;
    }

    /**
     * Removes an item from the ArrayList if the item is
     * in the list when asked to be deleted
     *
     * @param item the item to remove from the room
     * @return the item removed
     */
    public Item removeItem(Item item) {
    	Item removedItem = null;
    	if (checkItem(item)) {
    		items.remove(item);
    	}
    	return removedItem;
    }

    /**
     * Checks if the item passed is available in the room.
     * @param item the item to check
     * @return true if the item is available, false otherwise
     */
    public boolean checkItem(Item item) {
    	for (Item current : items) {
    		if (current.equals(item)) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Searches through the items ArrayList to see if there
     * is a item that matches the desctiption given
	 *
     * @param description the description of an item
     * @return the Item which matches the description given
     */
    public Item descriptionToItem(String description) {
    	boolean found = false;
    	for (int i = 0; !found && i < items.size(); i++) {
    		if (items.get(i).getDescription().equals(description)) {
    			return items.get(i);
    		}
    	}
    	return null;
    }
}
