package com.filper.login_with_facebook;


import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.*;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.filper.notifications.ManageService;
import com.filper.facebook.R;
import com.filper.location.ManageLocation;
import com.filper.tutorial.TutorialActivity;
import com.filper.webService.WebService;;
//import com.facebook.widget.ProfilePictureView;

public class SessionLoginFragment extends Fragment {

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private ManageService serviceManager;
    private ManageLocation manageLocation;
    private WebService webService;

    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filper_login, container, false);
        serviceManager = new ManageService();
        buttonLoginLogout = (Button) view.findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView) view.findViewById(R.id.instructionsOrLink);
        manageLocation = new ManageLocation(getActivity());
        //profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilepic);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(getActivity(), null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(getActivity());
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
                              
        updateView();

        return view;
    }
    
    
    
    

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    /**
     * Actualiza la vista del fragment actual
     */
	private void updateView() {
        final Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	webService = (WebService) this.getActivity().getApplication();
            buttonLoginLogout.setText(R.string.logout);            
            buttonLoginLogout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });   
            
            
            Request.newMeRequest(session, new Request.GraphUserCallback() {                	
				@Override
				public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Display the parsed user info                         	
                    	iniciaServicio(user, session);
                    	textInstructionsOrLink.setText("Conectado");  
                        String[] aux = manageLocation.getCoordenates();
                        Intent intent = new Intent(SessionLoginFragment.this.getActivity(), TutorialActivity.class);                        
                        startActivity(intent);
                    }					
				}
            }).executeAsync();
            
            
            
        } else {
            textInstructionsOrLink.setText(R.string.instructions);
            buttonLoginLogout.setText(R.string.login);
            //profilePictureView.setProfileId(null);
            buttonLoginLogout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

    
	private void iniciaServicio(GraphUser user, Session session)
	{
		serviceManager.startService(user.getId(), session.getAccessToken(), user.getFirstName(), "ws://198.199.120.36:9000", getActivity());
	}
	
	/*
    private String buildUserInfoDisplay(GraphUser user, Session session) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        userInfo.append(String.format("Username: %s\n\n", 
            user.getUsername()));
        userInfo.append(String.format("Id: %s\n\n", 
                user.getId()));
        userInfo.append(String.format("AccessToken: %s\n\n",
        		session.getAccessToken()));
        userInfo.append(String.format("Fist name: %s\n\n", 
                user.getFirstName()));
        //profilePictureView.setProfileId(user.getId());
        return userInfo.toString();
    }    */
    
    
    
    
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            //Log.d("com.filper.facebook", "asd1: " + session);
        } else {
            Session.openActiveSession(getActivity(), this, true, statusCallback);
            //Log.d("com.filper.facebook", "asd2: " + session);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            serviceManager.stopService(getActivity());
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
    
    
}