package Lab2;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailListCleaner {

    final static Logger logger = LogManager.getLogger(EmailListCleaner.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        logger.info("Choose input file format:");
        System.out.println("Choose input file format:");
        System.out.println("1. Text");
        logger.info("1. Text");
        System.out.println("2. CSV");
        logger.info("2. CSV");
        int choice = scanner.nextInt();
        
        String inputFilePath;
        String fileType;
        if (choice == 1) {
            inputFilePath = "src/main/resources/Props.txt"; 
            fileType = "Props.txt";// Set the path for text file
        } else if (choice == 2) {
        	fileType = "Prospects.csv";
            inputFilePath = "src/main/resources/Prospects.csv"; // Set the path for CSV file
        } else {
            System.out.println("Invalid choice.");
            logger.error("Invalid choice.");
            scanner.close();
            return;
        }
       
        String outputFilePath = "src/main/resources/Prospects_clean.csv";
        String outputFileType="Prospects_clean.csv";
        System.out.println(" ");
        System.out.println("File Cleaner\n");
        logger.info("File Cleaner\n");
        System.out.println("Source File: " + fileType);
        logger.info("Source File: " + fileType);
        System.out.println("Cleaned File: " + outputFileType);
        logger.info("Cleaned File: " + outputFileType);
       
        try {
            List<Prospect> prospects = readProspectsFromFile(inputFilePath, choice);
            validateProspects(prospects);
            cleanProspects(prospects);
            writeProspectsToFile(prospects, outputFilePath);
            System.out.println("Congratulations! Your file has been cleaned!\n");
            logger.info("Congratulations! Your file has been cleaned!");
         // Print input file content to console
            printFileContent(inputFilePath, "The " + fileType + " file");
            System.out.println(" ");

            // Print cleaned file content to console
            printUniqueFileContent(outputFilePath, "The " + outputFileType + " file");

            
        } catch (FileNotFoundException e) {
            logger.error("Error: The input file was not found. Please make sure the file exists.");
            System.err.println("Error: The input file was not found. Please make sure the file exists.");
        } catch (IOException e) {
            logger.error("Error: An I/O error occurred while processing the file.");
            System.err.println("Error: An I/O error occurred while processing the file.");
        } catch (NoSuchElementException | IllegalStateException e) {
            logger.error("Error: Invalid input format. Please provide valid input.");
        } finally {
            scanner.close();
        }
        
    }

    public static List<Prospect> readProspectsFromFile(String filePath, int fileType) throws IOException {
        List<Prospect> prospects = new ArrayList<>();
        File inputFile = new File(filePath);

        if (!inputFile.exists()) {
            throw new FileNotFoundException("Error: The input file does not exist.");
        }

        if (!inputFile.canRead()) {
            throw new IOException("Error: Cannot read the input file. Please check file permissions.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            if (fileType == 2) {
                reader.readLine(); // Skip header line for CSV
            }
            while ((line = reader.readLine()) != null) {
                String[] data;
                if (fileType == 2) {
                    data = line.split(","); // Split lines using tab character for CSV
                } else {
                    data = line.split("\\s*,\\s*"); // Split lines using comma for Text
                }
                if (data.length != 3) {
                    logger.error("Error: Invalid data format found. Skipping the line: " + line);
                    System.err.println("Error: Invalid data format found. Skipping the line: " + line);
                    continue;
                }
                String firstName = data[0].trim();
                String lastName = data[1].trim();
                String email = data[2].trim();
                prospects.add(new Prospect(firstName, lastName, email));
               
            }
        }
        return prospects;
    }

    public static void validateProspects(List<Prospect> prospects) {
        List<Prospect> validProspects = new ArrayList<>();
        Set<String> uniqueEmails = new HashSet<>();
        Set<String> duplicateEmails = new HashSet<>();

        for (Prospect prospect : prospects) {
            String email = prospect.getEmail();
            if (Prospect.isValidEmailFormat(email)) {
                if (!uniqueEmails.add(email)) {
                    duplicateEmails.add(email);
                }
                validProspects.add(prospect);
            }
        }

        // Handle duplicate prospects here if needed
      
            for (String duplicateEmail : duplicateEmails) {
           System.err.println("Duplicate email found: " + duplicateEmail + ". Removing the duplicate entry.");
                // Log or handle the duplicate emails as necessary
            }
        

        // Replace the original list with the filtered list
        prospects.clear();
        prospects.addAll(validProspects);
    }

    public static void cleanProspects(List<Prospect> prospects) {
        for (Prospect prospect : prospects) {
            String firstName = capitalizeFirstLetter(prospect.getFirstName().trim());
            String lastName = capitalizeFirstLetter(prospect.getLastName().trim());
            String email = prospect.getEmail().trim().toLowerCase();
            prospect.setFirstName(firstName);
            prospect.setLastName(lastName);
            prospect.setEmail(email);
        }
    }


    public static void writeProspectsToFile(List<Prospect> prospects, String filePath) throws IOException {
        Set<String> uniqueEmails = new HashSet<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("First,Last,email\n");
            for (Prospect prospect : prospects) {
                String firstName = capitalizeFirstLetter(prospect.getFirstName());
                String lastName = capitalizeFirstLetter(prospect.getLastName());
                String email = prospect.getEmail().toLowerCase();

                // Check if the email is unique before writing to the file
                if (uniqueEmails.add(email)) {
                    writer.write(firstName + "," + lastName + "," + email + "\n");
                }
            }
        }
    }
    
// new 
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split(" ");
        StringBuilder capitalizedString = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                char firstLetter = Character.toUpperCase(word.charAt(0));
                String restOfWord = word.substring(1).toLowerCase();
                capitalizedString.append(firstLetter).append(restOfWord).append(" ");
            }
        }
        // Remove the trailing space and return the capitalized string
        return capitalizedString.toString().trim();
    }    

    public static void printFileContent(String filePath, String message) throws IOException {
        System.out.println(message + " " );
        System.out.println(" ");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
    public static void printUniqueFileContent(String filePath, String message) throws IOException {
        System.out.println(message + " ");
        System.out.println(" ");

        Set<String> uniqueLines = new LinkedHashSet<>(); // Use a LinkedHashSet to maintain order of insertion

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                uniqueLines.add(line); // Add each line to the LinkedHashSet
            }
        }

        // Print unique lines from the LinkedHashSet
        for (String uniqueLine : uniqueLines) {
            System.out.println(uniqueLine);
        }
    }
    
    
}