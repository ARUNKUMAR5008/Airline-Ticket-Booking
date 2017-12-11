package com.gitlab.airlineticketbooking;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gitlab.airlineticketbooking.data.ATBContract;
import com.gitlab.airlineticketbooking.data.ATBDbHelper;

import java.util.Objects;


public class BookItineraryActivity extends Activity {

    private TextView bookingInfo, pageTitle;
    private Button btnBook;
    int flightId, userId;
    String hideBookBtn;
    ATBDbHelper myHelper;
    SQLiteDatabase myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_itinerary);

        bookingInfo = (TextView)findViewById(R.id.txtBookingInfo);
        pageTitle = (TextView)findViewById(R.id.txtBookItinerary) ;
        btnBook = (Button)findViewById(R.id.btnBook);

        Intent intent  = getIntent();
        flightId = intent.getIntExtra("flightId", 0);
        userId = intent.getIntExtra("userId", 0);
        hideBookBtn = intent.getStringExtra("hideBook");

        myHelper = new ATBDbHelper(BookItineraryActivity.this);
        myDb = myHelper.getWritableDatabase();

        Cursor cursor = null ;
        cursor = myDb.rawQuery("SELECT * FROM " + ATBContract.Flight.TABLE_NAME
                + " WHERE " + ATBContract.Flight._ID + "=" + flightId, null);

        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                bookingInfo.setText("\n " +
                        "Flight Name : " + cursor.getString(1) + "\n" +
                        " Flight Number : " + cursor.getString(2) + "\n" +
                        " Origin : " + cursor.getString(3) + "\n" +
                        " Destination : " + cursor.getString(4) + "\n" +
                        " Departure Date : " + cursor.getString(5) + "\n" +
                        " Arrival Date : " + cursor.getString(6) + "\n" +
                        " Total Travel Cost : " + cursor.getString(7) + "\n" +
                        " Total Travel Time : " + cursor.getString(8) + "\n");
            }
        }

        if(Objects.equals(hideBookBtn, "yes")){
            pageTitle.setText("Your Ticket");
            btnBook.setVisibility(View.GONE);
        }
        btnBook.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   ContentValues values = new ContentValues();

                   values.put(ATBContract.BookingInfo.COLUMN_NAME_FLIGHT_ID, flightId );
                   values.put(ATBContract.BookingInfo.COLUMN_NAME_USER_ID, userId);
                   long isInserted;
                   isInserted = myDb.insert(ATBContract.BookingInfo.TABLE_NAME, null, values);

                   if (isInserted == -1){
                       Toast.makeText(BookItineraryActivity.this, "Something went wrong! Try again...", Toast.LENGTH_LONG).show();
                   }
                   else{
                       Toast.makeText(BookItineraryActivity.this, "You have successfully booked an itinerary", Toast.LENGTH_LONG).show();
                   }

               }
           }
        );

    }
}
