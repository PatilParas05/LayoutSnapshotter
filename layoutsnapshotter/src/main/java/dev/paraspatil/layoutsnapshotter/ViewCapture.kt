package dev.paraspatil.layoutsnapshotter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


//handles capturing of android views- based UI as Bitmap
internal object ViewCapture {
    //capture a regular view as bitmap
    fun captureView(view: View,backgroundColor: Int = Color.WHITE): Bitmap {
        if (view.width == 0 || view.height == 0) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)
        )
            view.layout(0,0,view.measuredWidth,view.measuredHeight)
        }
        val bitmap = Bitmap.createBitmap(view.width,view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        if (backgroundColor != Color.TRANSPARENT){
            canvas.drawColor(backgroundColor)
        }
        view.draw(canvas)
        return bitmap
        }

//captures the full scrollable content of a ScrollView (not just visible portion)

    fun captureScrollView(scrollView: ScrollView, backgroundColor: Int = Color.WHITE): Bitmap {
        val child = scrollView.getChildAt(0)
        val totalHeight = child.height
        val width = scrollView.width

        val bitmap = Bitmap.createBitmap(width,totalHeight,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        if (backgroundColor != Color.TRANSPARENT){
            canvas.drawColor(backgroundColor)
        }
        child.draw(canvas)
        return bitmap
    }

    //captures the full scrollable content of a NestedScrollView

    fun captureNestedScrollView(nestedScrollView: NestedScrollView, backgroundColor: Int = Color.WHITE): Bitmap {
        val child = nestedScrollView.getChildAt(0)
        val totalHeight = child.height
        val width = nestedScrollView.width

        val bitmap = Bitmap.createBitmap(width,totalHeight,Bitmap.Config.ARGB_8888)
        val Canvas = Canvas(bitmap)

        if (backgroundColor != Color.TRANSPARENT){
            Canvas.drawColor(backgroundColor)
        }
        child.draw(Canvas)
        return bitmap
    }
    fun captureRecyclerView(recyclerView: RecyclerView, backgroundColor: Int = Color.WHITE): Bitmap {
        val adapter = recyclerView.adapter?:return captureView(recyclerView,backgroundColor)
        val layoutManager = recyclerView.layoutManager?: return captureView(recyclerView,backgroundColor)

        var totalHeight = 0
        val width = recyclerView.measuredWidth

        val bitmap = mutableListOf<Bitmap>()

        for (i in 0 until adapter.itemCount) {
            val viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
            adapter.onBindViewHolder(viewHolder, i)

            viewHolder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            viewHolder.itemView.layout(0, 0, width, viewHolder.itemView.measuredHeight)

            val itemBitmap = Bitmap.createBitmap(width, viewHolder.itemView.measuredHeight, Bitmap.Config.ARGB_8888)
            val itemCanvas = Canvas(itemBitmap)
            if (backgroundColor != Color.TRANSPARENT){
                itemCanvas.drawColor(backgroundColor)
            }
            viewHolder.itemView.draw(itemCanvas)

            bitmap.add(itemBitmap)
            totalHeight += viewHolder.itemView.measuredHeight
        }

        //Combine all items bitmap into one
        val fullBitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val fullCanvas = Canvas(fullBitmap)
        var currentTop = 0f
        bitmap.forEach { bmp ->
            fullCanvas.drawBitmap(bmp, 0f, currentTop, null)
            currentTop += bmp.height
            bmp.recycle()
        }
        return fullBitmap
    }
}