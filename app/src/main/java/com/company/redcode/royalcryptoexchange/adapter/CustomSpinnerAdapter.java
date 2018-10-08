package com.company.redcode.royalcryptoexchange.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.redcode.royalcryptoexchange.R;
import com.company.redcode.royalcryptoexchange.models.PaymentMethod;

public class CustomSpinnerAdapter extends BaseAdapter {

    Context c;
    ArrayList<PaymentMethod> objects;

    public CustomSpinnerAdapter(Context context, ArrayList<PaymentMethod> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        PaymentMethod cur_obj = objects.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.single_row_payment_spinner, parent, false);
        TextView tv_bnk_name = row.findViewById(R.id.tv_bnk_name);
        TextView tv_bnk_title = row.findViewById(R.id.tv_bnk_title);
        tv_bnk_name.setText(cur_obj.getBankName());
        tv_bnk_title.setText(cur_obj.getType());
        return row;
    }
}