
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailAppCreator {

    public static void main(String[] args) {
    	
    	final Logger logger = LogManager.getLogger(EmailAppCreator.class);
  	 
    	
    	System.out.println("Email Creator");
    	logger.info("Email Creator");
    	
        List<Contact> contacts = readContactsFromFile("src/main/resources/contacts.txt");
        if (contacts == null) {
            System.err.println("Error reading contacts from file.");
            logger.error("Error reading contacts from file.");
            return;
        }

        String emailTemplate = getEmailTemplate("src/main/resources/emailTemplate.txt");
        if (emailTemplate == null) {
            System.err.println("Error reading email template from file.");
            logger.error("Error reading email template from file.");
            return;
        }
        
        if (emailTemplate.isEmpty()) {
	        System.err.println("Error: Email template is empty.");
	        logger.error("Email template is empty.");
	        return;
	    }

        Properties config = loadConfiguration("src/main/resources/config.properties");
        if (config == null) {
            System.err.println("Error loading configuration.");
            logger.error("Error loading configuration.");
            return;
        }

        sendEmails(contacts, emailTemplate, config);
    }

    public static List<Contact> readContactsFromFile(String fileName) {
        List<Contact> contacts = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String email = parts[2].trim();
                    contacts.add(new Contact(firstName, email));
                } else {
                    System.err.println("Invalid data format: " + line);
                    return null; // Invalid data format
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null; // File not found or other I/O error
        }

        if (contacts.isEmpty()) {
            System.err.println("No valid contacts found in the file.");
            return null; // No valid contacts found
        }

        return contacts;
    }

    public static String getEmailTemplate(String templateFileName) {
        try (Scanner scanner = new Scanner(new File(templateFileName))) {
            StringBuilder template = new StringBuilder();
            while (scanner.hasNextLine()) {
                template.append(scanner.nextLine()).append("\n");
            }
            return template.toString();
        } catch (IOException e) {
            System.err.println("Error reading template file: " + e.getMessage());
            return null; // Template file not found or other I/O error
        }
    }

    public static Properties loadConfiguration(String fileName) {
        try (FileInputStream input = new FileInputStream(fileName)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            return null; // Configuration file not found or other I/O error
        }
    }

    public static void sendEmails(List<Contact> contacts, String template, Properties config) {
      
    	final Logger logger = LogManager.getLogger(EmailAppCreator.class);
    	
    	final String smtpHost = config.getProperty("smtp.host");
        final String smtpPort = config.getProperty("smtp.port");
        final String username = config.getProperty("smtp.username");
        final String password = config.getProperty("smtp.password");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        for (Contact contact : contacts) {
            String emailContent = template
                .replace("{email}", contact.getEmail())
                .replace("{first_name}", contact.getFirstName());

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(contact.getEmail()));
                message.setSubject("Deals!");
                message.setText(emailContent);

                Transport.send(message);
                
                logger.info("Email sent to " + contact.getEmail());
                logger.debug("Email content:\n" + emailContent);
                
             
                //System.out.println("Email sent to " + contact.getEmail());
                System.out.println(emailContent);
               
                
                
            } catch (MessagingException e) {
            	 logger.error("Error sending email to " + contact.getEmail() + ": " + e.getMessage());
            	System.err.println("Error sending email to " + contact.getEmail() + ": " + e.getMessage());
            }
        }
     // Log a message after sending all emails successfully
        logger.info("All emails sent successfully.");
    }
}

class Contact {
    private String firstName;
    private String email;

    public Contact(String firstName, String email) {
        this.firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        this.email = email.toLowerCase();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }
}

