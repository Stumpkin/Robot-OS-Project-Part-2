/**
 * Title: Account.java
 * Abstract: Account holds: a name, password, current price, is active state and has holds state
 * ID: 1012
 * Author: Jalen Banks
 * Date of Completion: 12/07/22
 */
package edu.csumb.bank1435.pr3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.text.DecimalFormat;


@Entity(tableName = "Accounts")
public class Account
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String pass;
    private String holdDate;
    private boolean hasHold;
    private String isActive;
    private double total;
    private String totalFormated;

    public Account(String n, String p)
    {
        this.name = n;
        this.pass = p;
        this.holdDate = "????";
        this.hasHold = false;
        this.isActive = "NO";
        this.total = 0;
        this.totalFormated = moneyFormatted();
    }
    public Account()
    {
        this.name = "????";
        this.pass = "????";
        this.holdDate = "????";
        this.hasHold = false;
        this.isActive = "NO";
        this.total = 0;
        this.totalFormated = moneyFormatted();
    }
    public String moneyFormatted()
    {
        String format = "###,##0.00";
        DecimalFormat df = new DecimalFormat(format);
        return "$" + df.format(total);
    }

    public void increaseTotal(double a)
    {
        this.total += a;
        this.totalFormated = moneyFormatted();
    }

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
    public void setIsActive(String act) { this.isActive = act; }
    public String getIsActive() { return this.isActive; }
    public String getTotalFormated() { return this.moneyFormatted(); }
    public double getTotal() { return this.total; }
    public void setTotal(double a) { this.total = a; }
    public void setTotalFormated(String tf) {this.totalFormated = tf; }
}
