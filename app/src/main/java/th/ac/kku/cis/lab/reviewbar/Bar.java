package th.ac.kku.cis.lab.reviewbar;

public class Bar {
    private String Name;
    private String ImageUrl;
    private String Phone;
    private String Location;
    private String Time;
    public Bar(){}

    public Bar(String name, String imageUrl, String phone, String location, String time) {
        this.Name = name;
        this.ImageUrl = imageUrl;
        this.Phone = phone;
        this.Location = location;
        this.Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
