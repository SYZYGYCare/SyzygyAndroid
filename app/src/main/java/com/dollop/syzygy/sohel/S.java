package com.dollop.syzygy.sohel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.ReminderListActivity;

import java.io.File;

public class S {
    public static void I(Context cx, Class<?> startActivity, Bundle data) {
        Intent i = new Intent(cx, startActivity);
        if (data != null)
            i.putExtras(data);
        cx.startActivity(i);
    }

    public static void I_clear(Context cx, Class<?> startActivity, Bundle data) {
        Intent i = new Intent(cx, startActivity);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data != null)
            i.putExtras(data);
        cx.startActivity(i);
    }

    public static void E(String msg) {
        if (true)
            Log.e("Log.E By Sohel", msg);
    }

    public static void Snack(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void newRemainderDialog(final Context cx) {
        AlertDialog.Builder adb = new AlertDialog.Builder(cx);
        adb.setTitle("Reminder");
        adb.setMessage("Click OK to view Reminder!");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                S.I(cx, ReminderListActivity.class, null);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }




    public static void noInternetDialog(final Context cx) {
        final Dialog dialog = new Dialog(cx);
        dialog.setContentView(R.layout.no_internet_dialog_box);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button codPopUpBtn = (Button) dialog.findViewById(R.id.dialogOkBtn);
        codPopUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static Dialog initProgressDialog(Context c) {
        /*ProgressDialog pd = new ProgressDialog(c);
        pd.setMessage("loading");
        pd.show();
        return pd;*/
        Dialog dialog = new Dialog(c);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public static void T(Context c, String msg) {
        if(null!=c){
            Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
        }

    }

    /*public static void Snack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        snackbar.show();
    }
*/
    public static void share(Context c, String subject, String shareBody) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        c.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void shareFile(Context c, File file, String subject, String shareBody) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if (file.exists()) {
            intentShareFile.setType("application/image");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, shareBody);
            c.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    public static void showSnackBar(Activity activity, String message) {
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, 5).show();
    }
}