package com.example.appproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class contact_us extends AppCompatActivity {

    Button call,email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        call=findViewById(R.id.callus);
        email=findViewById(R.id.emailus);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0549471626"));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","fix-it@firebase.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fix-It Support");
                emailIntent.putExtra(Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });
    }
}



