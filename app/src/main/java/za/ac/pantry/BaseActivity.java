package za.ac.pantry;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.concurrent.*;
abstract class BaseActivity extends Activity {
    private static final ExecutorService WORK=Executors.newSingleThreadExecutor();
    protected Database db;
    @Override public void onCreate(Bundle state) { super.onCreate(state);db=new Database(this); }
    /** Keep disk reads and writes off the Android main thread. */
    protected <T> void load(Callable<T> task,java.util.function.Consumer<T> result) {
        WORK.execute(()-> { try { T value=task.call();runOnUiThread(()-> { if(!isFinishing()&&!isDestroyed())result.accept(value); }); }
            catch(Exception e) { android.util.Log.e("PantryPlate","Database operation failed",e);runOnUiThread(()-> { if(!isFinishing())Toast.makeText(this,"Could not save or load data. Please try again.",Toast.LENGTH_LONG).show(); }); } });
    }
}
