import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;



public class FileProcessor {
    public static void main(String[] args) {
        String instrumentFile = "C:/data/input/InstrumentDetail.csv";
        String positionFile = "C:/data/input/PositionDetails.csv";
        String outputFile = "C:/Data/output";

        List<Instrument> instruments = readInstrumentFile(instrumentFile);
        List<PositionDetails> positions = readPositionFile(positionFile);

        List<String[]> outputData = generateOutputData(instruments, positions);
        writeOutputFile(outputFile, outputData);

        boolean isValid = validateOutputFile(outputFile, instrumentFile, positionFile);
        if (isValid) {
            System.out.println("Output file is valid.");
        } else {
            System.out.println("Output file is not valid.");
        }
    }

  
    private static List<Instrument> readInstrumentFile(String filename) {
        List<Instrument> instruments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }
                String[] rowData = line.split(",");
                int id = Integer.parseInt(rowData[0]);
                String name = rowData[1];
                String isin = rowData[2];
                double unitPrice = Double.parseDouble(rowData[3]);
                Instrument instrument = new Instrument(id, name, isin, unitPrice);
                instruments.add(instrument);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instruments;
    }


    private static List<PositionDetails> readPositionFile(String filename) {
        List<PositionDetails> positions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read and ignore the column headers
            String headerLine = reader.readLine();
            // Process the remaining lines as position details
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                int id = Integer.parseInt(rowData[0]);
                int instrumentId = Integer.parseInt(rowData[1]);
                int quantity = Integer.parseInt(rowData[2]);
                PositionDetails position = new PositionDetails(id, instrumentId, quantity);
                positions.add(position);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return positions;
    }

    private static List<String[]> generateOutputData(List<Instrument> instruments, List<PositionDetails> positions) {
        List<String[]> outputData = new ArrayList<>();

        
     // Add header row
        String[] header = new String[]{"ID", "Position ID", "ISIN", "Quantity", "Total Price"};
        outputData.add(header);
        
        for (PositionDetails position : positions) {
            Instrument instrument = findInstrumentById(instruments, position.getInstrumentId());

            if (instrument != null) {
                String[] outputEntry = new String[5];
                outputEntry[0] = String.valueOf(position.getId());
                outputEntry[1] = String.valueOf(position.getInstrumentId());
                outputEntry[2] = instrument.getIsin();
                outputEntry[3] = String.valueOf(position.getQuantity());
                outputEntry[4] = String.valueOf(position.getQuantity() * instrument.getUnitPrice());

                outputData.add(outputEntry);
            }
        }

        return outputData;
    }

private static boolean validateOutputFile(String outputFile, String instrumentFile, String positionFile) {
    List<Instrument> instruments = readInstrumentFile(instrumentFile);
    List<PositionDetails> positions = readPositionFile(positionFile);
    List<String[]> outputData = readOutputFile(outputFile);

    // Validate the output data against the input files
    for (String[] row : outputData) {
        int positionId = Integer.parseInt(row[1]);
        String isin = row[2];
        int quantity = Integer.parseInt(row[3]);
        double totalPrice = Double.parseDouble(row[4]);

        // Check if position ID exists in the positions list
        boolean positionExists = positions.stream()
                .anyMatch(position -> position.getId() == positionId);
        if (!positionExists) {
            return false;
        }

        // Check if ISIN exists in the instruments list
        boolean instrumentExists = instruments.stream()
                .anyMatch(instrument -> instrument.getIsin().equals(isin));
        if (!instrumentExists) {
            return false;
        }
        
       //verify quantity comparison 
        
        PositionDetails position = findPositionById(positions, positionId);
        if (position.getQuantity() != quantity) {
            return false;
        }
        
        // Calculate the total price based on quantity and unit price
        Instrument instrument = findInstrumentByIsin(instruments, isin);
        double calculatedTotalPrice = quantity * instrument.getUnitPrice();

        // Compare the calculated total price with the one in the output data
        if (Math.abs(calculatedTotalPrice - totalPrice) > 0.001) {
            return false;
        }
    }

    return true;
}


private static Instrument findInstrumentByIsin(List<Instrument> instruments, String isin) {
    for (Instrument instrument : instruments) {
        if (instrument.getIsin().equals(isin)) {
            return instrument;
        }
    }
    return null;
}

private static Instrument findInstrumentById(List<Instrument> instruments, int instrumentId) {
    for (Instrument instrument : instruments) {
        if (instrument.getId() == instrumentId) {
            return instrument;
        }
    }
    return null;
}

private static List<String[]> readOutputFile(String filename) {
    List<String[]> outputData = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] rowData = line.split(",");
            outputData.add(rowData);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return outputData;
}


private static void writeOutputFile(String directory, List<String[]> outputData) {
    // Generate a file name based on the current date and time
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String timestamp = dateFormat.format(new Date());
    String filename = directory + File.separator + "output_" + timestamp + ".csv";

    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        // Write the output data to the file
        for (String[] rowData : outputData) {
            String line = String.join(",", rowData);
            writer.println(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }}

    
    private static PositionDetails findPositionById(List<PositionDetails> positions, int positionId) {
        for (PositionDetails position : positions) {
            if (position.getId() == positionId) {
                return position;
            }
        }
        return null;
    }}










       




