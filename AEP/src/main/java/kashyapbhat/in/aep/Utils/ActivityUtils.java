package kashyapbhat.in.aep.Utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

import kashyapbhat.in.aep.Common.RecognisedText;

/**
 * Created by Kashyap Bhat on 14/02/19.
 */

public class ActivityUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int ConvertIntoNumeric(String val)
    {
        try
        {
            return Integer.parseInt(val);
        }
        catch(Exception ex)
        {
            return 0;
        }
    }

    public static void startNewActivity(Context context, String packageName) {
        if(isOnline(context))
        {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent == null) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
        else {
            Toast.makeText(context, "Please connect to Internet ", Toast.LENGTH_SHORT).show();
        }

//        For jumping from one activity to other
//
//        Intent i = new Intent(InstructorActivity.this, HomeActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public static void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image with text", null);
        return Uri.parse(path);
    }

    public static void runTextRecognition(Bitmap bitmap, Context context, RecognisedText recognisedText) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(firebaseVisionText -> {
                            processTextRecognitionResult( firebaseVisionText, context); //for further computation if required.
                        })
                        .addOnFailureListener(
                                e -> {
                                    // Task failed with an exception
                                    // ...
                                });
        while (!result.isComplete());
        if(result.isComplete()) {
            Logger.d("Inside MLKit Firebase : " + Objects.requireNonNull(result.getResult()).getText());
            Toast.makeText(context, "Inside MLKit Firebase : " + Objects.requireNonNull(result.getResult()).getText(), Toast.LENGTH_LONG).show();
            recognisedText.setResultText(Objects.requireNonNull(result.getResult()).getText());
        }
    }

    public static void processTextRecognitionResult(FirebaseVisionText texts, Context context) {
        String result = texts.getText();
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(context, "No text found", Toast.LENGTH_SHORT).show();
            return;
        }else {
            for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()) {
                String blockText = block.getText();
                Float blockConfidence = block.getConfidence();
                List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
                for (FirebaseVisionText.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Float lineConfidence = line.getConfidence();
                    List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
                    for (FirebaseVisionText.Element element : line.getElements()) {
                        String elementText = element.getText();
                        Float elementConfidence = element.getConfidence();
                        Toast.makeText(context, elementText + elementConfidence, Toast.LENGTH_SHORT).show();
                        List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                        Point[] elementCornerPoints = element.getCornerPoints();
                        Rect elementFrame = element.getBoundingBox();
                    }
                }
            }
        }
    }

    public static void runCloudTextRecognition(Bitmap bitmap, Context context, RecognisedText recognisedText) {
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(25)
                        .build();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(result -> {
                    Logger.d(result.getText());
                    Toast.makeText(context, "Inside MLKit Firebase : "+result.getText(), Toast.LENGTH_LONG).show();
                    recognisedText.setResultText(result.getText());
                    processCloudTextRecognitionResult(result, context); //for further computation if required.
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public static void processCloudTextRecognitionResult(FirebaseVisionDocumentText texts,Context context) {
        if (texts == null) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String result = texts.getText();
            for (FirebaseVisionDocumentText.Block block: texts.getBlocks()) {
                String blockText = block.getText();
                Float blockConfidence = block.getConfidence();
                List<RecognizedLanguage> blockRecognizedLanguages = block.getRecognizedLanguages();
                Rect blockFrame = block.getBoundingBox();
                for (FirebaseVisionDocumentText.Paragraph paragraph: block.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    Float paragraphConfidence = paragraph.getConfidence();
                    List<RecognizedLanguage> paragraphRecognizedLanguages = paragraph.getRecognizedLanguages();
                    Rect paragraphFrame = paragraph.getBoundingBox();
                    for (FirebaseVisionDocumentText.Word word: paragraph.getWords()) {
                        String wordText = word.getText();
                        Float wordConfidence = word.getConfidence();
                        List<RecognizedLanguage> wordRecognizedLanguages = word.getRecognizedLanguages();
                        Rect wordFrame = word.getBoundingBox();
                        for (FirebaseVisionDocumentText.Symbol symbol: word.getSymbols()) {
                            String symbolText = symbol.getText();
                            Float symbolConfidence = symbol.getConfidence();
                            List<RecognizedLanguage> symbolRecognizedLanguages = symbol.getRecognizedLanguages();
                            Rect symbolFrame = symbol.getBoundingBox();
                        }
                    }
                }
            }
        }
    }
}
