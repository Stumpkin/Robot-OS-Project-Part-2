package edu.csumb.bank1435.pr3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

@Entity(tableName = "Books")
public class Book
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    //private String author; commented out for now
    private String title;
    //private String isbn; commented out for now
    //private int year; commented out for now
    private double price;
    private String priceFormatted;

    public Book()
    {
        price = 0;
        priceFormatted = "$0.00";
    }

    public Book(String t, double p)
    {
        this.price = p;
        //this.author = a; commented out for now
        this.title = t;
        //this.isbn = is; commented out for now
        //this.year = y; commented out for now
        this.priceFormatted = setPriceFormat();
    }

    String setPriceFormat()
    {
        String format = "###,##0.00";
        DecimalFormat df = new DecimalFormat(format);
        return "$" + df.format(price);
    }

    public int getId() { return this.id; }

//    public String getAuthor() {
//        return this.author;
//    } commented out for now

    public String getTitle() {
        return this.title;
    }

//    public String getIsbn() { commented out for now
//        return this.isbn;
//    }

//    public int getYear() { commented out for now
//        return this.year;
//    }

    public double getPrice() { return this.price; }

    public String getPriceFormatted() { return this.priceFormatted; }

    public void setPriceFormatted(String thing) { this.priceFormatted = thing; }

    public void setId(int id) {
        this.id = id;
    }

//    public void setAuthor(String author) { commented out for now
//        this.author = author;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public void setIsbn(String isbn) { commented out for now
//        this.isbn = isbn;
//    }
//
//    public void setYear(int year) { commented out for now
//        this.year = year;
//    }

    public void setPrice(double price) { this.price = price; this.priceFormatted = setPriceFormat(); }
}
