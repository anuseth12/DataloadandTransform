import java.io.*;
import java.util.*;

public class Instrument {
    private int id;
    private String name;
    private String isin;
    private double unitPrice;

    public Instrument(int id, String name, String isin, double unitPrice) {
        this.id = id;
        this.name = name;
        this.isin = isin;
        this.unitPrice = unitPrice;
    }

    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getIsin() {
        return isin;
    }
    
    public void setIsin(String isin) {
        this.isin = isin;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}




