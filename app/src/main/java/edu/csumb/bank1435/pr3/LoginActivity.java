package edu.csumb.bank1435.pr3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.widget.TextView;
import android.widget.EditText;
import java.util.List;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity
{
    private EditText userText, passText;
    private Button logButton;
    private BookDatabase bookDB;
    private Intent contextSwitcher;

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.login_view);
        userText = (EditText) findViewById(R.id.login_username);
        passText = (EditText) findViewById(R.id.login_password);
        logButton = (Button) findViewById(R.id.loginButton);
        bookDB = BookDatabase.getDatabase(this);

        logButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login() & getIntent().getStringExtra("Button").equals("Cancel Hold"))
                {
                    contextSwitcher = new Intent(getApplicationContext(), CHActivity.class);
                    List<Book> searchResults = bookDB.getBookDao().getRentedBooksName(userText.getText().toString());
                    if (searchResults.size() > 0)
                    {
                        startActivity(contextSwitcher);
                    }

                    else
                    {
                        contextSwitcher = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(contextSwitcher);
                        Toast.makeText(getApplicationContext(), "No holds detected", Toast.LENGTH_LONG).show();
                    }
                }

                else if (adminLogin() & getIntent().getStringExtra("Button").equals("Admin"))
                {
                    contextSwitcher = new Intent(getApplicationContext(), MSActivity.class);
                    startActivity(contextSwitcher);
                }

                else
                {
                    contextSwitcher = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(contextSwitcher);
                    Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    boolean login()
    {
        List<Account> allAccounts = bookDB.getBookDao().getAllAccounts();
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        boolean flag = false;
        for (Account account: allAccounts)
        {
            if (account.getName().equals(username) && account.getPass().equals(password))
            {
                if (account.getName().equals("Admin2"))
                {
                    break;
                }
                account.setIsActive("YES");
                flag = true;
                break;
            }
        }

        if (flag)
        {
            bookDB.getBookDao().updateAllAccounts(allAccounts);
            return flag;
        }
        Toast.makeText(this,"Error: invalid login", Toast.LENGTH_SHORT).show();
        return flag;
    }

    boolean adminLogin()
    {
        String ultpass = "Admin2";
        String input = userText.getText().toString();
        String input2 = passText.getText().toString();
        List<Account> adminAccountFound = bookDB.getBookDao().accountSearchByName(ultpass);

        if (adminAccountFound.size() > 0)
        {
            Account temp = adminAccountFound.get(0);
            if (temp.getName().equals(input) & temp.getPass().equals(input2))
            {
                temp.setIsActive("YES");
                bookDB.getBookDao().update(temp);
                return true;
            }
            return false;
        }
        return false;
    }
}
