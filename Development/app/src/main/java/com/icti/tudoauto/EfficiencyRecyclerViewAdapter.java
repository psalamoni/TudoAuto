package com.icti.tudoauto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.icti.tudoauto.R;
import com.icti.tudoauto.Model.Measure;
import com.icti.tudoauto.EfficiencyFragmentList.OnDeleteInteractionListener;

import java.util.List;

public class EfficiencyRecyclerViewAdapter extends RecyclerView.Adapter<EfficiencyRecyclerViewAdapter.ViewHolder> {

    private List<Measure> mValues;
    private final OnDeleteInteractionListener mListener;

    EfficiencyRecyclerViewAdapter(List<Measure> items, OnDeleteInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_mean, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setFields(mValues.get(position));

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onDeleteInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    void updateData(List<Measure> means) {
        mValues.clear();
        mValues = means;
        notifyDataSetChanged();
    }

    public List<Measure> getValues() {
        return mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageButton mDeleteButton;
        final TextView mFuelView;
        final TextView mEfficiencyView;
        final TextView mLocationView;
        Measure mItem;

        ViewHolder(View view) {
            super(view);
            mDeleteButton = view.findViewById(R.id.delete_efficiency);
            mFuelView = view.findViewById(R.id.item_fuel);
            mEfficiencyView = view.findViewById(R.id.item_efficiency);
            mLocationView = view.findViewById(R.id.item_location);
        }

        void setFields(Measure measure) {
            mItem = measure;
            mFuelView.setText(measure.getFueltype());
            mEfficiencyView.setText(String.format("%s Km/l", measure.getMeasureavg()));
            mLocationView.setText(measure.getPosition().getAdressLine());
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
