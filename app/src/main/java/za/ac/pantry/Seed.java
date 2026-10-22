package za.ac.pantry;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
final class Seed {
    static void insert(SQLiteDatabase db) {
        add(db,"Tomato toast",8,"Toast the bread in a dry pan. Slice the tomato. Top the toast with tomato and drizzle with olive oil.","bread:2:piece,tomato:1:piece,olive oil:5:ml");
        add(db,"Simple scrambled eggs",6,"Beat the eggs with milk. Melt butter in a pan over low heat. Add the eggs and stir gently until fully set.","egg:2:piece,milk:30:ml,butter:10:g");
        add(db,"Banana oat bowl",7,"Simmer oats and milk for five minutes, stirring. Slice the banana over the warm oats.","oats:50:g,milk:200:ml,banana:1:piece");
        add(db,"Carrot soup",25,"Chop carrots and onion. Soften onion in olive oil. Add carrots and water. Simmer until tender and blend carefully.","carrot:200:g,onion:1:piece,olive oil:10:ml,water:400:ml");
        add(db,"Tomato pasta",20,"Boil pasta in water until tender and drain. Chop tomato and garlic. Gently cook garlic in olive oil, add tomato and simmer. Toss with pasta.","pasta:100:g,water:1000:ml,tomato:2:piece,garlic:5:g,olive oil:10:ml");
        add(db,"Potato onion hash",25,"Dice potatoes and onion. Heat olive oil. Cook potatoes covered until tender, stirring regularly. Add onion and cook until golden.","potato:2:piece,onion:1:piece,olive oil:15:ml");
        add(db,"Cheese toast",8,"Top bread with cheese. Toast under a grill until the cheese melts and the bread turns golden.","bread:2:piece,cheese:40:g");
        add(db,"Spinach omelette",10,"Beat eggs. Wilt spinach in butter in a pan. Pour in eggs and cook until set, then fold.","egg:2:piece,spinach:50:g,butter:10:g");
        add(db,"Rice and peas",22,"Bring rice and water to a simmer. Cover and cook until almost tender. Stir in peas and cook until hot and water is absorbed.","rice:100:g,water:250:ml,peas:80:g");
        add(db,"Yoghurt apple cup",5,"Dice the apple and stir into yoghurt. Scatter oats on top and serve immediately.","yoghurt:150:g,apple:1:piece,oats:20:g");
        add(db,"Pepper egg skillet",12,"Slice pepper. Cook in olive oil until soft. Add beaten eggs and stir until fully cooked.","pepper:1:piece,egg:2:piece,olive oil:10:ml");
        add(db,"Garlic spinach pasta",18,"Boil pasta in water and drain. Gently heat garlic in oil, add spinach until wilted, then toss with pasta.","pasta:100:g,water:1000:ml,garlic:5:g,spinach:60:g,olive oil:10:ml");
        add(db,"Tomato cucumber salad",7,"Dice tomato and cucumber. Toss with olive oil and lemon juice. Serve fresh.","tomato:2:piece,cucumber:1:piece,olive oil:10:ml,lemon juice:10:ml");
        add(db,"Peanut banana toast",5,"Toast the bread. Spread with peanut butter and top with sliced banana.","bread:2:piece,peanut butter:25:g,banana:1:piece");
        add(db,"Carrot rice pot",25,"Grate carrot and chop onion. Soften onion in oil. Add rice, carrot and water. Cover and simmer until rice is tender.","rice:100:g,carrot:100:g,onion:1:piece,olive oil:10:ml,water:250:ml");
        add(db,"Warm chickpea bowl",10,"Drain cooked chickpeas. Dice tomato. Warm both with olive oil in a pan, stirring until hot.","chickpeas:200:g,tomato:1:piece,olive oil:10:ml");
        add(db,"Apple oat porridge",10,"Dice apple. Simmer apple with oats and milk, stirring until oats soften and the mixture thickens.","apple:1:piece,oats:50:g,milk:200:ml");
        add(db,"Cheesy potato mash",25,"Peel and dice potatoes. Boil in water until tender and drain. Mash with milk and cheese.","potato:2:piece,water:1000:ml,milk:50:ml,cheese:30:g");
        add(db,"Cucumber yoghurt dip",5,"Grate cucumber and squeeze out excess liquid. Stir into yoghurt with finely chopped garlic. Serve chilled.","cucumber:1:piece,yoghurt:150:g,garlic:3:g");
        add(db,"Pea spinach soup",20,"Simmer peas in water until tender. Add spinach and cook for two minutes. Stir in milk and blend carefully.","peas:150:g,water:250:ml,spinach:50:g,milk:50:ml");
    }
    private static void add(SQLiteDatabase db,String name,int minutes,String method,String ingredients) {
        ContentValues v=new ContentValues(); v.put("name",name); v.put("minutes",minutes); v.put("method",method);
        long id=db.insertOrThrow("recipe",null,v);
        for(String raw:ingredients.split(",")) {
            String[] p=raw.split(":"); ContentValues i=new ContentValues();
            i.put("recipe_id",id); i.put("name",p[0]); i.put("quantity",Double.parseDouble(p[1])); i.put("unit",p[2]);
            db.insertOrThrow("requirement",null,i);
        }
    }
}
