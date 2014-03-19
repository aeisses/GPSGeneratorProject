package com.example.gpsgenerator;

import android.app.Activity;
import android.content.res.AssetManager;
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

public class MainActivity extends Activity {

/*	public static final int[] routes = {
		1,2,4,5,6,7,9,10,11,14,15,16,17,18,19,20,21,22,23,31,
		32,33,34,35,41,42,51,52,53,54,55,56,57,58,59,60,61,62,
		63,64,65,66,68,72,80,81,82,83,84,85,86,87,88,89,90,
		159,165,185,320,330,370,400,401,402
	};
	*/
	public static final int[] routes = {
		1,2,4,7,9,10,14,16,18,19,20,21,22,
		51,52,53,54,55,56,57,58,59,60,61,
		65,66,68,72,80,87,88,89,90,
		185,320,401
	};
	
	public static final double[] checkOneLocations = {
        44.648100, -63.620820,
        44.6818804463, -63.6682468173,
        44.6769201433, -63.6633657675,
        // 5 and 6 missing
        44.6718078448, -63.6162303049,
        44.648290664, -63.6212546869,
        44.6816416562, -63.5373870752,
        // 11 missing
        44.5951888593, -63.6368036834,
        // 15 missing
        44.6614883228, -63.6573722565,
        // 17 missing
        44.6571755895, -63.6079515487,
        44.5999392173, -63.6125287557,
        44.6542787516, -63.5802913588,
        44.6463034013, -63.5860387462,
        44.6202642522, -63.664646097,
        // 23 missing
        // 31 missing
        // 32 missing
        // 33 missing
        // 34 missing
        // 35 missing
        // 41 missing
        // 42 missing
        44.6708387937, -63.5754500424,
        44.6383329623, -63.6716624941,
        44.6463034013, -63.5860387462,
        44.6861942566, -63.5614550202,
        44.6861942566, -63.5614550202,
        44.6702096656, -63.5060175115,
        44.6699435308, -63.5055301977,
        44.6780893528, -63.5092771243,
        44.6699435308, -63.5055301977,
        44.6268488398, -63.5154775376,
        44.691218235, -63.4846971833,
        44.6710740495, -63.5750502815,
        // 62 missing
        // 63 missing
        // 64 missing
        44.6702096656, -63.5060175115,
        44.6805045195, -63.5366906829,
        44.6985413038, -63.493340029,
        44.7004152565, -63.5663559608,
        44.7327238917, -63.6563119356,
        // 81 missing
        // 82 missing
        // 83 missing
        // 84 missing
        // 85 missing
        // 86 missing
        44.6707164872, -63.5752388436,
        // 88 missing
        44.6707164872, -63.5752388436,
        44.6997960071, -63.6882364997,
        // 159 missing
        // 165 missing        
        44.7678267674, -63.699552236,
        44.8858776952, -63.5160616503,
        // 330 missing
        // 370 missing
        // 400 missing
        44.678522053, -63.258768783
        // 401 missing
	};
	
	public static final double[] checkTwoLocations = {
        44.642117, -63.579960,
        44.6817239481, -63.6716032339,
        44.6466300795, -63.6085809108,
        // 5 and 6 missing
        44.6673103349, -63.6061263758,
        44.6652182061, -63.6132079937,
        44.6309197679, -63.5889294666,
        // 11 missing
        44.6060002924, -63.6240279133,
        // 15 missing
        44.6559457706, -63.6633017726,
        // 17 missing
        44.6513580534, -63.5969470939,
        44.6339745468, -63.6198309823,
        44.6230294578, -63.6177923467,
        44.6378307977, -63.6879564515,
        44.6344778998, -63.6292576079,
        // 23 missing
        // 31 missing
        // 32 missing
        // 33 missing
        // 34 missing
        // 35 missing
        // 41 missing
        // 42 missing
        44.6954120428, -63.6013209508,
        44.6464550213, -63.6174436379,
        44.6713550409, -63.5799771734,
        44.6835512365, -63.5289703782,
        44.711049472, -63.5474178719,
        44.6842138861, -63.5604172497,
        44.6702872659, -63.5371336328,
        44.6777766307, -63.5185029718,
        44.6690733052, -63.5572088284,
        44.6064192909, -63.468676803,
        44.6123348175, -63.4884929995,
        44.670601044, -63.5388058348,
        // 62 missing
        // 63 missing
        // 64 missing
        44.6547596588, -63.4807819127,
        44.666981207, -63.5444726146,
        44.7035618505, -63.4983120434,
        44.6866637913, -63.564056323,
        44.7132488834, -63.6765668702,
        // 81 missing
        // 82 missing
        // 83 missing
        // 84 missing
        // 85 missing
        // 86 missing
        44.7403241454, -63.6465100539,
        // 88 missing
        44.7371106347, -63.6440194096,
        44.6623427719, -63.6227612023,
        // 159 missing
        // 165 missing
        44.6992649594, -63.6061559637,
        44.7920944033, -63.5854897141,
        // 330 missing
        // 370 missing
        // 400 missing
        44.6727365846, -63.2613794753
        // 401 missing
	};
	
	public static final double[] checkThreeLocations = {
        44.658012, -63.589750,
        44.6612216278, -63.6567216616,
        44.6493096869, -63.5729088357,
        // 5 and 6 missing
        44.6716451872, -63.6124314473,
        44.6260067862, -63.5743812936,
        44.6365381765, -63.5898121073,
        // 11 missing
        44.6502264961, -63.5756515111,
        // 15 missing
        44.6701859764, -63.5735207077,
        // 17 missing
        44.6327423422, -63.5826481999,
        44.6480778308, -63.620846908,
        44.566867528, -63.5624229693,
        44.6626347282, -63.7472859459,
        44.6480778308, -63.620846908,
        // 23 missing
        // 31 missing
        // 32 missing
        // 33 missing
        // 34 missing
        // 35 missing
        // 41 missing
        // 42 missing
        44.711777566, -63.6002410341,
        44.6941203897, -63.5830688162,
        44.6844692528, -63.5891602368,
        44.7020431936, -63.5394314775,
        44.743182103, -63.5599423895,
        44.6998090878, -63.5675076642,
        44.6490902269, -63.5480922427,
        44.670601044, -63.5388058348,
        44.6707161559, -63.5744203275,
        44.6703513856, -63.5746077457,
        44.6835407949, -63.4910163474,
        44.671962708, -63.532199702,
        // 62 missing
        // 63 missing
        // 64 missing
        44.6451077418, -63.4773959218,
        44.6645862121, -63.5335444292,
        44.7089666715, -63.4825910514,
        44.6699435308, -63.5055301977,
        44.6542787516, -63.5802913588,
        // 81 missing
        // 82 missing
        // 83 missing
        // 84 missing
        // 85 missing
        // 86 missing
        44.7678267674, -63.699552236,
        // 88 missing
        44.7678267674, -63.699552236,
        44.6493096869, -63.5729088357,
        // 159 missing
        // 165 missing
        44.6503159497, -63.5756954776,
        44.6499144052, -63.5776799768,
        // 330 missing
        // 370 missing
        // 400 missing
        44.6661676737, -63.2580639665
        // 401 missing
	};
	
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

    public void sendAllRoutes(View view)
    {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("Sending");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                	String[] routeCalls = new String[routes.length];
                	for (int i = 0; i< routes.length; i++)
                	{
                		routeCalls[i] = createNewUserForRoute(routes[i]);
                		Log.d("Aaron",routeCalls[i]);
                	}
                	int counter = 0;
                	while (true)
                	{
                    	double[] pointsArray;
                		if (counter == 0)
                		{
                			pointsArray = checkOneLocations;
                			counter++;
                		} 
                		else if (counter == 1)
                		{
                			pointsArray = checkTwoLocations;                		
                			counter++;
                		}
                		else
                		{
                			pointsArray = checkThreeLocations;
                			counter = 0;
                		}
                		for (int i=0; i<routeCalls.length; i++)
                		{
                			sendLocationToServer(routeCalls[i],pointsArray[i*2],pointsArray[i*2+1]);
                		}
                		try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                	}
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            TextView textView = (TextView)findViewById(R.id.textView);
//                            textView.setText("Waiting");
//                        }
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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
                        if (sendLocationToServer(locationStringUserTwo,randArray[0],randArray[1]))
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
                        if (sendLocationToServer(locationStringUserThree,randArray[0],randArray[1]))
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
                        if (sendLocationToServer(locationStringUserFour,randArray[0],randArray[1]))
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

    public Boolean sendLocationToServer(String url, double lat, double lng)
    {
    	Log.i("Url: ",url);
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

    public String createNewUserForRoute(int route)
    {
        try {
            HttpClient client = new DefaultHttpClient();
            EditText urlBar = (EditText)findViewById(R.id.urlBar);
            String postURL = urlBar.getText().toString() + "/" + route;
            HttpPost post = new HttpPost(postURL);
            post.setHeader("Content-type", "application/json");
            HttpResponse responsePOST = client.execute(post);
            if (responsePOST.getStatusLine().getStatusCode() == 201)
            {
                Header[] h = responsePOST.getAllHeaders();
                if (h.length > 2)
                    return h[8].getValue().replaceAll("buserver","api");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
                    return h[8].getValue().replaceAll("buserver","api");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
