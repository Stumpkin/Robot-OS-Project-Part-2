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


    public Log()
    {
        this.type = "????";
        this.desc = "";
    }

    public Log(String typ, String d)
    {
        this.type = typ;
        this.desc = d;
    }

    public String display()
    {
        return this.type + " | " + this.desc + "\n";
    }

    public void setId(int a) { this.id = a; }
    public void setType(String t) { this.type = t; }
    public void setDesc(String d) { this.desc = d; }
    public int getId() { return this.id; }
    public String getType() { return this.type; }
    public String getDesc() { return this.desc; }
}
