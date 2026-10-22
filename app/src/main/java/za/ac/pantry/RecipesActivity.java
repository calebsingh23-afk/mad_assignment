package za.ac.pantry;
import android.content.Intent;
import android.widget.*;
import java.util.*;
import java.time.LocalDate;
public class RecipesActivity extends BaseActivity {
    @Override protected void onResume(){super.onResume();render();}
    private void render(){
        LinearLayout body=Ui.page(this,"No shopping needed","Cook what you have","Every ingredient. Enough of each. Ready right now.",RecipesActivity.class);
        TextView count=Ui.text(this,"Checking all 20 recipes...",14,Ui.GREEN);body.addView(count);
        ListView list=new ListView(this);list.setDivider(null);list.setDividerHeight(Ui.dp(this,12));body.addView(list,new LinearLayout.LayoutParams(-1,0,1));
        TextView empty=Ui.text(this,"No recipes match your pantry yet. Add more ingredients, or check your quantities. Water and oil must also be in your pantry.",18,Ui.MUTED);body.addView(empty);list.setEmptyView(empty);
        load(()->{List<Ingredient> pantry=db.pantry();List<Recipe> result=new ArrayList<>();for(Recipe r:db.recipes())if(Matcher.matches(r,pantry,LocalDate.now()))result.add(r);return result;},recipes->{count.setText(recipes.size()+" recipes you can make   /   Strict matches only");list.setAdapter(new RecipeAdapter(this,recipes));list.setOnItemClickListener((p,v,pos,id)->startActivity(new Intent(this,RecipeDetailActivity.class).putExtra("recipe_id",id)));});
    }
}
