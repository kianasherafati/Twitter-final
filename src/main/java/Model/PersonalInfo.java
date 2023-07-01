package Model;

import java.io.Serializable;
import java.util.Objects;

public class PersonalInfo implements Serializable {
    private String website;
    private String location;
    private String bio;

    public PersonalInfo(String website, String location, String bio) {
        this.website = website;
        this.location = location;
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    @Override
    public String toString() {
        if(this.website.equals("exit")) {
            this.website = "-";
        }
        if(this.location.equals("exit")) {
            this.location = "-";
        }
        if(Objects.equals(this.bio, ("exit"))) {
            this.bio = "-";
        }
        return "     PersonalInfo\n" + "-----------------------"+ "\n" + "bio : \n" + bio + "\n"
                +"location : " + location + "\n" +
                "website : " + website + "\n";
    }

}
