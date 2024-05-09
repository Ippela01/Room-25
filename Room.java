//rappresenta le stanze nel gioco. Ogni istanza di Room ha una descrizione, uscite verso altre stanze e un elenco di oggetti presenti nella stanza.
import java.util.HashMap;
import java.util.Map;

public class Room {
    private String description;
    private Map<String, Room> exits;
    private Map<String, Item> items;
    private boolean doorClosed;
    private boolean doorLocked;
    private String requiredKey;

    public Room(String description) {
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new HashMap<>();
        this.doorClosed = false;
        this.doorLocked = false;
        this.requiredKey = null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public void setExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void addItem(String itemName, Item item) {
        items.put(itemName, item);
    }

    public void removeItem(String itemName) {
        items.remove(itemName);
    }

    public boolean isDoorClosed() {
        return doorClosed;
    }

    public boolean isDoorLocked() {
        return doorLocked;
    }

    public void setDoorClosed(boolean closed) {
        this.doorClosed = closed;
    }

    public void setLockedDoor(boolean locked, Item keyItem) {
        this.doorLocked = locked;
        if (locked) {
            this.requiredKey = keyItem.getName();          // Imposta il nome dell'oggetto come chiave richiesta per aprire la porta
        } else {
            this.requiredKey = null;             // Rimuovi la chiave richiesta quando la porta non è più chiusa a chiave
        }
    }    

    public String getRequiredKey() {
        return requiredKey;
    }

    public void setRequiredKey(String requiredKey) {
        this.requiredKey = requiredKey;
    }

    public boolean hasRequiredKey(Map<String, Item> inventory) {
        return requiredKey != null && inventory.containsKey(requiredKey);
    }
}
