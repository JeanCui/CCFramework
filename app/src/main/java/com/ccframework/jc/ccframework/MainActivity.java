package com.ccframework.jc.ccframework;


import com.ccframework.weibo.activities.WBAuthActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ccframework.facebook.activities.FacebookActivity;
import com.ccframework.twitter.activities.TwitterActivity;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

import static android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "as4qY871B3ERmyeUJdZFOO8PH";
    private static final String TWITTER_SECRET = "zGU6KhwVw777yBDPlQ6S79WLXAwYrao5szZ4gTmUQDIwmdFa2r";

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private static final String TAG = "MainActivity";
    public static boolean PICK_IMAGE_FINISH = false;
    public static String mImagePath;

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"Facebook","Twitter","Email","Picture","Save"};
    int ICONS[] = {R.drawable.ic_fb,R.drawable.ic_twitter,R.drawable.ic_email,R.drawable.ic_pic,R.drawable.ic_save};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Jin Cui";
    String EMAIL = "cuijin92@gmail.com";
    int PROFILE = R.drawable.avatar;


    private DrawPanel drawPanel;
    private Toolbar toolBar;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle


    private FloatingActionButton bubbleControlFAB;
    private FloatingActionMenu bubbleControlMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawPanel = (DrawPanel)findViewById(R.id.draw);
        toolBar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened when drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State



        final View actionB = findViewById(R.id.action_b);
        actionB.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent fb_intent;
                fb_intent = new Intent(MainActivity.this, FacebookActivity.class);
                startActivity(fb_intent);

            }
        });

        final View actionA = findViewById(R.id.action_a);
        actionA.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent tw_intent;
                tw_intent = new Intent(MainActivity.this, TwitterActivity.class);
                startActivity(tw_intent);

            }
        });

        final View actionC = findViewById(R.id.action_c);
        actionC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wb_intent;
                wb_intent = new Intent(MainActivity.this, WBAuthActivity.class);
                startActivity(wb_intent);
            }
        });

        final View actionD = findViewById(R.id.action_d);
        actionD.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);


            }
        });


        ViewTreeObserver vto = drawPanel.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int startX = drawPanel.getLeft();
                int startY = drawPanel.getTop();

                int toolBarWidth = toolBar.getHeight();
                FloatingActionButton.LayoutParams lp = new FloatingActionButton.LayoutParams(100, 100);
//                lp.leftMargin = startX + 200;
//                lp.topMargin = startY + 200 + toolBarWidth;
                bubbleControlFAB.setLayoutParams(lp);

                drawPanel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



        // Set up the white button on the lower right corner
        // more or less with default parameter
        final ImageView fabIconNew = new ImageView(this);
//        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));



        bubbleControlFAB =  new FloatingActionButton.Builder(this).setPosition(8)
                .setContentView(fabIconNew)
                .build();
//        bubbleControlFAB.setVisibility(View.INVISIBLE);


        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);



        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_chat_light));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera_light));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_video_light));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_place_light));


        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        bubbleControlMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .setRadius(150)
                .attachTo(bubbleControlFAB)
                .build();

        // Listen menu open and close events to animate the button content view
        bubbleControlMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of bubbleControlFAB 45 degrees clockwise

//                fabIconNew.setRotation(0);
//                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
//                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
//                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of bubbleControlFAB 45 degrees counter-clockwise

//                fabIconNew.setRotation(45);
//                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
//                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
//                animation.start();
            }
        });


//        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
//        actionC.setTitle("Hide/Show Action above");
//        actionC.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//            }
//        });
//        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(actionC);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Fabric.with(this, new TweetComposer());

    }


    public void setBubbleControlFABPosition(int x, int y){
        int startX = drawPanel.getLeft();
        int startY = drawPanel.getTop();

        int toolBarHeight = toolBar.getHeight();
        FloatingActionButton.LayoutParams lp = new FloatingActionButton.LayoutParams(100, 100);
        lp.leftMargin = startX + x;
        lp.topMargin = startY + y + toolBarHeight;
        bubbleControlFAB.setLayoutParams(lp);

    }

    public void performClickOnBubbleFAB(){
//        bubbleControlMenu.open(true);
        bubbleControlFAB.performClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
//        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();
                // NEED MORE TEST HERE
                mImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
                PICK_IMAGE_FINISH = true;

            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
