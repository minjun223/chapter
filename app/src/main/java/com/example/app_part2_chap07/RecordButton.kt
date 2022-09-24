package com.example.app_part2_chap07

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton


class RecordButton (
    context: Context,
    attrs: AttributeSet
): AppCompatImageButton(context, attrs){

    init {
        setBackgroundResource(R.drawable.shape_oval_button)
    }

    fun updateIconwithState(state: State){
        when(state){
            State.BEFORE_RECORDING->{
                setImageResource(R.drawable.ic_baseline_fiber_manual_record_24)
            }
            State.ON_RECORDING-> {
                setImageResource(R.drawable.ic__stop)
            }
            State.AFTER_RECORDING-> {
                setImageResource(R.drawable.ic_play)
            }
            State.ON_PLAYING-> {
                setImageResource(R.drawable.ic__stop)
            }
        }
    }
}