package com.looigi.cambiolacarta.acra;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.looigi.cambiolacarta.VariabiliGlobali;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.model.Element;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.sender.ReportSenderFactory;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.Locale;

public class CustomAcraSender implements ReportSenderFactory {
	private final String[] RECIPIENTS = {"looigi@gmail.com"};
	private Context ctx;

	public CustomAcraSender() {
	}

	@NonNull
	@Override
	public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration config) {
		ctx=context;
		return new CustomReportSender();
	}

	private class CustomReportSender implements ReportSender {
		@Override
		public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
			String subject = "Crash Cambia la Carta Android " + convertTimestampInDate(System.currentTimeMillis());
			String body = parseLog(errorContent);
			String recipients = TextUtils.join(",", RECIPIENTS);

			MailSender sender = new MailSender("looigi@gmail.com", "Piripacchio227!");
			try {
				sender.sendMail(subject, body, "looigi@gmail.com", recipients);
			} catch(Exception ignored) {
				// VariabiliGlobali.getInstance().getLog().ScriveLog(new Object(){}.getClass().getEnclosingMethod().getName(), "Invio messaggio di errore ACRA non riuscito");
				// VariabiliGlobali.getInstance().getLog().ScriveMessaggioDiErrore(e);
				// e.printStackTrace();
			}
		}

		private String convertTimestampInDate(long timestamp)
		{
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			calendar.setTimeInMillis(timestamp);
			return 	calendar.get(Calendar.DAY_OF_MONTH) + "-" +
					(calendar.get(Calendar.MONTH)+1) + "-" +
					calendar.get(Calendar.YEAR) + " " +
					calendar.get(Calendar.HOUR_OF_DAY) + ":" +
					calendar.get(Calendar.MINUTE) + ":" +
					calendar.get(Calendar.SECOND) + "." +
					calendar.get(Calendar.MILLISECOND) + " >";
		}

		private String parseLog(CrashReportData errorContent) {
			String s;
			StringBuilder strBuilder = new StringBuilder();
            String Giocatore="";

			strBuilder.append("---------------------------------------------------------\n");
			for(EnumMap.Entry<ReportField, Element> entry : errorContent.entrySet()) {
				strBuilder.append(entry.getKey());
				strBuilder.append(": ");
				strBuilder.append(entry.getValue());
				strBuilder.append("\n");
			}

			return strBuilder.toString();
		}
	}
}
