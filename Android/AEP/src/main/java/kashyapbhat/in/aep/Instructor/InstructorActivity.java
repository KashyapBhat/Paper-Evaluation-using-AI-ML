package kashyapbhat.in.aep.Instructor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kashyapbhat.in.aep.Common.RecognisedText;
import kashyapbhat.in.aep.Instructor.Model.InstructorPojo;
import kashyapbhat.in.aep.R;
import kashyapbhat.in.aep.Utils.ActivityUtils;

/**
 * Created by Kashyap Bhat on 14/02/19.
 */

public class InstructorActivity extends AppCompatActivity implements InstructorContract.View {

    private InstructorPresenter instructorPresenter;
    private Context context;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    InstructorPojo iP = new InstructorPojo(0,null,null,0,null);
    RecognisedText recognisedText = new RecognisedText("");

    @BindView(R.id.editTextBook)
    EditText editBook;

    @BindView(R.id.editqno)
    EditText editQno;

    @BindView(R.id.editques)
    EditText editQues;

    @BindView(R.id.editkey)
    EditText editKey;

    @BindView(R.id.edittot)
    EditText editTot;

    @BindView(R.id.resultImage)
    ImageView resultImage;

    @OnClick(R.id.buttonText)
    void getTextBook()
    {
        setData();
        instructorPresenter.saveSharedPrefernce(iP.getQno(), iP.getQues(), iP.getKeyword(), iP.getTolMarks(), iP.getText());
//        ActivityUtils.startNewActivity(context, Constants.scannerPakageUri);
//        callIntent();
        callCamera();
    }

    @OnClick(R.id.buttonDoneInstructor)
    void finishDone()
    {
        setData();
        instructorPresenter.saveSharedPrefernce(iP.getQno(), iP.getQues(), iP.getKeyword(), iP.getTolMarks(), iP.getText());
        instructorPresenter.insertData(iP.getQno(), iP.getQues(), iP.getKeyword(), iP.getTolMarks(), iP.getText());
        ActivityUtils.clearForm(findViewById(R.id.layout_instructor));
        iP = new InstructorPojo(0,null,null,0,null);
        instructorPresenter.clearSharedPreference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        ButterKnife.bind(this);
        context=getApplicationContext();
        instructorPresenter = new InstructorPresenter(this,context);
        getPermissions();
//        callIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Logger.d("Inside Open Camera ");
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) (extras != null ? extras.get("data") : "");
            Picasso.with(context).load(ActivityUtils.getImageUri(context,Objects.requireNonNull(bitmap))).into(resultImage);
            ActivityUtils.runTextRecognition(bitmap,context,recognisedText);
            processAfterGettingData(recognisedText.getResultText());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        callIntent();
    }

    private void processAfterGettingData(String sharedText) {
        if (sharedText != null) {
            iP.setText(sharedText);
            instructorPresenter.changeText(iP.getText());
            instructorPresenter.getSharedPreference();
            setFields();
        }
    }

    public void setFields() {
        if(iP.getQno()!=0)
        editQno.setText(String.valueOf(iP.getQno()));
        editQues.setText(iP.getQues());
        editKey.setText(iP.getKeyword());

        if(iP.getTolMarks()!=0)
        editTot.setText(String.valueOf(iP.getTolMarks()));
        editBook.setText(iP.getText());
    }

    public void setData() {
        String qno = editQno.getText().toString();
        iP.setQno(ActivityUtils.ConvertIntoNumeric(qno));
        iP.setQues(editQues.getText().toString());
        iP.setKeyword(editKey.getText().toString());
        String tot = editTot.getText().toString();
        iP.setTolMarks(ActivityUtils.ConvertIntoNumeric(tot));
        iP.setText(editBook.getText().toString());
    }

    public void setDataFromSharedPreference(int qno, String ques, String keyword, int tolMarks, String text){
        iP.setQno(qno);
        iP.setQues(ques);
        iP.setKeyword(keyword);
        iP.setTolMarks(tolMarks);
        iP.setText(text);

//        TODO : remove while using
//        String allData = iP.getQno()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getQues()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getKeyword()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getTolMarks()+System.getProperty("line.separator")+System.getProperty("line.separator")+iP.getText();
//        Toast.makeText(context, allData, Toast.LENGTH_LONG).show();
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