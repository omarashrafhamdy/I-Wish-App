package DTO;

public class RegistrationDTO {
    String header;
    String fName;
    String lName;
    String userName;
    String email;
    String password;
    String dob;
    int balance;

    public RegistrationDTO(String header, String fName, String lName, String userName, String email, String password, String dob, int balance) {
        this.header = header;
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.balance = balance;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    
}
