package za.ac.pantry.test;
import android.app.Instrumentation;
import android.os.Bundle;
import za.ac.pantry.*;
/** Executes CRUD against the real Android SQLite engine, without external test libraries. */
public class DatabaseChecks extends Instrumentation {
    @Override public void onCreate(Bundle args) { super.onCreate(args); start(); }
    private void require(boolean value,String label) { if(!value) throw new AssertionError(label); }
    @Override public void onStart() {
        Bundle result=new Bundle();Database db=new Database(getTargetContext());long id=0;
        try {
            require(db.recipes().size()==20,"20 seeded recipes");
            for(Recipe r:db.recipes())require(!r.ingredients.isEmpty()&&!r.method.isEmpty(),"complete recipe");
            id=db.save(new Ingredient(0,"Integration test oats",100,"g",""));
            require(id>0&&db.ingredient(id).quantity==100,"create and read");
            db.save(new Ingredient(id,"Integration test oats",200,"g",""));
            require(db.ingredient(id).quantity==200,"update");
            db.close();db=new Database(getTargetContext());
            require(db.ingredient(id).quantity==200,"persistence after helper close and reopen");
            require(db.recipes().size()==20,"seed remains idempotent");
            db.delete(id);require(db.ingredient(id)==null,"delete");id=0;
            result.putString("stream","PASS: SQLite create/read/update/delete, reopen persistence, 20 complete recipes and idempotent seed.\n");
            finish(-1,result);
        } catch(Throwable error) { result.putString("stream","FAIL: "+error);finish(0,result); }
        finally {if(id!=0)db.delete(id);db.close();}
    }
}
