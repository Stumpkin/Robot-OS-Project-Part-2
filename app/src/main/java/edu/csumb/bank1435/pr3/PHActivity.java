package edu.csumb.bank1435.pr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    //private LoginActivity
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
                // do date checks
                if (!validDates())
                {
                    debugTextView.setText("Invalid hold and/or return date");
                    dialouge.dismiss();
                    return;
                }
                boolean flag = false;
                List<Account> allAccounts = bookDB.getBookDao().getAllAccounts();
                String tempName = inputUserText.getText().toString();
                String tempPass = inputPassText.getText().toString();
                for (Account account : allAccounts)
                {
                    if (account.getName().equals(tempName) && account.getPass().equals(tempPass))
                    {
                        flag = true;
                        if (!account.getHasHold())
                        {
                            search(account, true);
                            account.setHasHold(true);
                            account.setHoldDate(rentalDateString);
                            dialouge.dismiss();
                            break;
                        }

                        else
                        {
                            search(account, false);
                            break;
                        }
                    }
                }

                if (!flag)
                {
                    Toast.makeText(getApplicationContext(), "Cannot find account", Toast.LENGTH_LONG).show();
                    debugTextView.setText("Cannot find account");
                    dialouge.dismiss();
                    return;
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

    void search(Account someAccount, boolean noHold) // get all of the books and do search from there
    {
        if (!noHold)
        {
            Toast.makeText(this, "This Account already has a hold", Toast.LENGTH_SHORT).show();
            debugTextView.setText("This Account already has a hold");
            return;
        }

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
                for (Book book : allBooks)
                {
                    if (book.getTitle().equals(temp.getTitle()))
                    {
                        book.setAvaliable("NO");
                        book.setRentalDate(rentalDateString);
                        book.setReturnDate(returnDateString);
                        book.setReturnDate(someAccount.getName());
                        break;
                    }
                }
                bookDB.getBookDao().update(allBooks);
                String date = DateFormat.getDateTimeInstance().format(new Date());
                Toast.makeText(this, searchResults.get(0).getTitle() +
                        " has been successfully checked out at " + date, Toast.LENGTH_LONG).show();
                debugTextView.setText(searchResults.get(0).getTitle() + " has been successfully checked" +
                        " out at " + date + " by " + someAccount.getName());

            }

            else
            {
                Toast.makeText(this, "This book is not available", Toast.LENGTH_LONG).show();
                debugTextView.setText("This book is not available");
            }
        }

        else
        {
            Toast.makeText(this, "Cannot find book", Toast.LENGTH_LONG).show();
            debugTextView.setText("Cannot find book");
        }
    }

    boolean validDates()
    {
        String rental = reforamtDateString(rentalDateString);
        String returnal = reforamtDateString(returnDateString);
        LocalDate date1 = LocalDate.parse(rental);
        LocalDate date2 = LocalDate.parse(returnal);
        LocalDate week = date1.plusWeeks(1);
        LocalDate today = LocalDate.now();
        if (date1.compareTo(today) >= 0 && date2.compareTo(today) >= 0)
        {
            if (date2.compareTo(date1) > 0 && date2.compareTo(week) < 0)
            {
                return true;
            }
        }
        return false;
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






//        String sample = m + "/" + y + "/" + d;
//        minus??
//        add??
//        adjust??
//        plusdays
//        LocalDate otherDate = LocalDate.of(y, m, d);
//        System.out.println(y + m + d);
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//        //String currentDate = date.format(dateFormat);
//        someText.setText(otherDate.plusDays(7).format(dateFormat));