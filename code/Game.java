/**
 *  This class is the main class of the "Cave explorer" game. 
 *  "Cave explorer" is a very simple, text based adventure game. Users 
 *  can walk around some scenery. Pick up, drop and use items.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser, the rooms, the items and starts the game.
 *  It also evaluates and executes the commands that the parser returns.
 * 
 * @author  Michael Kölling, David J. Barnes and Jacob Adlers
 * @version 2015-11-11
 */

public class Game {
	private Parser parser;
	private Player player;
	
	/**
	 * The main method to start the game. It creates an instance of
	 * the game and calls it's game method.
	 */
	public static void main(String[] args){
		Game game = new Game();
		game.play();
	}
		
	/**
	 * Create the game and initialise its internal map.
	 */
	public Game() {
		parser = new Parser();
		player = new Player(); //IMPLEMENT HOW TO CHOOSE NAME
		createRooms();
	}

	/**
	 * Create all the rooms and link their exits together.
	 * Also set the players starting position and
	 * creates items in some of the rooms.
	 */
	private void createRooms() {
		Room 	exit, bigRoom1, bigRoom2, smallRoom1, smallRoom2, 
				crack, deadEnd, tunnel1, tunnel2, tunnel3, tunnel4, tunnel5;
	  
		// create the rooms
		exit = new Room("in the blocked exit of the cave");
		bigRoom1 = new Room("in a big opening");
		bigRoom2 = new Room("in a big room");
		smallRoom1 = new Room("in a smaller space");
		smallRoom2 = new Room("in a small opening");
		crack = new Room("in a very tiny crack in the wall");
		deadEnd = new Room("in a dead end");
		tunnel1 = new Room("in a tunnel");
		tunnel2 = new Room("in a narrow tunnel");
		tunnel3 = new Room("in a long corridor");
		tunnel4 = new Room("in a hallway");
		tunnel5 = new Room("in yet another tunnel");

		// initialise room exits
		exit.setExit("east", bigRoom1);

		bigRoom1.setExit("north", tunnel1);
		bigRoom1.setExit("south", smallRoom1);
		bigRoom1.setExit("east", tunnel3);
		bigRoom1.setExit("west", exit);

		bigRoom2.setExit("north", tunnel5);
		bigRoom2.setExit("south", smallRoom2);
		bigRoom2.setExit("east", deadEnd);
		bigRoom2.setExit("west", smallRoom2);

		smallRoom1.setExit("north", bigRoom1);
		smallRoom1.setExit("south", smallRoom2);
		smallRoom1.setExit("east", tunnel5);

		smallRoom2.setExit("north", smallRoom1);
		smallRoom2.setExit("south", bigRoom2);
		smallRoom2.setExit("east", bigRoom2);

		crack.setExit("west", tunnel1);

		deadEnd.setExit("west", bigRoom2);
		
		tunnel1.setExit("north", tunnel2);
		tunnel1.setExit("south", bigRoom1);
		tunnel1.setExit("east", crack);

		tunnel2.setExit("north", tunnel1);
		tunnel2.setExit("south", tunnel4);

		tunnel3.setExit("east", tunnel4);
		tunnel3.setExit("west", bigRoom1);

		tunnel4.setExit("north", tunnel2);
		tunnel4.setExit("west", tunnel3);

		tunnel5.setExit("south", bigRoom2);
		tunnel5.setExit("west", smallRoom1);


		player.setCurrentRoom(exit);  // start game at the exit

		// Create items and adds them to a room
		crack.addItem(new Item("lighter", 100));
		bigRoom2.addItem(new Item("TNT", 7450));
		// create some useless items
		exit.addItem(new Item("big-boulder", 11000));
		deadEnd.addItem(new Item("bone", 700));
		bigRoom1.addItem(new Item("broken-miners-helmet", 350));
		tunnel3.addItem(new Item("rock", 300));
		tunnel2.addItem(new Item("rat", 400));
		smallRoom1.addItem(new Item("bat", 150));
	}

	/**
	 *  Main play routine. Loops until end of play.
	 */
	public void play() {            
		printWelcome();

		// Enter the main command loop.  Here we repeatedly read commands and
		// execute them until the game is over.
				
		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Thank you for playing. Good bye.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("You are out exploring a cave far away from home.");
		System.out.println("Just when you are about to get out a wild earthquake rumbles the cave.");
		System.out.println("A big chunk of the ceiling aswell as parts of the walls collapse");
		System.out.println("and together all of the rocks completely hinders your way out.");
		System.out.println("Quickly, find something in the cave to get out. And hurry");
		System.out.println("the oxygen in the cave will soon run out!");
		System.out.println("Type 'help' if you need help.");
		System.out.println();
		System.out.println(player.getCurrentRoom().getLongDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * @param command The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private boolean processCommand(Command command) {
		boolean wantToQuit = false;

		if(command.isUnknown()) {
			System.out.println("I don't know what you mean...");
			return false;
		}

		String commandWord = command.getCommandWord();
		if (commandWord.equals("help")) {
			printHelp();
		} else if (commandWord.equals("go")) {
			wantToQuit = goRoom(command);
		} else if (commandWord.equals("quit")) {
			wantToQuit = quit(command);
		} else if (commandWord.equals("take")) {
			take(command);
		} else if (commandWord.equals("drop")) {
			drop(command);
		} else if (commandWord.equals("use")) {
			wantToQuit = use(command);
		} else if (commandWord.equals("look")) {
			look(command);
		}
		// else command not recognised.
		return wantToQuit;
	}

	// implementations of user commands:

	/**
	 * Print out some help information.
	 * Here we print a stupid message and a list of the 
	 * available command words.
	 */
	private void printHelp() {
		System.out.println("You search the cave for things that can help you get out.");
		System.out.println("It's very dark. The oxygen is about to run out.");
		System.out.println();
		System.out.println("Your command words are:");
		parser.showCommands();
	}

	/** 
	 * Try to in to one direction. If there is an exit, enter the new
	 * room, otherwise print an error message.
	 */
	private boolean goRoom(Command command) {
		if(!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Go where?");
			return false;
		}

		String direction = command.getSecondWord();

		// Try to leave current room.
		Room nextRoom = player.getCurrentRoom().getExit(direction);

		if (nextRoom == null) {
			System.out.println("You can't walk through solid rock, sadly...");
		}
		else {
			player.setCurrentRoom(nextRoom);
			if(player.getOxygenLeft() <= 0) {
				printLoseOxygen();
				return true;
			}
			player.decreaseOxygen();
			System.out.println(player.getCurrentRoom().getLongDescription());
		}
		return false;
	}

	/** 
	 * "Quit" was entered. Check the rest of the command to see
	 * whether we really quit the game.
	 * @param command the command entered by the user
	 * @return true, if this command quits the game, false otherwise.
	 */
	private boolean quit(Command command) {
		if(command.hasSecondWord()) {
			System.out.println("Quit what?");
			return false;
		}
		else {
			return true;  // signal that we want to quit
		}
	}

	/** 
	 * Try to pick up an item. Only succeeds if the item is in the
	 * current room and the player can carry the item.
	 * If it fails; print an error message why.
	 * @param command the command entered by the user
	 */
	private void take(Command command) {
		if(command.hasSecondWord()) {
			// check if the Item is in the room, if not itemInRoom is set to null
			Item itemInRoom = player.getCurrentRoom().descriptionToItem(command.getSecondWord());
			if (itemInRoom == null) {
				System.out.println("There is no such thing as a " + command.getSecondWord() + " in this room");
				return;
			}
			if (player.canCarryItem(itemInRoom)) {
				// remove the item from the room the player is in
				player.getCurrentRoom().removeItem(itemInRoom);
				// give the same item to the player
				player.pickUpItem(itemInRoom);
				System.out.println("You succesfully took the " + itemInRoom.getDescription());
				if (itemInRoom.getDescription().equals("TNT")) {
					printTntHelp();
				}
			} else {
				System.out.println("It's too heavy for you. Drop something or go lift some scrap and come back again later!");
			}
		} else {
			System.out.println("Take what? What do you want?");
			return;
		}
	}

	/** 
	 * Try to drop an item. Only succeeds if the player is holdning the item.
	 * If it fails; print an error message why.
	 * @param command the command entered by the user
	 */
	private void drop(Command command) {
		if(command.hasSecondWord()) {
			Item itemToDrop = player.getItem(command.getSecondWord());
			if (itemToDrop != null) {
				// remove the item from the player
				player.dropItem(itemToDrop);
				// add the item to the room
				player.getCurrentRoom().addItem(itemToDrop);
				System.out.println("You succesfully dropped the " + itemToDrop.getDescription());
				return;
			} else {
				System.out.println("You don't have no such thing as " + command.getSecondWord() + " to drop right now");
			}
		} else {
			System.out.println("Drop what? The base?");
			return;
		}
	}

	/** 
	 * Try to use an item. Only returns true if the item being
	 * used is a lighter and the player is in a room that has
	 * TNT in it. If the TNT is in the exit room then win the game
	 * If the conditions are failed; print an error message why
	 * and return false.
	 * @param command the command entered by the user
	 * @return true, if the item used quits the game, false otherwise.
	 */
	private boolean use(Command command) {
		// check if the player is currently carrying the item 
		if (player.getItem(command.getSecondWord()) != null) {
			boolean gameOver = useItem(command.getSecondWord());
			return gameOver;
		} else {
			System.out.println("Use what? You don't have any " + command.getSecondWord() + " to use.");
		}
		return false;
	}

	/**
	 * Checks if the item is usable and calls it's respective
	 * method. Otherwise prints "You can't use this item".
	 * @param itemDescription description of the item to use
	 * @return true if the usage of an item ends the game
	 */
	private boolean useItem(String itemDescription) {
		// check what item the user tries to use
		if (itemDescription.equals("lighter")) {
			return useLighter();
		} else {
			System.out.println("You can't use this item.");
		}
		return false;
	}

	/**
	 * Checks if all the conditions to use a lighter are met.
	 * if so 'ignites' it and depending on the players position
	 * the user wins or looses.
	 * @return true if player is in Room exit, false otherwise
	 */
	private boolean useLighter() {
		Room currentRoom = player.getCurrentRoom();
		// check if the TNT is in the room
		if (currentRoom.checkItem(currentRoom.descriptionToItem("TNT"))) {
			// if the current room is the blocked part -> win
			if (player.getCurrentRoom().getShortDescription().equals("in the blocked exit of the cave")) {
				if (player.getItem("clothes") != null) {
					printWinClothesOn();
				} else {
					printWinClothesOff();
				}
			} else {
				printLoseTntBlown();
			}
			return true;
		} else {
			System.out.println("There is nothing in this room to light up.");
		}
		return false;
	}

	/**
	 * Prints the closing message to the user when the user lost
	 * by igniting the TNT in the wrong room
	 */
	private void printLoseTntBlown() {
		System.out.println();
		System.out.println("You ignited the TNT in the middle of the cave.");
		System.out.println("Everything around you collapsed and you where");
		System.out.println("smashed dead by all the rocks.");
		System.out.println("Sad ending but I'm sorry to tell you: game over..");
		System.out.println();
	}
	
	/**
	 * Prints the closing message to the user when the user lost
	 * by running out of oxygen
	 */
	private void printLoseOxygen() {
		System.out.println();
		System.out.println("The room you enter is all blurry. You try to lean against");
		System.out.println("the a wall but you fall to the ground. You can see a light");
		System.out.println("coming closer and closer. The oxygen left in your body");
		System.out.println("is not enough to keep it running as you would have liked.");
		System.out.println("Sad ending but I'm sorry to tell you: game over..");
		System.out.println();
	}

	/**
	 * Prints the closing message to the user when the user won with clothes on
	 */
	private void printWinClothesOn() {
		System.out.println();
		System.out.println("That's one hell of a blow! We where just about");
		System.out.println("to start removing the boulders to rescue you.");
		System.out.println("You seem to do fine on your own though.");
		System.out.println("Do you need a ride home? We planned to go to");
		System.out.println("a pub in town, wanna join?");
		System.out.println();
	}

	/**
	 * Prints the closing message to the user when the user won with clothes off
	 */
	private void printWinClothesOff() {
		System.out.println();
		System.out.println("That's one hell of a blow! We where just about");
		System.out.println("to.. woah! Hey there, what happened to your clothes?!");
		System.out.println("..No wait don't even tell us, I don't think we really");
		System.out.println("want to know, especially not with any details.");
		System.out.println("Well we are glad you got out alive, I'm out of");
		System.out.println("here now. ASAP.");
		System.out.println();
	}

	/**
	 * Prints info on how to use the TNT
	 */
	private void printTntHelp() {
		System.out.println("Drop the TNT in the room you want to use it in");
		System.out.println("then 'use' any item that can ignite the TNT");
		System.out.println("while in the same room.");
	}

	/** 
	 * Prints the long description of the room aswell as what you
	 * are currently carrying and the total weight of the items.
	 * @param command the command entered by the user
	 */
	private void look(Command command) {
		if(command.hasSecondWord()) {
			System.out.println("Just look.. please");
			return;
		}
		// print most of the information
		System.out.println(player.getCurrentRoom().getLongDescription());
		// add what items are currently on
		System.out.println("You are currently carrying: " + player.currentlyHolding());
		// add current weight
		System.out.println("The total weight of your items is " + player.getCurrentWeight() + "g and you can carry up to " + player.getMaxload() + "g.");
		// add the amount of oxygen left
		System.out.println("You estimate that you can walk through about " + player.getOxygenLeft() + " more rooms before ");
		System.out.println("you run out of oxygen.");
	}
}
