package kashyapbhat.in.aep.Student;

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
import kashyapbhat.in.aep.Student.Model.StudentAPI;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Context.MODE_PRIVATE;

public class StudentPresenter implements StudentContract.Presenter {

    private Context context;
    private StudentContract.View view;

    StudentPresenter(StudentContract.View view,Context context)
    {
        this.view = view;
        this.context = context;
    }

    @Override
    public void saveSharedPrefernce(String usnString, String q1, String q2, String q3, String q4, String q5, String q6,int buttonClick) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.M_Student_Preference_NAME, MODE_PRIVATE).edit();
        editor.putString("usn",usnString);
        editor.putString("qa1",q1);
        editor.putString("qa2",q2);
        editor.putString("qa3",q3);
        editor.putString("qa4",q4);
        editor.putString("qa5",q5);
        editor.putString("qa6",q6);
        editor.putInt("bno",buttonClick);
        editor.apply();
    }

    @Override
    public void getSharedPreference() {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.M_Student_Preference_NAME, MODE_PRIVATE);
        String usn = sharedPref.getString("usn",null);
        String qa1 = sharedPref.getString("qa1",null);
        String qa2 = sharedPref.getString("qa2",null);
        String qa3 = sharedPref.getString("qa3",null);
        String qa4 = sharedPref.getString("qa4",null);
        String qa5 = sharedPref.getString("qa5",null);
        String qa6 = sharedPref.getString("qa6",null);
        int bno = sharedPref.getInt("bno",0);
        view.setDataFromSharedPreference(usn,qa1,qa2,qa3,qa4,qa5,qa6,bno);
    }

    @Override
    public void clearSharedPreference() {
        SharedPreferences preferences = context.getSharedPreferences(Constants.M_Student_Preference_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void changeText(String sharedPrefName, String sharedText) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.M_Student_Preference_NAME, MODE_PRIVATE).edit();
        editor.putString(sharedPrefName,sharedText);
        editor.apply();
    }

    @Override
    public void insertData(String usnString, String q1, String q2, String q3, String q4, String q5, String q6,Context context) {
        databaseStoreAnswer(usnString,q1,q2,q3,q4,q5,q6,context);
        insertAnswer(usnString,q1,q2,q3,q4,q5,q6,context);
    }

    @SuppressLint("WrongConstant")
    private void databaseStoreAnswer(String usn, String s1, String s2, String s3, String s4, String s5, String s6, Context context) {
        SQLiteDatabase db;
        db = context.openOrCreateDatabase( "aep.db", SQLiteDatabase.CREATE_IF_NECESSARY,null);
        try {
            final String drop="DROP TABLE IF EXISTS `answer`;";
            db.execSQL(drop);
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS `answer` (`usn` varchar(20) NOT NULL,`q1` varchar(300) NOT NULL,`q2` varchar(300) NOT NULL,`q3` varchar(300) NOT NULL,`q4` varchar(300) NOT NULL,`q5` varchar(300) NOT NULL,`q6` varchar(300) DEFAULT NULL, PRIMARY KEY (`usn`));";
            db.execSQL(CREATE_TABLE_CONTAIN);
            Toast.makeText(context, "table created ", Toast.LENGTH_LONG).show();
            String sql = "INSERT or replace INTO answer (usn,q1,q2,q3,q4,q5,q6) VALUES('"+usn+"','"+s1+"','"+s2+"','"+s3+"','"+s4+"','"+s5+"','"+s6+"');";
            db.execSQL(sql);

            @SuppressLint("Recycle")
            Cursor cursor = db.rawQuery("select * from `answer`",null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String usnr = cursor.getString(cursor.getColumnIndex("usn"));
                    String q1r = cursor.getString(cursor.getColumnIndex("q1"));
                    String q2r = cursor.getString(cursor.getColumnIndex("q2"));
                    String q3r = cursor.getString(cursor.getColumnIndex("q3"));
                    String q4r = cursor.getString(cursor.getColumnIndex("q4"));
                    String q5r = cursor.getString(cursor.getColumnIndex("q5"));
                    String q6r = cursor.getString(cursor.getColumnIndex("q6"));

                    String allData = usnr+System.getProperty("line.separator")+System.getProperty("line.separator")+q1r+System.getProperty("line.separator")+System.getProperty("line.separator")+q2r+System.getProperty("line.separator")+System.getProperty("line.separator")+q3r+System.getProperty("line.separator")+System.getProperty("line.separator")+q4r+System.getProperty("line.separator")+System.getProperty("line.separator")+q5r+System.getProperty("line.separator")+System.getProperty("line.separator")+q6r;

                    //TODO : remove while using
                    Toast.makeText(context, allData, Toast.LENGTH_LONG).show();
                    cursor.moveToNext();
                }
            }
        }
        catch (Exception e) {
            Toast.makeText(context, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void insertAnswer(String usnString, String q1, String q2, String q3, String q4, String q5, String q6, Context context) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constants.ROOT_URL)
                .build();
        StudentAPI api = adapter.create(StudentAPI.class);
        api.insertUser(
                usnString,
                q1,
                q2,
                q3,
                q4,
                q5,
                q6,
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
                        Toast.makeText(context, " Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
