package com.example.bikerentalcu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI {

    private final WeakReference<Context> contextRef;
    private final String email;
    private final String subject;
    private final String message;
    private final String fromEmail = "walebike8@gmail.com";
    private final String fromPassword = "ticfhjalccaldqcq"; // Make sure this is a Gmail App Password

    private ProgressDialog progressDialog;

    public JavaMailAPI(Context context, String email, String subject, String message) {
        this.contextRef = new WeakReference<>(context);
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    public void sendEmail() {
        Context context = contextRef.get();
        if (context == null) return;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending booking confirmation...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            boolean success = false;
            Exception error = null;

            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, fromPassword);
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(fromEmail));
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);

                Log.d("JavaMailAPI", "Sending email to " + email + "...");
                Transport.send(mimeMessage);
                Log.d("JavaMailAPI", "Email sent successfully!");
                success = true;

            } catch (Exception e) {
                error = e;
                Log.e("JavaMailAPI", "Failed to send email", e);
            }

            boolean finalSuccess = success;
            Exception finalError = error;

            handler.post(() -> {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Context ctx = contextRef.get();
                if (ctx != null) {
                    if (finalSuccess) {
                        Toast.makeText(ctx, "Booking confirmation email sent", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ctx, "Failed to send booking confirmation", Toast.LENGTH_LONG).show();
                        if (finalError != null) {
                            Toast.makeText(ctx, "Error: " + finalError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        });
    }
}
