package com.looigi.cambiolacarta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DialogMessaggio
{
    //-------- Singleton ----------//
    private static DialogMessaggio instance = null;
    private String Message;
    private boolean Error;
    private String titleDialog;
    private boolean Scelta;

    private DialogMessaggio() {
    }

    public static DialogMessaggio getInstance() {
        if (instance == null) instance = new DialogMessaggio();
        return instance;
    }

    //-------- Variables ----------//
    private Dialog dialog;

    //-------- Methods ----------//
    public void show(Context context, String message, boolean Error, String titleDialog, boolean Scelta)
    {
        this.Error=Error;
        this.titleDialog=titleDialog;
        this.Scelta = Scelta;

        Message = message;

        create(context);
    }

    private void create(Context context)
    {
        View inflate = View.inflate(context, R.layout.dialog_messaggio, null);
        TextView txtLog = (TextView) inflate.findViewById( R.id.dialog_tp_log);

        txtLog.setText(Message);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(inflate);
        builder.setTitle(titleDialog);
        if (Error) {
            builder.setIcon(R.drawable.error);
            builder.setTitle(titleDialog);
        } else {
            builder.setIcon(R.drawable.completed);
            builder.setTitle("looVF");
        }

        if (Scelta) {
            builder.setPositiveButton("Ok", onClickOK);
            builder.setNegativeButton("Annulla", onClickAnnulla);
        } else {
            builder.setPositiveButton("Ok", onClickOK);
        }

        if (VariabiliGlobali.getInstance().getActivityPrincipale()==null) {
            RefreshActivity.getInstance().RilanciaActivity();
        }

        if (VariabiliGlobali.getInstance().getActivityPrincipale()==null) {
            if (VariabiliGlobali.getInstance().getContext()!=null) {
                Toast.makeText(VariabiliGlobali.getInstance().getContext(),
                        Message,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            VariabiliGlobali.getInstance().getActivityPrincipale().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch (Exception ignored) {
                        Toast.makeText(VariabiliGlobali.getInstance().getContext(),
                                Message,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private OnClickListener onClickAnnulla = new OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.cancel();
        }
    };

    private OnClickListener onClickOK = new OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.cancel();
        }
    };
}
