package com.rayn.utility;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rayn.R;
import com.rayn.interfaces.OnItemClickCustom;
import com.rayn.models.StudentsModel;
import com.rayn.models.TeachersClassModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ravib on 05/20/2017.
 */
public class Functions extends Application {

    private ArrayList<StudentsModel> mStudentsAL;
    private StudentsModel mStudent;

    public ArrayList<StudentsModel> getmStudentsAL() {
        return mStudentsAL;
    }

    public void setmStudentsAL(ArrayList<StudentsModel> mStudentsAL) {
        this.mStudentsAL = mStudentsAL;
    }

    public StudentsModel getmStudent() {
        return mStudent;
    }

    public void setmStudent(StudentsModel mStudent) {
        this.mStudent = mStudent;
    }

    private ArrayList<TeachersClassModel> mTeachersClassAL;

    public ArrayList<TeachersClassModel> getmTeachersClassAL() {
        return mTeachersClassAL;
    }

    public void setmTeachersClassAL(ArrayList<TeachersClassModel> mTeachersClassAL) {
        this.mTeachersClassAL = mTeachersClassAL;
    }

    private static TeachersClassModel Teacher;

    public TeachersClassModel getTeacher() {
        return Teacher;
    }

    public void setTeacher(TeachersClassModel mTeacher) {
        this.Teacher = mTeacher;
    }

    public static Functions instance = null;

    public static Functions getInstance() {

        if (instance == null) {
            instance = new Functions();

        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static void showOKDialog(Context context, String title, String message, final OnItemClickCustom onItemClickCustom) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            alertbox.setTitle(title);
        }
        alertbox.setMessage(message);
        alertbox.setCancelable(false);
        alertbox.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (null != onItemClickCustom) {
                    onItemClickCustom.onClick(0, 0, 0);
                }
                arg0.dismiss();

            }
        });
        alertbox.show();
    }

    public static void showListSelectionDialog(Context context, String title, ArrayList<String> items,
                                               final OnItemClickCustom onItemClickCustom) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            alertbox.setTitle(title);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(items);

        alertbox.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != onItemClickCustom) {
                    onItemClickCustom.onClick(position, position, item);

                }

            }
        });

        alertbox.show();
    }

    public void displayRoundImage(final Context context, String image_url, final ImageView imageView) {

        Glide.with(context)
                .load(image_url)
                .asBitmap()
                .thumbnail(0.3f)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

    }

    public void displayImagePlain(Context context, String image_url, boolean isThumb, ImageView imageView) {

        if (isThumb) {
            Glide.with(context)
                    .load(image_url)
                    .asBitmap()
                    .sizeMultiplier(0.5f).
                    thumbnail(0.2f)

                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(image_url)
                    .asBitmap()
                    .thumbnail(0.2f)

                    .into(imageView);
        }

    }

    public void displayImage(Context context, String image_url, boolean isThumb, ImageView imageView) {

        if (isThumb) {
            Glide.with(context)
                    .load(image_url)
                    .asBitmap()
                    .sizeMultiplier(0.5f).
                    thumbnail(0.2f)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(image_url)
                    .asBitmap()
                    .thumbnail(0.2f)
                    .into(imageView);
        }

    }


    public void setFontNormal(String fontName, View... view) {
        if (null != view) {
            Typeface robotoBold = Typeface.createFromAsset(view[0].getContext().getAssets(), fontName);

            for (int i = 0; i < view.length; i++) {
                if (view[i] instanceof TextView) {
                    ((TextView) view[i]).setTypeface(robotoBold);
                } else if (view[i] instanceof EditText) {
                    ((EditText) view[i]).setTypeface(robotoBold);
                } else if (view[i] instanceof RadioButton) {
                    ((RadioButton) view[i]).setTypeface(robotoBold);
                } else if (view[i] instanceof CheckBox) {
                    ((CheckBox) view[i]).setTypeface(robotoBold);
                } else if (view[i] instanceof AutoCompleteTextView) {
                    ((AutoCompleteTextView) view[i]).setTypeface(robotoBold);
                } else if (view[i] instanceof Button) {
                    ((Button) view[i]).setTypeface(robotoBold);
                }
            }
        }
    }

    public void showToast(Context ctx, String msg, boolean isShort) {
        if (!msg.equals("")) {
            Toast.makeText(ctx, msg, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        }
    }

    public HashMap getHashmapObject(String... nameValuePair) {
        HashMap HashMap = null;

        if (null != nameValuePair && nameValuePair.length % 2 == 0) {

            HashMap = new HashMap<>();

            int i = 0;
            while (i < nameValuePair.length) {
                HashMap.put(nameValuePair[i], nameValuePair[i + 1]);
                i += 2;
            }

        }

        return HashMap;
    }

    public static String formatDecimal(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat formatter = (DecimalFormat) nf;
        return formatter.format(d);
    }

    public JsonArray getJsonArray(String... nameValuePair) {

        JsonArray jsonElements = new JsonArray();

        JsonObject jsonObject;
        if (null != nameValuePair && nameValuePair.length % 2 == 0) {
            int i = 0;
            while (i < nameValuePair.length) {
                jsonObject = new JsonObject();
                jsonObject.addProperty(nameValuePair[i], nameValuePair[i + 1]);
                jsonElements.add(jsonObject);
                i += 2;
            }

        }
        return jsonElements;
    }

    public JsonObject getJsonObject(String... nameValuePair) {
        JsonObject HashMap = null;
        if (null != nameValuePair && nameValuePair.length % 2 == 0) {
            HashMap = new JsonObject();
            int i = 0;
            while (i < nameValuePair.length) {
                HashMap.addProperty(nameValuePair[i], nameValuePair[i + 1]);
                i += 2;
            }

        }

        return HashMap;
    }

    public String formateDacimelUpto_one_digit(double number) {
        DecimalFormat f = new DecimalFormat("##.0");
        System.out.println(f.format(number));
        return f.format(number);
    }

    public String readStreamFully(long len, InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        long readCount = 0;
        int progress = 0, prevProgress = 0;

        String currLine = null;
        try {
			/* Read until all response is read */
            while ((currLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(currLine + "\n");
                readCount += currLine.length();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public void setUnderLine(TextView tv) {
        SpannableString content = new SpannableString(tv.getText().toString().trim());
        content.setSpan(new UnderlineSpan(), 0, tv.getText().toString().trim().length(), 0);
        tv.setText(content);
    }

    public String toTitleCase(String str){
        if (str == null) {
            return "";
        }
        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();
        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    public String formatDate(String date,String formatInput,String formatOutput){
        String dateToDisplay = "";
        SimpleDateFormat sdfInput = new SimpleDateFormat(formatInput);
        SimpleDateFormat sdfOutput = new SimpleDateFormat(formatOutput);
        try {
            Date dateInput = sdfInput.parse(date);
            dateToDisplay = sdfOutput.format(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToDisplay;
    }
}