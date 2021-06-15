package com.example.login2;

public class leagues {
    public String email, leaguename, location;

    public leagues() {

    }

    public leagues(String email, String leaguename, String location) {
        this.email = email;
        this.leaguename = leaguename;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLeaguename() {
        return leaguename;
    }

    public void setLeaguename(String leaguename) {
        this.leaguename = leaguename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
