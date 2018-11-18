package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dollop.syzygy.Model.AnyRemainder;
import com.dollop.syzygy.Model.RemainderFor;
import com.dollop.syzygy.Model.RequiredCare;
import com.dollop.syzygy.Model.RequiredCareType;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.fragment.client.AnyReminderFragment;
import com.dollop.syzygy.fragment.client.ReminderForFragment;
import com.dollop.syzygy.fragment.client.RequieredCareFragment;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.utility.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dollop.syzygy.fragment.client.ClientMainFragment.getDate;

public class ClientReminderActivity extends BaseActivity {

    ArrayList<RemainderFor> arrayListRemainderFor = new ArrayList<>();
    ArrayList<RequiredCare> arrayListRequiredCare = new ArrayList<>();
    ArrayList<AnyRemainder> arrayListAnyCare = new ArrayList<>();
    ArrayList<RequiredCareType> arrayListRequiredCareType = new ArrayList<>();

    ArrayList<String> arrayListRemainderForStr = new ArrayList<>();
    ArrayList<String> arrayListRequiredCareStr = new ArrayList<>();
    ArrayList<String> arrayListAnyCareStr = new ArrayList<>();
    ArrayList<String> arrayListRequiredCareTypeStr = new ArrayList<>();
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.spinRemainderFor)
    Spinner spinRemainderFor;
    @BindView(R.id.spinRequiredCare)
    Spinner spinRequiredCare;
    @BindView(R.id.spinCareType)
    Spinner spinCareType;
    @BindView(R.id.spinAnyCare)
    Spinner spinAnyCare;
    @BindView(R.id.editHoursInterval)
    EditText editHoursInterval;
    @BindView(R.id.textSelectDateTime)
    TextView textSelectDateTime;
    @BindView(R.id.layoutSelectDateTime)
    LinearLayout layoutSelectDateTime;
    @BindView(R.id.btnSetRemainder)
    Button btnSetRemainder;
    private String typeId;
    private String pos="0";
    private String mSelectedDateTime = "";
    private int day, month, year, hour, minute;
    private String currentDate = "";
    private String mSelectedDate = "";
    private String mSelectedTime = "";

    @Override
    
    protected int getContentResId() {
        return R.layout.activity_remainder_new;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setToolbarWithBackButton("Reminder");

        loadData();


        layoutSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });


        editHoursInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SavedData.saveRemainderHour(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSetRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pos.equals("0")) {
                    if (!editHoursInterval.getText().toString().equals("")) {
                        if (!textSelectDateTime.getText().toString().equals("Select Date & Time")) {
                            saveRemainderToServer();
                        } else {
                            S.T(ClientReminderActivity.this, "Select Date and TIme");
                        }
                    } else {
                        S.T(ClientReminderActivity.this, "Select Interval");
                    }
                } else {
                    if (!textSelectDateTime.getText().toString().equals("Select Date & Time")) {
                        saveRemainderToServer();
                    } else {
                        S.T(ClientReminderActivity.this, "Select Date and TIme");
                    }
                }
            }
        });

        spinRemainderFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SavedData.saveRemainderFor(arrayListRemainderForStr.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinRequiredCare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SavedData.saveRequiredCate(arrayListRequiredCare.get(position).getCategory_name());
                typeId = arrayListRequiredCare.get(position).getReminder_category_id();
                arrayListRequiredCareType.clear();
                arrayListRequiredCareTypeStr.clear();
                loadCareTypeData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinAnyCare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SavedData.saveAnyRemainder(arrayListAnyCare.get(position).getCategory_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinCareType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SavedData.saveRequiredCateType(arrayListRequiredCareTypeStr.get(position));
                pos = String.valueOf(position);
                if (position == 0) {
                    editHoursInterval.setVisibility(View.INVISIBLE);
                } else {
                    editHoursInterval.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /*****************
     * Method to select date from date picker
     ***********************/
    public void dateDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mSelectedDate = Constants.dateFormatForDisplayForThisAppOnly(year, monthOfYear+1, dayOfMonth);
                SavedData.saveRemainderDate(mSelectedDate);
                timeDialog();

            }
        }, year, month, day);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dpDialog.setMinDate(calendar1);
        dpDialog.show(getFragmentManager(), "DatePickerDialog");

    }


    /*****************
     * Method to select time from time picker
     ***********************/
    public void timeDialog() {
        TimePickerDialog tpDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                mSelectedTime = Constants.timeFormatForDisplay(hourOfDay, minute);
                SavedData.saveRemainderTime(mSelectedTime);
                mSelectedDateTime = mSelectedDate + "/" + mSelectedTime;
                textSelectDateTime.setText(mSelectedDateTime);

            }
        }, hour, minute, false);
        tpDialog.show(getFragmentManager(), "TimePickerDialog");

    }

    private void addRemainderPopup() {
        final View dialogView = View.inflate(ClientReminderActivity.this, R.layout.dialog_date_time, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(ClientReminderActivity.this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                long time = calendar.getTimeInMillis();
                SavedData.saveRemainderTime(getDate(time, "HH:mm"));
                SavedData.saveRemainderDate(getDate(time, "yyyy-MM-dd"));
                textSelectDateTime.setText(SavedData.getRemainderDate() + " / " + SavedData.getRemainderTime());
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void saveRemainderToServer() {
        S.E("remainder params : " + getParams());
        new JSONParser(this).parseVollyStringRequest(Const.URL.ADD_REMAINDER, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("remainder response : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        SavedData.saveRemainderFor("");
                        SavedData.saveRequiredCate("");
                        SavedData.saveRequiredCateType("");
                        SavedData.saveRemainderDate("");
                        SavedData.saveRemainderTime("");
                        SavedData.saveAnyRemainder("");
                        SavedData.saveRemainderHour("");
                        S.I(ClientReminderActivity.this, ReminderListActivity.class, null);
                        finish();
                    } else {
                        S.T(ClientReminderActivity.this, "Please try again later");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("start_date", SavedData.getRemainderDate());
        params.put("time", SavedData.getRemainderTime());
        params.put("reminder_for", SavedData.getRemainderFor());
        params.put("required_care", SavedData.getRequiredCate());
        params.put("reminder_type", SavedData.getRequiredCateType());
        params.put("any_reminder", SavedData.getAnyRemainder());

        if (!SavedData.getRemainderHour().equals("1")) {
            params.put("interval", editHoursInterval.getText().toString());
        } else {
            params.put("interval", "0");
        }
        return params;
    }

    private void loadData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_REMAINDER_DEFAULT_DATA, 0, null, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("reminder data : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray reminder_for = data.getJSONArray("reminder_for");
                        for (int i = 0; i < reminder_for.length(); i++) {
                            JSONObject json = reminder_for.getJSONObject(i);
                            RemainderFor remainderFor = new RemainderFor();
                            remainderFor.setCategory_name(json.getString("category_name"));
                            remainderFor.setCategory_name(json.getString("reminder_category_id"));
                            arrayListRemainderFor.add(remainderFor);

                            if(arrayListRemainderForStr!= null && arrayListRemainderForStr.size()>0)
                            {
                                if(!arrayListRemainderForStr.contains(json.getString("category_name")))
                                arrayListRemainderForStr.add(json.getString("category_name"));
                            }
                            else
                                arrayListRemainderForStr.add(json.getString("category_name"));

                        }
                        ArrayAdapter adapter = new ArrayAdapter(ClientReminderActivity.this, android.R.layout.simple_list_item_1, arrayListRemainderForStr);
                        spinRemainderFor.setAdapter(adapter);

                        JSONArray reminder_care = data.getJSONArray("reminder_care");
                        for (int i = 0; i < reminder_care.length(); i++) {
                            JSONObject json = reminder_care.getJSONObject(i);
                            RequiredCare remainderFor = new RequiredCare();
                            remainderFor.setCategory_name(json.getString("category_name"));
                            remainderFor.setReminder_category_id(json.getString("reminder_category_id"));
                            arrayListRequiredCare.add(remainderFor);
                            arrayListRequiredCareStr.add(json.getString("category_name"));
                        }
                        ArrayAdapter adapter1 = new ArrayAdapter(ClientReminderActivity.this, android.R.layout.simple_list_item_1, arrayListRequiredCareStr);
                        spinRequiredCare.setAdapter(adapter1);

                        JSONArray any_reminder = data.getJSONArray("any_reminder");
                        for (int i = 0; i < any_reminder.length(); i++) {
                            JSONObject json = any_reminder.getJSONObject(i);
                            AnyRemainder remainderFor = new AnyRemainder();
                            remainderFor.setCategory_name(json.getString("category_name"));
                            remainderFor.setReminder_category_id(json.getString("reminder_category_id"));

                            arrayListAnyCare.add(remainderFor);
                            arrayListAnyCareStr.add(json.getString("category_name"));
                        }

                        ArrayAdapter adapter2 = new ArrayAdapter(ClientReminderActivity.this, android.R.layout.simple_list_item_1, arrayListAnyCareStr);
                        spinAnyCare.setAdapter(adapter2);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadCareTypeData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_REMAINDER_TYPE, 1, getParamsForCareType(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("reminder data : " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            RequiredCareType requiredCareType = new RequiredCareType();
                            requiredCareType.setReminder_category_id(json.getString("care_type_1"));
                            arrayListRequiredCareType.add(requiredCareType);
                            arrayListRequiredCareTypeStr.add(json.getString("care_type_1"));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(ClientReminderActivity.this, android.R.layout.simple_list_item_1, arrayListRequiredCareTypeStr);
                        spinCareType.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForCareType() {
        HashMap<String, String> params = new HashMap<>();
        params.put("reminder_category_id", typeId);
        return params;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ReminderForFragment(), "Reminder For");
        adapter.addFrag(new RequieredCareFragment(), "Requiered Care");
        adapter.addFrag(new AnyReminderFragment(), "Any Reminder");
        viewPager.setAdapter(adapter);
    }

    public void openNextFragment() {
//        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
    }

    public void openPreviousFragment() {
//        viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
    }

    public void openRemainderFor() {
//        viewpager.setCurrentItem(0);
    }

    public void openReequiredCare() {
//        viewpager.setCurrentItem(1);
    }

    public void openAnyRemainder() {
//        viewpager.setCurrentItem(2);
    }

    public void openRemainderList() {
//        viewpager.setCurrentItem(3);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.remainder_main, menu);
        menu.findItem(R.id.actionRemainder).setTitle(Html.fromHtml("<font color='#ff3824'>Reminder's</font>"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.actionRemainder:
                S.I(ClientReminderActivity.this, ReminderListActivity.class, null);
                finish();
                break;
        }
        return true;
    }

}
