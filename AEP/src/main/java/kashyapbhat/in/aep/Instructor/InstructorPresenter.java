package kashyapbhat.in.aep.Instructor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kashyapbhat.in.aep.Common.Constants;
import kashyapbhat.in.aep.Instructor.Model.InstructorAPI;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Context.MODE_PRIVATE;

public class InstructorPresenter implements InstructorContract.Presenter {

    private InstructorContract.View view;
    private Context context;

    InstructorPresenter(InstructorContract.View view, Context context)
    {
        this.context =context;
        this.view = view;
    }

    @Override
    public void saveSharedPrefernce(int qno, String ques, String keyword, int tolMarks, String text) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.M_Instructor_Preference_NAME, MODE_PRIVATE).edit();
        editor.putInt("quesno",qno);
        editor.putString("ques",ques);
        editor.putString("keyword",keyword);
        editor.putInt("tolmark",tolMarks);
        editor.putString("text",text);
        editor.apply();
    }

    @Override
    public void getSharedPreference() {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.M_Instructor_Preference_NAME, MODE_PRIVATE);
        int qno = sharedPref.getInt("quesno", 0);
        String ques =sharedPref.getString("ques", "");
        String keyword = sharedPref.getString("keyword","");
        int tolMarks = sharedPref.getInt("tolmark",0);
        String text = sharedPref.getString("text", "");
        view.setDataFromSharedPreference(qno,ques,keyword,tolMarks,text);
    }

    @Override
    public void clearSharedPreference() {
        SharedPreferences preferences = context.getSharedPreferences(Constants.M_Student_Preference_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void changeText(String sharedText) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.M_Instructor_Preference_NAME, MODE_PRIVATE).edit();
        editor.putString("text",sharedText);
        editor.apply();
    }

    @Override
    public void insertData(int qno, String ques, String keyword, int tolMarks, String text) {
        databaseStoreQuestion(qno, ques, keyword, tolMarks, text,context);
        insertQuestion(qno, ques, keyword, tolMarks, text,context);
    }

    @SuppressLint("WrongConstant")
    public void insertQuestion(int vqno, String vquestion, String vkeywords, int vtotalmarks, String vtextpic, Context context) {
        SQLiteDatabase db;
        db = context.openOrCreateDatabase( "aep.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        try {
            //TODO : remove while using
            final String drop="DROP TABLE IF EXISTS `question`;";
            db.execSQL(drop);

            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS `question` (`qno` int(11) NOT NULL,`question` varchar(150) NOT NULL, `keywords` varchar(300) NOT NULL, `totalmarks` int(11) NOT NULL, `textpic` varchar(300) NOT NULL, PRIMARY KEY (`qno`));";
            db.execSQL(CREATE_TABLE_CONTAIN);
            Toast.makeText(context, " Question added ", Toast.LENGTH_SHORT).show();
            String sql = "INSERT or replace INTO question (qno,question,keywords,totalmarks,textpic) VALUES("+vqno+",'"+vquestion+"','"+vkeywords+"',"+vtotalmarks+",'"+vtextpic+"')" ;
            db.execSQL(sql);

            @SuppressLint("Recycle")
            Cursor cursor = db.rawQuery("select * from `question`",null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String sqno = cursor.getString(cursor.getColumnIndex("qno"));
                    String sques = cursor.getString(cursor.getColumnIndex("question"));
                    String skey = cursor.getString(cursor.getColumnIndex("keywords"));
                    String stot = cursor.getString(cursor.getColumnIndex("totalmarks"));
                    String stext = cursor.getString(cursor.getColumnIndex("textpic"));

                    String allData = sqno+System.getProperty("line.separator")+System.getProperty("line.separator")+sques+System.getProperty("line.separator")+System.getProperty("line.separator")+skey+System.getProperty("line.separator")+System.getProperty("line.separator")+stot+System.getProperty("line.separator")+System.getProperty("line.separator")+stext;

                    //TODO : remove while using
                    Toast.makeText(context, allData, Toast.LENGTH_LONG).show();
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void databaseStoreQuestion(int vqno, String vquestion, String vkeywords, int vtotalmarks, String vtextpic, Context context) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constants.ROOT_URL)
                .build();
        InstructorAPI api = adapter.create(InstructorAPI.class);
        api.insertUser(
                vqno,
                vquestion,
                vkeywords,
                vtotalmarks,
                vtextpic,
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try {
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Logger.e("Retrofit : "+error);
                        Toast.makeText(context, " Error: "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
