package dev.paraspatil.snapkit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.paraspatil.layoutsnapshotter.LayoutSnapshotter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DemoScreen()
            }
        }
    }
}

@Composable
fun DemoScreen() {
    val context = LocalContext.current
    val rootView = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "LayoutSnapshotter Demo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Sample invoice card to capture
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Invoice #INV-2026-001", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Item: Android Dev Course")
                Text("Amount: ₹4,999")
                Text("Status: Paid ✅")
            }
        }

        // Capture as Bitmap
        Button(
            onClick = {
                val bitmap = LayoutSnapshotter.capture(rootView)
                val uri = LayoutSnapshotter.saveToGallery(context, bitmap)
                Toast.makeText(context, "Saved to gallery! $uri", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📸 Capture & Save to Gallery")
        }

        // Share Screenshot
        Button(
            onClick = {
                val bitmap = LayoutSnapshotter.capture(rootView)
                LayoutSnapshotter.share(context, bitmap)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📤 Share Screenshot")
        }

        // Export PDF
        Button(
            onClick = {
                val pdfFile = LayoutSnapshotter.exportPdf(context, rootView, "invoice_2026")
                if (pdfFile != null) {
                    Toast.makeText(context, "PDF saved: ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
                    LayoutSnapshotter.sharePdf(context, pdfFile)
                } else {
                    Toast.makeText(context, "PDF export failed", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📄 Export as PDF & Share")
        }
    }
}