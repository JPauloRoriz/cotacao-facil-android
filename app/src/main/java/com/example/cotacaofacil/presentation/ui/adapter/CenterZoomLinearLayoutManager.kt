import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CenterZoomLinearLayoutManager(
    context: Context,
    private val mShrinkDistance: Float = 0.9f,
    val mShrinkAmount: Float = 0.15f
) : LinearLayoutManager(context, HORIZONTAL, false) {



    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return if (orientation == HORIZONTAL) {
            super.scrollHorizontallyBy(dx, recycler, state).also { scaleChildren() }
        } else {
            0
        }
    }

    private fun scaleChildren() {
        val midpoint = width / 2f
        val d1 = mShrinkDistance * midpoint
        for (i in 0 until childCount) {
            val child = getChildAt(i) as View
            val d = d1.coerceAtMost(abs(midpoint - (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f))
            val scale = 1f - mShrinkAmount * d / d1
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)

        val orientation = orientation
        if (orientation == HORIZONTAL) {
            val midpoint = width / 2f
            val d0 = 0f
            val d1 = mShrinkDistance * midpoint
            val s0 = 1f
            val s1 = 1f - mShrinkAmount
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child?.let {
                    val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
                    val d = d1.coerceAtMost(abs(midpoint - childMidpoint))
                    val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                    child.scaleX = scale
                    child.scaleY = scale
                }
            }
        } else {
            val midpoint = height / 2f
            val d0 = 0f
            val d1 = mShrinkDistance * midpoint
            val s0 = 1f
            val s1 = 1f - mShrinkAmount
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child?.let {
                    val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
                    val d = Math.min(d1, Math.abs(midpoint - childMidpoint))
                    val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                    child.scaleX = scale
                    child.scaleY = scale
                }
            }
        }
    }
}