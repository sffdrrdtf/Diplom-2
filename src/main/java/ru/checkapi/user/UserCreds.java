package ru.checkapi.user;
import static ru.checkapi.user.GeneratorRandom.faker;

public class UserCreds {

    private String email;
    private String password;
    private String name;

    public UserCreds(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;

    }

    public UserCreds() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserCreds randomDataUser() {
        return new UserCreds((faker.name().firstName() + "@example.com"),
                (faker.number().digits(6)), (faker.name().firstName()));

    }
}
