package com.example.michellewong.colourpickerexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.larswerkman.holocolorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {
    private static final String COLOUR_PICKER_TAG = "colour_picker";
    public static final String COLOUR_ARG = "colour";
    ImageView mSquare;
    int mSelectedColour = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSquare = (ImageView)findViewById(R.id.colour_picker_square);
        View mSquareTouch = findViewById(R.id.colour_picker_square_touch);
        if (savedInstanceState != null) {
            onColourChanged(savedInstanceState.getInt(COLOUR_ARG));
        }

        mSquareTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment current = fragmentManager.findFragmentByTag(COLOUR_PICKER_TAG);

                if (current == null) {
                    Bundle args = new Bundle();
                    args.putInt(COLOUR_ARG, mSelectedColour);
                    current = new ColourPickerFragment();
                    current.setArguments(args);
                    ft.add(R.id.fragment_container, current, COLOUR_PICKER_TAG);
                } else {
                    ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                    ft.remove(current);
                }
               ft.commit();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLOUR_ARG, mSelectedColour);
    }

    public void onColourChanged(int colour) {
        mSelectedColour = colour;
        if (mSquare != null) {
            mSquare.setColorFilter(mSelectedColour);
        }
    }

    public void onColourSelectionTimeout() {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment current = fragmentManager.findFragmentByTag(COLOUR_PICKER_TAG);
            if (current != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.remove(current);
                ft.commit();
            }
        } catch (IllegalStateException ignored) {
            // no way to avoid if savedinstance state already called
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
