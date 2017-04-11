package stream.customdialogue;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomActionsheetAdapter extends BaseAdapter{

    private List<String> mDatas;
    private List<String> mDestructive;

    public CustomActionsheetAdapter(List<String> datas, List<String> destructive){
        this.mDatas =datas;
        this.mDestructive =destructive;
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
        String data= mDatas.get(position);
        Holder holder=null;
        View view =convertView;
        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view=inflater.inflate(R.layout.alertbutton, null);
            holder= createHolder(view);
            view.setTag(holder);
        }
        else{
            holder=(Holder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(),data,position);
        return view;
    }
    public Holder createHolder(View view){
        return new Holder(view);
    }

    class Holder {
        private TextView buttonText;

        public Holder(View view){
            buttonText = (TextView) view.findViewById(R.id.alerttext);
        }
        public void UpdateUI(Context context,String data,int position){
            buttonText.setText(data);
            if (mDestructive!= null && mDestructive.contains(data)){
                buttonText.setTextColor(ContextCompat.getColor(context, R.color.negative));
            }
            else{
                buttonText.setTextColor(ContextCompat.getColor(context, R.color.positive));
            }
        }
    }
}