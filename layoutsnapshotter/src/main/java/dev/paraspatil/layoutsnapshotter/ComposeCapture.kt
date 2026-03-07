package dev.paraspatil.layoutsnapshotter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.compose.ui.platform.ComposeView

//handles capturing of jetpack compose UI as Bitmap
internal object ComposeCapture{

    //captures ComposeView (bridge between Jetpack Compose and View system)
    //works on any compose screen wrapped in ComposeView

    fun captureComposeView(composeView: ComposeView,backgroundColor: Int = Color.WHITE): Bitmap {
        return ViewCapture.captureView(composeView,backgroundColor)
    }

    //captures any View that hosts Compose content
    //useful when having reference to the root view of a composable screen

    fun captureComposeHost(view: View,backgroundColor: Int = Color.WHITE): Bitmap{
        return ViewCapture.captureView(view,backgroundColor)
    }


}