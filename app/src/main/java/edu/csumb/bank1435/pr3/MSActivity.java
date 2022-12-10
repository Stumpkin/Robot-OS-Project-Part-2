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
    private Button cBookButton, backbutton;
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
