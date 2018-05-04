package app.com.regiko.wherehaveyoubeentoday;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ковтун on 14.02.2018.
 */


    public class CustomAdapter extends ArrayAdapter<PointItem> {
        ArrayList<PointItem> list = new ArrayList<>();
    Context mContext;
        public CustomAdapter(Context context, ArrayList<PointItem> arrayList) {
            super(context, R.layout.pointer_item, arrayList);
            this.list = arrayList;
            this.mContext=context;
        }

        @Override
        @NonNull
        @SuppressWarnings("NullableProblems")
        public View getView(int position, View convertView, ViewGroup parent)  {
            MyViewHolder mViewHolder;
            if (convertView == null) {
                convertView =LayoutInflater.from(getContext()).inflate(R.layout.pointer_item, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {

                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            PointItem currentItem = list.get(position);

            mViewHolder.date.setText(currentItem.getDateTime());
            mViewHolder.lat.setText(String.valueOf(currentItem.getLat()));
            mViewHolder.longt.setText(String.valueOf(currentItem.getLogt()));

            return convertView;
        }
        private class MyViewHolder {
            TextView date, lat,longt ;

            private MyViewHolder(View item) {
                date = (TextView) item.findViewById(R.id.date);
                lat = (TextView) item.findViewById(R.id.lat);
                longt = (TextView) item.findViewById(R.id.longt);

            }
        }
    }

