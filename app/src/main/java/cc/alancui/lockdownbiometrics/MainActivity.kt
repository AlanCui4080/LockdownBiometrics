package cc.alancui.lockdownbiometrics

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import cc.alancui.lockdownbiometrics.ui.theme.LockdownBiometricsTheme

class MainActivity : ComponentActivity() {
    private val TAG = "LockdownBiometricsActivity"
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LockdownBiometricsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LaunchedEffect(Unit){
                        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
                        val receiverName = ComponentName(context, LockAdminReceiver::class.java)
                        if (!dpm.isAdminActive(receiverName)) {
                            Log.d(TAG, "isAdminActive: false")
                            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, receiverName)
                                putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"Lock the Biometrics")
                            }
                            context.startActivity(intent)
                        }
                        else {
                            Log.d(TAG, "isAdminActive: true")
                        }
                        Log.d(TAG, "now try to dpm.lockNow()")
                        dpm.lockNow()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LockdownBiometricsTheme {
        Greeting("Android")
    }
}
