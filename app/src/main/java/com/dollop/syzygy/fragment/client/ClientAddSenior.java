package com.dollop.syzygy.fragment.client;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.activity.client.ClientSeniorListActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.multipart.AppHelper;
import com.dollop.syzygy.sohel.multipart.VolleyMultipartRequest;
import com.dollop.syzygy.sohel.multipart.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientAddSenior.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClientAddSenior#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientAddSenior extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button BtnSaveSeniorId;
    CircleImageView SeniorImageLoadId;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    private static final int REQUEST = 1337;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    Uri mImageCaptureUri;
    Bitmap productImageBitmap;
    EditText EtSeniorNameId, EtAgeId, EtMobilrId, etRelationId,EtDescriptionId, EtNeedId, EtInstrutionId, EtAddressId;
    RadioGroup RadioGroupId;
    RadioButton maleBtn;
    RadioButton femaleBtn;

    String Token;
    Unbinder unbinder;
    private OnFragmentInteractionListener mListener;

    public ClientAddSenior() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientAddSenior.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientAddSenior newInstance(String param1, String param2) {
        ClientAddSenior fragment = new ClientAddSenior();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_add_senior, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((ClientMainActivity) getActivity()).launchFragmentTitle("People You Care");
        Token = SavedData.gettocken_id();
        Log.e("Token", "Token121" + Token);

        EtSeniorNameId = (EditText) view.findViewById(R.id.EtSeniorNameId);
        EtAgeId = (EditText) view.findViewById(R.id.EtAgeId);
        EtAddressId = (EditText) view.findViewById(R.id.EtAddressId);
        EtMobilrId = (EditText) view.findViewById(R.id.EtMobilrId);
        EtDescriptionId = (EditText) view.findViewById(R.id.EtDescriptionId);
        etRelationId = (EditText) view.findViewById(R.id.etRelationId);
        EtInstrutionId = (EditText) view.findViewById(R.id.EtInstrutionId);
        EtNeedId = (EditText) view.findViewById(R.id.EtNeedId);
        BtnSaveSeniorId = (Button) view.findViewById(R.id.BtnSaveSeniorId);
        RadioGroupId = (RadioGroup) view.findViewById(R.id.RadioGroupId);
        maleBtn = (RadioButton) view.findViewById(R.id.maleBtn);
        femaleBtn = (RadioButton) view.findViewById(R.id.femaleBtn);
        SeniorImageLoadId = (CircleImageView) view.findViewById(R.id.SeniorImageLoadId);
        SeniorImageLoadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        BtnSaveSeniorId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAccount.isEmpty(EtSeniorNameId, EtAgeId, EtMobilrId)) {
                    addSenior();
                } else {
                    UserAccount.EditTextPointer.setError("Fields Can't be Empty!");
                    UserAccount.EditTextPointer.requestFocus();

                }

            }
        });
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void SelectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkCameraPermission())
                            cameraIntent();
                        else
                            requestPermission();
                    } else
                        cameraIntent();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
            SeniorImageLoadId.setImageBitmap(productImageBitmap);
            SeniorImageLoadId.setVisibility(View.VISIBLE);

        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            SeniorImageLoadId.setImageURI(galleryURI);
        }

    }


    public void addSenior() {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.Add_Senior_Url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                try {
                    JSONObject mainObject = new JSONObject(resultResponse);
                    int status = mainObject.getInt("status");
                    String message = mainObject.getString("message");
                    if (status == 200) {
/*
                        Toast.makeText(getActivity(), "Senior Added Successfull", Toast.LENGTH_SHORT).show();
*/
                        getActivity().finish();
                    } else {
/*
                        Toast.makeText(getActivity(), "SeniorNot Added", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (
                        JSONException e
                        )

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
                Map<String, String> postParameters = new HashMap<>();
                postParameters.put("token", Token);
                Log.e("Token", "Token" + Token);
                postParameters.put("senior_name", EtSeniorNameId.getText().toString());
                postParameters.put("senior_age", EtAgeId.getText().toString());
                postParameters.put("address", EtAddressId.getText().toString());
                postParameters.put("special_instruction", EtInstrutionId.getText().toString());
                postParameters.put("special_needs", EtNeedId.getText().toString());
                postParameters.put("description", EtDescriptionId.getText().toString());
                if (maleBtn.isChecked()) {
                    postParameters.put("senior_gender", "Male");
                } else {
                    postParameters.put("senior_gender", "Female");
                }
                postParameters.put("senior_phone", EtMobilrId.getText().toString());
                Log.e("", "" + postParameters);
                return postParameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) SeniorImageLoadId.getDrawable()).getBitmap();
                    Log.e("Image_post ", "Image_post" + bitmap);
                    if (bitmap != null)
                        params.put("profile_pic", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(

                getActivity()

        ).

                addToRequestQueue(multipartRequest);

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }
}
