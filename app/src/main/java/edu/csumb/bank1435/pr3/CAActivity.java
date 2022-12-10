package edu.csumb.bank1435.pr3;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.LinearLayout;
import java.util.Date;
import java.text.DateFormat;
import android.widget.Toast;
import android.content.Context;
import java.util.List;
import android.content.Intent;

public class CAActivity extends Activity
{
    private EditText usernameText, passText;
    private Button subButton;
    private LinearLayout LL;
    private Context context;
    private Intent contentSwitch;
    BookDatabase bookDB;
    int errors, iPerror;

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.create_account_activity);
        usernameText = (EditText) findViewById(R.id.ca_userText);
        passText = (EditText) findViewById(R.id.ca_passText);
        subButton = (Button) findViewById(R.id.ca_caButton);
        LL = (LinearLayout) findViewById(R.id.ca_Layout);
        context = getApplicationContext();
        contentSwitch = new Intent(getApplicationContext(), MainActivity.class);
        errors = 0;
        iPerror = 0;

        subButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(LL.getWindowToken(), 0);

                String username = usernameText.getText().toString();
                String password = passText.getText().toString();
                resetText();
                String date = DateFormat.getDateTimeInstance().format(new Date());
                List<Account> searchResults = bookDB.getBookDao().accountSearchByName(username);
                int letterCount = 0;
                int numberCount = 0;
                if (searchResults.size() > 0) //
                {
                    Toast.makeText(context, "Error: User's account already exist", Toast.LENGTH_SHORT).show();
                    if (errors == 1)
                    {
                        startActivity(contentSwitch);
                        return;
                    }

                    else
                    {
                        errors++;
                        return;
                    }

                }

                if (username.length() == 0 || password.length() == 0)
                {
                    Toast.makeText(context, "Error: No text detected in the username or password field",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int a = 0; a < username.length(); a++)
                {
                    if ((username.charAt(a) >= 32 && username.charAt(a) <= 47) ||
                        (username.charAt(a) >= 58 && username.charAt(a) <= 64) ||
                        (username.charAt(a) >= 91 && username.charAt(a) <= 96) ||
                        (username.charAt(a) >= 123 && username.charAt(a) <= 126))
                    {
                        Toast.makeText(context, "Error: Special character detected in username",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                for (int c = 0; c < password.length(); c++)
                {
                    if (password.charAt(c) >= 65 && password.charAt(c) <= 90)
                    {
                        letterCount++;
                    }

                    else if (password.charAt(c) >= 97 && password.charAt(c) <= 122)
                    {
                        letterCount++;
                    }

                    else if (password.charAt(c) >= 30 && password.charAt(c) <= 57)
                    {
                        numberCount++;
                    }

                    else if ((password.charAt(c) >= 32 && password.charAt(c) <= 47) ||
                            (password.charAt(c) >= 58 && password.charAt(c) <= 64) ||
                            (password.charAt(c) >= 91 && password.charAt(c) <= 96) ||
                            (password.charAt(c) >= 123 && password.charAt(c) <= 126))
                    {
                        Toast.makeText(context, "Error: Special character detected in password",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (letterCount >= 3 && numberCount >= 1)
                {
                    Account temp = new Account(username, password);
                    bookDB.getBookDao().insert(temp);
                    String message = username + "'s account created at " + date;
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    Log logTemp = new Log("Account Creation", message);
                    bookDB.getBookDao().insert(logTemp);
                }

                else
                {
                    Toast.makeText(context, "Error: This password doesn't meet the requirements",
                            Toast.LENGTH_SHORT).show();

                    if (iPerror == 1)
                    {
                        startActivity(contentSwitch);
                    }

                    else
                    {
                        iPerror++;
                    }
                }
            }
        });

        bookDB = BookDatabase.getDatabase(this);
    }

    void resetText()
    {
        usernameText.setText("");
        passText.setText("");
    }
}
