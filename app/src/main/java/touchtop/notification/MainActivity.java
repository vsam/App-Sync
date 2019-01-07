package touchtop.notification;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

import java.util.UUID;

public class MainActivity extends Activity {

    TableLayout tab;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //adding the data to firebase
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TableLayout) findViewById(R.id.tab);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("My Setting", 0);
        SharedPreferences.Editor editor = settings.edit();
        String check = settings.getString("My Setting", null);
        if (check == null) {
            //set check to a random number for the first time
            check = UUID.randomUUID().toString().substring(0, 7);
            editor.putString("homeScore", check);
            editor.apply();

        }
        final String uuid = check;
        final Button btnG = (Button) findViewById(R.id.checkGmail);
        btnG.setBackgroundColor(Color.LTGRAY);
        btnG.setTextColor(Color.BLACK);
        btnG.setOnClickListener(new View.OnClickListener() {
            //Added UUID to the child name
            @Override
            public void onClick(View view) {
                //get the tag to see if the button already changed color
                String colorG = (String)btnG.getTag();
                if (colorG != null) {
                    mDatabase.child("Gmail" + uuid).push().setValue("Trust");
                    btnG.setBackgroundColor(Color.LTGRAY);
                    Toast.makeText(getApplicationContext(), "You got an email: " + colorG, Toast.LENGTH_SHORT).show();
                    btnG.setTag(null);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You got 0 emails: " + colorG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnF = (Button) findViewById(R.id.checkFB);
        btnF.setBackgroundColor(Color.CYAN);
        btnF.setOnClickListener(new View.OnClickListener() {
            //added UUID to the child name
            @Override
            public void onClick(View view) {
                //get the tag to see if the button already changed color
                String colorF = (String)btnF.getTag();
                if (colorF != null ) {
                    mDatabase.child("Facebook" + uuid).push().setValue("Progress");
                    btnF.setBackgroundColor(Color.CYAN);
                    Toast.makeText(getApplicationContext(), "You got a FB notification: "+ colorF, Toast.LENGTH_SHORT).show();
                    btnF.setTag(null);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You got 0 FB notifications: " + colorF, Toast.LENGTH_SHORT).show();
                }

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

    }

    // Add for the onbackpress action so it doesnt crash!!!
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String pack = intent.getStringExtra("package");
                String title = intent.getStringExtra("title");
                String text = intent.getStringExtra("text");

                //adding the data to firebase
                //DatabaseReference mDatabase;

                //Toast.makeText(getApplicationContext(), "We got this covered: "+ pack, Toast.LENGTH_SHORT).show();
                //com.google.android.gm
                if (pack.equals("com.google.android.gm")) {
                    //Toast.makeText(getApplicationContext(), "You got an email", Toast.LENGTH_SHORT).show();
                    Button activegm = (Button) findViewById(R.id.checkGmail);
                    activegm.setBackgroundColor(Color.RED);
                    activegm.setTag("1");

                    //mDatabase = FirebaseDatabase.getInstance().getReference();
                    //mDatabase.child("Gmail").push().setValue(pack);

                }

                if (pack.equals("com.facebook.katana")) {
                    //Toast.makeText(getApplicationContext(), "You got a FB notification", Toast.LENGTH_SHORT).show();
                    Button activefb = (Button) findViewById(R.id.checkFB);
                    activefb.setBackgroundColor(Color.BLUE);
                    activefb.setTag("1");
                    //mDatabase = FirebaseDatabase.getInstance().getReference();
                    //mDatabase.child("Facebook").push().setValue(pack);
                }


                /***  Prints the notification on the phone
                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B0719"));
                textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
                tr.addView(textview);
                tab.addView(tr);
                 ***/


            }
    };
}

