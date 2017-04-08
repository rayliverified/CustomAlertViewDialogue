# CustomDialog

A Custom Alert Dialogue created using Fancy Dialog and Android AlertView.
https://github.com/geniusforapp/fancyDialog
https://github.com/saiwu-bigkoo/Android-AlertView

## Code Sample
```
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
                
                
```

## Code Sample for custom font
```
/* Buttons can be emphasized by setting the typface to BOLD */
 
 builder.setPositiveTypeface(Typeface.DEFAULT_BOLD)
 builder.setNegativeTypeface(Typeface.DEFAULT_BOLD)
 
```
