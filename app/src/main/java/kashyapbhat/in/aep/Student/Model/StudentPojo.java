package kashyapbhat.in.aep.Student.Model;

/**
 * Created by Kashyap Bhat on 17/02/19.
 */

public class StudentPojo {
    private String usn,sQ1,sQ2,sQ3,sQ4,sQ5,sQ6;
    private int bno;

    public StudentPojo(String usn, String sQ1, String sQ2, String sQ3, String sQ4, String sQ5, String sQ6,int bno) {
        this.usn = usn;
        this.sQ1 = sQ1;
        this.sQ2 = sQ2;
        this.sQ3 = sQ3;
        this.sQ4 = sQ4;
        this.sQ5 = sQ5;
        this.sQ6 = sQ6;
        this.bno = bno;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getsQ1() {
        return sQ1;
    }

    public void setsQ1(String sQ1) {
        this.sQ1 = sQ1;
    }

    public String getsQ2() {
        return sQ2;
    }

    public void setsQ2(String sQ2) {
        this.sQ2 = sQ2;
    }

    public String getsQ3() {
        return sQ3;
    }

    public void setsQ3(String sQ3) {
        this.sQ3 = sQ3;
    }

    public String getsQ4() {
        return sQ4;
    }

    public void setsQ4(String sQ4) {
        this.sQ4 = sQ4;
    }

    public String getsQ5() {
        return sQ5;
    }

    public void setsQ5(String sQ5) {
        this.sQ5 = sQ5;
    }

    public String getsQ6() {
        return sQ6;
    }

    public void setsQ6(String sQ6) {
        this.sQ6 = sQ6;
    }

    public int getBno() {
        return bno;
    }

    public void setBno(int bno) {
        this.bno = bno;
    }
}
