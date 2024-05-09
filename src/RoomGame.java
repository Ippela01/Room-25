//contiene la logica principale del gioco, inclusi i metodi per muoversi tra le stanze, raccogliere e depositare oggetti, visualizzare l'inventario e altro ancora.

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; 
public class RoomGame {
    private static class Room {
        String description;
        Map<String, String> exits = new HashMap<>();
        Map<String, Item> items = new HashMap<>();
        boolean doorClosed;                  // Aggiunto attributo per indicare se la porta è chiusa
        boolean doorLocked;                // Aggiunto attributo per indicare se la porta è chiusa a chiave      
        private Item requiredKey;              // Aggiunto attributo per specificare il nome della chiave richiesta
        Room(String description) {
            this.description = description;
        }

        public boolean isDoorClosed() {
            return doorClosed;
        }

        public boolean isDoorLocked() {
            return doorLocked;
        }

        public Item getRequiredKey() {
            return requiredKey;
        }

        public void setDoorClosed(boolean closed) {
            this.doorClosed = closed;
        }

        public void setLockedDoor(boolean locked, Item requiredKey) {
            this.doorLocked = locked;
            this.requiredKey = requiredKey;
        }

        public boolean hasRequiredKey(Map<String, Item> inventory) {
            return requiredKey != null && inventory.containsValue(requiredKey);
        }
    }

    private static Room currentRoom;
    private static Map<String, Room> rooms = new HashMap<>();
    private static Map<String, Item> inventory = new HashMap<>();

    public static void main(String[] args) {
        initializeRooms();
        currentRoom = rooms.get("Market Square");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(currentRoom.description);
            System.out.println("Possible exits: " + currentRoom.exits.keySet());

            String command = scanner.nextLine();
            if (command.equals("map")) {
                GameMap.map(); // Esegue il metodo map() della classe GameMap
            } else {
                executeCommand(command);
            }
        }
    }

    private static void initializeRooms() {
        Room marketSquare = new Room("You are at Market Square. There is a sword lying on the ground.");
        Room room1 = new Room("You are in Ballroom.");
        Room room2 = new Room("You are in Dark Alley. There is a key on the ground");
        Room room3 = new Room("You are in Royal Kitchen." );
        Room room4 = new Room("You are in Armory." );
        Room room5 = new Room("You are in  King's Bedroom." );
        Room room6 = new Room("You are in Museum." );
        Room room7 = new Room("You are in Solar." );
        Room room8 = new Room("You are in Throne Room." );
        Room room9 = new Room("You are in Royal Library." );

        marketSquare.exits.put("N", "Room 1");
        room1.exits.put("S", "Market Square");
        room1.exits.put("E", "Room 2");
        room1.exits.put("N", "Room 4");
        room1.exits.put("W", "Room 6");
        room2.exits.put("N", "Room 3");
        room2.exits.put("W", "Room 1");
        room3.exits.put("N", "Room 9");
        room3.exits.put("S", "Room 2");
        room3.exits.put("W", "Room 4");
        room4.exits.put("N", "Room 8");
        room4.exits.put("E", "Room 3");
        room4.exits.put("S", "Room 1");
        room4.exits.put("W", "Room 5");
        room5.exits.put("N", "Room 7");
        room5.exits.put("E", "Room 4");
        room5.exits.put("S", "Room 6");
        room6.exits.put("N", "Room 5");
        room6.exits.put("E", "Room 1");
        room7.exits.put("E", "Room 8");
        room7.exits.put("S", "Room 5");
        room8.exits.put("E", "Room 9");
        room8.exits.put("S", "Room 4");
        room8.exits.put("W", "Room 7");
        room9.exits.put("S", "Room 3");
        room9.exits.put("W", "Room 8");

        marketSquare.items.put("sword", new Item("sword", "A sharp sword"));
        room2.items.put("key", new Item("keyA", "A key with the engraving of an armor"));
        room3.items.put("key", new Item("keyF", "A false key"));
        room4.items.put("cane", new Item("ff", "jj"));
        room5.items.put("gatto", new Item("ss", "hh"));
        room6.items.put("pinguino", new Item("aa", "pp"));
        room7.items.put("delfino", new Item("gg", "oo"));
        room8.items.put("fagiano", new Item("rr", "ii"));
        room9.items.put("alano", new Item("alano", "ss"));

        room4.setDoorClosed(true);
        room4.setLockedDoor(true, new Item("keyA",""));

        rooms.put("Market Square", marketSquare);
        rooms.put("Room 1", room1);
        rooms.put("Room 2", room2);
        rooms.put("Room 3", room3);
        rooms.put("Room 4", room4);
        rooms.put("Room 5", room5);
        rooms.put("Room 6", room6);
        rooms.put("Room 7", room7);
        rooms.put("Room 8", room8);
        rooms.put("Room 9", room9);

    }

    private static void executeCommand(String command) {
        String[] parts = command.split(" ");

        if (parts.length == 1) {
            switch (parts[0]) {
                case "N", "S", "E", "W":
                    move(parts[0]);
                    break;
                case "inv":
                    showInventory();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        } else if (parts.length == 2) {
            switch (parts[0]) {
                case "pick":
                    pickItem(parts[1]);
                    break;
                case "drop":
                    dropItem(parts[1]);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        } else {
            System.out.println("Invalid command.");
        }
    }

    private static void move(String direction) {
        String nextRoomName = currentRoom.exits.get(direction);
        if (nextRoomName != null) {
            Room nextRoom = rooms.get(nextRoomName);
            if (nextRoom.isDoorClosed()) { // Controlla se la porta è chiusa
                if (nextRoom.isDoorLocked()) { // Controlla se la porta è chiusa a chiave
                    if (nextRoom.hasRequiredKey(inventory)) { // Controlla se il giocatore ha la chiave richiesta
                        // Verifica specificamente se l'oggetto nell'inventario ha il nome corretto
                        if (inventory.containsKey("keyA")) {
                            System.out.println("You unlock the door with the key.");
                            nextRoom.setDoorClosed(false); // Apri la porta
                        } else {
                            System.out.println("The door is locked. You need the correct key to open it.");
                            return; // Non permettere al giocatore di muoversi se la porta è chiusa a chiave e non ha la chiave corrispondente
                        }
                    } else {
                        System.out.println("The door is locked. You need a key to open it.");
                        return; // Non permettere al giocatore di muoversi se la porta è chiusa a chiave e non ha la chiave corrispondente
                    }
                } else {
                    System.out.println("The door is closed. You open it.");
                    nextRoom.setDoorClosed(false); // Apri la porta
                }
            }
            currentRoom = nextRoom;
        } else {
            System.out.println("You can't go in that direction.");
        }
    }
    
    

    private static void pickItem(String itemName) {
        Item item = currentRoom.items.get(itemName);
        if (item != null) {
            // Crea un nuovo oggetto Item con il nome e la descrizione corretti
            Item newItem = new Item(itemName, item.getDescription());
            // Aggiungi l'oggetto all'inventario usando il nome corretto come chiave
            inventory.put(itemName, newItem);
            currentRoom.items.remove(itemName);
            System.out.println("You picked up: " + item.getDescription());
        } else {
            System.out.println("There is no such item in this room.");
        }
    }
    

    private static void dropItem(String itemName) {
        Item item = inventory.get(itemName);
        if (item != null) {
            currentRoom.items.put(itemName, item);
            inventory.remove(itemName);
            System.out.println("You dropped: " + item.getDescription());
        } else {
            System.out.println("You don't have that item.");
        }
    }

    private static void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory: " + inventory.keySet());
        }
    }

    private static class Item {
        private String name;
        private String description;

        public Item(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
        public String getName() {
            return name;
        }
        
    }
}
