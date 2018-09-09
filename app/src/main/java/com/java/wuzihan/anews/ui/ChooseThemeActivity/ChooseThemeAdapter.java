package com.java.wuzihan.anews.ui.ChooseThemeActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.ChooseThemeViewModel;
import com.java.wuzihan.anews.database.entity.Theme;

import java.util.List;

public class ChooseThemeAdapter extends
        RecyclerView.Adapter<ChooseThemeAdapter.ViewHolder> {

    private Context context;

    private int lastSelectedPosition = -1;
    private List<Theme> theme;
    private ChooseThemeViewModel viewModel;

    public ChooseThemeAdapter(Context ctx, List<Theme> theme, Theme curTheme, ChooseThemeViewModel viewModel) {
        this.theme = theme;
        context = ctx;
        this.viewModel = viewModel;
    }

    @Override
    public ChooseThemeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);

        ChooseThemeAdapter.ViewHolder viewHolder =
                new ChooseThemeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChooseThemeAdapter.ViewHolder holder,
                                 int position) {
        holder.textView.setText(theme.get(position).getName());

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.selectionState.setChecked(lastSelectedPosition == position);
        holder.theme = theme.get(position);
    }

    @Override
    public int getItemCount() {
        return theme.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public RadioButton selectionState;
        public Theme theme;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text_theme);
            selectionState = (RadioButton) view.findViewById(R.id.radio_theme);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.setSelectedTheme(theme);
                    Log.d("theme", theme.getName());
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
