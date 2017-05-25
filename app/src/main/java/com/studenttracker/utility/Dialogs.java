package com.studenttracker.utility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studenttracker.R;
import com.studenttracker.interfaces.OnItemClickAdapter;
import com.studenttracker.interfaces.OnItemClickCostom;
import com.studenttracker.interfaces.OnItemClickInAdapter;

import java.util.List;

/**
 * Created by ravib on 23/05/2017.
 */
public class Dialogs {

    public interface DialogListener {
        void onDialogButtonClick(int id, Object o);
    }

    public static void showOkCancelDialog(Context context, String message, final OnItemClickInAdapter clickInAdapter) {


        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setMessage(message);

        alertbox.setCancelable(false);

        alertbox.setPositiveButton(context.getString(R.string.retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                clickInAdapter.onClickItems(0, 0, null);

                arg0.dismiss();
            }
        });
        alertbox.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertbox.show();


    }

    public static void updateAppDialog(Context context, String title, String message, boolean isCancelButton, final DialogListener dialogListener) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            alertbox.setTitle(title);
        }
        alertbox.setMessage(message);

        alertbox.setCancelable(false);

        if (isCancelButton) {
            alertbox.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        alertbox.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (dialogListener != null)
                    dialogListener.onDialogButtonClick(1, null);

                arg0.dismiss();
            }
        });
        alertbox.show();

    }

    //0 title
//1 message
//2 left button
//3 right button
    public static void confirmationDialog(Context context, final OnItemClickInAdapter clickInAdapter, String... params) {


        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setMessage(params[1]);
        alertbox.setTitle(params[0]);

        alertbox.setCancelable(false);


        if (params.length > 3) {

            alertbox.setNegativeButton(params[2], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    clickInAdapter.onClickItems(0, 0, null);
                    arg0.dismiss();
                }
            });
            alertbox.setPositiveButton(params[3], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    clickInAdapter.onClickItems(1, 0, null);

                    arg0.dismiss();
                }
            });

        } else {
            alertbox.setNeutralButton(params[2], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    clickInAdapter.onClickItems(0, 0, null);
                    arg0.dismiss();
                }
            });
        }
        alertbox.show();


    }

    @SuppressLint("NewApi")
    public static void showNetworkDialog(final Context context) {

        Dialog alertDialog = null;
        if (alertDialog != null && alertDialog.isShowing()) {
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
            alertbox.setMessage(context.getResources().getString(R.string.MSG_INTERNETERROR));
            alertbox.setTitle(context.getResources().getString(R.string.msgTitle));
            alertbox.setCancelable(false);
            //	alertbox.setTitle(Gravity.CENTER);
            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    context.startActivity(intent);*/

                    arg0.dismiss();
                }
            });


            alertDialog = alertbox.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            } else {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }


            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }


    @SuppressLint("NewApi")
    public static void showAlertDialog(final String title, final String message, final Context context) {
        Dialog alertDialog = null;
        if (alertDialog != null && alertDialog.isShowing()) {
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
            alertbox.setMessage(message);
            alertbox.setTitle(title);
            alertbox.setCancelable(false);
            //	alertbox.setTitle(Gravity.CENTER);
            alertbox.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    context.startActivity(intent);*/
                    arg0.dismiss();
                }
            });

            alertDialog = alertbox.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            } else {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }


            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
//
//    public static void showAlertDialogForCreateTeam(final String title, final String message, final Context context, final FixtureModels fixtureModels) {
//        Dialog alertDialog = null;
//        if (alertDialog != null && alertDialog.isShowing()) {
//        } else {
//            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
//            alertbox.setMessage(message);
//            alertbox.setTitle(title);
//            alertbox.setCancelable(false);
//            //	alertbox.setTitle(Gravity.CENTER);
//            alertbox.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//
//                    arg0.dismiss();
//                }
//            });
//
//            alertbox.setPositiveButton(context.getString(R.string.create_team), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                    Intent intent = new Intent(context, CreateTeamActivity.class);
//                    intent.putExtra("FixtureModels", fixtureModels);
//                    intent.putExtra("Edit", false);
//                    ((Activity) context).startActivityForResult(intent, 01);
//                    arg0.dismiss();
//                }
//            });
//
//            alertDialog = alertbox.create();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//            } else {
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            }
//
//
//            alertDialog.setCancelable(false);
//            alertDialog.show();
//        }
//    }

    @SuppressLint("NewApi")
    public static void showAlertDialog(final String title, final String message, final Context context, final OnItemClickCostom onClick) {
        Dialog alertDialog = null;
        if (alertDialog != null && alertDialog.isShowing()) {
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
            alertbox.setMessage(message);
            alertbox.setTitle(title);
            alertbox.setCancelable(false);
            //	alertbox.setTitle(Gravity.CENTER);
            alertbox.setNeutralButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    arg0.dismiss();
                    onClick.onClick(0, 0, null);
                }
            });

            alertDialog = alertbox.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            } else {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }


            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }


    public static Dialog getProgressesDialog(Context ct, String message) {
        Dialog dialog = new Dialog(ct, R.style.progressDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.progress_dialog_loader);
        dialog.setCanceledOnTouchOutside(false);

        TextView message_tv = (TextView) dialog.findViewById(R.id.message_tv);
      /*  if (null == message && message.equals("")) {
            message_tv.setVisibility(View.GONE);
        } else {
            message_tv.setText(message);
        }*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    public static void showListSelection(Context context, int titleResId, final
    OnItemClickAdapter onItemClickAdapter, final List<String> items) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(items);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != onItemClickAdapter) {
                    onItemClickAdapter.onClick(0, position, item);
                }
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();
    }
}
