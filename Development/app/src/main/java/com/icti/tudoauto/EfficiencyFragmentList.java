package com.icti.tudoauto;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Measure;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EfficiencyFragmentList extends Fragment {

    private OnDeleteInteractionListener mDeleteListener;
    private EfficiencyRecyclerViewAdapter mAdapter;
    Context context;


    public EfficiencyFragmentList() {
        // Required empty public constructor
    }

    public static EfficiencyFragmentList newInstance() {
        return new EfficiencyFragmentList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_efficiency_list, container, false);


        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new EfficiencyRecyclerViewAdapter(new ArrayList<>(), mDeleteListener);
        recyclerView.setAdapter(mAdapter);

        exibirTodosRequisitos();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;

        super.onAttach(context);

        if (context instanceof OnDeleteInteractionListener) {
            mDeleteListener = (OnDeleteInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDeleteListener = null;
    }

    private void alert(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void exibirTodosRequisitos() {

        OnLoadedMeasuresListener mLoadedListener = mAdapter::updateData;

        mLoadedListener.onLoadedMeasures(Application.getMeasures());

    }



    private interface OnLoadedMeasuresListener {
        void onLoadedMeasures(List<Measure> measures);
    }

    public interface OnDeleteInteractionListener {
        void onDeleteInteraction(int position);
    }

}

