package com.example.bluetooth_classic

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.example.bluetooth_classic.ui.theme.BluetoothclassicTheme

//Adapter para utilizar el bluetooth
/* No funcionó de la forma en que dice la documentación, se utilizó una forma antigua para el adapter
private val bluetoothManager: BluetoothManager? = getSystemService(BluetoothManager::class.java)
private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter() */
private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /*val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)*/
        super.onCreate(savedInstanceState)
        setContent {
            BluetoothclassicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var devices = remember { mutableStateOf("") }
    Box(modifier = modifier
        .fillMaxWidth()
        .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bluetooth classic",
                modifier = modifier,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    //Boton prender bluetooth
                    Button(onClick = { turnOnBluetooth( context ) }) {
                        Text(text = "Prender")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    //Boton apagar bluetooth
                    Button(onClick = { turnOffBluetooth( context ) }) {
                        Text(text = "Apagar")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    //Boton visible dispositivo
                    Button(onClick = { discoveredDevice( context ) }) {
                        Text(text = "Visibilidad")
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    //Boton mostrar dispositivos vinculados
                    Button(onClick = { devices.value = pairDevices( context ) }) {
                        Text(text = "Vinculados")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Dispositivos")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = devices.value, modifier = modifier)
        }
    }
}



private fun turnOnBluetooth(context: Context) {
    if (bluetoothAdapter?.isEnabled == false) {
        Toast.makeText(context, "Encendiendo bluetooth...", Toast.LENGTH_SHORT).show()
        /* Tampoco funciono a como dice la documentación, no reconoce la constante "REQUEST_ENABLE_BT"
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        */
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            context.startActivity(enableBtIntent)
            return
        }
    }else{
        Toast.makeText(context, "El bluetooth ya se encuentra activo", Toast.LENGTH_SHORT).show()
    }
}

private fun turnOffBluetooth(context: Context) {
    Toast.makeText(context, "Apagando el bluetooth...", Toast.LENGTH_SHORT).show()
    if (bluetoothAdapter?.isEnabled == true){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.disable()
            return
        }
    }else{
        Toast.makeText(context, "El bluetooth ya se encuentra desactivado", Toast.LENGTH_SHORT).show()
    }
}

private fun discoveredDevice(context: Context){
    if (bluetoothAdapter?.isDiscovering == false){
        val discoverBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            context.startActivity(discoverBtIntent)
            return
        }
    }else{

    }
}

private fun pairDevices(context: Context): String {

    if (bluetoothAdapter?.isEnabled == true){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val devices: Set<BluetoothDevice> =
                bluetoothAdapter.bondedDevices

            var list = ""

            for (device in devices){
                list = "\n Device : "+device.name+" , "+device
            }
            return list
        }else{
            return "No tienes permiso"
        }
    }else{
        Toast.makeText(context, "Enciende el bluetooth primero", Toast.LENGTH_SHORT).show()
        return "Enciende el bluetooth..."
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BluetoothclassicTheme {
        Greeting()
    }
}