package za.ac.pantry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import java.time.*;

public class IngredientActivity extends BaseActivity {
  private EditText name, quantity, expiry;
  private Spinner unit;
  private long id;
  private Button save;
  private final String[] units = {"piece", "g", "kg", "ml", "l"};

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    id = getIntent().getLongExtra("ingredient_id", 0);
    LinearLayout body =
        Ui.page(
            this,
            "Make it count",
            id == 0 ? "Add ingredient" : "Edit ingredient",
            "Keep quantities honest. Every ingredient matters.",
            PantryActivity.class);
    ScrollView scroll = new ScrollView(this);
    LinearLayout form = Ui.column(this);
    scroll.addView(form);
    body.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
    name =
        field(
            form,
            "Ingredient name",
            "e.g. Tomatoes",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    quantity =
        field(
            form,
            "Quantity",
            "e.g. 2",
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    form.addView(Ui.text(this, "Unit", 14, Ui.GREEN));
    unit = new Spinner(this);
    unit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));
    form.addView(unit, new LinearLayout.LayoutParams(-1, Ui.dp(this, 52)));
    expiry =
        field(
            form,
            "Expiry date optional",
            "YYYY-MM-DD",
            InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
    form.addView(
        Ui.text(
            this,
            "Expired ingredients stay visible, but never qualify for recipes. Use g/kg for weight"
                + " and ml/l for liquids.",
            13,
            Ui.MUTED));
    Ui.gap(form, 14);
    save = Ui.button(this, "Save ingredient", this::save);
    form.addView(save);
    Ui.gap(form, 12);
    if (id != 0) {
      Button delete =
          Ui.button(
              this,
              "Delete ingredient",
              () ->
                  new AlertDialog.Builder(this)
                      .setTitle("Remove this ingredient?")
                      .setMessage("This will also refresh the recipes you can make.")
                      .setNegativeButton("Keep", null)
                      .setPositiveButton(
                          "Delete",
                          (d, w) -> {
                            save.setEnabled(false);
                            load(
                                () -> {
                                  db.delete(id);
                                  return true;
                                },
                                ok -> {
                                  Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT)
                                      .show();
                                  finish();
                                });
                          })
                      .show());
      delete.setBackground(Ui.shape(Ui.CLAY, Ui.dp(this, 14)));
      form.addView(delete);
      load(
          () -> db.ingredient(id),
          i -> {
            if (i == null) {
              finish();
              return;
            }
            name.setText(i.name);
            quantity.setText("" + i.quantity);
            expiry.setText(i.expiry);
            for (int k = 0; k < units.length; k++)
              if (units[k].equals(i.unit)) unit.setSelection(k);
          });
    }
  }

  private EditText field(LinearLayout form, String label, String hint, int type) {
    form.addView(Ui.text(this, label, 14, Ui.GREEN));
    EditText e = new EditText(this);
    e.setSingleLine(true);
    e.setTextSize(16);
    e.setHint(hint);
    e.setInputType(type);
    e.setContentDescription(label);
    form.addView(e, new LinearLayout.LayoutParams(-1, Ui.dp(this, 54)));
    return e;
  }

  private void save() {
    String n = name.getText().toString().trim(), date = expiry.getText().toString().trim();
    double q;
    if (n.isEmpty() || n.length() > 60 || !n.matches(".*[\\p{L}].*")) {
      name.setError("Enter an ingredient name with letters, up to 60 characters");
      name.requestFocus();
      return;
    }
    try {
      q = Double.parseDouble(quantity.getText().toString());
      if (!Double.isFinite(q) || q <= 0 || q > 1000000) throw new NumberFormatException();
    } catch (NumberFormatException e) {
      quantity.setError("Enter a quantity greater than 0 and at most 1000000");
      quantity.requestFocus();
      return;
    }
    if (!date.isEmpty())
      try {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) throw new DateTimeException("format");
        LocalDate.parse(date);
      } catch (DateTimeException e) {
        expiry.setError("Use a real date in YYYY-MM-DD format");
        expiry.requestFocus();
        return;
      }
    save.setEnabled(false);
    load(
        () -> db.save(new Ingredient(id, n, q, (String) unit.getSelectedItem(), date)),
        result -> {
          Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
          finish();
        });
  }
}
