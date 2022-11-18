package com.jordyf15.storyapp.ui.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.jordyf15.storyapp.R

class EmailEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused && !android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
            error = resources.getString(R.string.email_error)
        }
    }

    private fun init() {
        doOnTextChanged { text, _, _, _ ->
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                error = resources.getString(R.string.email_error)
            }
        }
    }
}