package ru.check_api.user;

import com.github.javafaker.Faker;

public class GeneratorRandom extends CreateUser {
    static Faker faker = new Faker();

    public CreateUser getCreateUser() {
        String email = (faker.name().firstName() + "@example.com");
        String password = (faker.number().digits(6));
        String name = ((faker.name().firstName()));
        return new CreateUser(email, password, name);
    }

    public CreateUser getCreateUserWithoutPass() {
        String email = (faker.name().firstName() + "@example.com");
        String password = ("");
        String name = ((faker.name().firstName()));
        return new CreateUser(email, password, name);
    }

    public CreateUser getCreateLogin() {
        String email = (faker.name().firstName() + "@example.comtest");
        String password = (faker.number().digits(6) + "test");
        String name = ((faker.name().firstName()));
        return new CreateUser(email, password, name);
    }

}
