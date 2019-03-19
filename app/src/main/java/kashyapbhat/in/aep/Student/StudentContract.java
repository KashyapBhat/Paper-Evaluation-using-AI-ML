package kashyapbhat.in.aep.Student;

import android.content.Context;

public interface StudentContract {
    interface View{
        void setDataFromSharedPreference(String usnString, String q1, String q2, String q3, String q4, String q5, String q6,int buttonClick);
    }

    interface Presenter{
        void saveSharedPrefernce(String usnString, String q1, String q2, String q3, String q4, String q5, String q6,int buttonClick);
        void getSharedPreference();
        void clearSharedPreference();
        void changeText(String sharedPrefName, String sharedText);
        void insertData(String usnString, String q1, String q2, String q3, String q4, String q5, String q6,Context context);
    }
}
