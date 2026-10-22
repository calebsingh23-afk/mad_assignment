import java.time.LocalDate;
import java.util.*;
import za.ac.pantry.*;

public class MatcherTest {
  static int passed;
  static LocalDate today = LocalDate.of(2026, 10, 23);

  static Ingredient i(String name, double qty, String unit) {
    return new Ingredient(0, name, qty, unit, "");
  }

  static Recipe recipe(Ingredient... ingredients) {
    Recipe r = new Recipe(1, "Test", "Cook", 5);
    r.ingredients.addAll(Arrays.asList(ingredients));
    return r;
  }

  static void check(String name, boolean actual, boolean expected) {
    if (actual != expected) throw new AssertionError(name);
    passed++;
    System.out.println("PASS " + name);
  }

  static boolean match(Recipe r, Ingredient... pantry) {
    return Matcher.matches(r, Arrays.asList(pantry), today);
  }

  public static void main(String[] args) {
    Recipe toast = recipe(i("bread", 2, "piece"), i("tomato", 1, "piece"), i("olive oil", 5, "ml"));
    check("empty pantry", match(toast), false);
    check(
        "one missing ingredient excluded",
        match(toast, i("bread", 2, "piece"), i("tomato", 1, "piece")),
        false);
    check(
        "all requirements exact boundary",
        match(toast, i("bread", 2, "piece"), i("tomato", 1, "piece"), i("olive oil", 5, "ml")),
        true);
    check(
        "insufficient quantity",
        match(toast, i("bread", 1, "piece"), i("tomato", 1, "piece"), i("olive oil", 5, "ml")),
        false);
    check(
        "case space plural aliases",
        match(recipe(i("tomato", 1, "piece")), i("  TOMATOES  ", 1, "piece")),
        true);
    check("kilograms to grams", match(recipe(i("oats", 500, "g")), i("oats", 0.5, "kg")), true);
    check("litres to millilitres", match(recipe(i("milk", 200, "ml")), i("milk", 0.2, "l")), true);
    check(
        "mass cannot substitute volume",
        match(recipe(i("milk", 200, "ml")), i("milk", 200, "g")),
        false);
    check(
        "count cannot substitute mass",
        match(recipe(i("tomato", 100, "g")), i("tomatoes", 100, "piece")),
        false);
    check(
        "duplicate stocks aggregate",
        match(recipe(i("oats", 500, "g")), i("oats", 200, "g"), i("oats", 0.3, "kg")),
        true);
    check(
        "duplicate requirements aggregate",
        match(recipe(i("oats", 300, "g"), i("oats", 300, "g")), i("oats", 500, "g")),
        false);
    Ingredient expired = new Ingredient(0, "milk", 500, "ml", "2026-10-22");
    check("expired excluded", match(recipe(i("milk", 100, "ml")), expired), false);
    check(
        "expiry today accepted",
        match(recipe(i("milk", 100, "ml")), new Ingredient(0, "milk", 100, "ml", "2026-10-23")),
        true);
    check(
        "expired duplicate does not inflate stock",
        match(recipe(i("milk", 200, "ml")), expired, i("milk", 100, "ml")),
        false);
    check("empty recipe rejected", match(recipe(), i("milk", 100, "ml")), false);
    check(
        "extra pantry ingredients allowed",
        match(recipe(i("egg", 2, "piece")), i("eggs", 3, "piece"), i("rice", 100, "g")),
        true);
    check(
        "five requirements four excluded",
        match(
            recipe(i("a", 1, "g"), i("b", 1, "g"), i("c", 1, "g"), i("d", 1, "g"), i("e", 1, "g")),
            i("a", 1, "g"),
            i("b", 1, "g"),
            i("c", 1, "g"),
            i("d", 1, "g")),
        false);
    System.out.println(passed + " matching checks passed");
  }
}
