package edu.csumb.bank1435.pr3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;


@Entity(tableName = "Accounts")
public class Account
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String pass;
    private String holdDate;
    private boolean hasHold;

    public Account(String n, String p)
    {
        this.name = n;
        this.pass = p;
        this.holdDate = "????";
        this.hasHold = false;
    }
    public Account()
    {
        this.name = "????";
        this.pass = "????";
        this.holdDate = "????";
        this.hasHold = false;
    }

//    public void addBook(Book b)
//    {
//        rentedBooks.add(b);
//    }

    public String getName() { return this.name; }
    public String getPass() { return this.pass; }
    public int getId() { return this.id; }
    public String getHoldDate() { return this.holdDate; }
    public boolean getHasHold() { return this.hasHold; }
    public void setId(int i) { this.id = i; }
    public void setName(String n) { this.name = n; }
    public void setPass(String p) { this.pass = p; }
    public void setHoldDate(String a) { this.holdDate = a; }
    public void setHasHold(boolean h) { this.hasHold = h; }

}
