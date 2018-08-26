package stream.customalert;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stream.customalert.ui.CustomBlurDialogue;

public class CustomAlertDialogue extends DialogFragment {
    public static final String TAG = CustomAlertDialogue.class.getSimpleName();
    private Builder builder;
    private Style style = Style.DIALOGUE;
    private ArrayList<String> inputList;
    private TextView positiveText;
    private ArrayList<String> tagList;
    private static CustomAlertDialogue instance = new CustomAlertDialogue();

    public static CustomAlertDialogue getInstance() {
        return instance;
    }

    private View layout;
    private TextView title, message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("Builder", "Restore");

        if (savedInstanceState != null) {
            if (builder == null) {
                builder = savedInstanceState.getParcelable(Builder.class.getSimpleName());
                Log.d("Builder", "Restore Not Null");
            }
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        setRetainInstance(true);
        if (builder != null)
        {
            if (!builder.getCancelable())
            {
                this.setCancelable(false);
            }
            else
            {
                this.setCancelable(true);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (builder != null)
            outState.putParcelable(Builder.class.getSimpleName(), builder);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        if (builder.getStyle() != null)
        {
            style = builder.getStyle();
        }

        switch (style)
        {
            case DIALOGUE:
                wlp.windowAnimations = R.style.CustomDialogAnimation;
                wlp.gravity = Gravity.CENTER;
                break;
            case ACTIONSHEET:
                wlp.windowAnimations = R.style.CustomActionsheetAnimation;
                wlp.gravity = Gravity.BOTTOM;
                break;
            case SELECTOR:
                wlp.windowAnimations = R.style.CustomDialogAnimation;
                wlp.gravity = Gravity.CENTER;
                break;
            case INPUT:
                wlp.windowAnimations = R.style.CustomDialogAnimation;
                wlp.gravity = Gravity.CENTER;
                break;
        }

        if (builder.getGravity() != null)
        {
            wlp.gravity = builder.getGravity();
            switch (builder.getGravity())
            {
                case Gravity.CENTER:
                    wlp.windowAnimations = R.style.CustomDialogAnimation;
                    break;
                case Gravity.BOTTOM:
                    wlp.windowAnimations = R.style.CustomActionsheetAnimation;
                    break;
            }
        }

        window.setAttributes(wlp);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (builder.getStyle() != null)
        {
            style = builder.getStyle();
            Log.d("View Inflator", "Inflated");
        }

        switch (style)
        {
            case DIALOGUE:
                return inflater.inflate(R.layout.alert, container, false);
            case ACTIONSHEET:
                return inflater.inflate(R.layout.alert_actionsheet, container,false);
            case SELECTOR:
                return inflater.inflate(R.layout.alert, container, false);
            case INPUT:
                return inflater.inflate(R.layout.alert_input, container, false);
        }

        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (builder != null) {

            initCommonView(view);

            //Style specific elements
            if (style == Style.DIALOGUE)
            {
                initDialogueView(view);
            }
            if (style == Style.ACTIONSHEET)
            {
                initActionsheetView(view);
            }
            if (style == Style.SELECTOR)
            {
                initSelectorView(view);
            }
            if (style == Style.INPUT)
            {
                initInputView(view);
            }
        }
    }

    private void initCommonView(View view) {
        //Common elements
        layout = view.findViewById(R.id.main_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (builder.getCancelable())
                {
                    dismiss();
                }
            }
        });
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        if (builder.getTitle() != null) {
            title.setText(builder.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }
        if (builder.getMessage() != null) {
            message.setText(builder.getMessage());
            message.setMovementMethod(new ScrollingMovementMethod());
        } else {
            message.setVisibility(View.GONE);
        }

        if (builder.isAutoHide()) {
            int time = builder.getTimeToHide() != 0 ? builder.getTimeToHide() : 10000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAdded() && getActivity() != null)
                        dismiss();
                }
            }, time);
        }
    }

    private void initDialogueView(View view) {

        ViewStub viewStub = view.findViewById(R.id.viewStubHorizontal);
        viewStub.inflate();

        LinearLayout alertButtons = view.findViewById(R.id.alertButtons);

        if (builder.getNegativeText() != null) {
            View negativeButton = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_button, null);
            TextView negativeText = negativeButton.findViewById(R.id.alerttext);
            negativeText.setText(builder.getNegativeText());
            if (builder.getNegativeTypeface() != null)
            {
                negativeText.setTypeface(builder.getNegativeTypeface());
            }
            if (builder.getNegativeColor() != 0)
            {
                negativeText.setTextColor(ContextCompat.getColor(view.getContext(), builder.getNegativeColor()));
            }
            else
            {
                negativeText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.negative));
            }
            negativeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.getOnNegativeClicked().OnClick(v, getDialog());
                }
            });
            alertButtons.addView(negativeButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        }

        if (builder.getPositiveText() != null) {

            //Add a divider between buttons if button exists.
            View divider = new View(view.getContext());
            divider.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.divider));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) view.getContext().getResources().getDimension(R.dimen.size_divider), LinearLayout.LayoutParams.MATCH_PARENT);
            alertButtons.addView(divider, params);

            View positiveButton = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_button, null);
            TextView positiveText = positiveButton.findViewById(R.id.alerttext);
            positiveText.setText(builder.getPositiveText());
            if (builder.getPositiveTypeface() != null)
            {
                positiveText.setTypeface(builder.getPositiveTypeface());
            }
            if (builder.getPositiveColor() != 0)
            {
                positiveText.setTextColor(ContextCompat.getColor(view.getContext(), builder.getPositiveColor()));
            }
            else
            {
                positiveText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.positive));
            }
            positiveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.getOnPositiveClicked().OnClick(v, getDialog());
                }
            });
            alertButtons.addView(positiveButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        }

        float radius = 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && builder.getDecorView() != null)
        {
            CustomBlurDialogue blurDialogue = view.findViewById(R.id.blurview);
            blurDialogue.create(builder.getDecorView(), radius);
        }
    }

    private void initActionsheetView(View view) {

        float radius = 5;

        final TextView cancelButton = view.findViewById(R.id.cancel);
        if(builder.getCancelText() != null){
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText(builder.getCancelText());
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.getOnCancelClicked().OnClick(v, getDialog());
                }
            });
            if (builder.getCancelColor() != 0)
            {
                cancelButton.setTextColor(ContextCompat.getColor(view.getContext(), builder.getCancelColor()));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && builder.getDecorView() != null)
            {
                CustomBlurDialogue blurDialogue = view.findViewById(R.id.blurview_button);
                blurDialogue.create(builder.getDecorView(), radius);
            }
        }

        if (builder.getTitle() != null || builder.getMessage() != null)
        {
            if (builder.getDestructive() != null || builder.getOthers() != null)
            {
                //Add a divider between header and listview
                View header = view.findViewById(R.id.header_divider);
                header.setVisibility(View.VISIBLE);
            }
        }

        initListView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && builder.getDecorView() != null)
        {
            CustomBlurDialogue blurDialogue = view.findViewById(R.id.blurview);
            blurDialogue.create(builder.getDecorView(), radius);
        }
    }

    private void initSelectorView(View view) {

        ViewStub viewStub = view.findViewById(R.id.viewStubVertical);
        viewStub.inflate();
        initListView(view);

        float radius = 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && builder.getDecorView() != null)
        {
            CustomBlurDialogue blurDialogue = view.findViewById(R.id.blurview);
            blurDialogue.create(builder.getDecorView(), radius);
        }
    }

    private void initListView(View view) {
        ArrayList<String> mData = new ArrayList<String>();
        if (builder.getDestructive() != null)
        {
            mData.addAll(builder.getDestructive());
        }
        if (builder.getOthers() != null)
        {
            mData.addAll(builder.getOthers());
        }
        ListView alertButtonListView = view.findViewById(R.id.listview);
        CustomActionsheetAdapter adapter = new CustomActionsheetAdapter(mData, builder.getDestructive());
        alertButtonListView.setAdapter(adapter);
        if (builder.getOnItemClickListener() != null)
        {
            alertButtonListView.setOnItemClickListener(builder.getOnItemClickListener());
        }
    }

    private void initInputView(final View view) {

        //Initialize and reset input/tag arrays.
        inputList = new ArrayList<>();
        tagList = new ArrayList<>();

        //Initialize Dialogue View buttons. Identical code except for Positive button's OnInputClick listener
        ViewStub viewStub1 = view.findViewById(R.id.viewStubHorizontal);
        viewStub1.inflate();

        LinearLayout alertButtons = view.findViewById(R.id.alertButtons);

        if (builder.getNegativeText() != null) {
            View negativeButton = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_button, null);
            TextView negativeText = negativeButton.findViewById(R.id.alerttext);
            negativeText.setText(builder.getNegativeText());
            if (builder.getNegativeTypeface() != null)
            {
                negativeText.setTypeface(builder.getNegativeTypeface());
            }
            if (builder.getNegativeColor() != 0)
            {
                negativeText.setTextColor(ContextCompat.getColor(view.getContext(), builder.getNegativeColor()));
            }
            else
            {
                negativeText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.negative));
            }
            negativeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.getOnNegativeClicked().OnClick(v, getDialog());
                }
            });
            alertButtons.addView(negativeButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        }

        if (builder.getPositiveText() != null) {

            //Add a divider between buttons if button exists.
            View divider = new View(view.getContext());
            divider.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.divider));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) view.getContext().getResources().getDimension(R.dimen.size_divider), LinearLayout.LayoutParams.MATCH_PARENT);
            alertButtons.addView(divider, params);

            View positiveButton = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_button, null);
            positiveText = positiveButton.findViewById(R.id.alerttext);
            positiveText.setText(builder.getPositiveText());
            if (builder.getPositiveTypeface() != null)
            {
                positiveText.setTypeface(builder.getPositiveTypeface());
            }
            if (builder.getPositiveColor() != 0)
            {
                positiveText.setTextColor(ContextCompat.getColor(view.getContext(), builder.getPositiveColor()));
            }
            else
            {
                positiveText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.positive));
            }
            positiveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String tag : tagList)
                    {
                        EditText editInput = view.findViewWithTag(tag);
                        inputList.add(editInput.getText().toString());
                    }
                    builder.getOnInputClicked().OnClick(v, getDialog(), inputList);
                }
            });
            alertButtons.addView(positiveButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        }

        //Initialize input boxes.
        ViewStub viewStub = view.findViewById(R.id.viewStubVerticalInput);
        viewStub.inflate();

        LinearLayout alertInput = view.findViewById(R.id.alertInput);

        if (builder.getLineInputHint() != null) {

            for (int i = 0; i < builder.getLineInputHint().size(); i++)
            {
                View lineInput = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_input_line, null);
                TextInputLayout inputLayout = lineInput.findViewById(R.id.alert_input_layout);
                inputLayout.setHint(builder.getLineInputHint().get(i));
                TextInputEditText editInput = lineInput.findViewById(R.id.alert_input_text);
                editInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            if (builder.getPositiveText() != null)
                            {
                                Log.d("Input", "Submit");
                                positiveText.performClick();
                            }
                        }
                        return false;
                    }
                });
                if (builder.getLineInputText().size() > i)
                {
                    if (builder.getLineInputText().get(i) != null)
                    {
                        editInput.setText(builder.getLineInputText().get(i));
                    }
                }
                editInput.setTag("Line" + i);
                tagList.add("Line" + i);
                alertInput.addView(lineInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        }

        if (builder.getBoxInputHint() != null) {

            for (int i = 0; i < builder.getBoxInputHint().size(); i++)
            {
                View boxInput = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_input_box, null);
                TextInputLayout inputLayout = boxInput.findViewById(R.id.alert_input_layout);
                inputLayout.setHint(builder.getBoxInputHint().get(i));
                EditText editInput = boxInput.findViewById(R.id.alert_input_text);
                if (builder.getBoxInputText().size() > i)
                {
                    if (builder.getBoxInputText().get(i) != null)
                    {
                        editInput.setText(builder.getBoxInputText().get(i));
                    }
                }
                editInput.setTag("Box" + i);
                tagList.add("Box" + i);
                alertInput.addView(boxInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        }

        float radius = 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && builder.getDecorView() != null)
        {
            CustomBlurDialogue blurDialogue = view.findViewById(R.id.blurview);
            blurDialogue.create(builder.getDecorView(), radius);
        }
    }

    private Dialog show(Activity activity, Builder builder) {
        this.builder = builder;
        if (!isAdded())
            show(((AppCompatActivity) activity).getSupportFragmentManager(), TAG);
        return getDialog();
    }

    public static class Builder implements Parcelable {

        private String title;
        private String message;
        private String positiveText;
        private String negativeText;
        private String cancelText;

        private int titleColor;
        private int messageColor;
        private int positiveColor;
        private int negativeColor;
        private int cancelColor;
        private int backgroundColor;
        private int timeToHide;

        private Typeface titleFont;
        private Typeface messageFont;
        private Typeface positiveTypeface;
        private Typeface negativeTypeface;
        private Typeface alertTypeface;

        private OnPositiveClicked onPositiveClicked;
        private OnNegativeClicked onNegativeClicked;
        private OnCancelClicked onCancelClicked;
        private OnInputClicked onInputClicked;

        private ArrayList<String> destructive;
        private ArrayList<String> others;
        private ArrayList<String> lineInputHint;
        private ArrayList<String> lineInputText;
        private ArrayList<String> boxInputHint;
        private ArrayList<String> boxInputText;
        private AdapterView.OnItemClickListener onItemClickListener;

        private boolean autoHide;
        private boolean cancelable = true;

        private Integer gravity = Gravity.CENTER;
        private Style style;
        private View decorView;
        private Context context;

        protected Builder(Parcel in) {
            title = in.readString();
            message = in.readString();
            positiveText = in.readString();
            negativeText = in.readString();
            cancelText = in.readString();
            titleColor = in.readInt();
            messageColor = in.readInt();
            positiveColor = in.readInt();
            negativeColor = in.readInt();
            cancelColor = in.readInt();
            backgroundColor = in.readInt();
            timeToHide = in.readInt();
            destructive = in.createStringArrayList();
            others = in.createStringArrayList();
            lineInputHint = in.createStringArrayList();
            lineInputText = in.createStringArrayList();
            boxInputHint = in.createStringArrayList();
            boxInputText = in.createStringArrayList();
            autoHide = in.readByte() != 0;
            cancelable = in.readByte() != 0;
            gravity = in.readInt();
            style = Style.valueOf(in.readString());
        }

        /**
         * @param style - set DIALOGUE, ACTIONSHEET, SELECTOR, INPUT to select AlertView type.
         * @return
         */
        public Builder setStyle(Style style) {
            if(style != null) {
                this.style = style;
            }
            return this;
        }
        public Style getStyle() { return style; }

        public Builder setGravity(Integer gravity) {
            if(gravity != null) {
                this.gravity = gravity;
            }
            return this;
        }
        public Integer getGravity() { return gravity; }

        /**
         * @param title - set AlertView title text.
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public String getTitle() { return title; }

        /**
         * @param message - set AlertView message text.
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        public String getMessage() {
            return message;
        }

        /**
         * @param positiveButtonText - set Confirmation Alert right button text.
         * @return
         */
        public Builder setPositiveText(String positiveButtonText) {
            this.positiveText = positiveButtonText;
            return this;
        }
        public String getPositiveText() {
            return positiveText;
        }

        /**
         * @param negativeText - set Simple Alert button text. Set Confirmation Alert left button text.
         * @return
         */
        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }
        public String getNegativeText() {
            return negativeText;
        }

        /**
         * @param cancel - set Action Sheet cancel button text.
         * @return
         */
        public Builder setCancelText(String cancel) {
            this.cancelText = cancel;
            return this;
        }
        public String getCancelText() { return cancelText; }

        /**
         * @param titleColor - set title text color.
         * @return
         */
        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }
        public int getTitleColor() {
            return titleColor;
        }

        /**
         * @param messageColor - set message text color.
         * @return
         */
        public Builder setMessageColor(int messageColor) {
            this.messageColor = messageColor;
            return this;
        }
        public int getMessageColor() {
            return messageColor;
        }

        /**
         * @param positiveTextColor - set positive button text color.
         * @return
         */
        public Builder setPositiveColor(int positiveTextColor) {
            this.positiveColor = positiveTextColor;
            return this;
        }
        public int getPositiveColor() {
            return positiveColor;
        }

        /**
         * @param negativeColor - set negative button text color.
         * @return
         */
        public Builder setNegativeColor(int negativeColor) {
            this.negativeColor = negativeColor;
            return this;
        }
        public int getNegativeColor() {
            return negativeColor;
        }

        /**
         * @param cancelColor - set cancel button text color.
         * @return
         */
        public Builder setCancelColor(int cancelColor) {
            this.cancelColor = cancelColor;
            return this;
        }
        public int getCancelColor() {
            return cancelColor;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }
        public int getBackgroundColor() {
            return backgroundColor;
        }

        /**
         * @param titleFontPath - set title text font. Must pass the path to the font in the assets folder.
         * @return
         */
        public Builder setTitleFont(String titleFontPath) {
            this.titleFont = Typeface.createFromAsset(context.getAssets(), titleFontPath);
            return this;
        }
        public Typeface getTitleFont() {
            return titleFont;
        }

        /**
         * @param bodyFontPath - set message text font. Must pass the path to the font in the assets folder.
         * @return
         */
        public Builder setMessageFont(String bodyFontPath) {
            this.messageFont = Typeface.createFromAsset(context.getAssets(), bodyFontPath);
            return this;
        }
        public Typeface getMessageFont() {
            return messageFont;
        }

        /**
         * @param positiveTypeface - set positive button text typeface.
         * @return
         */
        public Builder setPositiveTypeface(Typeface positiveTypeface) {
            this.positiveTypeface = positiveTypeface;
            return this;
        }
        public Typeface getPositiveTypeface() {
            return positiveTypeface;
        }

        /**
         * @param negativeTypeface - set negative button text typeface.
         * @return
         */
        public Builder setNegativeTypeface(Typeface negativeTypeface) {
            this.negativeTypeface = negativeTypeface;
            return this;
        }
        public Typeface getNegativeTypeface() {
            return negativeTypeface;
        }

        public Builder setAlertTypeface(String alertTypeface) {
            this.alertTypeface = Typeface.createFromAsset(context.getAssets(), alertTypeface);
            return this;
        }
        public Typeface getAlertTypeface() {
            return alertTypeface;
        }

        /**
         * @param onPositiveClicked - pass a listener to be called when the positive button is clicked.
         * @return
         */
        public Builder setOnPositiveClicked(OnPositiveClicked onPositiveClicked) {
            this.onPositiveClicked = onPositiveClicked;
            return this;
        }
        public OnPositiveClicked getOnPositiveClicked() {
            return onPositiveClicked;
        }

        /**
         * @param onNegativeClicked - pass a listener to be called when the negative button is clicked.
         * @return
         */
        public Builder setOnNegativeClicked(OnNegativeClicked onNegativeClicked) {
            this.onNegativeClicked = onNegativeClicked;
            return this;
        }
        public OnNegativeClicked getOnNegativeClicked() {
            return onNegativeClicked;
        }

        /**
         * @param onCancelClicked - pass a listener to be called when the cancel button is clicked.
         * @return
         */
        public Builder setOnCancelClicked(OnCancelClicked onCancelClicked) {
            this.onCancelClicked = onCancelClicked;
            return this;
        }
        public OnCancelClicked getOnCancelClicked() {
            return onCancelClicked;
        }

        /**
         * @param onItemClickListener - pass a listener to be called when a selection item is clicked.
         * @return
         */
        public Builder setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }
        public AdapterView.OnItemClickListener getOnItemClickListener() { return onItemClickListener; }

        /**
         * @param onInputClicked - pass a listener to be called when an input box is submitted.
         * @return
         */
        public Builder setOnInputClicked(OnInputClicked onInputClicked) {
            this.onInputClicked = onInputClicked;
            return this;
        }
        public OnInputClicked getOnInputClicked() {
            return onInputClicked;
        }

        /**
         * @param destructive - converts a String ArrayList into destructive options in the selector.
         * @return
         */
        public Builder setDestructive(ArrayList<String> destructive) {
            this.destructive = destructive;
            return this;
        }
        public ArrayList<String> getDestructive() { return destructive; }

        /**
         * @param others - converts a String ArrayList into neutral options in the selector.
         * @return
         */
        public Builder setOthers(ArrayList<String> others) {
            this.others = others;
            return this;
        }
        public ArrayList<String> getOthers() { return others; }

        /**
         * @param lineInputText - converts a String ArrayList into single line text input boxes.
         * @return
         */
        public Builder setLineInputText(ArrayList<String> lineInputText) {
            this.lineInputText = lineInputText;
            return this;
        }
        public ArrayList<String> getLineInputText() {
            if (lineInputText == null)
                return new ArrayList<>();
            return lineInputText;
        }

        /**
         * @param lineInputHint - converts a String ArrayList into single line input boxes hints.
         *                     Array length must match LineInputText length.
         * @return
         */
        public Builder setLineInputHint(ArrayList<String> lineInputHint) {
            this.lineInputHint = lineInputHint;
            return this;
        }
        public ArrayList<String> getLineInputHint() {
            return lineInputHint;
        }

        /**
         * @param boxInputText - converts a String ArrayList into multiline text input boxes.
         * @return
         */
        public Builder setBoxInputText(ArrayList<String> boxInputText) {
            this.boxInputText = boxInputText;
            return this;
        }
        public ArrayList<String> getBoxInputText() {
            if (boxInputText == null)
                return new ArrayList<>();
            return boxInputText;
        }

        /**
         * @param boxInputHint - converts a String ArrayList into multiline input boxes hints.
         *                     Array length must match BoxInputText length.
         * @return
         */
        public Builder setBoxInputHint(ArrayList<String> boxInputHint) {
            this.boxInputHint = boxInputHint;
            return this;
        }
        public ArrayList<String> getBoxInputHint() {
            return boxInputHint;
        }

        /**
         * @param autoHide - set `true` to automatically hide alert after a set time.
         * @return
         */
        public Builder setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }
        public boolean isAutoHide() {
            return autoHide;
        }

        /**
         * @param timeToHide - set time in milliseconds for popup to automatically dismiss.
         *                   No listeners are triggered when dismissed automatically.
         * @return
         */
        public Builder setTimeToHide(int timeToHide) {
            this.timeToHide = timeToHide;
            return this;
        }
        public int getTimeToHide() {
            return timeToHide;
        }

        /**
         * @param cancelable - set false to prevent dialogue dismissal through tapping outside or pressing the back button.
         *                   Force the user to an choose option.
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }
        public boolean getCancelable() { return cancelable; }

        /**
         * @param decorView - pass the Window DecorView for a nice blurred background. Defaults to overlay color.
         *                  Here's how to pass the correct DecorView in the following classes:
         *                  Activity - use `getWindow().getDecorView()`
         *                  Fragment - use `getActivity().getWindow().getDecorView()`
         *                  Viewholder - use `((Activity) mContext).getWindow().getDecorView()`
         * @return
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Builder setDecorView(View decorView) {

            this.decorView = decorView;
            return this;
        }
        public View getDecorView() { return decorView; }

        /**
         * The Dialog Fragment is extremely picky about the `Activity` passed into the builder.
         * If the improper Activity is passed, the dialogue will crash!
         * Here's how to pass the proper Activity in the following cases:
         * Activity - construct with `ClassName.this`
         * Fragment - construct with `getActivity()`
         * ViewHolder - construct with `getActivity().getApplicationContext()`
         * Do not attempt to construct the dialogue with `getContext()`.
         * The Builder requires an Activity and passing a Context does not work!
         * @param context - pass the Dialogue's parent activity.
         * @return
         */
        public Builder(Context context) { this.context = context; }

        /**
         * Construct the Dialogue Builder.
         * @return
         */
        public Builder build() {
            return this;
        }

        /**
         * Display the Dialogue with Builder parameters.
         * @return
         */
        public Dialog show() {
            return CustomAlertDialogue.getInstance().show(((Activity) context), this);
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeString(message);
            parcel.writeString(positiveText);
            parcel.writeString(negativeText);
            parcel.writeString(cancelText);
            parcel.writeInt(titleColor);
            parcel.writeInt(messageColor);
            parcel.writeInt(positiveColor);
            parcel.writeInt(negativeColor);
            parcel.writeInt(cancelColor);
            parcel.writeInt(backgroundColor);
            parcel.writeInt(timeToHide);
            parcel.writeStringList(destructive);
            parcel.writeStringList(others);
            parcel.writeStringList(lineInputHint);
            parcel.writeStringList(lineInputText);
            parcel.writeStringList(boxInputHint);
            parcel.writeStringList(boxInputText);
            parcel.writeByte((byte) (autoHide ? 1 : 0));
            parcel.writeByte((byte) (cancelable ? 1 : 0));
            parcel.writeInt(gravity);
            parcel.writeString(style.toString());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnPositiveClicked {
        void OnClick(View view, Dialog dialog);
    }

    public interface OnNegativeClicked {
        void OnClick(View view, Dialog dialog);
    }

    public interface OnCancelClicked {
        void OnClick(View view, Dialog dialog);
    }

    public interface OnInputClicked {
        void OnClick(View view, Dialog dialog, ArrayList<String> inputList);
    }

    public enum Style{
        DIALOGUE,
        ACTIONSHEET,
        SELECTOR,
        INPUT
    }

    public class CustomActionsheetAdapter extends BaseAdapter {

        private List<String> mDatas;
        private List<String> mDestructive;

        public CustomActionsheetAdapter(List<String> datas, List<String> destructive){
            this.mDatas = datas;
            this.mDestructive = destructive;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String data = mDatas.get(position);
            Holder holder = null;
            View view = convertView;
            if(view == null){
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.alert_button, null);
                holder = createHolder(view);
                view.setTag(holder);
            }
            else{
                holder = (Holder) view.getTag();
            }
            holder.UpdateUI(parent.getContext(), data, position);
            return view;
        }
        public Holder createHolder(View view){
            return new Holder(view);
        }

        class Holder {

            private View buttonDivider;
            private TextView buttonText;

            public Holder(View view){
                buttonText = view.findViewById(R.id.alerttext);
                buttonDivider = view.findViewById(R.id.button_divider);
            }
            public void UpdateUI(Context context, String data, int position){

                buttonText.setText(data);
                if (position == 0)
                {
                    buttonDivider.setVisibility(View.GONE);
                }
                else
                {
                    buttonDivider.setVisibility(View.VISIBLE);
                }

                if (mDestructive!= null && mDestructive.contains(data)){
                    buttonText.setTextColor(ContextCompat.getColor(context, R.color.negative));
                }
                else{
                    buttonText.setTextColor(ContextCompat.getColor(context, R.color.positive));
                }
            }
        }
    }

    public static class Units {
        /**
         * Converts dp to pixels.
         */
        public static int dpToPx(Context context, int dp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }
    }
}
