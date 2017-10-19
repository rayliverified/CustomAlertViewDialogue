package stream.customalert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAlertDialogue extends DialogFragment {
    public static final String TAG = CustomAlertDialogue.class.getSimpleName();
    private Builder builder;
    private Style style = Style.DIALOGUE;
    private Integer gravity = Gravity.CENTER;
    private ArrayList<String> inputList;
    private TextView positiveText;
    private ArrayList<String> tagList;
    private static CustomAlertDialogue instance = new CustomAlertDialogue();

    public static CustomAlertDialogue getInstance() {
        return instance;
    }

    private TextView title, message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (builder != null) {
                builder = savedInstanceState.getParcelable(Builder.class.getSimpleName());
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
            switch (gravity)
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
    }

    private void initActionsheetView(View view) {

        TextView cancelButton = view.findViewById(R.id.cancel);
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
                cancelButton.setTextColor(builder.getCancelColor());
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
    }

    private void initSelectorView(View view) {

        ViewStub viewStub = view.findViewById(R.id.viewStubVertical);
        viewStub.inflate();
        initListView(view);
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

        private Integer gravity;
        private Style style;
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

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }
        public Context getContext() {
            return context;
        }

        public Builder setStyle(Style style) {
            if(style != null) {
                this.style = style;
            }
            return this;
        }
        public Style getStyle() { return style; }

        public Builder setGravity(Integer gravity) {
            this.gravity = gravity;
            return this;
        }
        public Integer getGravity() { return gravity; }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public String getTitle() { return title; }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        public String getMessage() {
            return message;
        }

        public Builder setPositiveText(String positiveButtonText) {
            this.positiveText = positiveButtonText;
            return this;
        }
        public String getPositiveText() {
            return positiveText;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }
        public String getNegativeText() {
            return negativeText;
        }

        public Builder setCancelText(String cancel) {
            this.cancelText = cancel;
            return this;
        }
        public String getCancelText() { return cancelText; }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }
        public int getTitleColor() {
            return titleColor;
        }

        public Builder setMessageColor(int messageColor) {
            this.messageColor = messageColor;
            return this;
        }
        public int getMessageColor() {
            return messageColor;
        }

        public Builder setPositiveColor(int positiveTextColor) {
            this.positiveColor = positiveTextColor;
            return this;
        }
        public int getPositiveColor() {
            return positiveColor;
        }

        public Builder setNegativeColor(int negativeColor) {
            this.negativeColor = negativeColor;
            return this;
        }
        public int getNegativeColor() {
            return negativeColor;
        }

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

        public Builder setTimeToHide(int timeToHide) {
            this.timeToHide = timeToHide;
            return this;
        }
        public int getTimeToHide() {
            return timeToHide;
        }

        public Builder setTitleFont(String titleFontPath) {
            this.titleFont = Typeface.createFromAsset(context.getAssets(), titleFontPath);
            return this;
        }
        public Typeface getTitleFont() {
            return titleFont;
        }

        public Builder setMessageFont(String bodyFontPath) {
            this.messageFont = Typeface.createFromAsset(context.getAssets(), bodyFontPath);
            return this;
        }
        public Typeface getMessageFont() {
            return messageFont;
        }

        public Builder setPositiveTypeface(Typeface positiveTypeface) {
            this.positiveTypeface = positiveTypeface;
            return this;
        }
        public Typeface getPositiveTypeface() {
            return positiveTypeface;
        }

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

        public Builder setOnPositiveClicked(OnPositiveClicked onPositiveClicked) {
            this.onPositiveClicked = onPositiveClicked;
            return this;
        }
        public OnPositiveClicked getOnPositiveClicked() {
            return onPositiveClicked;
        }

        public Builder setOnNegativeClicked(OnNegativeClicked onNegativeClicked) {
            this.onNegativeClicked = onNegativeClicked;
            return this;
        }
        public OnNegativeClicked getOnNegativeClicked() {
            return onNegativeClicked;
        }

        public Builder setOnCancelClicked(OnCancelClicked onCancelClicked) {
            this.onCancelClicked = onCancelClicked;
            return this;
        }
        public OnCancelClicked getOnCancelClicked() {
            return onCancelClicked;
        }

        public Builder setOnInputClicked(OnInputClicked onInputClicked) {
            this.onInputClicked = onInputClicked;
            return this;
        }
        public OnInputClicked getOnInputClicked() {
            return onInputClicked;
        }

        public Builder setDestructive(ArrayList<String> destructive) {
            this.destructive = destructive;
            return this;
        }
        public ArrayList<String> getDestructive() { return destructive; }

        public Builder setOthers(ArrayList<String> others) {
            this.others = others;
            return this;
        }
        public ArrayList<String> getOthers() { return others; }

        public Builder setLineInputHint(ArrayList<String> lineInputHint) {
            this.lineInputHint = lineInputHint;
            return this;
        }
        public ArrayList<String> getLineInputHint() {
            return lineInputHint;
        }

        public Builder setLineInputText(ArrayList<String> lineInputText) {
            this.lineInputText = lineInputText;
            return this;
        }
        public ArrayList<String> getLineInputText() {
            if (lineInputText == null)
                return new ArrayList<>();
            return lineInputText;
        }

        public Builder setBoxInputText(ArrayList<String> boxInputText) {
            this.boxInputText = boxInputText;
            return this;
        }
        public ArrayList<String> getBoxInputText() {
            if (boxInputText == null)
                return new ArrayList<>();
            return boxInputText;
        }

        public Builder setBoxInputHint(ArrayList<String> boxInputHint) {
            this.boxInputHint = boxInputHint;
            return this;
        }
        public ArrayList<String> getBoxInputHint() {
            return boxInputHint;
        }

        public Builder setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }
        public AdapterView.OnItemClickListener getOnItemClickListener() { return onItemClickListener; }

        public Builder setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }
        public boolean isAutoHide() {
            return autoHide;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }
        public boolean getCancelable() { return cancelable; }

        public Builder setActivity(Context context) {
            this.context = context;
            return this;
        }

        public Builder(Context context) { this.context = context; }

        public Builder build() {
            return this;
        }

        public Dialog show() {
            return CustomAlertDialogue.getInstance().show(((Activity) context), this);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
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
}
