package com.irsyad.pariwisata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.api.tempat.Tempat;
import com.irsyad.pariwisata.api.tempat.TempatModel;
import com.irsyad.pariwisata.helper.Endpoint;
import com.irsyad.pariwisata.helper.Util;

public class TerdekatAdapter extends BaseAdapter {

    LayoutInflater inflater;
    TempatModel tempatModel;
    Context context;
    Tempat tempat;

    public TerdekatAdapter(TempatModel tempatModel, Context context) {
        this.tempatModel = tempatModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tempatModel.getPesan().size();
    }

    @Override
    public Tempat getItem(int position) {
        return tempatModel.getPesan().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_terdekat, null);

        TextView tvNama = convertView.findViewById(R.id.tvNama);
        TextView tvAlamat = convertView.findViewById(R.id.tvAlamat);
        TextView tvKet = convertView.findViewById(R.id.tvKet);
        ImageView iv = convertView.findViewById(R.id.iv);

        tempat = tempatModel.getPesan().get(position);

        if (tempat.getNama()!=null){
            tvNama.setText(tempat.getNama());
        }
        if (tempat.getAlamat()!=null){
            tvAlamat.setText(tempat.getAlamat());
        }
        Glide.with(context)
                .load(Endpoint.base_url_foto + tempat.getFoto())
                .into(iv);
        if (tempat.getJarak() == null || tempat.getJarak().length() == 0 ){
            tvKet.setText("-");
        }else{
            tvKet.setText(tempat.getJarak() + " km");
        }
        return convertView;
    }

}
