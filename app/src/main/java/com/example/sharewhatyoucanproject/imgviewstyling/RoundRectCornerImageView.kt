package com.example.sharewhatyoucanproject.imgviewstyling

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.bumptech.glide.Glide.init

class RoundRectCornerImageView : androidx.appcompat.widget.AppCompatImageView {
    private val radius = 18.0f
    private var path: Path? = null
    private var rect: RectF? = null

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle,
    ) {
        init()
    }

    private fun init() {
        path = Path()
    }

    protected override fun onDraw(canvas: Canvas) {
        rect = RectF(0F, 0F, this.getWidth().toFloat(), this.getHeight().toFloat())
        path!!.addRoundRect(rect!!, radius, radius, Path.Direction.CW)
        path?.let { canvas.clipPath(it) }
        super.onDraw(canvas)
    }
}
