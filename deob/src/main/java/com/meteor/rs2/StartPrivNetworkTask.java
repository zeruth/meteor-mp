package com.meteor.rs2;

import org.openrs2.deob.annotation.Pc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jagex2.client.Client;
import sign.signlink;

public class StartPrivNetworkTask extends NetworkTask{
    InetAddress host;
    @Override
    protected String doInBackground(Void... voids) {
        try {
            host = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "host";
    }

    @Override
    protected void onPostExecute(String result) {
        signlink.startpriv(host);
        Client client = new Client();
        client.announceInstance();
        client.initApplication(789, 532);
    }
}
