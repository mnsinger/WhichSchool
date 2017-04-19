package hoops.whichschool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText mYearFrom, mYearTo;
    ArrayList<Player> players = new ArrayList<Player>();
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String TAG = "MainActivity.java";

        Log.v(TAG, "ABOUT TO START LOADING ARRAYLIST...");
        try {
            XmlPullParser parser = this.getResources().getXml(R.xml.players_universities);
            parser.next();
            // null
            Log.v(TAG, "FIRST XML TAG: " + parser.getName());
            parser.next();
            // root
            Log.v(TAG, "SECOND XML TAG: " + parser.getName());
            //parser.next();
            // ?
            //Log.v(TAG, "THIRD XML TAG: " + parser.getName());
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                Player player = new Player();
                String tagName = parser.getName(); // should be name
                String name = readText(parser);
                if (name == "") {
                    break;
                }
                player.name = name;
                parser.next(); // yearFrom
                player.yearFrom = Integer.valueOf(readText(parser));
                parser.next(); // yearTo
                player.yearTo = Integer.valueOf(readText(parser));
                parser.next(); // pos
                player.position = readText(parser);
                parser.next(); // height
                player.height = readText(parser);
                parser.next(); // weight
                String weight = readText(parser);
                if (weight == "") {
                    player.weight = 0;
                }
                else {
                    player.weight = Integer.valueOf(weight);
                }
                parser.next(); // bday
                player.bday = readText(parser);
                parser.next(); // college
                player.college = readText(parser);
                Log.v(TAG, "name: " + player.name + ", yearFrom: " + player.yearFrom + ", yearTo: " + player.yearTo + ", position: " + player.position + ", height: " + player.height + ", weight: " + player.weight + ", bday: " + player.bday + ", college: " + player.college);
                players.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mYearFrom = (EditText) findViewById(R.id.yearFrom);
        mYearTo = (EditText) findViewById(R.id.yearTo);
        final TextView playerName = (TextView) findViewById(R.id.playerName);
        final TextView playedFrom = (TextView) findViewById(R.id.playedFrom);
        final TextView playedTo = (TextView) findViewById(R.id.playedTo);
        final TextView position = (TextView) findViewById(R.id.position);
        final TextView height = (TextView) findViewById(R.id.height);
        final TextView weight = (TextView) findViewById(R.id.weight);
        final TextView bday = (TextView) findViewById(R.id.bday);
        final Button clickButton = (Button) findViewById(R.id.yearsButton);
        final Button collegeButton = (Button) findViewById(R.id.college);

        final String[] college = {""};
        clickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ***Do what you want with the click here***

                collegeButton.setText("WHICH COLLEGE?");
                Player p = getRandomPlayer();

                playerName.setText(p.name);
                playedFrom.setText(String.valueOf(p.yearFrom));
                playedTo.setText(String.valueOf(p.yearTo));
                position.setText(p.position);
                height.setText(p.height);
                weight.setText(String.valueOf(p.weight));
                bday.setText(p.bday);
                college[0] = p.college;
            }
        });

        collegeButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ***Do what you want with the click here***
                collegeButton.setText(college[0]);
            }
        });


    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private Player getRandomPlayer() {
        Random rand = new Random();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int from = rand.nextInt((year - 1948) + 1) + 1948;
        if (mYearFrom.length() != 4)
            mYearFrom.setText(String.valueOf(from));
        int to = rand.nextInt((year - from) + 1) + from;
        if (mYearTo.length() != 4)
            mYearTo.setText(String.valueOf(to));

        Player p = new Player();
        int i=0;

        //Log.v(TAG, "year from: " + mYearFrom.toString());
        //Log.v(TAG, "year to: " + mYearTo.toString());

        Log.v(TAG, "p.yearFrom: " + p.yearFrom + ", p.yearTo: " + p.yearTo + ", i: " + i);
        // p.yearFrom = 0 // p.yearTo = 9999 // i = 0
        while (noOverLap(p.yearFrom, p.yearTo) && i < 500) {
            p = players.get(rand.nextInt((players.size())));
            Log.v(TAG, "p.yearFrom: " + p.yearFrom + ", p.yearTo: " + p.yearTo + ", i: " + i);
            i++;
        }

        if (i == 500) {
            Player p1 = new Player();
            p1.name = "Invalid Years";
            return p1;
        }

        return p;

    }

    public boolean noOverLap(int from, int to) {
        if (from >= Integer.valueOf(mYearFrom.getText().toString()) && from <= Integer.valueOf(mYearTo.getText().toString()))
            return false;
        if (to <= Integer.valueOf(mYearTo.getText().toString()) && to >= Integer.valueOf(mYearFrom.getText().toString()))
            return false;
        return true;
    }

}
