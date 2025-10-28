package app.gamenative.ui.screen.settings
import androidx.compose.foundation.layout.height
import java.io.File
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.opengl.GLSurfaceView
import android.content.Context
import android.opengl.GLES20
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGLConfig
import androidx.compose.ui.platform.LocalUriHandler
import app.gamenative.Constants
import app.gamenative.PrefManager
import app.gamenative.ui.component.dialog.LibrariesDialog
import app.gamenative.ui.theme.settingsTileColors
import app.gamenative.ui.theme.settingsTileColorsAlt
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch

@Composable
fun SettingsGroupInfo() {
    fun getCpuModel(): String {
        // Try to get marketing name from system properties
        val propKeys = listOf(
            "ro.soc.model", // Often contains Snapdragon marketing name
            "ro.board.platform",
            "ro.product.board"
        )
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val getMethod = clazz.getMethod("get", String::class.java)
            for (key in propKeys) {
                val value = getMethod.invoke(null, key) as? String
                if (!value.isNullOrBlank() && value.contains("Snapdragon", ignoreCase = true)) {
                    return value
                }
            }
        } catch (_: Exception) {}
        // Fallback to /proc/cpuinfo parsing
        return try {
            val cpuInfo = File("/proc/cpuinfo").readText()
            Regex("Hardware\\s*:\\s*(.+)").find(cpuInfo)?.groupValues?.get(1)
                ?: Regex("model name\\s*:\\s*(.+)").find(cpuInfo)?.groupValues?.get(1)
                ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    // GPU renderer state
    var gpuRenderer by rememberSaveable { mutableStateOf<String?>(null) }

    // Custom GLSurfaceView.Renderer to fetch GPU renderer string
    class Renderer(val onRendererReady: (String) -> Unit) : GLSurfaceView.Renderer {
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            val renderer = GLES20.glGetString(GLES20.GL_RENDERER) ?: "Unknown"
            onRendererReady(renderer)
        }
        override fun onDrawFrame(gl: GL10?) {}
        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {}
    }
    var showAboutDialog by rememberSaveable { mutableStateOf(false) }
    SettingsGroup(title = { Text(text = "Info") }) {
        SettingsMenuLink(
            colors = settingsTileColors(),
            title = { Text(text = "About") },
            subtitle = { Text(text = "Show version and device info") },
            onClick = { showAboutDialog = true },
        )
        if (showAboutDialog) {
            val context = LocalContext.current
            LaunchedEffect(showAboutDialog) {
                if (gpuRenderer == null) {
                    // Will be set by GLSurfaceView below
                }
            }
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("Extra Info") },
                text = {
                    val versionName = try {
                        context.packageManager.getPackageInfo(context.packageName, 0).versionName
                    } catch (e: Exception) { "Unknown" }
                    val cpuModel = getCpuModel()
                    val deviceInfo = "Model: " + android.os.Build.MODEL + "\n" +
                        "Manufacturer: " + android.os.Build.MANUFACTURER + "\n" +
                        "Android Version: " + android.os.Build.VERSION.RELEASE + "\n" +
                        "CPU: " + cpuModel + "\n" +
                        "GPU: " + (gpuRenderer ?: "Detecting...")
                    androidx.compose.foundation.layout.Column {
                        Text("Version: $versionName\n$deviceInfo")
                        if (gpuRenderer == null) {
                            AndroidView(
                                factory = { ctx ->
                                    GLSurfaceView(ctx).apply {
                                        setRenderer(Renderer {
                                            gpuRenderer = it
                                        })
                                        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY)
                                    }
                                },
                                update = {},
                                modifier = Modifier.height(1.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    androidx.compose.material3.Button(onClick = { showAboutDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
        val uriHandler = LocalUriHandler.current
        var askForTip by rememberSaveable { mutableStateOf(!PrefManager.tipped) }
        var showLibrariesDialog by rememberSaveable { mutableStateOf(false) }

        LibrariesDialog(
            visible = showLibrariesDialog,
            onDismissRequest = { showLibrariesDialog = false },
        )

        SettingsMenuLink(
            colors = settingsTileColors(),
            title = { Text("Send tip") },
            subtitle = { Text(text = "Contribute to ongoing development") },
            icon = { Icon(imageVector = Icons.Filled.MonetizationOn, contentDescription = "Tip") },
            onClick = {
                uriHandler.openUri(Constants.Misc.KO_FI_LINK)
                askForTip = false
                PrefManager.tipped = !askForTip
            },
        )

        SettingsSwitch(
            colors = settingsTileColorsAlt(),
            state = askForTip,
            title = { Text("Ask for tip on startup") },
            subtitle = { Text(text = "Stops the tip message from appearing") },
            onCheckedChange = {
                askForTip = it
                PrefManager.tipped = !askForTip
            },
        )

        SettingsMenuLink(
            colors = settingsTileColors(),
            title = { Text(text = "Source code") },
            subtitle = { Text(text = "View the source code of this project") },
            onClick = { uriHandler.openUri(Constants.Misc.GITHUB_LINK) },
        )

        SettingsMenuLink(
            colors = settingsTileColors(),
            title = { Text(text = "Libraries Used") },
            subtitle = { Text(text = "See what technologies make GameNative possible") },
            onClick = { showLibrariesDialog = true },
        )

        SettingsMenuLink(
            colors = settingsTileColors(),
            title = { Text(text = "Privacy Policy") },
            subtitle = { Text(text = "Opens a link to GameNative's privacy policy") },
            onClick = {
                uriHandler.openUri(Constants.Misc.PRIVACY_LINK)
            },
        )
    }
}
