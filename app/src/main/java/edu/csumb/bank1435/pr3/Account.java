package edu.csumb.bank1435.pr3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Accounts")
public class Account
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String pass;
    //List<Book> rentedBooks;

    public Account(String n, String p)
    {
        this.name = n;
        this.pass = p;
    }
    public Account()
    {
        this.name = "????";
        this.pass = "????";
    }

//    public void addBook(Book b)
//    {
//        rentedBooks.add(b);
//    }

    public String getName() { return this.name; }
    public String getPass() { return this.pass; }
    public int getId() { return this.id; }
    //public List<Book> getRentedBooks() { return this.rentedBooks; }
    public void setId(int i) { this.id = i; }
    public void setName(String n) { this.name = n; }
    public void setPass(String p) { this.pass = p; }
    //public void setRentedBooks(List<Book> rb) { this.rentedBooks = rb; }
}
