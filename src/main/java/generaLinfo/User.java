package generaLinfo;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    private final long uniqID;
    private String name;
    private String surname;
    private int age;

    private static final long serialVersionUID = 8753248963608288165L;

    public User(long uniqID) {
        this.uniqID = uniqID;
        this.name = "No";
        this.surname = "Name";
        this.age = -1;
    }

    public User(long uniqID, String name, String surname) {
        this.uniqID = uniqID;
        this.name = name;
        this.surname = surname;
        this.age = -1;
    }

    public User(long uniqID, String name, String surname, int age) {
        this.uniqID = uniqID;
        this.name = name;
        this.surname = surname;

        if(age > 0) this.age = age;
        else this.age = -1;
    }

    public long getUniqID() { return uniqID; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public int getAge() { return age; }

    public void setName(String name) { this.name = name; }

    public void setSurname(String surname) { this.surname = surname; }

    public void setAge(int age) { this.age = age; }


    @Override
    public String toString() {
        return "[ID: " + uniqID + ". Nick: " + name + " " + surname + ". Age: " + age + "]";
    }
}
