package Lab2;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Prospect {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(Prospect.class);
    private String firstName;
    private String lastName;
    private String email;

    public Prospect(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String Lastname) {
        this.lastName = lastName;
    }

    public void cleanData() {
        this.firstName = this.firstName.trim().toLowerCase();
        this.lastName = this.lastName.trim().toLowerCase();
        this.firstName = Character.toUpperCase(this.firstName.charAt(0)) + this.firstName.substring(1);
        this.lastName = Character.toUpperCase(this.lastName.charAt(0)) + this.lastName.substring(1);
        this.email = this.email.trim().toLowerCase();
    }

    public static boolean isValidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE).matcher(email).matches();
    }

	
}
