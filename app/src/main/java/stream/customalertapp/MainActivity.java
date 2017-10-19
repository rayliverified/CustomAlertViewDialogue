package stream.customalertapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import stream.customalert.CustomAlertDialogue;
import stream.custombutton.CustomButton;

public class MainActivity extends AppCompatActivity{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        CustomButton btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                        .setTitle("Custom Alert")
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

        CustomButton btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                        .setCancelable(false)
                        .setTitle("Delete Items")
                        .setMessage("Delete all completed items?")
                        .setPositiveText("Confirm")
                        .setPositiveColor(R.color.negative)
                        .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                        .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                                Toast.makeText(mContext, "Items Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeText("Cancel")
                        .setNegativeColor(R.color.positive)
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

        CustomButton btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> destructive = new ArrayList<>();
                destructive.add("Choice 1");

                ArrayList<String> other = new ArrayList<>();
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

                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.SELECTOR)
                        .setDestructive(destructive)
                        .setOthers(other)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                CustomAlertDialogue.getInstance().dismiss();
                                Toast.makeText(mContext, "Selected " + i, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
                alert.show();
                Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(30);
            }
        });

        CustomButton btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> other = new ArrayList<String>();
                other.add("Copy");
                other.add("Forward");

                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.ACTIONSHEET)
                        .setTitle("Action Sheet")
                        .setTitleColor(R.color.text_default)
                        .setCancelText("More...")
                        .setOnCancelClicked(new CustomAlertDialogue.OnCancelClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(10);
                                dialog.dismiss();
                                Handler handler = new Handler();
                                Runnable r = new Runnable() {
                                    public void run() {
                                        MoreSelector();
                                    }
                                };
                                handler.postDelayed(r, 50);
                            }
                        })
                        .setOthers(other)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selection = adapterView.getItemAtPosition(i).toString();
                                Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(10);
                                switch (selection)
                                {
                                    case "Copy":
                                        CustomAlertDialogue.getInstance().dismiss();
                                        Toast.makeText(mContext, "Copied", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "Forward":
                                        CustomAlertDialogue.getInstance().dismiss();
                                        Toast.makeText(mContext, "Forwarded", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })
                        .build();
                alert.show();
                Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(30);
            }
        });

        CustomButton btn5 = findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> lineHint = new ArrayList<>();
                lineHint.add("Username");
                lineHint.add("Email Address");
                lineHint.add("Name");
                lineHint.add("Zip Code");

                ArrayList<String> lineText = new ArrayList<>();
                lineText.add("sampleuser");
                lineText.add(null);
                lineText.add("Sample User");

                ArrayList<String> boxHint = new ArrayList<>();
                boxHint.add("Message");

                ArrayList<String> boxText = new ArrayList<>();
                boxText.add("BoxText");

                CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                        .setStyle(CustomAlertDialogue.Style.INPUT)
                        .setTitle("Submit Feedback")
                        .setMessage("We love to hear feedback! Please share your thoughts and comments:")
                        .setPositiveText("Submit")
                        .setPositiveColor(R.color.positive)
                        .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                        .setOnInputClicked(new CustomAlertDialogue.OnInputClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog, ArrayList<String> inputList) {
                                Toast.makeText(mContext, "Sent", Toast.LENGTH_SHORT).show();
                                for (String input : inputList)
                                {
                                    Log.d("Input", input);
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeText("Cancel")
                        .setNegativeColor(R.color.negative)
                        .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setLineInputHint(lineHint)
                        .setLineInputText(lineText)
                        .setBoxInputHint(boxHint)
                        .setBoxInputText(boxText)
                        .build();
                alert.show();
            }
        });
    }

    public void MoreSelector()
    {
        ArrayList<String> destructive = new ArrayList<String>();
        destructive.add("Delete");
        ArrayList<String> other = new ArrayList<String>();
        other.add("Details");

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(MainActivity.this)
                .setStyle(CustomAlertDialogue.Style.SELECTOR)
                .setDestructive(destructive)
                .setOthers(other)
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selection = adapterView.getItemAtPosition(i).toString();
                        Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(10);
                        switch (selection)
                        {
                            case "Delete":
                                CustomAlertDialogue.getInstance().dismiss();
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                break;
                            case "Details":
                                CustomAlertDialogue.getInstance().dismiss();
                                Toast.makeText(mContext, "Details", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .build();
        alert.show();
    }
}
