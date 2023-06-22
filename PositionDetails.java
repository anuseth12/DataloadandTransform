import java.io.*;
import java.util.*;

public class PositionDetails {
    private int id;
    private int instrumentId;
    private int quantity;

    public PositionDetails(int id, int instrumentId2, int quantity) {
        this.id = id;
        this.instrumentId = instrumentId2;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
   
    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

