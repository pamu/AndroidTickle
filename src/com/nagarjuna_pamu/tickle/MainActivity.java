package com.nagarjuna_pamu.tickle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	
    	private EditText msg;
    	private Button btn;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            /**
             * Inflate Edit Text from XML
             */
            msg = (EditText) rootView.findViewById(R.id.editText1);
            
            /**
             * Inflate Button from XML
             */
            btn = (Button) rootView.findViewById(R.id.button1);
            
            btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					/**
			    	 * get the next from Edit Text View
			    	 */
			    	String message = msg.getText().toString();
			    	
			    	if(message.trim().equals("")) {
			    		
			    		Toast.makeText(getActivity(),"Message is Empty" , Toast.LENGTH_LONG).show();
			    		
			    		/**
			    		 * Don't send any message as message is empty
			    		 */
			    		
			    	}else{
			    		
			    		Toast.makeText(getActivity(),"message typed := "+message.trim() , Toast.LENGTH_LONG).show();
			    		
			    		/**
			    		 *Now send an intent to MessageSender Service
			    		 */
			    		Intent sendIntent = new Intent(getActivity().getApplicationContext(), MessageSenderService.class);
			    		/**
			    		 * wrap the message in the Bundle
			    		 */
			    		Bundle args = new Bundle();
			    		args.putString("message", message);
			    		/**
			    		 * attach the Bundle to the Intent
			    		 */
			    		sendIntent.putExtras(args);
			    		/**
			    		 * Invoke the service with the above intent (call is asynchronous)
			    		 */
			    		getActivity().startService(sendIntent);
			    	}
					
				}
			});
            
            return rootView;
        }
    }
    
}
