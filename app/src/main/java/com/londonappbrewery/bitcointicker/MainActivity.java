package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/";
    private final String PUBLIC_KEY = "OGE3MDZkYjBmMWY2NDdmZWI0NjljYzExODJkMjg0MTY";
    private final String DEBUG_NAME = "Bitcoin";



    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i_position, long l) {


                String selectionStr = adapterView.getItemAtPosition(i_position).toString();
                //letsDoSomeNetworking(selectionStr);
                Log.d(DEBUG_NAME, "Selected : " + selectionStr);
                String url = "https://apiv2.bitcoinaverage.com/indices/global/ticker/" + selectionStr;
                //url = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTCUSD";

                letsDoSomeNetworking(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(DEBUG_NAME, "Nothing selected");
            }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(DEBUG_NAME,"Got into LetsdoIt with the url of : " + url);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                Log.d(DEBUG_NAME, "Response JSON : " + responseBody.toString());
                try {
                    String price = responseBody.getString("last");
                    updateUI(price);
                    Log.d(DEBUG_NAME,"Price received : " +price);

                }catch (JSONException e){
                    e.printStackTrace();
                    //return null;

                    Log.d(DEBUG_NAME,"Something went wrong! :(");

                }
            }
          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                Log.d(DEBUG_NAME,"Something went wrong with the connection statusCode : "+statusCode+e+response);
          };

        });




    }

    private void updateUI(String price){
        mPriceTextView.setText(price);
    }


}
