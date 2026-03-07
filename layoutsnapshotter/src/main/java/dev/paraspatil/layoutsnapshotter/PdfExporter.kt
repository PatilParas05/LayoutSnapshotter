package dev.paraspatil.layoutsnapshotter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.graphics.Canvas
import android.graphics.Paint
import java.io.File
import java.io.FileOutputStream

internal object PdfExporter {

    //export bitmap to a pdf file

    fun exportBitmapToPdf(
        context: Context,
        bitmap: Bitmap,
        fileName: String = "layout_snapshot"

    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas: Canvas= page.canvas
        val paint = Paint().apply { isAntiAlias = true }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        pdfDocument.finishPage(page)

        val outputFile = getOutputFile(context, "$fileName.pdf")
        return  try {
            FileOutputStream(outputFile).use { stream ->
                pdfDocument.writeTo(stream)
            }
            pdfDocument.close()
            outputFile
        } catch (e: Exception) {
            pdfDocument.close()
            e.printStackTrace()
            null
        }
    }

    //captures a view and exports it directly to a PDF file

    fun exportViewToPdf(
        context: Context,
        view: View,
        fileName: String = "layout_snapshot",
        backgroundColor: Int = Color.WHITE
    ): File? {
        val bitmap = ViewCapture.captureView(view,backgroundColor)
        return exportBitmapToPdf(context,bitmap,fileName)
}
    //returns output file in the app external files directory (no permission required)

    private fun getOutputFile(context: Context, fileName: String): File {
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?:context.filesDir
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, fileName)
    }
}