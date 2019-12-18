package com.example.onurb.playlistrecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MoodSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout happy_layout, calm_layout, loving_layout,
            alone_layout, energetic_layout, melancholy_layout,
            nostalgic_layout, aggressive_layout, night_layout;

    private Intent list_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_select);

        // Getting the layouts //
        happy_layout = (LinearLayout) findViewById(R.id.ll1_1_ms);
        calm_layout = (LinearLayout) findViewById(R.id.ll1_2_ms);
        loving_layout = (LinearLayout) findViewById(R.id.ll1_3_ms);
        alone_layout = (LinearLayout) findViewById(R.id.ll2_1_ms);
        energetic_layout = (LinearLayout) findViewById(R.id.ll2_2_ms);
        melancholy_layout = (LinearLayout) findViewById(R.id.ll2_3_ms);
        nostalgic_layout = (LinearLayout) findViewById(R.id.ll_3_1_ms);
        aggressive_layout = (LinearLayout) findViewById(R.id.ll_3_2_ms);
        night_layout = (LinearLayout) findViewById(R.id.ll_3_3_ms);

        // We'll set onClickListeners to the layouts //
        happy_layout.setOnClickListener(this);
        calm_layout.setOnClickListener(this);
        loving_layout.setOnClickListener(this);
        alone_layout.setOnClickListener(this);
        energetic_layout.setOnClickListener(this);
        melancholy_layout.setOnClickListener(this);
        nostalgic_layout.setOnClickListener(this);
        aggressive_layout.setOnClickListener(this);
        night_layout.setOnClickListener(this);

        // Creating intent object //
        list_intent = new Intent(MoodSelectActivity.this, ListPlaylistsActivity.class);


    }

    // onClick Function is the same for all layouts, just the view parameter changes //
    @Override
    public void onClick(View view) {

        // We'll use switch case in order to send true parameter //
        switch (view.getId()){
            case R.id.ll1_1_ms:     // Means Happy //
                list_intent.putExtra("Keyword", "Happy");
                startActivity(list_intent);
                break;

            case R.id.ll1_2_ms:     // Means Calm //
                list_intent.putExtra("Keyword", "Calm");
                startActivity(list_intent);
                break;

            case R.id.ll1_3_ms:     // Means Loving //
                list_intent.putExtra("Keyword", "Love");
                startActivity(list_intent);
                break;

            case R.id.ll2_1_ms:     // Means Alone //
                list_intent.putExtra("Keyword", "Alone");
                startActivity(list_intent);
                break;

            case R.id.ll2_2_ms:     // Means Energetic //
                list_intent.putExtra("Keyword", "Energetic");
                startActivity(list_intent);
                break;

            case R.id.ll2_3_ms:     // Means Melancholy //
                list_intent.putExtra("Keyword", "Melancholy");
                startActivity(list_intent);
                break;

            case R.id.ll_3_1_ms:    // Means Nostalgic //
                list_intent.putExtra("Keyword", "Nostalgic");
                startActivity(list_intent);
                break;

            case R.id.ll_3_2_ms:    // Means Aggressive //
                list_intent.putExtra("Keyword", "Aggressive");
                startActivity(list_intent);
                break;

            case R.id.ll_3_3_ms:    // Means Night //
                list_intent.putExtra("Keyword", "Disco");
                startActivity(list_intent);
                break;
        }
    }
}

