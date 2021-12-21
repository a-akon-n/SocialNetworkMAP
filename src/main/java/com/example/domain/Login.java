package com.example.domain;

import java.util.Objects;

public class Login extends Entity<Long>{
    private String password;
    public Login(Long userId, String password) {
        super(userId);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return password.equals(login.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }


}
