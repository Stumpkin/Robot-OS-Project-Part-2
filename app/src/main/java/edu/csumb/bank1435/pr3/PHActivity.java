/**
 * Title: PHActivity.java
 * Abstract: User can place a hold and select how many days to rent a book using a calendar then they must sign in
 * Rental dates must some time today or past today's date and return date must be within 7 days of the
 * rental date
 * Author: Jalen Banks
 * ID: 1012
 * Date of Completion: 12/07/22
 */
package edu.csumb.bank1435.pr3;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.widget.TextView;
import java.util.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;
import android.widget.EditText;
import android.widget.CalendarView;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PHActivity extends AppCompatActivity
{
    private Button rentalDateButton, returnDateButton, subButton, loginButton;
    private TextView assignedRentalDate, assignedReturnDate, booksText, debugTextView;
    private AlertDialog.Builder bobBuilder;
    private AlertDialog dialouge;
    private CalendarView calendar, calendar2;
    private String rentalDateString, returnDateString;
    private EditText inputText, inputUserText, inputPassText;
    private LinearLayout LL;
    private BookDatabase bookDB;
    private Intent contextSwitcher;
    private boolean failedOnce = false;

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.place_hold_activity);

        rentalDateButton = (Button) findViewById(R.id.ph_rentalDate_button);
        returnDateButton = (Button) findViewById(R.id.ph_returnDate_button);
        assignedRentalDate = (TextView) findViewById(R.id.ph_rentalDate_Text);
        assignedReturnDate = (TextView) findViewById(R.id.ph_returnDate_text);
        booksText = (TextView) findViewById(R.id.ph_currentBookText);
        inputText = (EditText) findViewById(R.id.ph_inputText);
        subButton = (Button) findViewById(R.id.ph_submitButton);
        debugTextView = (TextView) findViewById(R.id.ph_debugTextView);
        LL = (LinearLayout) findViewById(R.id.ph_layout);

        bookDB = BookDatabase.getDatabase(this);

        rentalDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createRentalDialog();
            }
        });

        returnDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createReturnDialog();
            }
        });

        subButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow (LL.getWindowToken(), 0);
                createLoginDialogue();
            }
        });
        displayBooks();
    }

    public void createRentalDialog()
    {
        bobBuilder = new AlertDialog.Builder(this);
        final View popView = getLayoutInflater().inflate(R.layout.rental_date_view, null);
        calendar = (CalendarView) popView.findViewById(R.id.ph_holdCalendar);
        bobBuilder.setView(popView);
        dialouge = bobBuilder.create();
        dialouge.show();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d)
            {
                m++;
                String sample = m + "/" + d + "/" + y;
                //DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                assignedRentalDate.setText(sample);
                rentalDateString = assignedRentalDate.getText().toString();
                dialouge.dismiss();
            }
        });
    }

    public void createReturnDialog()
    {
        bobBuilder = new AlertDialog.Builder(this);
        final View popView = getLayoutInflater().inflate(R.layout.return_date_view, null);
        calendar2 = popView.findViewById(R.id.ph_returnCalendar);
        bobBuilder.setView(popView);
        dialouge = bobBuilder.create();
        dialouge.show();
        calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                m++;
                String sample = m + "/" + d +"/" + y;
                assignedReturnDate.setText(sample);
                returnDateString = assignedReturnDate.getText().toString();
                dialouge.dismiss();
            }
        });
    }


    public void createLoginDialogue()
    {
        bobBuilder = new AlertDialog.Builder(this);
        final View popView = getLayoutInflater().inflate(R.layout.login_view, null);
        inputUserText = (EditText) popView.findViewById(R.id.login_username);
        inputPassText = (EditText) popView.findViewById(R.id.login_password);
        loginButton = (Button) popView.findViewById(R.id.loginButton);
        LL = (LinearLayout) popView.findViewById(R.id.login_Layout);
        bobBuilder.setView(popView);
        dialouge = bobBuilder.create();
        dialouge.show();


        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow (LL.getWindowToken(), 0);
                if (!validDates())
                {
                    //debugTextView.setText("Invalid hold and/or return date");
                    Toast.makeText(getApplicationContext(), "Invalid hold and/or return date",
                            Toast.LENGTH_LONG).show();
                    dialouge.dismiss();
                    return;
                }
                boolean flag = false;

                List<Account> allAccounts = bookDB.getBookDao().getAllAccounts();
                String tempName = inputUserText.getText().toString();
                String tempPass = inputPassText.getText().toString();
                contextSwitcher = new Intent(getApplicationContext(), MainActivity.class);
                for (Account account : allAccounts)
                {
                    if (account.getName().equals(tempName) && account.getPass().equals(tempPass))
                    {
                        flag = true;
                        if (!account.getHasHold())
                        {
                            search(account);
                            account.setHasHold(true);
                            dialouge.dismiss();
                            break;
                        }

                        else
                        {
                            search(account);
                            dialouge.dismiss();
                            break;
                        }
                    }
                }

                if (!flag)
                {
                    Toast.makeText(getApplicationContext(), "Cannot find account", Toast.LENGTH_LONG).show();
                    //debugTextView.setText("Cannot find account");
                    if (!failedOnce)
                    {
                        failedOnce = true;
                        return;
                    }

                    else
                    {
                        dialouge.dismiss();

                        startActivity(contextSwitcher);
                        return;
                    }

                }
                bookDB.getBookDao().updateAllAccounts(allAccounts);
                dialouge.dismiss();

            }
        });


    }
    void displayBooks()
    {
        List<Book> currentBooks = bookDB.getBookDao().getAll();
        String displayString = "Books " ;
        for (Book book : currentBooks)
        {
            displayString += "\n" + book.getTitle() + ", " + book.getAuthor() + ", "+
                    book.getPriceFormatted();
        }
        booksText.setText(displayString);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void search(Account someAccount)
    {
        String target = inputText.getText().toString();
        List<Book> searchResults = bookDB.getBookDao().searchbyTitle(target);
        List<Book> allBooks = bookDB.getBookDao().getAll();

        if (searchResults.size() > 0)
        {
            if (searchResults.get(0).getAvaliable().equals("YES"))
            {
                Book temp = searchResults.get(0);
                temp.setAvaliable("NO");
                searchResults.get(0).setAvaliable("NO");
                String priceTemp = "";
                for (Book book : allBooks)
                {
                    if (book.getTitle().equals(temp.getTitle()))
                    {
                        book.setAvaliable("NO");
                        book.setRentalDate(rentalDateString);
                        book.setReturnDate(returnDateString);
                        book.setRentedBy(someAccount.getName());
                        book.setDays(rentedDays());
                        someAccount.increaseTotal(book.getPrice() * rentedDays());
                        temp.setPrice(book.getPrice() * rentedDays());
                        priceTemp =  temp.getPriceFormatted();
                        break;
                    }
                }
                bookDB.getBookDao().update(allBooks);
                String date = DateFormat.getDateTimeInstance().format(new Date());
                String message = searchResults.get(0).getTitle() +
                        " has been successfully checked out at " + date + " from " + rentalDateString + " to " + returnDateString +
                        " for a total of: " + priceTemp + " for " + someAccount.getName();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                //debugTextView.setText(message);

                Log tempLog = new Log("New Hold", searchResults.get(0).getTitle() + " has been " +
                        "checked out at " + rentalDateString + " to " + returnDateString + " by " +
                        someAccount.getName(), date);
                bookDB.getBookDao().insert(tempLog);

            }

            else
            {
                Toast.makeText(this, "This book is not available", Toast.LENGTH_LONG).show();
                //debugTextView.setText("This book is not available");
            }
        }

        else
        {
            Toast.makeText(this, "Cannot find book", Toast.LENGTH_LONG).show();
            debugTextView.setText("Cannot find book");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    boolean validDates()
    {
        if (rentalDateString == null || returnDateString == null)
        {
            return false;
        }

        String rental = reforamtDateString(rentalDateString);
        String returnal = reforamtDateString(returnDateString);
        LocalDate date1 = LocalDate.parse(rental);
        LocalDate date2 = LocalDate.parse(returnal);
        LocalDate week = date1.plusWeeks(1);
        LocalDate today = LocalDate.now();
        if (date2.compareTo(today) >= 0)
        {
            if ((date2.compareTo(date1) >= 0 && date2.compareTo(week) < 0) & date1.compareTo(today) >= 0)
            {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    int rentedDays()
    {
        String rental = reforamtDateString(rentalDateString);
        String returnal = reforamtDateString(returnDateString);
        LocalDate date1 = LocalDate.parse(rental);
        LocalDate date2 = LocalDate.parse(returnal);
        return date2.compareTo(date1) + 1;
    }

    String reforamtDateString(String a)
    {
        int [] breakdown = new int [3];
        String [] args = a.split("/");
        for (int b = 0; b < args.length; b++)
        {
            breakdown[b] = Integer.parseInt(args[b]);
        }

        String month = String.format("%02d", breakdown[0]);
        String day = String.format("%02d", breakdown[1]);
        String year = String.format("%04d", breakdown[2]);
        return year + "-" + month + "-" + day;
    }
}