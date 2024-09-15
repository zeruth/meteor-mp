package com.meteor.rs2;

import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // This is executed on the main thread before the background task
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Perform network operation here
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Update UI with the result
        // Example: update a TextView
    }
}