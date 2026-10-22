package za.ac.pantry;
import android.os.Bundle;
import android.widget.*;
public class RecipeDetailActivity extends BaseActivity {
    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        LinearLayout body=Ui.page(this,"From pantry to plate","The recipe","Simple food. Thoughtfully used.",RecipesActivity.class);
        ScrollView scroll=new ScrollView(this);LinearLayout content=Ui.column(this);scroll.addView(content);body.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        long id=getIntent().getLongExtra("recipe_id",-1);
        load(()->{for(Recipe r:db.recipes())if(r.id==id)return r;return null;},r->{
            if(r==null){content.addView(Ui.text(this,"Recipe unavailable",18,Ui.CLAY));return;}
            content.addView(Ui.title(this,r.name));content.addView(Ui.text(this,r.minutes+" minutes  /  1 serving",14,Ui.GREEN));Ui.gap(content,12);
            LinearLayout ingredients=Ui.card(this);ingredients.addView(Ui.text(this,"WHAT YOU NEED",12,Ui.CLAY));
            for(Ingredient i:r.ingredients)ingredients.addView(Ui.text(this,i.name+"     "+Ui.amount(i),16,Ui.INK));content.addView(ingredients);Ui.gap(content,18);
            content.addView(Ui.text(this,"MAKE IT",12,Ui.CLAY));String[] steps=r.method.split("(?<=\\.) ");for(int k=0;k<steps.length;k++)content.addView(Ui.text(this,(k+1)+". "+steps[k],17,Ui.INK));Ui.gap(content,16);
            content.addView(Ui.text(this,"Suggestions check availability but do not reserve ingredients. Cooking does not deduct stock automatically.",13,Ui.MUTED));
        });
    }
}
