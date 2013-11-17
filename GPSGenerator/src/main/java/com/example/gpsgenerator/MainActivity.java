package com.example.gpsgenerator;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.os.StrictMode;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.apache.http.entity.StringEntity;
import org.apache.http.Header;
import android.widget.TextView;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends ActionBarActivity {

    public static final double[] startlocations = {
            44.648100, -63.620820,
            44.647865, -63.618840,
            44.649227, -63.616480,
            44.651344, -63.616333,
            44.653248, -63.612846,
            44.654370, -63.609966,
            44.653305, -63.608560,
            44.651432, -63.606052,
            44.650166, -63.604317,
            44.649147, -63.602920,
            44.646828, -63.601555,
            44.644207, -63.600246,
            44.642868, -63.599530,
            44.640600, -63.598312,
            44.638930, -63.597412,
            44.637660, -63.596210,
            44.638030, -63.594620,
            44.638916, -63.591230,
            44.639460, -63.588960,
            44.640240, -63.587110,
            44.640694, -63.585217,
            44.641330, -63.582977,
            44.642117, -63.579960,
            44.642654, -63.577988,
            44.643295, -63.575546,
            44.643986, -63.572980,
            44.646130, -63.573510,
            44.646755, -63.573837,
            44.648410, -63.574604,
            44.650246, -63.575570,
            44.650917, -63.579712,
            44.650650, -63.581276,
            44.652283, -63.583730,
            44.653310, -63.585220,
            44.654346, -63.586685,
            44.655710, -63.588676,
            44.658012, -63.589750,
            44.671036, -63.575092};
    public double[] locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        locations = new double[((startlocations.length-2)*4)+2];
        for (int i = 0; i<startlocations.length-2; i+=2)
        {
            double startPointLat = startlocations[i];
            double endPointLat = startlocations[i+2];
            double startPointLong = startlocations[i+1];
            double endPointLong = startlocations[i+3];
            double increaseLat = (double)((endPointLat - startPointLat)/4);
            double increaseLong = (double)((endPointLong - startPointLong)/4);
            int j = i*4;
            locations[j] = startlocations[i];
            locations[j+1] = startlocations[i+1];
            for (int k = 1; k<4; k++) {
                locations[j+(2*k)] = startlocations[i]+(increaseLat*k);
                locations[j+1+(2*k)] = startlocations[i+1]+(increaseLong*k);
            }
        }
        locations[locations.length-2] = startlocations[startlocations.length-2];
        locations[locations.length-1] = startlocations[startlocations.length-1];
//        EditText urlBar = (EditText)findViewById(R.id.urlBar);
//        urlBar.setText("http://knowtime.ca/api/alpha_1/users/new/1", TextView.BufferType.EDITABLE);
    }

    public double[] getRandomPoint(double lat, double lng)
    {
        double[] returnArray = new double[2];
        Random r = new Random();
        double deltaLat = (double)((r.nextInt(200)-100)/100000.0);
        double deltaLng = (double)((r.nextInt(200)-100)/100000.0);
        returnArray[0] = lat + deltaLat;
        returnArray[1] = lng + deltaLng;
        return returnArray;
    }

    public void fileRead(View view)
    {
        InputStream is = getResources().openRawResource(R.raw.stops);
        System.out.println(is);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try
        {
            String line = reader.readLine();
            while (line != null)
            {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendBusLocations(View view)
    {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("Sending");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    boolean oneForward = true;
                    boolean twoForward = false;
                    boolean threeForward = true;
                    boolean fourForward = false;
                    String locationStringUserOne = createNewUser();
                    String locationStringUserTwo = createNewUser();
                    String locationStringUserThree = createNewUser();
                    String locationStringUserFour = createNewUser();
                    int userOneStartPoint = 0;
                    int userTwoStartPoint = (int)(locations.length/2)-1;
                    int userThreeStartPoint = (int)(locations.length/2)-1;
                    int userFourStartPoint = locations.length;

//                    Log.i("LocationString: ",locationString);
//                    for (int i=0; i<locations.length; i+=2)
                    while(true)
                    {
                        if (userOneStartPoint >= locations.length-2) {
                            userOneStartPoint = locations.length-2;
                            oneForward = false;
                        }
                        if (userOneStartPoint <= 0) {
                            userOneStartPoint = 0;
                            oneForward = true;
                        }
                        if (sendLocationToServer(locationStringUserOne,locations[userOneStartPoint],locations[userOneStartPoint+1]))
                        {
                            Log.i("SendingOne","Location");
                            if (oneForward) {
                                userOneStartPoint+=2;
                            } else {
                                userOneStartPoint-=2;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        if (userTwoStartPoint >= locations.length-2) {
                            userTwoStartPoint = locations.length-2;
                            twoForward = false;
                        }
                        if (userTwoStartPoint <= 0) {
                            userTwoStartPoint = 0;
                            twoForward = true;
                        }
                        if (sendLocationToServer(locationStringUserTwo,locations[userTwoStartPoint],locations[userTwoStartPoint+1]))
                        {
                            Log.i("SendingTwo","Location");
                            if (twoForward) {
                                userTwoStartPoint+=2;
                            } else {
                                userTwoStartPoint-=2;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        if (userThreeStartPoint >= locations.length-2) {
                            userThreeStartPoint = locations.length-2;
                            threeForward = false;
                        }
                        if (userThreeStartPoint <= 0) {
                            userThreeStartPoint = 0;
                            threeForward = true;
                        }
                        if (sendLocationToServer(locationStringUserThree,locations[userThreeStartPoint],locations[userThreeStartPoint+1]))
                        {
                            Log.i("SendingThree","Location");
                            if (threeForward) {
                                userThreeStartPoint+=2;
                            } else {
                                userThreeStartPoint-=2;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        if (userFourStartPoint >= locations.length-2) {
                            userFourStartPoint = locations.length-2;
                            fourForward = false;
                        }
                        if (userFourStartPoint <= 0) {
                            userFourStartPoint = 0;
                            fourForward = true;
                        }
                        if (sendLocationToServer(locationStringUserFour,locations[userFourStartPoint],locations[userFourStartPoint+1]))
                        {
                            Log.i("SendingFour","Location");
                             if (fourForward) {
                                userFourStartPoint+=2;
                            } else {
                                userFourStartPoint-=2;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = (TextView)findViewById(R.id.textView);
                            textView.setText("Waiting");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void sendPeopleLocations(View view)
    {
        TextView textView = (TextView)findViewById(R.id.textView);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String locationStringUserOne = createNewUser();
                    String locationStringUserTwo = createNewUser();
                    String locationStringUserThree = createNewUser();
                    String locationStringUserFour = createNewUser();
                    int movementPoint = 0;
                    boolean isForward = true;
                    while(true)
                    {
                        if (isForward) {
                            movementPoint+=2;
                        } else {
                            movementPoint-=2;
                        }
                        Log.i("Start Point Lat"," "+locations[movementPoint]);
                        Log.i("Start Point Lng"," "+locations[movementPoint+1]);
                        double[] randArray = getRandomPoint(locations[movementPoint],locations[movementPoint+1]);
                        if (sendLocationToServer(locationStringUserOne,randArray[0],randArray[1]))
                        {

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        randArray = getRandomPoint(locations[movementPoint],locations[movementPoint+1]);
                        if (sendLocationToServer(locationStringUserOne,randArray[0],randArray[1]))
                        {

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        randArray = getRandomPoint(locations[movementPoint],locations[movementPoint+1]);
                        if (sendLocationToServer(locationStringUserOne,randArray[0],randArray[1]))
                        {

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        randArray = getRandomPoint(locations[movementPoint],locations[movementPoint+1]);
                        if (sendLocationToServer(locationStringUserOne,randArray[0],randArray[1]))
                        {

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        if (movementPoint < 0) {
                            isForward = true;
                            movementPoint = 0;
                        }
                        if (movementPoint >= locations.length) {
                            isForward = false;
                            movementPoint = locations.length - 1;
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = (TextView)findViewById(R.id.textView);
                            textView.setText("Waiting");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public Boolean sendLocationToServer(String url, double lat, double lng)
    {
        Log.i("Lat:"," "+lat);
        Log.i("Lng:"," "+lng);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("lat",""+lat);
            jsonParam.put("lng",""+lng);
            StringEntity se = new StringEntity(jsonParam.toString());
            post.setEntity(se);
            HttpResponse responsePost = client.execute(post);
            if (responsePost.getStatusLine().getStatusCode() != 200)
            {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public String createNewUser()
    {
        try {
            HttpClient client = new DefaultHttpClient();
            EditText urlBar = (EditText)findViewById(R.id.urlBar);
            String postURL = urlBar.getText().toString();
            HttpPost post = new HttpPost(postURL);
            post.setHeader("Content-type", "application/json");
            HttpResponse responsePOST = client.execute(post);
            if (responsePOST.getStatusLine().getStatusCode() == 201)
            {
                Header[] h = responsePOST.getAllHeaders();
                if (h.length > 2)
                    return h[1].getValue().replaceAll("buserver","api");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
