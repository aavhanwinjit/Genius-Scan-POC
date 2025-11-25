package com.ram.courier

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.geniusscansdk.core.GeniusScanSDK
import com.geniusscansdk.readablecodeflow.ReadableCodeConfiguration
import com.geniusscansdk.readablecodeflow.ReadableCodeFlow
import com.geniusscansdk.readablecodeflow.ReadableCodeFlowResult

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GeniusScanSDK.setLicenseKey(this, "533c50075d540400075c035339525a0e4a1153091a555d4c105f511468090709500057510a020e")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val readableCodeLauncher = registerForActivityResult(
            ReadableCodeFlow.createContract()
        ) { result ->
            when (result) {
                is ReadableCodeFlowResult.Success -> {
                    // Process detected codes
                    result.codes.forEach { code ->
                        Log.d("Scanner", "Detected ${code.type}: ${code.value}")
                    }
                }
                is ReadableCodeFlowResult.Canceled -> {
                    // User cancelled
                }
                is ReadableCodeFlowResult.Error -> {
                    // Handle error
                    Log.e("Scanner", "Error: ${result.message}")
                }
            }
        }

        findViewById<Button>(R.id.btnGeniusScan).setOnClickListener {
            val configuration = ReadableCodeConfiguration(true)

            readableCodeLauncher.launch(configuration)
        }
    }
}
