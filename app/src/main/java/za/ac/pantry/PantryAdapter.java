package za.ac.pantry;

import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import java.time.LocalDate;
import java.util.*;

public class PantryAdapter extends BaseAdapter {
  private final Context context;
  private final List<Ingredient> items;
  private final boolean alerts;

  public PantryAdapter(Context c, List<Ingredient> data, boolean alerts) {
    context = c;
    items = data;
    this.alerts = alerts;
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public Ingredient getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).id;
  }

  @Override
  public View getView(int position, View recycled, ViewGroup parent) {
    LinearLayout card;
    if (recycled instanceof LinearLayout) {
      card = (LinearLayout) recycled;
      card.removeAllViews();
    } else card = Ui.card(context);
    Ingredient i = getItem(position);
    TextView name = Ui.text(context, i.name, 19, Ui.INK);
    name.setTypeface(null, Typeface.BOLD);
    card.addView(name);
    card.addView(Ui.text(context, Ui.amount(i) + "   /   Tap to edit", 14, Ui.MUTED));
    if (!i.expiry.isEmpty()) {
      LocalDate expiry = LocalDate.parse(i.expiry), today = LocalDate.now();
      String label =
          expiry.isBefore(today)
              ? "Expired - excluded from recipes"
              : alerts && !expiry.isAfter(today.plusDays(3))
                  ? "Use soon - " + i.expiry
                  : "Best before " + i.expiry;
      card.addView(
          Ui.text(
              context,
              label,
              12,
              expiry.isBefore(today) || alerts && !expiry.isAfter(today.plusDays(3))
                  ? Ui.CLAY
                  : Ui.MUTED));
    }
    card.setContentDescription(
        i.name
            + ", "
            + Ui.amount(i)
            + (i.expiry.isEmpty() ? "" : ", expires " + i.expiry)
            + ". Tap to edit.");
    return card;
  }
}
