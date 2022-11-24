package edu.csumb.bank1435.pr3;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button caButton, phButton, chButton, msButton;
    BookDatabase bookDB;
    Intent contextSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        caButton = (Button) findViewById(R.id.mm_caButton);
        phButton = (Button) findViewById(R.id.mm_placeButton);
        //chButton = (Button) findViewById(R.id.mm_cancelButton);
        //msButton = (Button) findViewById(R.id.mm_manageButton);

        caButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                contextSwitch = new Intent(getApplicationContext(), CAActivity.class);
                startActivity(contextSwitch);
            }
        });
        bookDB = BookDatabase.getDatabase(this);
        loadDatabase();
    }

    void loadDatabase()
    {
        List<Account> currentAccounts = bookDB.getBookDao().getAllAccounts();
        List<Book> currentBooks = bookDB.getBookDao().getAll();
        if (currentAccounts.size() <= 0)
        {
            Account[] defaultAccounts = new Account[4];
            defaultAccounts[0] = new Account("Admin2", "Admin2");
            defaultAccounts[1] = new Account("alice5", "csumb100");
            defaultAccounts[2] = new Account("Brian7", "123abc");
            defaultAccounts[3] = new Account("chris12", "CHRIS12");
            bookDB.getBookDao().insert(defaultAccounts);
        }

        if (currentBooks.size() <= 0)
        {
            Book[] defaultBook = new Book[5];
            defaultBook[0] = new Book("Absolute Java", 0.25);
            defaultBook[1] = new Book("Absolute Java 2", 0.50);
            defaultBook[2] = new Book("Python", 1.0);
            defaultBook[3] = new Book("Intro to CS", 0.25);
            defaultBook[4] = new Book("Hot Java", 0.05);
            bookDB.getBookDao().insert(defaultBook);
        }
    }
}