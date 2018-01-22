package demopay.gusheng.merchantpay_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/9/12.
 */

public class LookAdapter extends BaseAdapter {

    private Context context;
    private final List<Map<String, Object>> lists;


    public LookAdapter(Context context, List<Map<String, Object>> list) {
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
        LookAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new LookAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.list, null);
            viewHolder.nameTitle = (TextView) convertView.findViewById(R.id.nameTitle);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.reminder = (TextView) convertView.findViewById(R.id.reminderTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LookAdapter.ViewHolder) convertView.getTag();
        }
        Map<String, Object> item = getItem(position);
        viewHolder.nameTitle.setText((String)item.get("name"));
        viewHolder.type.setText((String)item.get("type"));
        viewHolder.money.setText((String)item.get("money") + "元");
        viewHolder.content.setText("交易时间：" + (String)item.get("content"));

        if (item.get("reminder").equals("1")){

            viewHolder.reminder.setVisibility(View.GONE);

        }else if (item.get("reminder").equals("8")){

            viewHolder.reminder.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameTitle;
        TextView type;
        TextView money;
        TextView content;
        TextView reminder;
    }

}
