package helper;


import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import model.CreateUserRequest;
import java.util.Locale;

public class UserGenerator {
    public static CreateUserRequest getRandomUser() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        String email = fakeValuesService.bothify("????##@gmail.com");
        String password = fakeValuesService.bothify("????##??###");
        String name = fakeValuesService.bothify("???????????");
        return new CreateUserRequest(email, password, name);
    }

    public static CreateUserRequest getRandomUserWithoutField() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        String email = fakeValuesService.bothify("????##@gmail.com");
        String password = fakeValuesService.bothify("????##??###");
        String name = "";
        return new CreateUserRequest(email, password, name);
    }
}
