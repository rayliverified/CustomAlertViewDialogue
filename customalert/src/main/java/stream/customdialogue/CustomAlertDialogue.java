package stream.customdialogue;

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
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAlertDialogue extends DialogFragment {
    public static final String TAG = CustomAlertDialogue.class.getSimpleName();
    private OnItemClickListener onItemClickListener;
    private Builder builder;
    private Style style = Style.DIALOGUE;
    private Integer gravity = Gravity.CENTER;
    private static CustomAlertDialogue instance = new CustomAlertDialogue();

    public static CustomAlertDialogue getInstance() {
        return instance;
    }

    private TextView title, message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (builder != null) {
                builder = (Builder) savedInstanceState.getParcelable(Builder.class.getSimpleName());
            }
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        setRetainInstance(true);
        if (builder.getCancelable() == false)
        {
            this.setCancelable(false);
        }
        else
        {
            this.setCancelable(true);
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
                Log.d("Dialogue Layout", "Dialogue");
                return inflater.inflate(R.layout.alert, container, false);
            case ACTIONSHEET:
                Log.d("Dialogue Layout", "Dialogue");
                return inflater.inflate(R.layout.actionsheet, container,false);
            case SELECTOR:
                Log.d("Dialogue Layout", "Selector");
                return inflater.inflate(R.layout.alert, container, false);
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
        }
    }

    private void initCommonView(View view) {
        //Common elements
        title = (TextView) view.findViewById(R.id.title);
        message = (TextView) view.findViewById(R.id.message);
        if (builder.getTitle() != null) {
            title.setText(builder.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }
        if (builder.getMessage() != null) {
            message.setText(builder.getMessage());
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

        ViewStub viewStub = (ViewStub) view.findViewById(R.id.viewStubHorizontal);
        viewStub.inflate();

        LinearLayout alertButtons = (LinearLayout) view.findViewById(R.id.alertButtons);

        if (builder.getNegativeText() != null) {
            View negativeButton = LayoutInflater.from(view.getContext()).inflate(R.layout.alertbutton, null);
            TextView negativeText = (TextView) negativeButton.findViewById(R.id.alerttext);
            negativeText.setClickable(true);
            negativeText.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
            negativeText.setText(builder.getNegativeText());
            negativeText.setTypeface(builder.getNegativeTypeface());
            negativeText.setTextColor(ContextCompat.getColor(view.getContext(), builder.getNegativeColor()));
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
            View divier = new View(view.getContext());
            divier.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.divider));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)view.getContext().getResources().getDimension(R.dimen.size_divider), LinearLayout.LayoutParams.MATCH_PARENT);
            alertButtons.addView(divier,params);

            View positiveButton = LayoutInflater.from(view.getContext()).inflate(R.layout.alertbutton, null);
            TextView positiveText = (TextView) positiveButton.findViewById(R.id.alerttext);
            positiveText.setClickable(true);
            positiveText.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
            positiveText.setText(builder.getPositiveText());
            positiveText.setTypeface(builder.getPositiveTypeface());
            positiveText.setTextColor(ContextCompat.getColor(view.getContext(), builder.getPositiveColor()));
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

        TextView cancelButton = (TextView) view.findViewById(R.id.cancel);
        if(builder.getCancelText() != null){
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText(builder.getCancelText());
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ArrayList<String> mData = new ArrayList<String>();
        if (builder.getDestructive() != null)
        {
            mData.addAll(builder.getDestructive());
        }
        if (builder.getOthers() != null)
        {
            mData.addAll(builder.getOthers());
        }
        ListView alertButtonListView = (ListView) view.findViewById(R.id.listview);
        CustomActionsheetAdapter adapter = new CustomActionsheetAdapter(mData, builder.getDestructive());
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(onItemClickListener != null)
                    onItemClickListener.onItemClick(CustomAlertDialogue.this, position);
                dismiss();
            }
        });
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
        private int backgroundColor;
        private int timeToHide;

        private Typeface titleFont;
        private Typeface messageFont;
        private Typeface positiveTypeface;
        private Typeface negativeTypeface;
        private Typeface alertTypeface;

        private OnPositiveClicked onPositiveClicked;
        private OnNegativeClicked onNegativeClicked;

        private ArrayList<String> destructive;
        private ArrayList<String> others;
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
            backgroundColor = in.readInt();
            timeToHide = in.readInt();
            destructive = in.createStringArrayList();
            others = in.createStringArrayList();
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

        public Builder setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

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

    public enum Style{
        DIALOGUE,
        ACTIONSHEET,
        SELECTOR
    }
}
