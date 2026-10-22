package za.ac.pantry;

import java.time.LocalDate;
import java.util.*;

/** Pure Java policy shared by Android UI and executable regression tests. */
public final class Matcher {
  private Matcher() {}

  public static String canonical(String name) {
    String n = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    switch (n) {
      case "tomatoes":
        return "tomato";
      case "potatoes":
        return "potato";
      case "leaves":
        return "leaf";
      case "eggs":
        return "egg";
      case "onions":
        return "onion";
      case "carrots":
        return "carrot";
      case "bananas":
        return "banana";
      case "apples":
        return "apple";
      case "bell peppers":
      case "peppers":
      case "capsicum":
        return "pepper";
      default:
        return n;
    }
  }

  public static String dimension(String unit) {
    switch (unit) {
      case "g":
      case "kg":
        return "mass";
      case "ml":
      case "l":
        return "volume";
      default:
        return "count";
    }
  }

  public static double base(double quantity, String unit) {
    return quantity * ((unit.equals("kg") || unit.equals("l")) ? 1000 : 1);
  }

  public static boolean matches(Recipe recipe, List<Ingredient> pantry, LocalDate today) {
    Map<String, Double> available = new HashMap<>(), required = new HashMap<>();
    for (Ingredient item : pantry) {
      if (item.expiry != null
          && !item.expiry.isEmpty()
          && LocalDate.parse(item.expiry).isBefore(today)) continue;
      String key = canonical(item.name) + "|" + dimension(item.unit);
      available.merge(key, base(item.quantity, item.unit), Double::sum);
    }
    for (Ingredient item : recipe.ingredients) {
      String key = canonical(item.name) + "|" + dimension(item.unit);
      required.merge(key, base(item.quantity, item.unit), Double::sum);
    }
    if (required.isEmpty()) return false;
    for (Map.Entry<String, Double> entry : required.entrySet())
      if (available.getOrDefault(entry.getKey(), 0.0) + 1e-9 < entry.getValue()) return false;
    return true;
  }
}
