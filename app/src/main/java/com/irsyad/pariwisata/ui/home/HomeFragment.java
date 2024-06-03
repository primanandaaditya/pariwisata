package com.irsyad.pariwisata.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.adapter.TempatAdapter;
import com.irsyad.pariwisata.api.kategori.IKategori;
import com.irsyad.pariwisata.api.kategori.Kategori;
import com.irsyad.pariwisata.api.kategori.KategoriModel;
import com.irsyad.pariwisata.api.kategori.KategoriUtil;
import com.irsyad.pariwisata.api.reqres.IUser;
import com.irsyad.pariwisata.api.reqres.UserModel;
import com.irsyad.pariwisata.api.reqres.UserUtil;
import com.irsyad.pariwisata.api.tempat.ITempat;
import com.irsyad.pariwisata.api.tempat.Tempat;
import com.irsyad.pariwisata.api.tempat.TempatModel;
import com.irsyad.pariwisata.api.tempat.TempatUtil;
import com.irsyad.pariwisata.databinding.FragmentHomeBinding;
import com.irsyad.pariwisata.helper.Endpoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ChipGroup cgKategori;
    Chip cKategori;
    ProgressDialog progressDialog;
    ListView lvTempatHome;
    TextView tvKosongHome;
    SearchView sv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    void findID(){
        cgKategori = getActivity().findViewById(R.id.cgKategori);
        lvTempatHome = getActivity().findViewById(R.id.lvTempatHome);
        tvKosongHome = getActivity().findViewById(R.id.tvKosongHome);
        sv = getActivity().findViewById(R.id.sv);
        sv.setQueryHint("Cari nama tempat");

        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getAllTempat();
                return false;
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()){
                    getAllTempat();
                }else{
                    tempatSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    void getKategori(){
        progressDialog =new ProgressDialog(getActivity());
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        IKategori iKategori = KategoriUtil.getKategoriInterface();
        iKategori.getKategori().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<KategoriModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Error " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(KategoriModel kategoriModel) {
                progressDialog.dismiss();

                //render chip dalam chipgroup
                for(Kategori k: kategoriModel.getPesan()){
                    cKategori = new Chip(getActivity());
                    cKategori.setText(k.getNama());
                    cKategori.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tempatByKategori(k.getId_kategori());
                        }
                    });
                    cgKategori.addView(cKategori);
                }
            }
        });
    }

    void getAllTempat(){
//        progressDialog =new ProgressDialog(getActivity());
//        progressDialog.setMessage(Endpoint.mohon_tunggu);
//        progressDialog.show();

        ITempat iTempat = TempatUtil.getTempatInterface();
        iTempat.allTempat().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TempatModel>() {
            @Override
            public void onCompleted() {
//                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
//                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Error " + e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNext(TempatModel tempatModel) {
                if (tempatModel.getPesan().isEmpty()){
                    tvKosongHome.setVisibility(View.VISIBLE);
                    lvTempatHome.setVisibility(View.GONE);
                }else{
                    tvKosongHome.setVisibility(View.GONE);
                    lvTempatHome.setVisibility(View.VISIBLE);
                    TempatAdapter tempatAdapter = new TempatAdapter(tempatModel, getActivity());
                    lvTempatHome.setAdapter(tempatAdapter);
                }
            }
        });
    }

    void tempatByKategori(String id_kategori){
        progressDialog =new ProgressDialog(getActivity());
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        ITempat iTempat = TempatUtil.getTempatInterface();
        iTempat.tempatByKategori(id_kategori).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TempatModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Error " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(TempatModel tempatModel) {
                progressDialog.dismiss();
                if (tempatModel.getPesan().isEmpty()){
                    tvKosongHome.setVisibility(View.VISIBLE);
                    lvTempatHome.setVisibility(View.GONE);
                }else{
                    tvKosongHome.setVisibility(View.GONE);
                    lvTempatHome.setVisibility(View.VISIBLE);
                    TempatAdapter tempatAdapter = new TempatAdapter(tempatModel, getActivity());
                    lvTempatHome.setAdapter(tempatAdapter);
                }
            }
        });
    }

    void tempatSearch(String key){
        progressDialog =new ProgressDialog(getActivity());
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        ITempat iTempat = TempatUtil.getTempatInterface();
        iTempat.tempatSearch(key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TempatModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Error " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(TempatModel tempatModel) {
                progressDialog.dismiss();
                if (tempatModel.getPesan().isEmpty()){
                    tvKosongHome.setVisibility(View.VISIBLE);
                    lvTempatHome.setVisibility(View.GONE);
                }else{
                    tvKosongHome.setVisibility(View.GONE);
                    lvTempatHome.setVisibility(View.VISIBLE);
                    TempatAdapter tempatAdapter = new TempatAdapter(tempatModel, getActivity());
                    lvTempatHome.setAdapter(tempatAdapter);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findID();
        getKategori();
        getAllTempat();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}