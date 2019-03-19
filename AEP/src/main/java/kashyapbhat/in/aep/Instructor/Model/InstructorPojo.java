package kashyapbhat.in.aep.Instructor.Model;

/**
 * Created by Kashyap Bhat on 17/02/19.
 */

public class InstructorPojo {

    private int qno;
    private String ques;
    private String keyword;
    private int tolMarks;
    private String text;


    public InstructorPojo(int qno, String ques, String keyword, int tolMarks, String text) {
        this.qno = qno;
        this.ques = ques;
        this.keyword = keyword;
        this.tolMarks = tolMarks;
        this.text = text;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getTolMarks() {
        return tolMarks;
    }

    public void setTolMarks(int tolMarks) {
        this.tolMarks = tolMarks;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQno() {
        return qno;
    }

    public void setQno(int qno) {
        this.qno = qno;
    }
}
