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
import com.irsyad.pariwisata.api.user.User;
import com.irsyad.pariwisata.api.user.UserModel;
import com.irsyad.pariwisata.helper.Endpoint;

public class UserAdapter extends BaseAdapter {

    LayoutInflater inflater;
    UserModel userModel;
    Context context;
    User user;

    public UserAdapter(UserModel userModel, Context context) {
        this.userModel = userModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userModel.getPesan().size();
    }

    @Override
    public User getItem(int position) {
        return userModel.getPesan().get(position);
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
            convertView = inflater.inflate(R.layout.list_user, null);

        TextView tvNama = convertView.findViewById(R.id.tvNama);
        TextView tvAlamat = convertView.findViewById(R.id.tvAlamat);
        TextView tvRole = convertView.findViewById(R.id.tvRole);
        TextView tvTempat = convertView.findViewById(R.id.tvTempat);
        TextView tvTgllahir = convertView.findViewById(R.id.tvTgllahir);
        TextView tvGender = convertView.findViewById(R.id.tvGender);


        user = userModel.getPesan().get(position);

        if (user.getNama()!=null){
            tvNama.setText("  " + user.getNama());
        }
        if (user.getAlamat()!=null){
            tvAlamat.setText("  " + user.getAlamat());
        }
        if (user.getRole()!=null){
            tvRole.setText("  " + user.getRole());
        }
        if (user.getTempat()!=null){
            tvTempat.setText("  " + user.getTempat());
        }
        if (user.getTgllahir()!=null){
            tvTgllahir.setText("  " + user.getTgllahir());
        }
        if (user.getGender()!=null){
            tvGender.setText("  " + user.getGender());
        }
        return convertView;
    }
}
