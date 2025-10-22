package app.gamenative.ui.screen.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.gamenative.ui.theme.settingsTileColors
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.winlator.core.FileUtils
import java.io.File
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun DriverManagerDialog(open: Boolean, onDismiss: () -> Unit) {
    if (!open) return
    val ctx = LocalContext.current
    var lastMessage by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            val res = handlePickedUri(ctx, it)
            lastMessage = res
        }
    }

    // Gather installed custom drivers
    val installedDrivers = remember {
        mutableStateListOf<String>().apply {
            try {
                val root = File(ctx.filesDir, "installed_components/adrenotools_driver")
                if (root.isDirectory) {
                    val dirs = root.listFiles { f -> f.isDirectory } ?: arrayOf()
                    for (d in dirs) add(d.name)
                }
            } catch (_: Exception) {}
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Driver Manager") },
        text = {
            Column {
                Text(text = "Import a custom Graphics driver package")
                lastMessage?.let { Text(text = it, modifier = Modifier.padding(top = 8.dp)) }

                if (installedDrivers.isNotEmpty()) {
                    Text(text = "\nInstalled custom drivers:")
                    for (id in installedDrivers) {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                            Text(text = id, modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                // delete directory
                                try {
                                    val compDir = File(ctx.filesDir, "installed_components/adrenotools_driver/$id")
                                    if (compDir.exists()) FileUtils.delete(compDir)
                                } catch (e: Exception) {
                                    lastMessage = "Failed to remove $id: ${e.message}"
                                }
                                installedDrivers.remove(id)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { launcher.launch(arrayOf("application/zip", "*/*")) }) { Text("Import ZIP") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Close") } },
    )
}

private fun handlePickedUri(context: Context, uri: Uri): String {
    try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            val manifestName = FileUtils.readZipManifestNameFromInputStream(input)
            if (manifestName == null) return "Selected zip is invalid: no manifest JSON with name/libraryName found."
        } ?: return "Failed to open selected file"

        // Re-open to extract (streams were consumed)
        context.contentResolver.openInputStream(uri)?.use { input ->
            val identifier = FileUtils.readZipManifestNameFromInputStream(input) ?: return "Invalid manifest"
            // create destination dir
            val compDir = File(context.filesDir, "/installed_components/adrenotools_driver/$identifier")
            if (compDir.exists()) FileUtils.delete(compDir)
            compDir.mkdirs()
            // extract
            context.contentResolver.openInputStream(uri)?.use { extractStream ->
                val ok = FileUtils.extractZipFromInputStream(context, extractStream, compDir)
                return if (ok) "Installed driver: $identifier" else "Failed to extract zip"
            } ?: return "Failed to open file for extraction"
        }
    } catch (e: Exception) {
        return "Error importing driver: ${e.message}"
    }
    // Fallback return to satisfy Kotlin's requirement that all code paths return a String
    return "Unknown result"
}
