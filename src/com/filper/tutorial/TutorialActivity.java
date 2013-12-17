package com.filper.tutorial;

import com.filper.app.R;
import com.filper.webService.WebService;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.filper.webService.WebService;


public class TutorialActivity extends Activity{
	public float init_x;
    private ViewFlipper vf;
    private WebService webService;
    private int layoutCount = 0;
    private int maxLayout = 2;
    
	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_activity_tutorial);	 
	        vf = (ViewFlipper) findViewById(R.id.viewFlipper);
	        webService = (WebService)  getApplication();	        
	        vf.setOnTouchListener(new ListenerTouchViewFlipper());
	        
	    }
	   
	   private class ListenerTouchViewFlipper implements View.OnTouchListener{
		   
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	 
	            switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN: //Cuando el usuario toca la pantalla por primera vez
	                init_x=event.getX();
	                return true;
	            case MotionEvent.ACTION_UP: //Cuando el usuario deja de presionar
	                float distance =init_x-event.getX();
	 
	                if(distance<0)
	                {
	                     
	 					if (layoutCount > 0){													
							
		                    vf.setInAnimation(inFromLeftAnimation());
		                    vf.setOutAnimation(outToRightAnimation());
							vf.showPrevious();
							layoutCount--;
							Log.d("Tutorial", "tutorial " + layoutCount);	
						}
	                }
	 
	                if(distance>0)
	                {
	                	if (layoutCount <  maxLayout){		                	 						
	                	 vf.setInAnimation(inFromRightAnimation());
		                 vf.setOutAnimation(outToLeftAnimation());
	                     vf.showNext(); 
	                     layoutCount++;
	                     Log.d("Tutorial", "tutorial " + layoutCount);
	                	}
	                }
	 
	            default:
	                break;
	            }
	 
	            return false; 
	        }

	 
	    }
	
	   private Animation inFromRightAnimation() {
		   
	        Animation inFromRight = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
	        Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f );
	 
	        inFromRight.setDuration(500);
	        inFromRight.setInterpolator(new AccelerateInterpolator());
	 
	        return inFromRight;
	 
	    }
	 
	    private Animation outToLeftAnimation() {
	        Animation outtoLeft = new TranslateAnimation(
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, -1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoLeft.setDuration(500);
	        outtoLeft.setInterpolator(new AccelerateInterpolator());
	        return outtoLeft;
	    }
	 
	    private Animation inFromLeftAnimation() {
	        Animation inFromLeft = new TranslateAnimation(
	                Animation.RELATIVE_TO_PARENT, -1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        inFromLeft.setDuration(500);
	        inFromLeft.setInterpolator(new AccelerateInterpolator());
	        return inFromLeft;
	    }
	 
	    private Animation outToRightAnimation() {
	        Animation outtoRight = new TranslateAnimation(
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, +1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoRight.setDuration(500);
	        outtoRight.setInterpolator(new AccelerateInterpolator());
	        return outtoRight;
	    }
	
}
