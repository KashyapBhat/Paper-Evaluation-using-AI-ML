package kashyapbhat.in.aep.Student;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.logger.Logger;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kashyapbhat.in.aep.Common.RecognisedText;
import kashyapbhat.in.aep.R;
import kashyapbhat.in.aep.Student.Model.StudentPojo;
import kashyapbhat.in.aep.Utils.ActivityUtils;

/**
 * Created by Kashyap Bhat on 14/02/19.
 */

public class StudentActivity extends AppCompatActivity implements StudentContract.View {
    //TODO: Add url
    private static final String ROOT_URL = "";
    private StudentContract.Presenter presenter;
    private Context context;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    RecognisedText recognisedText = new RecognisedText("");
    StudentPojo sP = new StudentPojo(null,null,null,null,null,null,null,0);

    @BindView(R.id.usn)
    EditText usn;

    @BindView(R.id.q1)
    EditText q1;

    @BindView(R.id.q2)
    EditText q2;

    @BindView(R.id.q3)
    EditText q3;

    @BindView(R.id.q4)
    EditText q4;

    @BindView(R.id.q5)
    EditText q5;

    @BindView(R.id.q6)
    EditText q6;

    @OnClick(R.id.b1)
    void ques1()
    {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),1);
        //ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
        callCamera();
    }

    @OnClick(R.id.b2)
    void ques2()
    {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),2);
        //ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
        callCamera();
    }

    @OnClick(R.id.b3)
    void ques3()
    {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),3);
        //ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
        callCamera();
    }

    @OnClick(R.id.b4)
    void ques4()
    {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),4);
        //ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
        callCamera();
    }

    @OnClick(R.id.b5)
    void ques5()
    {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),5);
        //ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
        callCamera();
    }

    @OnClick(R.id.b6)
    void ques6()
    {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),6);
        //ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
        callCamera();
    }

    @OnClick(R.id.buttonDone)
    void submit() {
        setData();
        presenter.saveSharedPrefernce(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),0);
        presenter.insertData(sP.getUsn(),sP.getsQ1(),sP.getsQ2(),sP.getsQ3(),sP.getsQ4(), sP.getsQ5(),sP.getsQ6(),context);
        ActivityUtils.clearForm(findViewById(R.id.layout_answer));
        sP = new StudentPojo(null,null,null,null,null,null,null,0);
        presenter.clearSharedPreference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        context=getApplicationContext();
        presenter = new StudentPresenter(this,context);
        getPermissions();
//        callIntent();
    }

    private void callIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                processAfterGettingData(sharedText);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            ActivityUtils.runTextRecognition(bitmap,context, recognisedText);
            processAfterGettingData(recognisedText.getResultText());
        }
    }

    private void processAfterGettingData(String sharedText) {
        if (sharedText != null) {
            int buttonClick = sP.getBno();
            Logger.d("Button click number : "+buttonClick);
            switch (buttonClick)
            {
                case 1:
                    sP.setsQ1(sharedText);
                    presenter.changeText("qa1",sP.getsQ1());
                    presenter.getSharedPreference();
                    setText();
                    break;
                case 2:
                    sP.setsQ2(sharedText);
                    presenter.changeText("qa2",sP.getsQ2());
                    presenter.getSharedPreference();
                    setText();
                    break;
                case 3:
                    sP.setsQ3(sharedText);
                    presenter.changeText("qa3",sP.getsQ3());
                    presenter.getSharedPreference();
                    setText();
                    break;
                case 4:
                    sP.setsQ4(sharedText);
                    presenter.changeText("qa4",sP.getsQ4());
                    presenter.getSharedPreference();
                    setText();
                    break;
                case 5:
                    sP.setsQ5(sharedText);
                    presenter.changeText("qa5",sP.getsQ5());
                    presenter.getSharedPreference();
                    setText();
                    break;
                case 6:
                    sP.setsQ6(sharedText);
                    presenter.changeText("qa6",sP.getsQ6());
                    presenter.getSharedPreference();
                    setText();
                    break;
                case 0: Toast.makeText(context, "Data fetch incomplete", Toast.LENGTH_SHORT).show();
                    break;
                default: Logger.e("Button click " + buttonClick + " Not recognised properly" + sharedText );
                    break;
            }
        }
    }

    private void setData() {
        sP.setUsn(usn.getText().toString());
        sP.setsQ1(q1.getText().toString());
        sP.setsQ2(q2.getText().toString());
        sP.setsQ3(q3.getText().toString());
        sP.setsQ4(q4.getText().toString());
        sP.setsQ5(q5.getText().toString());
        sP.setsQ6(q6.getText().toString());
    }

    private void setText(){
        usn.setText(sP.getUsn());
        q1.setText(sP.getsQ1());
        q2.setText(sP.getsQ2());
        q3.setText(sP.getsQ3());
        q4.setText(sP.getsQ4());
        q5.setText(sP.getsQ5());
        q6.setText(sP.getsQ6());
    }

    public void setDataFromSharedPreference(String usnString, String q1, String q2, String q3, String q4, String q5, String q6,int buttonClick){
        sP.setUsn(usnString);
        sP.setsQ1(q1);
        sP.setsQ2(q2);
        sP.setsQ3(q3);
        sP.setsQ4(q4);
        sP.setsQ5(q5);
        sP.setsQ6(q6);
        sP.setBno(buttonClick);

//        TODO : remove while using
//        String allData = iP.getQno()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getQues()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getKeyword()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getTolMarks()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getText();
//        Toast.makeText(context, allData, Toast.LENGTH_LONG).show();
    }

    private void callCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
        }
    }

    private void getPermissions() {
        MultiplePermissionsListener dialog =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(this)
                        .withTitle("Camera & Storage Permission")
                        .withMessage("Please provide camera and storage permission to take pictures.")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(dialog).check();
    }
}
