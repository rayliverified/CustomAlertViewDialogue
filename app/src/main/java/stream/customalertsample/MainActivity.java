package stream.customalertsample;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import stream.customalert.CustomAlertDialogue;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<String> destructive = new ArrayList<>();
        destructive.add("Choice 1");

        final ArrayList<String> other = new ArrayList<>();
        other.add("Choice 2");
        other.add("Choice 3");
        other.add("Choice 4");
        other.add("Choice 5");
        other.add("Choice 6");
        other.add("Choice 7");
        other.add("Choice 8");
        other.add("Choice 9");
        other.add("Choice 10");
        other.add("Choice 11");
        other.add("Choice 12");
        other.add("Choice 13");
        other.add("Choice 14");
        other.add("Choice 15");
        other.add("Choice 16");
        other.add("Choice 17");
        other.add("Choice 18");
        other.add("Choice 19");
        other.add("Choice 20");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                        .setTitle("Custom Alert Dialogue")
                        .setMessage("This is a long description to test the dialogue's text wrapping functionality")
                        .setNegativeText("OK")
                        .setNegativeColor(R.color.negative)
                        .setNegativeTypeface(Typeface.DEFAULT_BOLD)
                        .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                alert.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
