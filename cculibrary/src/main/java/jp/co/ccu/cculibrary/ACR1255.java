package jp.co.ccu.cculibrary;

import com.acs.smartcardio.BluetoothSmartCard;
import com.acs.smartcardio.BluetoothTerminalManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

/* =================================================================================================
    SmartCardReader (ACR1255)
   ===============================================================================================*/
public class ACR1255 {

    private interface OnCommandSentListener {

        byte[] onCommandSent(Card card, byte[] command) throws CardException;
    }

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 5000;
    public boolean enable = false;

    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothTerminalManager mManager;
    public TerminalFactory mFactory;
    private Handler mHandler = new Handler();
    public String Uid ="";

    /* ---------------------------------------------------------------------------------------------
    コンストラクタ
    --------------------------------------------------------------------------------------------- */
    public ACR1255(Context context) {
        /*
         * Use this check to determine whether BLE is supported on the device.  Then you can
         * selectively disable BLE-related features.
         */
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(context.getApplicationContext(), R.string.error_bluetooth_le_not_supported,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        /*
         * Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
         * BluetoothAdapter through BluetoothManager.
         */
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        /* Checks if Bluetooth is supported on the device. */
        if (mBluetoothAdapter == null) {

            Toast.makeText(context.getApplicationContext(), R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("[mBluetoothAdapter.getName()]", String.format("%s", mBluetoothAdapter.getName()));

        /* Get the Bluetooth terminal manager. */
        mManager = BluetoothSmartCard.getInstance(context.getApplicationContext()).getManager();
        if (mManager == null) {

            Toast.makeText(context.getApplicationContext(), R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        /* Get the terminal factory. */
        mFactory = BluetoothSmartCard.getInstance(context.getApplicationContext()).getFactory();
        if (mFactory == null) {

            Toast.makeText(context.getApplicationContext(), R.string.error_bluetooth_provider_not_found,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("[mFactory.getProvider().getType()]", String.format("%s", mFactory.getType()));
/*
        mManager.startScan(BluetoothTerminalManager.TERMINAL_TYPE_ACR1255U_J1,
                new BluetoothTerminalManager.TerminalScanCallback() {
                    @Override
                    public void onScan(final CardTerminal terminal) {
                        Log.d("[terminal.getName()]", String.format("%s", terminal.getName()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                });
// Stop the scan.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                    mManager.stopScan();
//                    mScanButton.setEnabled(true);
            }
        }, SCAN_PERIOD);
        */
        enable = true;
        return;
    }
    /* ---------------------------------------------------------------------------------------------
    カード読取
    --------------------------------------------------------------------------------------------- */
    public void ReadCard(final CardTerminal terminal) {
//        Log.d("[terminal.getName()]", String.format("%s", terminal.getName()));

        try {
            while (terminal.waitForCardPresent(0)) {
                Card card = terminal.connect("*");
                byte[] command = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};

                Uid = runScript(card, command, new OnCommandSentListener() {

                    @Override
                    public byte[] onCommandSent(Card card, byte[] command)
                            throws CardException {
                        CardChannel channel = card.getBasicChannel();
                        CommandAPDU commandAPDU = new CommandAPDU(command);
                        ResponseAPDU responseAPDU = channel.transmit(commandAPDU);
//                                                Log.d("[terminal.getName()]", String.format("%s", responseAPDU.getBytes().toString()));
                        return responseAPDU.getBytes();
                    }
                });
                Log.d("ReadCard Uid", String.format("%s", Uid));
                card.disconnect(false);
                break;
            }
        } catch (CardException e) {
            Log.d("Error:", String.format("%s", e.getMessage()));
        }
    }
    /* ---------------------------------------------------------------------------------------------
    UID取得
    --------------------------------------------------------------------------------------------- */
    private String runScript(Card card, byte[] command, OnCommandSentListener listener) {

        String l_Uid ="";
        boolean commandLoaded = false;
        boolean responseLoaded = false;

        /* Read the first line. */
        if (!commandLoaded) {
            if ((command != null) && (command.length > 0)) {
                commandLoaded = true;
            }
        }

        if (commandLoaded && responseLoaded) {
            return "";
        }

        try {
            int numCommands = 0;
            while (true) {
                /* Send the command. */
                byte [] response = listener.onCommandSent(card, command);
                int size = response.length;
                if (response[size - 2] == (byte)0x90 && response[size - 1] == (byte)0x00) {

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < size - 2; i++) {
                        sb.append(String.format("%02X", response[i]));
                    }
                    l_Uid = sb.toString();
                    Log.d("runScript UID", String.format("%s", l_Uid));
                    break;
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {

            Log.d("Error", String.format("%s", e.getMessage()));

        } catch (CardException e) {
            Log.d("Error", String.format("%s", e.getMessage()));
            Throwable cause = e.getCause();

        } finally {
            return  l_Uid;
        }
    }
}
