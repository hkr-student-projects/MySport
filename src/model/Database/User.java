package model.Database;

public class User {
    private int userid;
    private String email;
    private String password;

    public void setId(int id) {
        userid = id;
    }

    public void setEmail(String emailLocal) {
        email = emailLocal;
    }

    public int getUserid() {
        return userid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
