package kashyapbhat.in.aep.Instructor;

public interface InstructorContract {
        interface View{
            void setDataFromSharedPreference(int qno, String ques, String keyword, int tolMarks, String text);
        }

        interface Presenter {
            void saveSharedPrefernce(int qno, String ques, String keyword, int tolMarks, String text);
            void getSharedPreference();
            void clearSharedPreference();
            void changeText(String sharedText);
            void insertData(int qno, String ques, String keyword, int tolMarks, String text);
        }
}
