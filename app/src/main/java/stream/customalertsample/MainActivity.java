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
import android.widget.Toast;

import stream.customdialogue.CustomAlertDialogue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                        .setTitle("Custom Alert")
                        .setMessage("This is a Custom Alert. Press OK to confirm. ")
                        .setNegativeText("Cancel")
                        .setPositiveText("OK")
                        .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                        .setNegativeColor(R.color.textColor_alert_button_destructive)
                        .setPositiveColor(R.color.textColor_alert_button_cancel)
                        .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                Toast.makeText(MainActivity.this, "Negative", Toast.LENGTH_SHORT).show();
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
