package com.hababk.appstore.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hababk.appstore.R;
import com.hababk.appstore.adapter.CategoryAdapter;
import com.hababk.appstore.network.response.MenuItemCategory;

import java.util.ArrayList;

/**
 * Created by a_man on 24-03-2018.
 */

public class CategorySelectionDialog extends Dialog {
    private ArrayList<MenuItemCategory> categories, selectedCategories;
    private RecyclerView categoriesRecycler;
    private Button doneSelection;

    public CategorySelectionDialog(@NonNull Context context, int styleRes, ArrayList<MenuItemCategory> categories, ArrayList<MenuItemCategory> categoriesSelected, final View.OnClickListener onClickListener) {
        super(context, styleRes);
        this.categories = categories;
        for (MenuItemCategory category : categoriesSelected) {
            int index = this.categories.indexOf(category);
            if (index != -1) {
                this.categories.get(index).setSelected(true);
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setCancelable(false);
        setContentView(R.layout.dialog_category_selection);
        categoriesRecycler = findViewById(R.id.categoriesRecycler);
        doneSelection = findViewById(R.id.doneSelection);
        setupRecycler();
        doneSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategories = new ArrayList<>();
                for (MenuItemCategory category : CategorySelectionDialog.this.categories)
                    if (category.isSelected())
                        selectedCategories.add(category);
                if (selectedCategories.isEmpty()) {
                    Toast.makeText(getContext(), "Select atleast one category", Toast.LENGTH_SHORT).show();
                } else {
                    onClickListener.onClick(v);
                    dismiss();
                }
            }
        });
    }

    private void setupRecycler() {
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecycler.setAdapter(new CategoryAdapter(getContext(), categories));
    }

    public ArrayList<MenuItemCategory> getSelection() {
        return selectedCategories;
    }
}
