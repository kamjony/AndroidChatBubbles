package com.example.androidbubble;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ChatHeadService extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private View floatingView;
    private View collapsedView;
    private View expandedView;

    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //using layout inflator, inflate the chat head layout
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head, null);

        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the chat head position
        params.gravity = Gravity.CENTER | Gravity.RIGHT;        //Initially view will be added to center|right corner
        params.x = 0;
        params.y = 100;


        //setting the floatingView to window services
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);


        //initialize collapsed and expanded view
        collapsedView = floatingView.findViewById(R.id.collapsedLayout);
        expandedView = floatingView.findViewById(R.id.expandedLayout);

        //adding click listener to close button and expanded view
        floatingView.findViewById(R.id.btnClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        //adding onTouch() actions when user's drag the chat head
        floatingView.findViewById(R.id.parentLayout).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //initializing the inital position
                        initialX = params.x;
                        initialY = params.y;
                        //get the location the user touched
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        //when the drag is ended switching the state of the widget
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //Calculate X and Y coordinates of the view
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //update layout with the latest X Y coordinates
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Remove chat head when this method is called
        if (floatingView != null) windowManager.removeView(floatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expandedLayout:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.btnClose:
                //closing the widget
                stopSelf();
                break;
        }
    }
}