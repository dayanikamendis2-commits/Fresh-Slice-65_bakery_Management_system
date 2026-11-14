package model;

public class User {
     //ENCAPSULATION CONCEPT
    private int id;
    private String username;
    private String password;
    private String role;
    private String fullname;

    public User(int id1, String username1, String password1, String role1) {}

    public User(int id, String username, String password, String role, String fullname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
    }

    public User(String username, String password, String role, String fullname) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
    }

    
   //ENCAPSULATION Concept
    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
}
