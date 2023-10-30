import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream; // Add this import
import java.io.PrintStream; // Add this import
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailAppCreatorTest {
    @SuppressWarnings("unused")
	private EmailAppCreator emailApp;
    
    @Before
    public void setUp() {
        emailApp = new EmailAppCreator();
    }
    
    @Test
    public void testReadContactsFromFileValidFile() {
        List<Contact> contacts = EmailAppCreator.readContactsFromFile("src/test/resources/validContacts.csv");
        assertNotNull(contacts);
        assertEquals(3, contacts.size()); // Check the number of contacts in the valid CSV file
    }
    
    @Test
    public void testGetEmailTemplateValidFile() {
        // Specify the path to a valid email template file for testing
        String templateFilePath = "src/test/resources/testEmailTemplate.txt";

        String emailTemplate = EmailAppCreator.getEmailTemplate(templateFilePath);

        assertNotNull(emailTemplate);
        assertTrue(emailTemplate.contains("{email}")); // Check if the template contains placeholders
        assertTrue(emailTemplate.contains("{first_name}"));
    }
    
    @Test
    public void testSendEmails() {
        // Mock configuration and contact list for testing
        Properties config = new Properties();
        config.setProperty("smtp.host", "smtp.example.com");
        config.setProperty("smtp.port", "587");
        config.setProperty("smtp.username", "testuser");
        config.setProperty("smtp.password", "testpassword");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John", "john@example.com"));
        contacts.add(new Contact("Jane", "jane@example.com"));
        
        // Capture console output for testing
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        // Perform the email sending test
        EmailAppCreator.sendEmails(contacts, "Test Email Content", config);

        // Check if the emails were sent successfully
        assertTrue(consoleOutput.toString().contains("Email sent to john@example.com"));
        assertTrue(consoleOutput.toString().contains("Email sent to jane@example.com"));

        // Reset console output
        System.setOut(System.out);
    }
}
