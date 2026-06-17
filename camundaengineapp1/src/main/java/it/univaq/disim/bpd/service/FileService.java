package it.univaq.disim.bpd.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {

    private static final String DIRECTORY_PATH = "requests/";

    public void writeConfirmedRequestToFile(String requestId, String username, String selectedZonesJson, String invoiceNumber, double amountDue) throws IOException {
        // Create directory if it doesn't exist
        Files.createDirectories(Paths.get(DIRECTORY_PATH));

        File file = new File(DIRECTORY_PATH + "order_" + requestId + ".txt");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("--- Confirmed Billposting Order ---\n");
            writer.write("Request ID: " + requestId + "\n");
            writer.write("Username: " + username + "\n");
            writer.write("Invoice Number: " + invoiceNumber + "\n");
            writer.write("Amount Due: " + amountDue + "\n");
            writer.write("\nSelected Zones:\n");
            writer.write(selectedZonesJson + "\n");
            writer.write("-----------------------------------\n");
        }
    }
}
