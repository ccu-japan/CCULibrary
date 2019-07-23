package jp.co.ccu.cculibrary;

import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mMacAddress {
        private static final String TAG = jp.co.ccu.cculibrary.mMacAddress.class.getSimpleName();
        private static StringBuilder mStringBuilder = new StringBuilder();

        public String getMacAddressString() {

            Map<String, String> macs = getMacAddress();

            mStringBuilder.setLength(0);
            for (Map.Entry<String, String> e : macs.entrySet()) {

                mStringBuilder.append("intf name:" + e.getKey());
                mStringBuilder.append(", mac:" + e.getValue());
                mStringBuilder.append("\n");
            }
            return mStringBuilder.toString();
        }

        public Map<String, String> getMacAddress() {
            HashMap<String, String> macs = new HashMap<String, String>();
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

                Log.d(TAG,"intf num:" + interfaces.size());
                for (NetworkInterface intf : interfaces) {
                    String name = intf.getName();
                    if (null != name) {
                        byte[] raw = intf.getHardwareAddress();
                        if (null != raw) {
                            String mac = getMacString(raw);
                            macs.put(name, mac);
                            Log.d(TAG,"intf name:" + name + ", mac:" + mac);
                        } else {
                            macs.put(name, null);
                            Log.d(TAG,"intf name:" + name + ", mac: null");
                        }
                    }
                }
            } catch(Exception e) {
                Log.d(TAG, "exception occured:", e);
                return null;
            }
            return macs;
        }

        private static String getMacString(byte[] raw) {
            mStringBuilder.setLength(0);
            for (int i = 0; i < raw.length; i++) {
                mStringBuilder.append(String.format("%02X ", raw[i]));
            }
            return mStringBuilder.toString();
        }
}
