package com.winlator.xenvironment.components;

import static com.winlator.core.ProcessHelper.splitCommand;

import android.content.Context;
import android.media.Image;
import android.os.Process;
import android.util.Log;

import com.winlator.PrefManager;
import com.winlator.box86_64.Box86_64Preset;
import com.winlator.box86_64.Box86_64PresetManager;
import com.winlator.contents.ContentProfile;
import com.winlator.contents.ContentsManager;
import com.winlator.core.Callback;
import com.winlator.core.DefaultVersion;
import com.winlator.core.FileUtils;
import com.winlator.core.envvars.EnvVars;
import com.winlator.core.ProcessHelper;
import com.winlator.core.TarCompressorUtils;
import com.winlator.xconnector.UnixSocketConfig;
import com.winlator.xenvironment.ImageFs;
import com.winlator.container.Container;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import app.gamenative.service.SteamService;

public class GlibcProgramLauncherComponent extends GuestProgramLauncherComponent {
    private String guestExecutable;
    private volatile int pid = -1; // Made instance-based and volatile for thread safety
    private String[] bindingPaths;
    private EnvVars envVars;
    private String box86Version = DefaultVersion.BOX86;
    private String box64Version = DefaultVersion.BOX64;
    private String box86Preset = Box86_64Preset.COMPATIBILITY;
    private String box64Preset = Box86_64Preset.COMPATIBILITY;
    private String steamType = DefaultVersion.STEAM_TYPE;
    private Callback<Integer> terminationCallback;
    private final Object lock = new Object(); // Made instance-based
    private boolean wow64Mode = true;
    private final String instanceId; // Track launcher instance for logging
    private final ContentsManager contentsManager;
    private final ContentProfile wineProfile;
    private File workingDir;

    public GlibcProgramLauncherComponent(ContentsManager contentsManager, ContentProfile wineProfile) {
        this.contentsManager = contentsManager;
        this.wineProfile = wineProfile;
        this.instanceId = "glibc-" + System.currentTimeMillis() + "-" + hashCode();
        Log.d("GlibcProgramLauncherComponent", "Created launcher instance: " + instanceId);
    }

    private Runnable preUnpack;
    public void setPreUnpack(Runnable r) { this.preUnpack = r; }
    @Override
    public void start() {
        Log.d("GlibcProgramLauncherComponent", "Starting launcher instance: " + instanceId);
        synchronized (lock) {
            // Don't call stop() here - let caller decide if they want to stop existing process
            if (pid != -1) {
                Log.w("GlibcProgramLauncherComponent", "Instance " + instanceId + " already has running process: " + pid);
                return;
            }
            
            extractBox64Files();
            copyDefaultBox64RCFile();
            if (preUnpack != null) preUnpack.run();
            pid = execGuestProgram();
            Log.d("GlibcProgramLauncherComponent", "Instance " + instanceId + " started process: " + pid);
            
            // Only set global state if we successfully started
            if (pid != -1) {
                SteamService.setGameRunning(true);
            }
        }
    }

    @Override
    public void stop() {
        Log.d("GlibcProgramLauncherComponent", "Stopping launcher instance: " + instanceId);
        synchronized (lock) {
            if (pid != -1) {
                Log.d("GlibcProgramLauncherComponent", "Instance " + instanceId + " stopping process: " + pid);
                
                // Try graceful shutdown first
                ProcessHelper.terminateProcess(pid);
                
                // Wait briefly for graceful shutdown
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Force kill if still running
                Process.killProcess(pid);
                Log.d("GlibcProgramLauncherComponent", "Instance " + instanceId + " stopped process: " + pid);
                
                int stoppedPid = pid;
                pid = -1;
                
                // Only clear global state if this was the last running instance
                // TODO: Implement proper reference counting for multiple game instances
                SteamService.setGameRunning(false);
                
                // Clean up only processes that are children of our specific PID
                cleanupChildProcesses(stoppedPid);
            }
        }
    }
    
    /**
     * Clean up only child processes of the specified PID, not all user processes
     */
    private void cleanupChildProcesses(int parentPid) {
        List<ProcessHelper.ProcessInfo> subProcesses = ProcessHelper.listSubProcesses();
        for (ProcessHelper.ProcessInfo subProcess : subProcesses) {
            // Only kill processes that are actually children of our process
            if (subProcess.ppid == parentPid) {
                Log.d("GlibcProgramLauncherComponent", 
                        "Instance " + instanceId + " cleaning up child process: " 
                        + subProcess.name + " (PID: " + subProcess.pid + ")");
                Process.killProcess(subProcess.pid);
            }
        }
    }

    public Callback<Integer> getTerminationCallback() {
        return terminationCallback;
    }

    public void setTerminationCallback(Callback<Integer> terminationCallback) {
        this.terminationCallback = terminationCallback;
    }

    public String getGuestExecutable() {
        return guestExecutable;
    }

    public void setGuestExecutable(String guestExecutable) {
        this.guestExecutable = guestExecutable;
    }

    public boolean isWoW64Mode() {
        return wow64Mode;
    }

    public void setWoW64Mode(boolean wow64Mode) {
        this.wow64Mode = wow64Mode;
    }

    public String[] getBindingPaths() {
        return bindingPaths;
    }

    public void setBindingPaths(String[] bindingPaths) {
        this.bindingPaths = bindingPaths;
    }

    public EnvVars getEnvVars() {
        return envVars;
    }

    public void setEnvVars(EnvVars envVars) {
        this.envVars = envVars;
    }

    public String getBox86Version() { return box86Version; }

    public void setBox86Version(String box86Version) { this.box86Version = box86Version; }

    public String getBox64Version() { return box64Version; }

    public void setBox64Version(String box64Version) { this.box64Version = box64Version; }

    public String getBox86Preset() {
        return box86Preset;
    }

    public void setBox86Preset(String box86Preset) {
        this.box86Preset = box86Preset;
    }

    public String getBox64Preset() {
        return box64Preset;
    }

    public void setBox64Preset(String box64Preset) {
        this.box64Preset = box64Preset;
    }

    public File getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }

    private int execGuestProgram() {
        Context context = environment.getContext();
        ImageFs imageFs = ImageFs.find(context);
        File rootDir = imageFs.getRootDir();

        PrefManager.init(context);
        boolean enableBox86_64Logs = PrefManager.getBoolean("enable_box86_64_logs", true);

        EnvVars envVars = new EnvVars();
        addBox64EnvVars(envVars, enableBox86_64Logs);
        envVars.put("HOME", imageFs.home_path);
        envVars.put("USER", ImageFs.USER);
        envVars.put("TMPDIR", imageFs.getRootDir().getPath() + "/tmp");
        envVars.put("DISPLAY", ":0");

        String winePath = wineProfile == null ? imageFs.getWinePath() + "/bin"
                : ContentsManager.getSourceFile(context, wineProfile, wineProfile.wineBinPath).getAbsolutePath();
        envVars.put("PATH", winePath + ":" +
                imageFs.getRootDir().getPath() + "/usr/bin:" +
                imageFs.getRootDir().getPath() + "/usr/local/bin");

        envVars.put("LD_LIBRARY_PATH", imageFs.getRootDir().getPath() + "/usr/lib");
        envVars.put("BOX64_LD_LIBRARY_PATH", imageFs.getRootDir().getPath() + "/usr/lib/x86_64-linux-gnu");
        envVars.put("ANDROID_SYSVSHM_SERVER", imageFs.getRootDir().getPath() + UnixSocketConfig.SYSVSHM_SERVER_PATH);
        envVars.put("FONTCONFIG_PATH", imageFs.getRootDir().getPath() + "/usr/etc/fonts");

        if ((new File(imageFs.getGlibc64Dir(), "libandroid-sysvshm.so")).exists() ||
                (new File(imageFs.getGlibc32Dir(), "libandroid-sysvshm.so")).exists
                        ())
            envVars.put("LD_PRELOAD", "libredirect.so libandroid-sysvshm.so");
        envVars.put("WINEESYNC_WINLATOR", "1");
        if (this.envVars != null) envVars.putAll(this.envVars);

        String box64Path = rootDir.getPath() + "/usr/local/bin/box64";

        // Check if box64 exists and log its details before executing
        File box64File = new File(box64Path);
        Log.d("GlibcProgramLauncherComponent", "About to execute box64 from: " + box64Path);

        String command = box64Path + " " + guestExecutable;
        Log.d("GlibcProgramLauncherComponent", "Final command: " + command);

        return ProcessHelper.exec(command, envVars.toStringArray(), workingDir != null ? workingDir : rootDir, (status) -> {
            Log.d("GlibcProgramLauncherComponent", "Instance " + instanceId + " process terminated with status " + status);
            synchronized (lock) {
                pid = -1;
            }
            // Only clear global state if this was the last running instance
            SteamService.setGameRunning(false);
            if (terminationCallback != null) terminationCallback.call(status);
        });
    }

    private void extractBox64Files() {
        ImageFs imageFs = environment.getImageFs();
        Context context = environment.getContext();
        PrefManager.init(context);
        String currentBox86Version = PrefManager.getString("current_box86_version", "");
        String currentBox64Version = PrefManager.getString("current_box64_version", "");
        File rootDir = imageFs.getRootDir();

        if (!box64Version.equals(currentBox64Version)) {
            ContentProfile profile = contentsManager.getProfileByEntryName("box64-" + box64Version);
            if (profile != null) {
                contentsManager.applyContent(profile);
            }
            else {
                TarCompressorUtils.extract(TarCompressorUtils.Type.ZSTD, context.getAssets(), "box86_64/box64-" + box64Version + ".tzst", rootDir);
            }
            PrefManager.putString("current_box64_version", box64Version);
        }
    }

    private void addBox64EnvVars(EnvVars envVars, boolean enableLogs) {
        Context context = environment.getContext();
        ImageFs imageFs = ImageFs.find(context);
        envVars.put("BOX64_NOBANNER", ProcessHelper.PRINT_DEBUG && enableLogs ? "0" : "1");
        envVars.put("BOX64_DYNAREC", "1");
        if (wow64Mode) envVars.put("BOX64_MMAP32", "1");

        if (enableLogs) {
            envVars.put("BOX64_LOG", "1");
            envVars.put("BOX64_DYNAREC_MISSING", "1");
        }

        envVars.putAll(Box86_64PresetManager.getEnvVars("box64", environment.getContext(), box64Preset));
        envVars.put("BOX64_X11GLX", "1");
        File box64RCFile = new File(imageFs.getRootDir(), "/etc/config.box64rc");
        envVars.put("BOX64_RCFILE", box64RCFile.getPath());
    }

    public void suspendProcess() {
        synchronized (lock) {
            if (pid != -1) ProcessHelper.suspendProcess(pid);
        }
    }

    public void resumeProcess() {
        synchronized (lock) {
            if (pid != -1) ProcessHelper.resumeProcess(pid);
        }
    }

    public String execShellCommand(String command) {
        Context context = environment.getContext();
        ImageFs imageFs = ImageFs.find(context);
        File rootDir = imageFs.getRootDir();

        PrefManager.init(context);
        StringBuilder output = new StringBuilder();
        EnvVars envVars = new EnvVars();
        envVars.put("HOME", imageFs.home_path);
        envVars.put("USER", ImageFs.USER);
        envVars.put("TMPDIR", imageFs.getRootDir().getPath() + "/tmp");
        envVars.put("DISPLAY", ":0");

        String winePath = wineProfile == null ? imageFs.getWinePath() + "/bin"
                : ContentsManager.getSourceFile(context, wineProfile, wineProfile.wineBinPath).getAbsolutePath();
        envVars.put("PATH", winePath + ":" +
                imageFs.getRootDir().getPath() + "/usr/bin:" +
                imageFs.getRootDir().getPath() + "/usr/local/bin");

        envVars.put("LD_LIBRARY_PATH", imageFs.getRootDir().getPath() + "/usr/lib");
        envVars.put("BOX64_LD_LIBRARY_PATH", imageFs.getRootDir().getPath() + "/usr/lib/x86_64-linux-gnu");
        envVars.put("ANDROID_SYSVSHM_SERVER", imageFs.getRootDir().getPath() + UnixSocketConfig.SYSVSHM_SERVER_PATH);
        envVars.put("FONTCONFIG_PATH", imageFs.getRootDir().getPath() + "/usr/etc/fonts");

        if ((new File(imageFs.getGlibc64Dir(), "libandroid-sysvshm.so")).exists() ||
                (new File(imageFs.getGlibc32Dir(), "libandroid-sysvshm.so")).exists
                        ())
            envVars.put("LD_PRELOAD", "libredirect.so libandroid-sysvshm.so");
        envVars.put("WINEESYNC_WINLATOR", "1");
        if (this.envVars != null) envVars.putAll(this.envVars);

        String box64Path = rootDir.getPath() + "/usr/local/bin/box64";

        String finalCommand = box64Path + " " + command;

        // Execute the command and capture its output
        try {
            Log.d("GlibcProgramLauncherComponent", "Shell command is " + finalCommand);
            java.lang.Process process = Runtime.getRuntime().exec(finalCommand, envVars.toStringArray(), workingDir != null ? workingDir : imageFs.getRootDir());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            output.append("Error: ").append(e.getMessage());
        }

        return output.toString();
    }
}
