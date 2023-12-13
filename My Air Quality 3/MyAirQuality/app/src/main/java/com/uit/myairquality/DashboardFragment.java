package com.uit.myairquality;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonArray;
import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Interfaces.CallWeather;
import com.uit.myairquality.Interfaces.ChartInterface;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.ChartResquest;
import com.uit.myairquality.Model.TokenResponse;
import com.uit.myairquality.Model.User;
import com.uit.myairquality.Model.WeatherResponse;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private Button button;
    private LineChart lineChart;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinnerTemp, spinnerTimeFrame, spinnerEnding;
    String[] temp = {"Temperature", "Humidity", "Rain Fall"};
    String[] time_frame = {"Day", "Month", "Year"};


    public DashboardFragment() {
        // Required empty public constructor


    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
//     TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }
    JSONArray Json;
    ChartResquest res;
    Button btnShow;
    User userCallApi;
    APIClient ApiClient;
    ChartInterface chartInterface = ApiClient.CallChart();
    Spinner spinner1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Spinner spinnerTemp, spinnerTimeFrame, spinnerEnding;

        userCallApi = new User("user", "123", "");

//        String receivedData = intent.getStringExtra("key");
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");

        CallToken callToken = ApiClient.CallToken();
        ChartInterface callWeather = ApiClient.CallChart();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        button = view.findViewById(R.id.btnShow);
        ChartResquest resquest= new ChartResquest("2023-12-08T10:04:12.000Z","2023-12-09T10:04:12.000Z");
        Call<TokenResponse> userCallToken = callToken.sendRequest(
                "password",
                "openremote",
                userCallApi.getUsername(),
                userCallApi.getPassword()
        );

        userCallToken.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    userCallApi.setToken(accessToken);
                    Log.d("response call", userCallApi.getToken());
                    Call<JsonArray> userCallChar = chartInterface.callchart(
                            "5zI6XqkQVSfdgOrZ1MyWEf",
                            "temperature",
                            "Bearer " + userCallApi.getToken(),
                            resquest
                    );
                    Log.i("Data",userCallApi.getToken()+"///////////"+lineChart.toString()+"///////////"+resquest);
                    btnShow = view.findViewById(R.id.btnShow);
                    btnShow.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {

                        }
                    });
                    userCallChar.enqueue(new Callback<JsonArray>() {


                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            JsonArray ChartJson = response.body();
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {

                        }
                    });
                } else {
                    Log.d("response call", "lỗi");
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("response call", t.getMessage().toString());
            }
        });
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_dashboard, container, false);

        spinnerTemp = view.findViewById(R.id.spinner_temp);
        spinnerTimeFrame = view.findViewById(R.id.spinner_timeframe);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.options_array,
                android.R.layout.simple_spinner_item

        );
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.options_array_1,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerTemp.setAdapter(adapter);
        spinnerTimeFrame.setAdapter(adapter1);

        spinnerTemp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Xử lý sự kiện khi một lựa chọn được chọn
                String selectedOption = parentView.getItemAtPosition(position).toString();
                // TODO: Xử lý dựa trên lựa chọn được chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }

        });
        spinnerTimeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
                // TODO: Xử lý sự kiện khi Spinner 1 được chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLineChart();
            }
        });

        return view;
    }
    private void setupLineChart() {
        // Tạo danh sách các Entry (điểm trên biểu đồ)
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 18));

        // Tạo DataSet và thiết lập màu sắc và các thuộc tính khác
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(getResources().getColor(R.color.red));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

        // Tạo dữ liệu của biểu đồ từ DataSet
        LineData lineData = new LineData(dataSet);

        // Thiết lập các thuộc tính của biểu đồ
        lineChart.setData(lineData);
        lineChart.getDescription().setText("Description");
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // Cập nhật biểu đồ
        lineChart.invalidate();
    }
}