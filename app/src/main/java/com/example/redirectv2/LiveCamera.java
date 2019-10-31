package com.example.redirectv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveCamera.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveCamera#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveCamera extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static boolean Debuging = false;
    private int COUNT_DOWN = 5;
    private ImageButton FAB;
    private boolean isProcess = false;
    private Mat mRgba;
    private Mat mGray;
    private Mat mByte;
    private Scalar CONTOUR_COLOR;
    private CameraBridgeViewBase mOpenCvCameraView;
    private TextView counter;
    //Base Loader Callback function

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override

        public void onManagerConnected(int status) {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                    Log.i("OPENCV CAM" , "OpenCV Loaded successfully");
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                }
            }

        }
    };







    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public LiveCamera() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveCamera.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveCamera newInstance(String param1, String param2) {
        LiveCamera fragment = new LiveCamera();
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
       final View view = inflater.inflate(R.layout.fragment_img, container, false);
       counter = (TextView) view.findViewById(R.id.counter);
       mOpenCvCameraView = (CameraBridgeViewBase) view.findViewById(R.id.live_camera_feed);
       mOpenCvCameraView.setCvCameraViewListener(this);



        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getActivity(),mLoaderCallback);



       return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFrameUpdate(CameraBridgeViewBase.CvCameraViewFrame frame) {
        if (mListener != null) {
            mListener.onLiveCameraInteraction(frame);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onPause()
    {
        super.onPause();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,getActivity(),mLoaderCallback);
        if(mOpenCvCameraView != null)
        {
            mOpenCvCameraView.disableView();
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mOpenCvCameraView != null)
        {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC3);
        mByte = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        this.mListener.onLiveCameraInteraction(inputFrame);

        if(LiveCamera.Debuging)
        {
            TextDetector.DetectAndShow(mGray,mRgba);
        }

        return mRgba;
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
        void onLiveCameraInteraction(CameraBridgeViewBase.CvCameraViewFrame frame);
    }



}
