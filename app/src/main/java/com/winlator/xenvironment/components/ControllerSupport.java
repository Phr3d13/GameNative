package com.winlator.xenvironment.components;

import android.content.Context;
import android.util.Log;

import com.winlator.core.envvars.EnvVars;
import com.winlator.inputcontrols.ControllerManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ControllerSupport {
    private static final String TAG = "ControllerSupport";

    /**
     * Create memory files for enabled players and set EVSHIM_MAX_PLAYERS in envVars.
     * Returns the enabled player count (at least 1).
     */
    public static int setupGamepadMemFiles(Context context, EnvVars envVars) {
        int enabledPlayerCount = 1;
        try {
            ControllerManager cm = ControllerManager.getInstance();
            cm.init(context.getApplicationContext());
            enabledPlayerCount = cm.getEnabledPlayerCount();
            if (enabledPlayerCount <= 0) enabledPlayerCount = 1;
        } catch (Exception e) {
            Log.w(TAG, "Failed to query ControllerManager, defaulting to 1", e);
            enabledPlayerCount = 1;
        }

        for (int i = 0; i < enabledPlayerCount; i++) {
            String memPath;
            if (i == 0) {
                memPath = "/data/data/app.gamenative/files/imagefs/tmp/gamepad.mem";
            } else {
                memPath = "/data/data/app.gamenative/files/imagefs/tmp/gamepad" + i + ".mem";
            }

            File memFile = new File(memPath);
            memFile.getParentFile().mkdirs();
            try (RandomAccessFile raf = new RandomAccessFile(memFile, "rw")) {
                raf.setLength(64);
            } catch (IOException e) {
                Log.e(TAG, "Failed to create mem file for player index " + i, e);
            }
        }

        if (envVars != null) {
            envVars.put("EVSHIM_MAX_PLAYERS", String.valueOf(enabledPlayerCount));
        }

        return enabledPlayerCount;
    }
}
