package com.ccframework.twitter.activities;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ccframework.jc.ccframework.R;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class TwitterActivity extends ActionBarActivity {

    private TwitterLoginButton loginButton;
    private String userToken;
    private String userSecret;
    private String userName;
    private TextView userNameText;
    private Button twitterBtn;
    private URL imageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        File myImageFile = new File("/storage/emulated/0/DCIM/Stickers/FB.jpeg");
        final Uri myImageUri = Uri.fromFile(myImageFile);

        try {
            imageUrl = new URL("http://ow.ly/K8q2F");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        twitterBtn = (Button) findViewById(R.id.compose);
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TweetComposer.Builder builder = new TweetComposer.Builder(TwitterActivity.this)
                        .text("just setting up my Fabric.").url(imageUrl);

                builder.show();
            }
        });
        userNameText = (TextView) findViewById(R.id.user_name);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session =
                        Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                userToken = authToken.token;
                userSecret = authToken.secret;
                userName = session.getUserName();
                userNameText.setText("Welcome " + userName + "!");

                twitterBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                userNameText.setText("Login Failed!");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
