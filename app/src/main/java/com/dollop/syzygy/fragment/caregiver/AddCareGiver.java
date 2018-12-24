package com.dollop.syzygy.fragment.caregiver;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.syzygy.Model.CareGiverServiceModel;
import com.dollop.syzygy.Model.CareGiverSpecialization;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.multipart.AppHelper;
import com.dollop.syzygy.sohel.multipart.VolleyMultipartRequest;
import com.dollop.syzygy.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.Manifest.permission.CAMERA;
import static android.os.Build.VERSION_CODES.M;

public class AddCareGiver extends Fragment {
    public static final int PERMISSION_REQUEST_CODE = 1111;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    @BindView(R.id.educationqualification)
    EditText educationqualification;
    @BindView(R.id.ragistrationno)
    EditText ragistrationno;
    @BindView(R.id.documentImageView)
    ImageView documentImageView;
    @BindView(R.id.submit)
    Button submit;
    Unbinder unbinder;
    @BindView(R.id.uploaddocumentlayout)
    LinearLayout uploaddocumentlayout;
    Uri mImageCaptureUri;
    Bitmap productImageBitmap;

    @BindView(R.id.Specilization)
    Spinner Specilization;

    @BindView(R.id.Specilizationlayout)
    LinearLayout Specilizationlayout;
    int flag;
    String checkimage2 = "";
    String checkImage3 = "";

    ArrayList<CareGiverServiceModel> serviceCareList = new ArrayList<>();
    ArrayList<String> ServiceName = new ArrayList();
    CareGiverSpecialization careGiverSpecialization;
    ArrayList<CareGiverSpecialization> careGiverSpecializationslist = new ArrayList<>();
    ArrayList<String> careGiverSpecializations_id = new ArrayList<>();

    @BindView(R.id.caregiversppiner)
    Spinner caregiversppiner;
    String service_id = "", specilization_id = "", checkimage = "";
    @BindView(R.id.uploaddocument1)
    TextView uploaddocument1;
    @BindView(R.id.documentImageView1)
    ImageView documentImageView1;
    @BindView(R.id.uploaddocument2)
    TextView uploaddocument2;
    @BindView(R.id.documentImageView2)
    ImageView documentImageView2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter serviceAdapter;
    private ArrayAdapter serviceAdapter12;
    private TextView uploaddocument;
    ArrayList<String> citynameList = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    @BindView(R.id.spCityListId)
    Spinner spCityListId;
    private String cityIdstr = "";
    private String current_city = "";
    private int selectedPostion = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_care_giver1, container, false);
        ButterKnife.bind(this, view);
        uploaddocument = (TextView) view.findViewById(R.id.uploaddocument);
        ((CareGiverMainActivity) getActivity()).launchFragmentTitle("Add CareGiver");
        getServices();

        uploaddocumentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =1;
                selectImage();
            }
        });
        documentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =1;
                selectImage();
            }
        });

        uploaddocument1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =2;
                selectImage();
            }
        });
        documentImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =2;
                selectImage();
            }
        });

        uploaddocument2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =3;
                selectImage();
            }
        });
        documentImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =3;
                selectImage();
            }
        });

       // getCityList();
        caregiversppiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("CareGiver Type")) {
                } else {
                    service_id = serviceCareList.get(position).getService_id();
                    specilization_id = "";
                    getSpecialiazation(service_id);
                    Specilizationlayout.setVisibility(View.VISIBLE);
                    getCityList();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Specilization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (careGiverSpecializationslist.size() > 0) {
                    specilization_id = careGiverSpecializationslist.get(position).getCaregiver_specialization_id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(educationqualification.getText().toString())) {
                    educationqualification.requestFocus();
                    educationqualification.setError("Please feel Your Qualification");
                    return;
                } else if (TextUtils.isEmpty(ragistrationno.getText().toString())) {
                    ragistrationno.requestFocus();
                    ragistrationno.setError("Please feel Your Ragistration no");
                    return;
                } else if (service_id.equals("")) {
                    S.T(getActivity(), "Please Choose a Caregiver");
                } else if (specilization_id.equals("")) {
                    S.T(getActivity(), "Please Choose a Specilization");
                } else if (!checkimage.equals("Image")) {
                    S.T(getActivity(), "Please add document");
                } else if(!checkimage2.equals("Image")){
                    S.T(getActivity(), "Please add document");
                }else if(!checkImage3.equals("Image")){
                    S.T(getActivity(), "Please add document");
                } if (caregiversppiner.getSelectedItem().equals("CareGiver Type")) {
                    S.T(getActivity(), "Please select Care Giver type");
                } else if (cityIdstr.equals("")) {
                    S.T(getActivity(), "Please Select a City");
                } else {
                    addCareGiver();
                }
            }
        });
        return view;
    }

    private void getCityList() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GetCityList, 1, getParamCityName(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("City ka response" + response);
                    S.E("city ka param" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        JSONArray data = object1.getJSONArray("data");
                        citynameList.add("Select a City");
                        CityId.add("");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String ids = jsonObject1.getString("id");
                            String city_name = jsonObject1.getString("city_name");

                            if (current_city.equals(ids)) {
                                selectedPostion = i + 1;

                            }
                            citynameList.add(city_name);
                            CityId.add(ids);


                        }
                        ArrayAdapter<String> citystringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, citynameList);

                        spCityListId.setAdapter(citystringArrayAdapter);
                        spCityListId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cityIdstr = CityId.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        spCityListId.setSelection(selectedPostion);
                    } else {
//                        avi.setVisibility(View.GONE);
                        S.T(getActivity(), "NO data!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("printStackTrace" + e);
                }
            }
        });
    }

    private Map<String, String> getParamCityName() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("service_id", service_id);
        return params;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

 /*   @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= M) {
                        if (checkCameraPermission())
                            cameraIntent();
                        else
                            requestPermission();
                    } else
                        cameraIntent();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= M) {
                        if (checkGalleryPermission())
                            galleryIntent();
                        else
                            requestGalleryPermission();
                    } else
                        galleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    private void galleryIntent() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri cameraURI = data.getData();
            productImageBitmap = (Bitmap) data.getExtras().get("data");
            if(flag==1){
                documentImageView.setImageBitmap(productImageBitmap);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage = "Image";
            }else if(flag == 2){
                documentImageView1.setImageBitmap(productImageBitmap);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument1.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage2 = "Image";
            }else if(flag == 3){
                documentImageView2.setImageBitmap(productImageBitmap);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument2.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkImage3 = "Image";
            }


        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            if(flag == 1){
                documentImageView.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage = "Image";
            }else if(flag == 2){
                documentImageView1.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument1.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage2 = "Image";
            }else if(flag == 3){
                documentImageView2.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument2.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkImage3 = "Image";
            }

        }
    }

    private void addCareGiver() {
        final ProgressDialog progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage("Please wait");
        progressBar.setTitle("Processing");
        progressBar.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.UPDATE_CAREGIVER, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                S.E("Add Care giver " + resultResponse);
                progressBar.dismiss();
                try {

                    JSONObject mainObject = new JSONObject(resultResponse);
                    int status = mainObject.getInt("status");
                    String message = mainObject.getString("message");
                    if (status == 200) {

                        Toast.makeText(getActivity(), "CareGiver Successfully ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), CareGiverMainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "you already registered with Ambulance", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e)

                {
                    e.printStackTrace();
                }
            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                progressBar.dismiss();
                if (networkResponse == null) {

                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }

        )

        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> postParameters = new HashMap<>();
                postParameters.put("service_id", service_id);
                postParameters.put("specialization_id", specilization_id);
                postParameters.put("qualificatin", educationqualification.getText().toString());
                postParameters.put("registration_no", ragistrationno.getText().toString());
                postParameters.put("token", SavedData.gettocken_id());
                postParameters.put("current_city", cityIdstr);
                postParameters.put("type", "1");
                SavedData.saveType("1");
                Log.e("postParams", "postParams" + postParameters);
                return postParameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {

                    Bitmap bitmap3 = ((BitmapDrawable) documentImageView.getDrawable()).getBitmap();
                    Log.e("lincensId ", "Image_post" + bitmap3);
                    if (bitmap3 != null) {
                        params.put("document", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap3), "image/png"));
                    }

                    Bitmap bitmap4 = ((BitmapDrawable) documentImageView1.getDrawable()).getBitmap();
                    Log.e("lincensId ", "Image_post" + bitmap3);
                    if (bitmap4 != null) {
                        params.put("image1", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap4), "image/png"));
                    }

                    Bitmap bitmap5 = ((BitmapDrawable) documentImageView2.getDrawable()).getBitmap();
                    Log.e("lincensId ", "Image_post" + bitmap3);
                    if (bitmap5 != null) {
                        params.put("image2", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap5), "image/png"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(

                getActivity()

        ).addToRequestQueue(multipartRequest);

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }

    private void getServices() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GetService_Url, 1, getParamService(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("Service ka response" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        ServiceName.add("CareGiver Type");
                        serviceCareList.add(new CareGiverServiceModel());
                        JSONArray content1 = object1.getJSONArray("data");
                        for (int j = 0; j < content1.length(); j++) {
                            JSONObject jsonObject1 = content1.getJSONObject(j);
                            CareGiverServiceModel careGiverServiceModel = new CareGiverServiceModel();
                            careGiverServiceModel.setService_id(jsonObject1.getString("service_id"));
                            careGiverServiceModel.setService_name(jsonObject1.getString("service_name"));
                            careGiverServiceModel.setServiceCreatedDate(jsonObject1.getString("service_created_date"));
                            serviceCareList.add(careGiverServiceModel);
                            ServiceName.add(jsonObject1.getString("service_name"));
                        }

                        serviceAdapter12 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ServiceName);
                        caregiversppiner.setAdapter(serviceAdapter12);


                        S.E("size " + serviceCareList.size());

                    } else {
//                        avi.setVisibility(View.GONE);
                        S.T(getActivity(), "NO data!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getPreviousAmbulance_Data();

            }
        });
    }

    private Map<String, String> getParamService() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        S.E("getService ki request" + params);
        return params;

    }

    private void getSpecialiazation(String service_id) {

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GETSpecializationList_Url, 1, getParams(service_id), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E(response);
                    JSONObject object = new JSONObject(response);
                    careGiverSpecializationslist.clear();
                    careGiverSpecializations_id.clear();

                    serviceAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, careGiverSpecializations_id);
                    Specilization.setAdapter(serviceAdapter);

                    if (object.getString("status").equals("200")) {
                        JSONArray content = object.getJSONArray("data");


                        for (int i = 0; i < content.length(); i++) {
                            JSONObject jsonObject = content.getJSONObject(i);
                            careGiverSpecialization = new CareGiverSpecialization();
//                            careGiverServiceModel.setService_id(jsonObject.getString("service_id"));
                            careGiverSpecialization.setSpecialization(jsonObject.getString("specialization"));
                            careGiverSpecialization.setCaregiver_specialization_id(jsonObject.getString("caregiver_specialization_id"));
                            careGiverSpecializationslist.add(careGiverSpecialization);
                            careGiverSpecializations_id.add(jsonObject.getString("specialization"));
                        }
                        serviceAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, careGiverSpecializations_id);
                        Specilization.setAdapter(serviceAdapter);

                    } else {
//                        avi.setVisibility(View.GONE);
                        S.T(getActivity(), "NO data!");
                        Specilizationlayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams(String service_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", SavedData.gettocken_id());
        param.put("service_id", service_id);
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }

    private void getPreviousAmbulance_Data() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_PREVIOUS_CAREGIVER, 1, getPreviousData(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("Service ka response" + response);
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("200")) {
                        JSONArray content1 = object.getJSONArray("dada");
                        for (int j = 0; j < content1.length(); j++) {
                            JSONObject jsonObject = content1.getJSONObject(j);
                            educationqualification.setText(jsonObject.getString("service_detail"));
                            ragistrationno.setText(jsonObject.getString("registration_no"));
                            current_city = jsonObject.getString("current_city");

                            Picasso.with(getActivity()).load(Const.URL.Image_Url_CareGiver + jsonObject.getString("document")).error(R.drawable.ic_document)
                                    .into(documentImageView);
                            /*Specilization.setSelection(careGiverSpecializations_id.indexOf(jsonObject.getString("specialization")));
                            serviceAdapter.notifyDataSetChanged();*/

                            caregiversppiner.setSelection(ServiceName.indexOf(jsonObject.getString("service_name")));
                            serviceAdapter12.notifyDataSetChanged();
                            checkimage = "Image";
                        }
                    } else {

                        S.T(getActivity(), "NO data!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("printStackTrace" + e);
                }
            }
        });
    }

    private Map<String, String> getPreviousData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        S.E("getService ki request" + params);
        return params;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
