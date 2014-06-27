package de.jalin.droid.sketch;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;


public class SettingActivity extends PreferenceActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBuildHeaders(final List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
       if (item.getItemId() == android.R.id.home) {
    	   finish();
       }
       return false;
    }
    
    @Override
    protected boolean isValidFragment(final String fragmentName) {
    	final String emailPrefsClassName = EMailPrefsFragment.class.getName();
		if (emailPrefsClassName.equals(fragmentName)) {
    		return true;
    	}
    	final String colorsPrefsClassName = ColorsPrefsFragment.class.getName();
		if (colorsPrefsClassName.equals(fragmentName)) {
    		return true;
    	}
    	return false;
    }
    
    public static class EMailPrefsFragment extends PreferenceFragment {
    	
    	@Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.email_preferences);
        }
    }
    
    public static class ColorsPrefsFragment extends PreferenceFragment {
    	
    	@Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.color_preferences);
        }
    }
}
