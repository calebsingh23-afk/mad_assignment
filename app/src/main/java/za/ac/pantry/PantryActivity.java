package za.ac.pantry;

import android.content.Intent;
import android.widget.*;
import java.time.LocalDate;
import java.util.*;

public class PantryActivity extends BaseActivity {
  @Override
  protected void onResume() {
    super.onResume();
    render();
  }

  private void render() {
    LinearLayout body =
        Ui.page(
            this,
            "A little less waste",
            "Your pantry",
            "Good food starts with what you already have.",
            PantryActivity.class);
    body.addView(
        Ui.button(
            this,
            "+  Add ingredient",
            () -> startActivity(new Intent(this, IngredientActivity.class))));
    Ui.gap(body, 14);
    TextView summary = Ui.text(this, "Opening your pantry...", 13, Ui.MUTED);
    body.addView(summary);
    ListView list = new ListView(this);
    list.setDivider(null);
    list.setDividerHeight(Ui.dp(this, 10));
    body.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));
    TextView empty =
        Ui.text(
            this,
            "A fresh start. Add your first ingredient to see what you can cook.",
            18,
            Ui.MUTED);
    body.addView(empty);
    list.setEmptyView(empty);
    load(
        () -> db.pantry(),
        items -> {
          boolean alerts = getSharedPreferences("settings", 0).getBoolean("alerts", true);
          long soon =
              items.stream()
                  .filter(
                      i ->
                          !i.expiry.isEmpty()
                              && !LocalDate.parse(i.expiry).isBefore(LocalDate.now())
                              && !LocalDate.parse(i.expiry).isAfter(LocalDate.now().plusDays(3)))
                  .count();
          summary.setText(
              items.size()
                  + " ingredients"
                  + (alerts ? "   /   " + soon + " to use soon" : "   /   Saved on this device"));
          PantryAdapter adapter = new PantryAdapter(this, items, alerts);
          list.setAdapter(adapter);
          list.setOnItemClickListener(
              (p, v, pos, id) ->
                  startActivity(
                      new Intent(this, IngredientActivity.class).putExtra("ingredient_id", id)));
        });
  }
}
