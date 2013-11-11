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

public class MainActivity extends ActionBarActivity {

    public static final double[] locations = {
            -63.6551,44.6437,
            -63.6557,44.6438,
            -63.656,44.6439,
            -63.6563,44.644,
            -63.6565,44.644,
            -63.6566,44.644,
            -63.6567,44.6441,
            -63.6567,44.6441,
            -63.6568,44.6441,
            -63.6568,44.6441,
            -63.6568,44.6441,
            -63.6569,44.6442,
            -63.6569,44.6442,
            -63.6569,44.6442,
            -63.657,44.6442,
            -63.657,44.6442,
            -63.6571,44.6443,
            -63.6571,44.6443,
            -63.6571,44.6443,
            -63.6572,44.6444,
            -63.6572,44.6444,
            -63.6572,44.6445,
            -63.6573,44.6445,
            -63.6573,44.6445,
            -63.6573,44.6446,
            -63.6573,44.6446,
            -63.6573,44.6447,
            -63.6573,44.6447,
            -63.6573,44.6448,
            -63.6573,44.6448,
            -63.6573,44.6449,
            -63.6573,44.6449,
            -63.6573,44.6449,
            -63.6573,44.645,
            -63.6573,44.645,
            -63.6573,44.645,
            -63.6572,44.6451,
            -63.6572,44.6451,
            -63.6572,44.6451,
            -63.6572,44.6451,
            -63.6571,44.6452,
            -63.6571,44.6452,
            -63.6571,44.6452,
            -63.6571,44.6452,
            -63.657,44.6453,
            -63.657,44.6453,
            -63.6569,44.6453,
            -63.6569,44.6453,
            -63.6569,44.6454,
            -63.6568,44.6454,
            -63.6568,44.6454,
            -63.6567,44.6454,
            -63.6567,44.6454,
            -63.6566,44.6454};
            //-63.6566,44.6454 -63.6566,44.6454 -63.6565,44.6454 -63.6565,44.6454 -63.6564,44.6454 -63.6564,44.6454 -63.6564,44.6454 -63.6563,44.6454 -63.6562,44.6454 -63.6562,44.6454 -63.6561,44.6454 -63.656,44.6454 -63.656,44.6454 -63.6559,44.6454 -63.6559,44.6454 -63.6558,44.6453 -63.6558,44.6453 -63.6557,44.6453 -63.6556,44.6453 -63.6556,44.6452 -63.6555,44.6452 -63.6555,44.6452 -63.6554,44.6452 -63.6554,44.6451 -63.6553,44.6451 -63.6553,44.6451 -63.6552,44.645 -63.6552,44.645 -63.6552,44.645 -63.6552,44.645 -63.6551,44.6449 -63.6551,44.6449 -63.6551,44.6449 -63.655,44.6448 -63.655,44.6448 -63.655,44.6447 -63.655,44.6447 -63.655,44.6447 -63.6549,44.6447 -63.6549,44.6446 -63.6549,44.6446 -63.6549,44.6445 -63.6549,44.6444 -63.6549,44.6444 -63.6549,44.6444 -63.655,44.6441 -63.6551,44.6437};

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
//        EditText urlBar = (EditText)findViewById(R.id.urlBar);
//        urlBar.setText("http://knowtime.ca/api/alpha_1/users/new/1", TextView.BufferType.EDITABLE);
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

    public void sendLocations(View view)
    {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("Sending");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String locationString = createNewUser();
                    Log.i("LocationString: ",locationString);
                    for (int i=0; i<locations.length; i+=2)
                    {
                        if (sendLocationToServer(locationString,locations[i],locations[i+1]))
                        {
                            Log.i("Location ","sent");
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
