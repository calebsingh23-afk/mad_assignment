package za.ac.pantry;

import android.os.Bundle;
import android.widget.*;

public class SettingsActivity extends BaseActivity {
  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    LinearLayout body =
        Ui.page(
            this,
            "Your kitchen your way",
            "Small good habits",
            "A calmer kitchen starts with a few useful choices.",
            SettingsActivity.class);
    ScrollView scroll = new ScrollView(this);
    LinearLayout content = Ui.column(this);
    scroll.addView(content);
    body.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
    LinearLayout card = Ui.card(this);
    card.addView(Ui.text(this, "USE IT WHILE IT IS GOOD", 12, Ui.CLAY));
    Switch alerts = new Switch(this);
    alerts.setText("Show expiring soon badges");
    alerts.setTextSize(16);
    alerts.setMinHeight(Ui.dp(this, 56));
    alerts.setChecked(getSharedPreferences("settings", 0).getBoolean("alerts", true));
    alerts.setOnCheckedChangeListener(
        (b, checked) ->
            getSharedPreferences("settings", 0).edit().putBoolean("alerts", checked).apply());
    card.addView(alerts);
    card.addView(
        Ui.text(
            this,
            "Highlights ingredients expiring within three days. These are in-app badges, with no"
                + " background notifications.",
            14,
            Ui.MUTED));
    content.addView(card);
    Ui.gap(content, 18);
    LinearLayout guide = Ui.card(this);
    guide.addView(Ui.text(this, "THE PANTRY PROMISE", 12, Ui.CLAY));
    guide.addView(Ui.title(this, "Everything means everything."));
    guide.addView(
        Ui.text(
            this,
            "Recipes need every listed ingredient and enough of it. Kilograms convert to grams and"
                + " litres to millilitres. Count, weight and volume stay separate. Expired stock is"
                + " excluded.",
            16,
            Ui.INK));
    content.addView(guide);
    Ui.gap(content, 18);
    content.addView(
        Ui.text(
            this,
            "Pantry & Plate 1.0\n"
                + "Smart Pantry Manager | MAD700\n"
                + "Caleb Singh | 402306657\n"
                + "Private on-device SQLite storage",
            13,
            Ui.MUTED));
  }
}
