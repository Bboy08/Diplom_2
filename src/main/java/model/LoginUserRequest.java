package model;


public class LoginUserRequest {
    private String email;
    private String password;

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginUserRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
