package dev.paraspatil.layoutsnapshotter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ScrollView
import androidx.compose.ui.platform.ComposeView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

//LayoutSnapShotter Capture any android UI as Image or PDF

object LayoutSnapshotter {
    // Captures any View as a Bitmap
    fun capture(view: View, backgroundColor: Int = Color.WHITE): Bitmap =
         ViewCapture.captureView(view, backgroundColor)

    //Captures the FULL content of a ScrollView (all scrollable content, not just visible)
    fun captureScrollView(view: View, backgroundColor: Int = Color.WHITE): Bitmap =
        ViewCapture.captureScrollView(view as ScrollView, backgroundColor)

    //Captures the FULL content of a NestedScrollView
    fun captureNestedScrollView(view: View, backgroundColor: Int = Color.WHITE): Bitmap =
        ViewCapture.captureNestedScrollView(view as NestedScrollView, backgroundColor)

    //Captures the FULL content of a RecyclerView (all items, even off-screen ones)
    fun captureRecyclerView(view: View, backgroundColor: Int = Color.WHITE): Bitmap =
        ViewCapture.captureRecyclerView(view as RecyclerView, backgroundColor)

   //Captures a Jetpack Compose ComposeView as a Bitmap
    fun capture(composeView: ComposeView, backgroundColor: Int = Color.WHITE): Bitmap =
        ComposeCapture.captureComposeView(composeView, backgroundColor)

    //Saves a Bitmap to the device gallery (Pictures/LayoutSnapshotter folder)
    fun saveToGallery(context: Context, bitmap: Bitmap, fileName: String = "layout_snapshot_\${System.currentTimeMillis()}"): Uri? =
        ShareUtils.saveToGallery(context, bitmap, fileName)

    //Opens Android's share sheet to share a PDF file
    fun sharePdf(context: Context, pdfFile: File, title: String = "Share PDF") =
        ShareUtils.sharePdf(context, pdfFile, title)

    //Opens Android's share sheet to share a Bitmap screenshot
    fun share(context: Context, bitmap: Bitmap, title: String = "Share Screenshot") =
        ShareUtils.share(context, bitmap, title)

    //Captures a View and exports it as a PDF file
    fun exportPdf(context: Context, view: View, fileName: String = "layout_snapshot", backgroundColor: Int = Color.WHITE): File? =
        PdfExporter.exportViewToPdf(context, view, fileName, backgroundColor)

    //Exports an existing Bitmap as a PDF file
    fun exportPdf(context: Context, bitmap: Bitmap, fileName: String = "layout_snapshot"): File? =
        PdfExporter.exportBitmapToPdf(context, bitmap,fileName)
}
