package za.ac.pantry;

import java.util.*;

public class Recipe {
  public long id;
  public String name, method;
  public int minutes;
  public List<Ingredient> ingredients = new ArrayList<>();

  public Recipe(long id, String name, String method, int minutes) {
    this.id = id;
    this.name = name;
    this.method = method;
    this.minutes = minutes;
  }
}
