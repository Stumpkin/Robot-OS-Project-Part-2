package edu.csumb.bank1435.pr3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Toast;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;

public class CBActivity extends Activity
{
    private EditText editTitle, editAuthor, editPrice;
    private Button createButton, backButton;
    private LinearLayout LL;
    private Intent contextSwitcher;
    private BookDatabase bookDB;
    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.add_book_activity);
        editTitle = (EditText) findViewById(R.id.ab_editTitle);
        editAuthor = (EditText) findViewById(R.id.ab_editAuthor);
        editPrice = (EditText) findViewById(R.id.ab_editPrice);
        LL = (LinearLayout) findViewById(R.id.ab_Layout);
        createButton = (Button) findViewById(R.id.ab_createButton);
        backButton = (Button) findViewById(R.id.ab_backButton);
        bookDB = BookDatabase.getDatabase(this);

        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow (LL.getWindowToken(), 0);
                resetText();
                createNewBook();
            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                contextSwitcher = new Intent(getApplicationContext(), MSActivity.class);
                startActivity(contextSwitcher);
            }
        });
    }

    void createNewBook() {
        String titleText = editTitle.getText().toString();
        String authorText = editAuthor.getText().toString();
        String priceText = editPrice.getText().toString();
        List<Book> allBooks = bookDB.getBookDao().getAll();
        List<Book> searched = bookDB.getBookDao().searchbyTitle(titleText);
        String[] titleSpaces = titleText.split(" ");
        String[] authorSpaces = authorText.split(" ");
        String currentDate = DateFormat.getDateInstance().format(new Date());

        if (searched.size() > 0) {
            Toast.makeText(getApplicationContext(), "Book already exist", Toast.LENGTH_LONG).show();
            return;
        } else if ((titleText.length() == 0 || authorText.length() == 0) || priceText.length() == 0) {
            Toast.makeText(getApplicationContext(), "No text detected in title/author/price field", Toast.LENGTH_LONG).show();
            return;
        } else if (priceText.contains(" ")) {
            Toast.makeText(getApplicationContext(), "Space detected in price field", Toast.LENGTH_LONG).show();
            return;
        } else if (titleSpaces.length <= 0 || authorSpaces.length <= 0) {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            double translatedPrice = Double.parseDouble(priceText);
            Book temp = new Book(titleText, authorText, translatedPrice);
            bookDB.getBookDao().insert(temp);
            String message = titleText + " was added in " + currentDate;
            Log currentLog = new Log("Book Added", message);
            bookDB.getBookDao().insert(currentLog);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "invalid price", Toast.LENGTH_LONG).show();
        }
    }

    void resetText()
    {
        editAuthor.setText("");
        editTitle.setText("");
        editPrice.setText("");
    }
}
