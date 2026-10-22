package za.ac.pantry;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.*;
public class RecipeAdapter extends BaseAdapter {
    private final Context context; private final List<Recipe> recipes;
    public RecipeAdapter(Context context,List<Recipe> recipes){this.context=context;this.recipes=recipes;}
    @Override public int getCount(){return recipes.size();}
    @Override public Recipe getItem(int position){return recipes.get(position);}
    @Override public long getItemId(int position){return getItem(position).id;}
    @Override public View getView(int position,View old,ViewGroup parent){
        LinearLayout card=Ui.card(context);Recipe r=getItem(position);
        card.addView(Ui.text(context,"READY FROM YOUR PANTRY",11,Ui.CLAY));card.addView(Ui.title(context,r.name));
        card.addView(Ui.text(context,r.minutes+" min  /  "+r.ingredients.size()+" ingredients  /  View recipe",13,Ui.GREEN));return card;
    }
}
