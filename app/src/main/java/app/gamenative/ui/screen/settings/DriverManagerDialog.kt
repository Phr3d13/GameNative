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
import androidx.compose.runtime.LaunchedEffect
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

    // Gather installed custom drivers via FileUtils helper and allow refreshing
    val installedDrivers = remember { mutableStateListOf<String>() }
    var driverToDelete by remember { mutableStateOf<String?>(null) }

    val refreshDriverList: () -> Unit = {
        installedDrivers.clear()
        try {
            val list = FileUtils.listInstalledAdrenoDrivers(ctx)
            installedDrivers.addAll(list)
        } catch (_: Exception) {}
    }

    LaunchedEffect(Unit) { refreshDriverList() }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            val res = handlePickedUri(ctx, it)
            lastMessage = res
            if (res.startsWith("Installed driver:")) refreshDriverList()
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
                            IconButton(onClick = { driverToDelete = id }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                )
                            }
                        }
                    }
                    // Confirmation dialog for deletion
                    driverToDelete?.let { id ->
                        AlertDialog(
                            onDismissRequest = { driverToDelete = null },
                            title = { Text(text = "Confirm Delete") },
                            text = { Text(text = "Are you sure you want to remove driver '$id'? This cannot be undone.") },
                            confirmButton = {
                                Button(onClick = {
                                    try {
                                        val ok = FileUtils.deleteInstalledAdrenoDriver(ctx, id)
                                        if (ok) {
                                            lastMessage = "Removed driver: $id"
                                            refreshDriverList()
                                        } else {
                                            lastMessage = "Failed to remove $id"
                                        }
                                    } catch (e: Exception) {
                                        lastMessage = "Error removing $id: ${e.message}"
                                    }
                                    driverToDelete = null
                                }) { Text(text = "Delete") }
                            },
                            dismissButton = {
                                Button(onClick = { driverToDelete = null }) { Text(text = "Cancel") }
                            },
                        )
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
    return try {
        val identifier = FileUtils.installAdrenoDriverFromUri(context, uri)
        if (identifier != null) {
            "Installed driver: $identifier"
        } else {
            "Failed to install driver: invalid ZIP or missing manifest"
        }
    } catch (e: Exception) {
        "Error importing driver: ${e.message}"
    }
}
