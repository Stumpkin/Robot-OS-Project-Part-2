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
import java.util.Date;
import java.text.DateFormat;

public class CHActivity extends Activity
{
    private TextView textView, totalView;
    private Button submitButton, backButton;
    private EditText inputText;
    private Intent contextSwitcher;
    private BookDatabase bookDB;
    private LinearLayout LL;
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
        LL = (LinearLayout) findViewById(R.id.ch_Layout);

        bookDB = BookDatabase.getDatabase(this);
        activeAccount = bookDB.getBookDao().getActiveAccount("YES").get(0);

        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow (LL.getWindowToken(), 0);
                if (search())
                {
                    updateHolds();
                    displayCurrentBooks();
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot find book", Toast.LENGTH_LONG).show();
                }
                resetText();
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
        String message = activeAccount.getName() + " has canceled their hold for ";
        String date = DateFormat.getDateInstance().format(new Date());
        for (Book book : allBooks)
        {
            if (book.getTitle().equals(targetTitle))
            {
                activeAccount.increaseTotal(-(book.getPrice() * book.getDays()));
                System.out.println(book.getPrice() * book.getDays());
                message += book.getTitle() + " from " + book.getRentalDate() + " to " + book.getReturnDate() +
                        " at " + date;
                book.resetBook();
                break;
            }
        }
        bookDB.getBookDao().update(allBooks);
        bookDB.getBookDao().update(activeAccount);
        Log temp = new Log("Cancel Hold", message);
        bookDB.getBookDao().insert(temp);
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

    void resetText()
    {
        inputText.setText("");
    }
}
