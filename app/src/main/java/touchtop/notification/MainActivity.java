package touchtop.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    TableLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TableLayout) findViewById(R.id.tab);

        Button btnG = (Button) findViewById(R.id.checkGmail);
        btnG.setBackgroundColor(Color.GRAY);
        btnG.setTextColor(Color.BLACK);
        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You got 0 emails", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnF = (Button) findViewById(R.id.checkFB);

        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You got 0 FB notifications", Toast.LENGTH_SHORT).show();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        //added this for firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String pack = intent.getStringExtra("package");
                String title = intent.getStringExtra("title");
                String text = intent.getStringExtra("text");

                //com.google.android.gm
                if (pack.equals("com.google.android.gm")) {
                    Toast.makeText(getApplicationContext(), "You got an email", Toast.LENGTH_SHORT).show();
                    Button temp = (Button) findViewById(R.id.checkGmail);
                    temp.setBackgroundColor(Color.RED);

                }


                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B0719"));
                textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
                tr.addView(textview);
                tab.addView(tr);

                //adding the data to firebase
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("notification").setValue(pack);
            }
    };
}

