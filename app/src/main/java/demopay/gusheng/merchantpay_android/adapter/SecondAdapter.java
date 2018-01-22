package demopay.gusheng.merchantpay_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import demopay.gusheng.merchantpay_android.R;


/**
 * Created by fby on 2017/6/13.
 */

public class SecondAdapter extends BaseAdapter {
    private Context context;
    private final List<Map<String, Object>> lists;


    public SecondAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.lists = list;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return (Map<String, Object>) lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SecondAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new SecondAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.list_second, null);
            viewHolder.nameTitle = (TextView) convertView.findViewById(R.id.nameTitle);
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SecondAdapter.ViewHolder) convertView.getTag();
        }
        Map<String, Object> item = getItem(position);
        viewHolder.nameTitle.setText((String)item.get("nameTitle"));
        viewHolder.imageview.setImageResource((int)item.get("imageview"));
        return convertView;
    }

    static class ViewHolder {

        ImageView imageview;
        TextView nameTitle;
    }
}
