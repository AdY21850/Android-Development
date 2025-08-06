package com.example.bikerentalcu;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class confirm_bike extends AppCompatActivity implements PaymentResultListener {

    private TextView bikeName, bikePrice, quantityText, totalPriceText, totalAmountText, locationText;
    private ImageView bikeImage;
    private EditText editDays;
    private Button confirmBtn;
    private TextView startDateText, endDateText;

    private CartItem cartItem;
    private String userName;
    private User user;
    private int quantity = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bike);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Checkout.preload(getApplicationContext());

        initializeViews();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        cartItem = getIntent().getParcelableExtra("cartItems");
        quantity = getIntent().getIntExtra("quantity", 1);

        if (cartItem != null) {
            populateData(cartItem, quantity);
        }

        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));

        confirmBtn.setOnClickListener(v -> {
            String location = locationText.getText().toString();
            String startDate = startDateText.getText().toString();
            String endDate = endDateText.getText().toString();
            String amount = totalAmountText.getText().toString().replaceAll("[^0-9]", "");
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String userName = prefs.getString("userName", "Guest");

            if (TextUtils.isEmpty(location) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            startRazorpayPayment(
                    location,
                    startDate,
                    endDate,
                    amount,
                    userName,
                    cartItem.getOwnerEmail(),
                    cartItem.getName(),
                    cartItem.getOwnerContact(),
                    cartItem.getownerupi()
            );
        });
    }

    private void initializeViews() {
        bikeName = findViewById(R.id.bikeName);
        bikePrice = findViewById(R.id.bikePrice);
        quantityText = findViewById(R.id.selectedCount);
        bikeImage = findViewById(R.id.bikeImage);
        totalPriceText = findViewById(R.id.totalPrice);
        totalAmountText = findViewById(R.id.totalAmount);
        editDays = findViewById(R.id.editDays);
        confirmBtn = findViewById(R.id.confirmBtn);
        locationText = findViewById(R.id.locationText);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
    }

    private void populateData(CartItem item, int quantity) {
        bikeName.setText(item.getName());
        bikePrice.setText("₹" + item.getPrice());
        quantityText.setText("Selected: " + quantity);
        Glide.with(this).load(item.getImageUrl()).into(bikeImage);
    }

    private void calculateAndDisplayPrice(CartItem item, int quantity, int days) {
        int price = item.getPrice() * quantity * days;
        int driverFee = 5;
        int totalAmount = price + driverFee;

        totalPriceText.setText( "₹" + price);
        totalAmountText.setText("₹" + totalAmount);
    }

    private void showDatePickerDialog(TextView targetTextView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year1, month1, dayOfMonth);
                    String selectedDate = dateFormat.format(selectedCal.getTime());
                    targetTextView.setText(selectedDate);

                    String startDateStr = startDateText.getText().toString();
                    String endDateStr = endDateText.getText().toString();

                    if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                        try {
                            Date start = dateFormat.parse(startDateStr);
                            Date end = dateFormat.parse(endDateStr);
                            long diff = Objects.requireNonNull(end).getTime() - Objects.requireNonNull(start).getTime();
                            int days = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
                            if (days > 0) {
                                editDays.setText(String.valueOf(days));
                                calculateAndDisplayPrice(cartItem, quantity, days);
                            } else {
                                editDays.setText("");
                                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        setAddressFromLocation(location);
                    } else {
                        locationText.setText("Unable to fetch location");
                    }
                });
    }

    private void setAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                locationText.setText(addressLine);
            } else {
                locationText.setText("Address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationText.setText("Geocoder error");
        }
    }

    private void startRazorpayPayment(String location, String startDate, String endDate, String amount,
                                      String userName, String ownerEmail, String bikeName,
                                      String ownerPhone, String ownerUpi) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_aj3ooR3fh3476A");

        try {
            int amountInPaise = Integer.parseInt(amount) * 100;

            JSONObject options = new JSONObject();
            options.put("name", "Bike Rental");
            options.put("description", "Rental for " + bikeName);
            options.put("currency", "INR");
            options.put("amount", amountInPaise);

            JSONObject prefill = new JSONObject();
            prefill.put("email", ownerEmail);
            prefill.put("contact", ownerPhone);

            options.put("prefill", prefill);

            checkout.open(this, options);

        } catch (Exception e) {
            Log.e("Razorpay Error", "Error in payment: ", e);
            Toast.makeText(this, "Payment setup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        sendBookingEmail();
        sendBookingEmail2();
        sendWhatsAppMessage();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
    }

    private void sendBookingEmail() {

        Log.d("mail sent ","started");

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userName = prefs.getString("userName", "Guest");
        String subject = "New Booking Confirmation - " + cartItem.getName();
        String message = "Hello,\n\nYou have received a new booking:\n\n"
                + "User: " + userName + "\n"
                + "Bike: " + cartItem.getName() + "\n"
                + "Location: " + locationText.getText().toString() + "\n"
                + "Start Date: " + startDateText.getText().toString() + "\n"
                + "End Date: " + endDateText.getText().toString() + "\n"
                + "Total Amount: ₹" + totalAmountText.getText().toString().replaceAll("[^0-9]", "") + "\n\n"
                + "Regards,\nBike Rental App";

        new JavaMailAPI(this, cartItem.getOwnerEmail(), subject, message).sendEmail();
        Toast.makeText(this, "bike rented for " + startDateText + " to " + endDateText, Toast.LENGTH_SHORT).show();

        Log.d("mail sent ","successfull");
    }

    private void sendBookingEmail2() {

        Log.d("mail sent ","started");

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userName = prefs.getString("userName", "Guest");
        String subject = "New Booking Confirmation - " + cartItem.getName();
        String message = "Hello,\n\nyou're booking for "+cartItem.getName()+"is confirmed:\n\n"
                + "owner: " + cartItem.getOwnerName() + "\n"
                + "Location: " + locationText.getText().toString() + "\n"
                + "Start Date: " + startDateText.getText().toString() + "\n"
                + "End Date: " + endDateText.getText().toString() + "\n"
                + "Total Amount: ₹" + totalAmountText.getText().toString().replaceAll("[^0-9]", "") + "\n\n"
                + "Regards,\nBike Rental App";

        new JavaMailAPI(this, cartItem.getusermail(), subject, message).sendEmail();
        Toast.makeText(this, "bike rented for " + startDateText + " to " + endDateText, Toast.LENGTH_SHORT).show();

        Log.d("mail sent ","successfull");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                locationText.setText("Location permission denied");
            }
        }
    }

    public void sendWhatsAppMessage() {
        OkHttpClient client = new OkHttpClient();

        String ACCOUNT_SID = "ACfdd86d04713f926981588a11422fe947";
        String AUTH_TOKEN = "1a3e9a177ae36b31bc63657c8059b019";
        String FROM_WHATSAPP = "whatsapp:+14155238886"; // Twilio sandbox WhatsApp number
        String TO_WHATSAPP = "whatsapp:" + cartItem.getOwnerContact();

        String messageBody = "Hello,\n\nYou have a new bike booking:\n"
                + "User: " +  userName + "\n"
                + "Bike: " + bikeName + "\n"
                + "Start Date: " + startDateText.getText().toString() + "\n"
                + "End Date: " + endDateText.getText().toString()+ "\n"
                + "Location: " +  locationText.getText().toString() + "\n"
                + "Total Amount: ₹" + totalAmountText.getText().toString().replaceAll("[^0-9]", "") + "\n\n"
                + "Please contact the user for further coordination.\n\nBike Rental App";

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "From=" + FROM_WHATSAPP
                + "&To=" + TO_WHATSAPP
                + "&Body=" + messageBody);

        Request request = new Request.Builder()
                .url("https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json")
                .post(body)
                .addHeader("Authorization", Credentials.basic(ACCOUNT_SID, AUTH_TOKEN))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        new Thread(() -> {
            try {
                okhttp3.Response response = client.newCall(request).execute();
                Log.d("Twilio", "Message Sent: " + response.body().string());
            } catch (Exception e) {
                Log.e("Twilio", "Error: ", e);
            }
        }).start();
    }
}
