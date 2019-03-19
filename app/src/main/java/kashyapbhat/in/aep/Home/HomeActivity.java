package kashyapbhat.in.aep.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kashyapbhat.in.aep.Instructor.InstructorActivity;
import kashyapbhat.in.aep.R;
import kashyapbhat.in.aep.Student.StudentActivity;
import kashyapbhat.in.aep.Utils.ActivityUtils;

/**
 * Created by Kashyap Bhat on 14/02/19.
 */

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.questionButton)
    ImageButton question;

    @BindView(R.id.answerButton)
    ImageButton answer;

    @BindView(R.id.toolbar_top)
    Toolbar toolbar;

    private Context context;

    @OnClick(R.id.questionButton)
    void setQuestion()
    {
        try{
            Intent i = new Intent(HomeActivity.this, InstructorActivity.class);
            if(ActivityUtils.isNetworkOnline(context))
            {
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
        } catch (Exception e)
        {
            Log.d("Error","Intent error");
        }
    }

    @OnClick(R.id.answerButton)
    void setAnswer()
    {
        try{
            Intent i = new Intent(HomeActivity.this, StudentActivity.class);
            if(ActivityUtils.isNetworkOnline(context))
            {
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else {
                Toast.makeText(context, " Please connect to Internet ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e)
        {
            Log.d("Error","Intent error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        context = getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
