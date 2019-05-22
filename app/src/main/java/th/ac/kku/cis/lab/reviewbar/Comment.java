package th.ac.kku.cis.lab.reviewbar;

import android.widget.Toast;

public class Comment {
    private String Name_Person,Email_Person,Message_Person,Name_Store;
    public Comment(){

    }

    public Comment(String name_Person, String email_Person, String message_Person,String name_Store) {
        this.Name_Person = name_Person;
        this.Email_Person = email_Person;
        this.Message_Person = message_Person;
        this.Name_Store = name_Store;
    }

    public String getName_Person() {
        return this.Name_Person;
    }

    public void setName_Person(String name_Person) {
        this.Name_Person = name_Person;
    }

    public String getEmail_Person() {
        return this.Email_Person;
    }

    public void setEmail_Person(String email_Person) {
        this.Email_Person = email_Person;
    }

    public String getMessage_Person() {
        return this.Message_Person;
    }

    public void setMessage_Person(String message_Person) {
        this.Message_Person = message_Person;
    }

    public String getName_Store() {
        return this.Name_Store;
    }

    public void setName_Store(String name_Store) {
        this.Name_Store = name_Store;
    }
}
