package practikum.api;

public class Login {

    public String email;
    public String password;

    public Login (String email, String password){
        this.email = email;
        this.password = password;
    }

    public Login() {
    }

    public static Login from (Users user) {
        return new Login (user.getEmail(), user.getPassword());
    }


}
