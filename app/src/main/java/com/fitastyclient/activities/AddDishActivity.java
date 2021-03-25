package com.fitastyclient.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.ShortIngredient;

import java.util.Random;


public class AddDishActivity extends MyAppCompatActivity {

    static public String name = "Name";
    static public String amount = "Amount";
    static public int titleRowHeight = 100;
    static public int ingredientRowHeight = 130;
    static public int ingredientNameWidth = 800;
    static public int ingredientNameWeight = 3;
    static public int ingredientNamePadding = 20;
    static public int ingredientAmountWidth = 300;
    static public int ingredientAmountWeight = 1;
    static public int ingredientAmountPadding = 10;
    static public int ingredientDeleteWidth = 120;
    static public int titleBackgroundColorId = R.color.veryLightBlue;


    private TableLayout ingredientsTable;

    private View.OnClickListener addButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInformationText(R.id.addDishButtonInfoText);
            Utils.log("add new dish");
        }
    };

    private View.OnClickListener ingredientsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            ShortIngredient ingredient = tryToAddIngredientToTable();
            addIngredientToIngredientsTable(ingredient);
        }
    };

    private ShortIngredient tryToAddIngredientToTable() {
        Random rand = new Random();
        int number = rand.nextInt(1000);
        int amount = rand.nextInt(1000);
        boolean isLiquid = rand.nextBoolean();
        String name = (isLiquid) ? "Milk" : "Apple";
        return new ShortIngredient(name + " #" + number, isLiquid, amount);
    }

    private void addDeleteButtonToRow(TableRow row) {
        ImageView view = new ImageView(this);
        view.setImageResource(android.R.drawable.btn_dialog);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
                container.invalidate();
            }
        });
        view.setPadding(0, 10, 0, 0);
        row.addView(view);
    }

    private void addTextViewToRow(TableRow titlesRow, String text, int viewWidth, int viewHeight,
                                  int viewWeight, int leftPadding, int backgroundColorId) {
        TextView view = new TextView(this);
        view.setLayoutParams(new TableRow.LayoutParams(viewWidth, viewHeight, viewWeight));
        view.setGravity(Gravity.CENTER_VERTICAL);
        if (leftPadding != 0) view.setPadding(leftPadding, 0, 0, 0);
        if (backgroundColorId != 0) view.setBackgroundColor(getColorById(backgroundColorId));
        if (!text.isEmpty()) {
            view.setTextColor(getColorById(R.color.black));
            view.setText(text);
        }
        titlesRow.addView(view);
    }

    private void addIngredientToIngredientsTable(ShortIngredient ingredient) {
        String units = (ingredient.getIsLiquid()) ? Utils.ML : Utils.GRAM;
        String amountString = ingredient.getAmount() + units;
        TableRow row = new TableRow(this);
        addTextViewToRow(row, ingredient.getIngredientName(), ingredientNameWidth,
                ingredientRowHeight, ingredientNameWeight, ingredientNamePadding, 0);
        addTextViewToRow(row, amountString, ingredientAmountWidth, ingredientRowHeight,
                ingredientAmountWeight, ingredientAmountPadding, 0);
        addDeleteButtonToRow(row);
        this.ingredientsTable.addView(row);
    }

    private void setIngredientsTableTitles() {
        TableRow titlesRow = new TableRow(this);
        addTextViewToRow(titlesRow, name, ingredientNameWidth, titleRowHeight,
                ingredientNameWeight, ingredientNamePadding, titleBackgroundColorId);
        addTextViewToRow(titlesRow, amount, ingredientAmountWidth, titleRowHeight,
                ingredientAmountWeight, ingredientAmountPadding, titleBackgroundColorId);
        addTextViewToRow(titlesRow, Utils.EMPTY, ingredientDeleteWidth, titleRowHeight,
                0, 0, titleBackgroundColorId);
        this.ingredientsTable.addView(titlesRow);
    }

    private void setComponents() {
        findViewById(R.id.addDishCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.addNewDishButton).setOnClickListener(this.addButtonClick);
        this.ingredientsTable = (TableLayout) findViewById(R.id.ingredientsTable);
        setIngredientsTableTitles();
        findViewById(R.id.ingredientsTableAddButton).setOnClickListener(
                this.ingredientsTableAddButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dish_layout);
        setComponents();
    }
}
