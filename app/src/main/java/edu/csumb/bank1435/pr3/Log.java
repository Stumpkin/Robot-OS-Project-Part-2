/**
 * Title: Log.java
 * Abstract: Database object that keeps track of what transactions took place in the system
 * Author: Jalen Banks
 * ID: 1012
 * Date of Completion: 12/07/22
 */
package edu.csumb.bank1435.pr3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Logs")
public class Log
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String desc;
    private String time;


    public Log()
    {
        this.type = "????";
        this.time = "????";
        this.desc = "";
    }

    public Log(String typ, String d, String tm)
    {
        this.type = typ;
        this.desc = d;
        this.time = tm;
    }

    public String display()
    {
        return this.type + " | " + this.desc + "\n" + "Time and Date: " + this.time + "\n\n";
    }

    public void setId(int a) { this.id = a; }
    public void setType(String t) { this.type = t; }
    public void setDesc(String d) { this.desc = d; }
    public void setTime(String t) { this.time = t; }
    public int getId() { return this.id; }
    public String getTime() { return this.time; }
    public String getType() { return this.type; }
    public String getDesc() { return this.desc; }
}
