package dev.paraspatil.layoutsnapshotter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import kotlin.js.ExperimentalJsFileName

//handle saving bitmap to gallery and sharing via android share sheet

 internal object ShareUtils {
//save the bitmap in device gallery
fun saveToGallery(
    context: Context,
    bitmap: Bitmap,
    fileName: String = "layout_snapshot_\${System.currentTimeMillis()}"
): Uri?{
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveToGalleryQ(context,bitmap,fileName)
    }else{
        saveToGalleryLegacy(context,bitmap,fileName)
    }
}
     private fun saveToGalleryQ(context: Context,bitmap: Bitmap,fileName: String):Uri?{
         val contentValues = ContentValues().apply {
             put(MediaStore.Images.Media.DISPLAY_NAME,"$fileName.png")
             put(MediaStore.Images.Media.MIME_TYPE,"image/png")
             put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/LayoutSnapshotter")
             put(MediaStore.Images.Media.IS_PENDING,1)
         }

         val resolver = context.contentResolver
         val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)?: return null

         return try {
             resolver.openOutputStream(uri)?.use { stream ->
                 bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
             }
             contentValues.clear()
             contentValues.put(MediaStore.Images.Media.IS_PENDING,0)
             resolver.update(uri,contentValues,null,null)
             uri
         }catch (e : Exception){
             resolver.delete(uri,null,null)
             e.printStackTrace()
             null
         }
     }
     private fun saveToGalleryLegacy(context: Context,bitmap: Bitmap,fileName: String):Uri?{
         val pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
         val appDir = File(pictureDir, "LayoutSnapshotter")
         if (!appDir.exists()) appDir.mkdirs()

         val file = File(appDir, "$fileName.png")
        return try {
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        }
            Uri.fromFile(file)
     }catch (e: Exception){
     e.printStackTrace()
         null
     }
     }

     //saves bitmap to apps cache directory (for sharing via FileProvider)

     fun saveCacheForSharing(context: Context,bitmap: Bitmap): File{
         val cacheDir = File(context.cacheDir,"layout_snapshotter_share")
         if (!cacheDir.exists())cacheDir.mkdirs()

         val file = File(cacheDir,"snapshot_${System.currentTimeMillis()}.png")
         FileOutputStream(file).use { stream ->
             bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
         }
         return file
     }

     //open android share sheet to share bitmap
     fun share(context: Context,bitmap: Bitmap,title: String="Share Screenshot"){
         val file = saveCacheForSharing(context,bitmap)
         val uri = FileProvider.getUriForFile(
             context,
             "${context.packageName}.layoutsnapshotter.provider",
             file
         )
         val intent = Intent(Intent.ACTION_SEND).apply {
             type = "image/png"
             putExtra(Intent.EXTRA_STREAM, uri)
             addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
         }
         context.startActivity(Intent.createChooser(intent, title))
     }

     //opens android share sheet  to share PDF file
     fun sharePdf(context: Context,pdfFile:File,title: String="Share PDF"){
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.layoutsnapshotter.provider",
            pdfFile
        )

         val intent = Intent(Intent.ACTION_SEND).apply {
             type = "application/pdf"
             putExtra(Intent.EXTRA_STREAM, uri)
             addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
         }
         context.startActivity(Intent.createChooser(intent, title))
     }
 }