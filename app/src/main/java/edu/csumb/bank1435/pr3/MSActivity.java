/**
 * Title: MSActivity.java
 * Abstract: Displays all of the recorded transactions in the system. The user can choose to create a book
 * Author: Jalen Banks
 * ID: 1012
 * Date of Completion: 12/09/22
 */
package edu.csumb.bank1435.pr3;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import java.util.List;

public class MSActivity extends Activity
{
    private TextView someText;
    private Button cBookButton, backbutton, quitButton;
    private BookDatabase bookDB;
    private Intent contextSwitcher;
    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.manage_system_activity);
        someText = (TextView) findViewById(R.id.ms_text);
        cBookButton = (Button) findViewById(R.id.ms_bookButton);
        backbutton = (Button) findViewById(R.id.ms_backButton);
        quitButton = (Button) findViewById(R.id.ms_OUTBUTTON);
        bookDB = BookDatabase.getDatabase(this);

        cBookButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                contextSwitcher = new Intent(getApplicationContext(), CBActivity.class);
                startActivity(contextSwitcher);
                return;
            }
        });

        backbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                exit();
                contextSwitcher = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(contextSwitcher);
                return;
            }
        });

        quitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finishAffinity();
                System.exit(0);
            }
        });
        loadLogs();
    }

    void loadLogs()
    {
        List<Log> allLogs = bookDB.getBookDao().getLogs();
        String message = "";
        for (Log thisLog : allLogs)
        {
            message += thisLog.display();
        }

        if (message.length() == 0)
        {
            message = "No logs recorded";
        }
        someText.setText(message);
    }

    void exit()
    {
            Account thisAccount = bookDB.getBookDao().getAllAccounts().get(0);
            thisAccount.setIsActive("NO");
            bookDB.getBookDao().update(thisAccount);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        exit();
    }
}
