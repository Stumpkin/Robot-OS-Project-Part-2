package edu.csumb.bank1435.pr3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import java.util.List;

public class CHActivity extends Activity
{
    private TextView textView, totalView;
    private Button submitButton, backButton;
    private EditText inputText;
    private Intent contextSwitcher;
    private BookDatabase bookDB;
    private Account activeAccount;

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.cancel_hold_activity);

        textView = (TextView) findViewById(R.id.ch_textView);
        totalView = (TextView) findViewById(R.id.ch_totalText);
        inputText = (EditText) findViewById(R.id.ch_inputText);
        submitButton = (Button) findViewById(R.id.ch_subButton);
        backButton = (Button) findViewById(R.id.ch_backButton);

        bookDB = BookDatabase.getDatabase(this);
        activeAccount = bookDB.getBookDao().getActiveAccount("YES").get(0);

        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (search())
                {
                    updateHolds();
                    displayCurrentBooks();
                }
            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                List<Account> allAccounts = bookDB.getBookDao().getAllAccounts();
                for (Account account : allAccounts)
                {
                    if (account.getIsActive().equals("YES"))
                    {
                        account = activeAccount;
                        account.setIsActive("NO");
                        break;
                    }
                }
                bookDB.getBookDao().updateAllAccounts(allAccounts);
                contextSwitcher = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(contextSwitcher);
            }
        });

        displayCurrentBooks();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        List<Account> allAccounts = bookDB.getBookDao().getAllAccounts();
        for (Account account : allAccounts)
        {
            if (account.getIsActive().equals("YES"))
            {
                account.setIsActive("NO");
                break;
            }
        }
        bookDB.getBookDao().updateAllAccounts(allAccounts);
    }

    void displayCurrentBooks()
    {
            List<Book> rentedBooks = bookDB.getBookDao().getRentedBooksName(activeAccount.getName());
            if (rentedBooks.size() <= 0) {
                textView.setText("No books rented");
                totalView.setText("Total: " + activeAccount.getTotalFormated());
                return;
            }
            String displayString = "=== Rented Books === \n";
            for (Book book : rentedBooks)
            {
                displayString += book.getTitle() + ", " + book.getAuthor() + ", " + book.getPriceFormatted() + " " +
                        book.getDays() + " Day(s)" + "\n";
            }
            textView.setText(displayString);
            totalView.setText("Total: " + activeAccount.getTotalFormated());
        }

    boolean search()
    {
        List<Book> rentedBooks = bookDB.getBookDao().getRentedBooksName(activeAccount.getName());
        String targetTitle = inputText.getText().toString();
        if (rentedBooks.size() > 0)
        {
            for (Book rb : rentedBooks)
            {
                if (rb.getTitle().equals(targetTitle))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return false;
        }
    }

    void updateHolds()
    {
        List<Book> allBooks = bookDB.getBookDao().getAll();
        String targetTitle = inputText.getText().toString();
        for (Book book : allBooks)
        {
            if (book.getTitle().equals(targetTitle))
            {
                activeAccount.increaseTotal(-(book.getPrice() * book.getDays()));
                System.out.println(book.getPrice() * book.getDays());

                book.resetBook();
                break;
            }
        }
        bookDB.getBookDao().update(allBooks);
        bookDB.getBookDao().update(activeAccount);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        List<Account> allAccounts = bookDB.getBookDao().getAllAccounts();
        for (Account account : allAccounts)
        {
            if (account.getIsActive().equals("YES"))
            {
                account.setIsActive("NO");
                break;
            }
        }
        bookDB.getBookDao().updateAllAccounts(allAccounts);
        contextSwitcher = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(contextSwitcher);
    }
}
