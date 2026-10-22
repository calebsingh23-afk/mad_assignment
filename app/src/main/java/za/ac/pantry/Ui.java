package za.ac.pantry;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.Locale;

final class Ui {
  static final int BG = Color.rgb(247, 243, 232),
      GREEN = Color.rgb(35, 83, 69),
      INK = Color.rgb(29, 49, 41),
      MUTED = Color.rgb(101, 113, 101),
      CLAY = Color.rgb(171, 77, 48);

  static int dp(Context c, int n) {
    return Math.round(n * c.getResources().getDisplayMetrics().density);
  }

  static GradientDrawable shape(int color, int radius) {
    GradientDrawable d = new GradientDrawable();
    d.setColor(color);
    d.setCornerRadius(radius);
    return d;
  }

  static TextView text(Context c, String s, int size, int color) {
    TextView t = new TextView(c);
    t.setText(s);
    t.setTextSize(size);
    t.setTextColor(color);
    t.setPadding(0, dp(c, 5), 0, dp(c, 5));
    return t;
  }

  static TextView title(Context c, String s) {
    TextView t = text(c, s, 32, INK);
    t.setTypeface(Typeface.create("serif", Typeface.BOLD));
    return t;
  }

  static LinearLayout column(Context c) {
    LinearLayout l = new LinearLayout(c);
    l.setOrientation(LinearLayout.VERTICAL);
    return l;
  }

  static LinearLayout card(Context c) {
    LinearLayout l = column(c);
    l.setPadding(dp(c, 18), dp(c, 14), dp(c, 18), dp(c, 14));
    l.setBackground(shape(Color.WHITE, dp(c, 20)));
    return l;
  }

  static void gap(LinearLayout l, int height) {
    View v = new View(l.getContext());
    l.addView(v, new LinearLayout.LayoutParams(1, dp(l.getContext(), height)));
  }

  static Button button(Context c, String label, Runnable action) {
    Button b = new Button(c);
    b.setText(label);
    b.setAllCaps(false);
    b.setTextSize(15);
    b.setTextColor(Color.WHITE);
    b.setMinHeight(dp(c, 52));
    b.setBackground(shape(GREEN, dp(c, 14)));
    b.setOnClickListener(v -> action.run());
    return b;
  }

  static String amount(Ingredient i) {
    return String.format(
        Locale.ROOT,
        "%s %s",
        i.quantity == (long) i.quantity
            ? Long.toString((long) i.quantity)
            : Double.toString(i.quantity),
        i.unit);
  }

  static void open(Activity a, Class<?> target) {
    Intent i = new Intent(a, target);
    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    a.startActivity(i);
  }

  static LinearLayout page(
      Activity a, String eyebrow, String title, String subtitle, Class<?> selected) {
    a.getWindow().setStatusBarColor(BG);
    a.getWindow().setNavigationBarColor(BG);
    LinearLayout root = column(a);
    root.setBackgroundColor(BG);
    root.setPadding(dp(a, 22), dp(a, 12), dp(a, 22), dp(a, 10));
    if (android.os.Build.VERSION.SDK_INT >= 35)
      root.setOnApplyWindowInsetsListener(
          (v, insets) -> {
            android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
            v.setPadding(
                dp(a, 22) + bars.left,
                dp(a, 12) + bars.top,
                dp(a, 22) + bars.right,
                dp(a, 10) + bars.bottom);
            return insets;
          });
    TextView brand = text(a, "P / P     PANTRY & PLATE", 13, GREEN);
    brand.setLetterSpacing(.16f);
    root.addView(brand);
    gap(root, 18);
    root.addView(text(a, eyebrow.toUpperCase(Locale.ROOT), 12, CLAY));
    root.addView(title(a, title));
    root.addView(text(a, subtitle, 14, MUTED));
    gap(root, 12);
    LinearLayout content = column(a);
    root.addView(content, new LinearLayout.LayoutParams(-1, 0, 1));
    LinearLayout nav = new LinearLayout(a);
    nav.setPadding(0, dp(a, 10), 0, 0);
    String[] names = {"Pantry", "Recipes", "Settings"};
    Class<?>[] classes = {PantryActivity.class, RecipesActivity.class, SettingsActivity.class};
    for (int k = 0; k < 3; k++) {
      final Class<?> dest = classes[k];
      Button b = new Button(a);
      b.setAllCaps(false);
      b.setText(names[k]);
      b.setTextSize(13);
      b.setTextColor(dest == selected ? Color.WHITE : GREEN);
      b.setBackground(shape(dest == selected ? GREEN : BG, dp(a, 12)));
      b.setOnClickListener(v -> open(a, dest));
      nav.addView(b, new LinearLayout.LayoutParams(0, dp(a, 50), 1));
    }
    root.addView(nav);
    a.setContentView(root);
    return content;
  }
}
