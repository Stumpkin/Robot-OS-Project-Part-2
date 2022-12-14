/**
 * Title: BookDatabase.java
 * Abstract: Using the singleton design pattern, loads the data tables of Books, Accounts, and Logs
 * Author: Jalen Banks
 * ID: 1012
 * Date of Completion: 12/09/22
 */
package edu.csumb.bank1435.pr3;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class, Account.class, Log.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase
{
    public abstract BookDao getBookDao();
    private static BookDatabase oneTime;

    static BookDatabase getDatabase(Context context)
    {
        if (oneTime == null)
        {
            oneTime = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "Book.DB").allowMainThreadQueries().build();
        }
        return oneTime;
    }
}