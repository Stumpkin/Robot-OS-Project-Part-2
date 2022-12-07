package edu.csumb.bank1435.pr3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

@Entity(tableName = "Books")
public class Book
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;
    private double price;
    private String priceFormatted;
    private String avaliable;
    private String rentalDate;
    private String returnDate;
    private String rentedBy;
    private int days;

    public Book()
    {
        this.price = 0;
        this.days = 0;
        this.priceFormatted = "$0.00";
        this.avaliable = "YES";
        this.rentalDate = "NONE";
        this.returnDate = "NONE";
        this.rentedBy = "NOBODY";
    }

    public Book(String t, String a, double p)
    {
        this.price = p;
        this.title = t;
        this.author = a;
        this.priceFormatted = setPriceFormat();
        this.avaliable = "YES";
        this.rentalDate = "NONE";
        this.returnDate = "NONE";
        this.rentedBy = "NOBODY";
        this.days = 0;
    }

    public Book(Book dook)
    {
        if (dook == this)
        {
            return;
        }

        else
        {
            this.price = dook.getPrice();
            this.title = dook.getTitle();
            this.author = dook.getAuthor();
            this.priceFormatted = dook.getPriceFormatted();
            this.avaliable = dook.getAvaliable();
            this.rentalDate = dook.getRentalDate();
            this.returnDate = dook.getReturnDate();
            this.rentedBy = dook.getRentedBy();
            this.days = 0;
        }
    }
    String setPriceFormat()
    {
        String format = "###,##0.00";
        DecimalFormat df = new DecimalFormat(format);
        return "$" + df.format(price);
    }

    public void resetBook()
    {
        this.days = 0;
        this.rentalDate = "NONE";
        this.returnDate = "NONE";
        this.rentedBy = "NOBODY";
        this.avaliable = "YES";
    }

    public int getId() { return this.id; }


    public String getTitle() {
        return this.title;
    }


    public double getPrice() { return this.price; }

    public String getPriceFormatted() { return this.priceFormatted; }

    public String getAuthor() { return this.author; }

    public void setPriceFormatted(String thing) { this.priceFormatted = thing; }

    public void setId(int id) {
        this.id = id;
    }

    public void setRentedBy(String n) { this.rentedBy = n; }

    public String getRentedBy() { return this.rentedBy; }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) { this.price = price; this.priceFormatted = setPriceFormat(); }

    public void setAvaliable(String av) { this.avaliable = av; }

    public String getAvaliable() { return this.avaliable; }

    public void setAuthor(String a) { this.author = a;}

    public String getRentalDate() { return this.rentalDate; }

    public String getReturnDate() { return this.returnDate; }

    public void setRentalDate(String rd) { this.rentalDate = rd; }

    public void setReturnDate(String rd) { this.returnDate = rd; }

    public void setDays(int d) { this.days = d; }

    public int getDays() { return this.days; }

}
