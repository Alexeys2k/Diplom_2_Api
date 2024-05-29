package practikum.create;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import practikum.api.Users;

public class GenUsers {
    public static Faker faker = new Faker();

    public static Users getSuccessCreateUser() {
        String email = faker.internet().emailAddress();
        String password = RandomStringUtils.randomAlphabetic(12);
        String name = faker.name().firstName();
        return new Users (email, password, name);
    }

    public static String getNewEmail(){
        String newEmail = faker.internet().emailAddress();
        return newEmail;
    }

    public static String getNewPassword(){
        String newPassword = RandomStringUtils.randomAlphabetic(12);
        return newPassword;
    }

    public static String getNewName(){
        String newName = faker.name().firstName();
        return newName;
    }
}
