package com.raynsmartschool.utility;

import android.app.Activity;

import com.raynsmartschool.R;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

public class DialogUtil {


/*
    public static void showOkDialog(final Context context, int titleResId,
                                    int msgResId) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.ok_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView titleTv = (TextView) dialog.findViewById(R.id.title_tv);
        if (titleResId != 0) {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(titleResId);
        }
        TextView messageTv = (TextView) dialog.findViewById(R.id.message_tv);
        messageTv.setText(msgResId);

        TextView okBtn = (TextView) dialog.findViewById(R.id.ok_btn);
        okBtn.setTag(dialog);
        okBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
*/

    public enum AlertType {
        Error,
        Success
    }
    //DialogUtil.Alert(this, getString(R.string.password_required), Functions.AlertType.Error);

    public static void Alert(Activity activity, String message, AlertType alertType, OnHideAlertListener onHideAlertListener) {
        if (alertType == AlertType.Error) {
            Alerter.create(activity).setText(message)
                    .setIcon(R.drawable.ic_error_outline_white_24dp)
                    .setBackgroundColor(R.color.red_500).setOnHideListener(onHideAlertListener).show();

        } else if (alertType == AlertType.Success)
            Alerter.create(activity).setText(message).setBackgroundColor(R.color.Accpeted).setIcon(R.drawable.ic_done_white_24dp).setOnHideListener(onHideAlertListener).show();
    }

    public static void Alert(Activity activity, String message, AlertType alertType) {
        if (alertType == AlertType.Error) {
            Alerter.create(activity).setText(message)
                    .setIcon(R.drawable.ic_error_outline_white_24dp)
                    .setBackgroundColor(R.color.translucent_50).show();

        } else if (alertType == AlertType.Success)
            Alerter.create(activity).setText(message).setBackgroundColor(R.color.Accpeted).setIcon(R.drawable.ic_done_white_24dp).show();
    }


   /* public static void showOkDialog(final Context context, String titleResId,
                                    String msgResId, OnClickListener clickListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.ok_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView titleTv = (TextView) dialog.findViewById(R.id.title_tv);
        if (titleResId.equalsIgnoreCase("")) {
            titleTv.setVisibility(View.GONE);
        } else {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(titleResId);
        }

        TextView messageTv = (TextView) dialog.findViewById(R.id.message_tv);
        messageTv.setText(msgResId);

        TextView okBtn = (TextView) dialog.findViewById(R.id.ok_btn);
        okBtn.setTag(dialog);
        okBtn.setOnClickListener(clickListener);
        dialog.show();
    }
*/
}