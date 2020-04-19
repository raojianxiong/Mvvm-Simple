package com.rjx.network.environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.widget.Toast;

import com.rjx.network.R;

/**
 * @author Jianxiong Rao
 * @date 2020-04-15
 */
public class EnvironmentActivity extends AppCompatActivity {
    public static final String NETWORK_ENVIRONMENT_PREF_KEY = "network_environment_type";
    private static String sCurrentNetworkEnvironment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmen);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new MyPreferenceFragment())
                .commit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sCurrentNetworkEnvironment = prefs.getString(EnvironmentActivity.NETWORK_ENVIRONMENT_PREF_KEY, "1");
    }

    public static class MyPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.environment_preference);
            findPreference(NETWORK_ENVIRONMENT_PREF_KEY).setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (!sCurrentNetworkEnvironment.equalsIgnoreCase(String.valueOf(newValue))) {
                Toast.makeText(getContext(), R.string.network_change_hint, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String newValue = prefs.getString(EnvironmentActivity.NETWORK_ENVIRONMENT_PREF_KEY,"1");
        if(!sCurrentNetworkEnvironment.equalsIgnoreCase(newValue)){
            Process.killProcess(Process.myPid());
        }else{
            finish();
        }
    }

    public static boolean isOfficialEnvironment(Application application) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        String environment = prefs.getString(EnvironmentActivity.NETWORK_ENVIRONMENT_PREF_KEY, "1");
        return "1".equalsIgnoreCase(environment);
    }

}
