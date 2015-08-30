package com.mitr.mitr.ui;

import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mitr.mitr.R;
import com.mitr.mitr.web.Handled;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;


public class MainActivity extends AppCompatActivity implements Handled {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "X0Pd9oaacmx5UXlMbvDc14lrvowK84f2FJTa9P6I", "UvI56IzEQ3J6ZN2PCIq6FOYuNCj7mCHK6JatsRsT");
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        System.out.print("Registering device for PUSH notif : " + deviceId);
        ParseInstallation.getCurrentInstallation().put("device_id", deviceId);
        ParseInstallation.getCurrentInstallation().saveInBackground();





        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("View Questions"));
        tabLayout.addTab(tabLayout.newTab().setText("My Questions"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handlerCallback(Message message) {

    }

    /**
     *
     * Sending a PUSH notif - syntax using Parse
     *
     * curl -X POST \
     -H "X-Parse-Application-Id: X0Pd9oaacmx5UXlMbvDc14lrvowK84f2FJTa9P6I" \
     -H "X-Parse-REST-API-Key: w47C4JOB6yQh80XQvMq58erKzQFiSnRjSe8gVK7a" \
     -H "Content-Type: application/json" \
     -d '{
     "where": {
     "device_id": "1234567890"
     },
     "data": {
     "alert": "Hello Mitra!"
     }
     }' \
     https://api.parse.com/1/push
     *
     */
}