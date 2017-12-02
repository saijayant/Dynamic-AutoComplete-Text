package newapp.sai_jayant.com.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.Attributes;

import Model.Name;
import network.RetrofitClient;
import network.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.NetworkUtil;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    RecyclerView Recyclerview;
    private ProgressDialog dialog;
    private ArrayList<Name> list = new ArrayList<>();
    private AutoCompleteTextView AutoText;
    private ArrayAdapter<Name> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Doing something, please wait.");
        AutoText = (AutoCompleteTextView) findViewById(R.id.name_auto);
        getData();


    }

    private void getData() {
        if (NetworkUtil.isNetworkAvailble(this)) {
            dialog.show();
            RetrofitInterface ret = RetrofitClient.getClient().create(RetrofitInterface.class);
            Call<JsonElement> call = ret.getName();
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    Log.d("String", "onResponse:  response" + response.body());

                    if (response.code() == 200) {

                        JsonObject moviesResponse = null;

//                        jr = response.body().getAsJsonArray();
                        Log.d("getDataOne", "onResponse: " + response.body());
                        String str = response.body().toString();
                        Log.d("response", "onResponse: " + str);
                        try {
                            JSONObject jsonObj = new JSONObject(str);

                            JSONArray jsonArray = null;

                            jsonArray = jsonObj.getJSONArray("company");
                            Log.d("network", "onResponse: " + jsonArray);

                            String[] list = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject finalobj = jsonArray.getJSONObject(i);
                                String company_name = finalobj.getString("company_name");
                                Log.d("company name", "onResponse: " + company_name);
                                list[i] = finalobj.getString("company_name");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (MainActivity.this, android.R.layout.select_dialog_item, list);
                            AutoText.setThreshold(1);

                            //Set adapter to AutoCompleteTextView
                            AutoText.setAdapter(adapter);
                            AutoText.setOnItemSelectedListener(MainActivity.this);
                            AutoText.setOnItemClickListener(MainActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("expexp", "onResponse: " + e);
                        }

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d("response", "onFailure: " + t + "      " + call);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        // Show Alert
        Toast.makeText(getBaseContext(), "Position:" + arg2 + " Month:" + arg0.getItemAtPosition(arg2),
                Toast.LENGTH_LONG).show();

        Log.d("AutocompleteContacts", "Position:" + arg2 + " Month:" + arg0.getItemAtPosition(arg2));

    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
