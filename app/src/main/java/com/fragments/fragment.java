package com.fragments;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class fragment extends AppCompatActivity implements UserFragment.OnFragmentInteractionListener {

    public void onFragmentInteraction(String id)
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the user interface layout for this Activity
        // The layout file is defined in the project res/layout/main_activity.xml file
        setContentView(R.layout.activity_fragment);

    }
    @Override
    public void onStart()
    {
        super.onStart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.skynet_search:
                openSearch();
                return true;
            case R.id.skynet_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //New Addition
    String TAG = "SkynetLog";
    NsdManager nsdManager;
    NsdManager.RegistrationListener registrationListener;
    String ServiceName;
    String ServiceType = "_skynetAI._tcp.";
    public void openSearch()
    {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        int m= wi.getIpAddress();
        String ip = Formatter.formatIpAddress(m);
        String mac = wi.getMacAddress();
        ServiceName = ip + "_"+mac;
        Log.d(TAG, ServiceName);
        registerService(3746);
    }
    public void registerService(int port)
    {
        tearDown();  // Cancel any previous registration request
        initializeRegistrationListener();
        nsdManager = (NsdManager)getSystemService(NSD_SERVICE);
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(ServiceName);
        serviceInfo.setServiceType(ServiceType);
        if (registrationListener!=null)
        {
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
        }
    }
    public void tearDown() {
        if (registrationListener != null) {
            try {
                nsdManager.unregisterService(registrationListener);
            }
            finally {
            }
            registrationListener = null;
        }
    }
    public void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                ServiceName = NsdServiceInfo.getServiceName();
                Log.d(TAG, "Service registered: " + ServiceName);
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Log.d(TAG, "Service registration failed: " + arg1);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.d(TAG, "Service unregistered: " + arg0.getServiceName());
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "Service unregistration failed: " + errorCode);
            }

        };
    }
}
