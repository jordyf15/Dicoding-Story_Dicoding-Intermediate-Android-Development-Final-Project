package com.jordyf15.storyapp.ui.customview

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.jordyf15.storyapp.R

class EmailEditText : AppCompatEditText {
    constructor(context: Context) : super(context){
        init()
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if(!focused && !android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()){
            error = resources.getString(R.string.email_error)
        }
    }

    private fun init() {
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                    error = resources.getString(R.string.email_error)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }
        })

    }
}