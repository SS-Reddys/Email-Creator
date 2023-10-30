import static org.junit.Assert.assertEquals;

import Lab2.EmailListCleaner;
import Lab2.Prospect;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EmailListCleanerTest {

    @Test
    public void testCapitalizeFirstLetter() {
        // Input string with multiple words
        String input = "hello world java programming";
        // Expected output after capitalizing the first letter of each word
        String expectedOutput = "Hello World Java Programming";

        // Call the method to capitalize the first letter of each word
        String result = EmailListCleaner.capitalizeFirstLetter(input);

        // Assert that the result matches the expected output
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testInvalidEmailFormat() throws IOException {
        Path tempFile = Files.createTempFile("invalidEmailFormat", ".txt");

        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("invalidemail1\n");
            writer.write("invalidemail2\n");
        }

        List<Prospect> prospects = EmailListCleaner.readProspectsFromFile(tempFile.toString(), 1);
        EmailListCleaner.validateProspects(prospects);

        assertEquals(0, prospects.size()); // Expecting 0 valid prospects due to invalid email format

        Files.delete(tempFile);
    }

    @Test
    public void testCleanData() {
        // Input prospect with lowercase first name, last name, and email
        Prospect prospect = new Prospect("john", "doe", "john.doe@example.com");

        // Call the cleanData method
        prospect.cleanData();

        // Assert that first name and last name are capitalized, and email is lowercase
        assertEquals("John", prospect.getFirstName());
        assertEquals("Doe", prospect.getLastName());
        assertEquals("john.doe@example.com", prospect.getEmail());
    }
}
