package za.ac.pantry;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class Database extends SQLiteOpenHelper {
  public Database(Context context) {
    super(context.getApplicationContext(), "pantry.db", null, 1);
  }

  @Override
  public void onConfigure(SQLiteDatabase db) {
    db.setForeignKeyConstraintsEnabled(true);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
        "CREATE TABLE pantry(id INTEGER PRIMARY KEY, name TEXT NOT NULL, quantity REAL NOT NULL"
            + " CHECK(quantity>0), unit TEXT NOT NULL, expiry TEXT NOT NULL DEFAULT '')");
    db.execSQL(
        "CREATE TABLE recipe(id INTEGER PRIMARY KEY, name TEXT NOT NULL, method TEXT NOT NULL,"
            + " minutes INTEGER NOT NULL)");
    db.execSQL(
        "CREATE TABLE requirement(id INTEGER PRIMARY KEY, recipe_id INTEGER NOT NULL REFERENCES"
            + " recipe(id) ON DELETE CASCADE, name TEXT NOT NULL, quantity REAL NOT NULL"
            + " CHECK(quantity>0), unit TEXT NOT NULL)");
    Seed.insert(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Add non-destructive migrations here when the schema version increases.
    throw new IllegalStateException(
        "No migration defined from " + oldVersion + " to " + newVersion);
  }

  public long save(Ingredient item) {
    ContentValues values = new ContentValues();
    values.put("name", item.name);
    values.put("quantity", item.quantity);
    values.put("unit", item.unit);
    values.put("expiry", item.expiry);
    if (item.id == 0) return getWritableDatabase().insertOrThrow("pantry", null, values);
    if (getWritableDatabase().update("pantry", values, "id=?", new String[] {"" + item.id}) != 1)
      throw new IllegalStateException("Ingredient no longer exists");
    return item.id;
  }

  public void delete(long id) {
    getWritableDatabase().delete("pantry", "id=?", new String[] {"" + id});
  }

  public List<Ingredient> pantry() {
    List<Ingredient> result = new ArrayList<>();
    try (Cursor c =
        getReadableDatabase()
            .rawQuery(
                "SELECT id,name,quantity,unit,expiry FROM pantry ORDER BY CASE WHEN expiry='' THEN"
                    + " 1 ELSE 0 END,expiry,name",
                null)) {
      while (c.moveToNext())
        result.add(
            new Ingredient(
                c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4)));
    }
    return result;
  }

  public Ingredient ingredient(long id) {
    for (Ingredient i : pantry()) if (i.id == id) return i;
    return null;
  }

  public List<Recipe> recipes() {
    List<Recipe> result = new ArrayList<>();
    try (Cursor c =
        getReadableDatabase()
            .rawQuery("SELECT id,name,method,minutes FROM recipe ORDER BY name", null)) {
      while (c.moveToNext())
        result.add(new Recipe(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3)));
    }
    for (Recipe r : result) {
      try (Cursor c =
          getReadableDatabase()
              .rawQuery(
                  "SELECT name,quantity,unit FROM requirement WHERE recipe_id=? ORDER BY id",
                  new String[] {"" + r.id})) {
        while (c.moveToNext())
          r.ingredients.add(new Ingredient(0, c.getString(0), c.getDouble(1), c.getString(2), ""));
      }
    }
    return result;
  }
}
