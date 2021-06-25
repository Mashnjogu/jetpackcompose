package com.example.drawingapp

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter

@SuppressLint("UnrememberedMutableState")
@Composable
fun PaintApp(){
    val action: MutableState<Pair<Boolean, Pair<Float, Float>>?> = mutableStateOf(null)
    val path = Path()
    val triggerList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()
    val collectList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()

    Canvas(modifier = Modifier.fillMaxSize()
        .pointerInteropFilter {
            when(it.action){
                MotionEvent.ACTION_DOWN -> {
                    action.value = Pair(true, Pair(it.x, it.y))
                    path.moveTo(it.x, it.y)
                    triggerList.add(Pair(true, Pair(it.x, it.y)))
                }
                MotionEvent.ACTION_MOVE -> {
                    action.value = Pair(false, Pair(it.x, it.y))
                    path.lineTo(it.x, it.y)
                    triggerList.add(Pair(false, Pair(it.x, it.y)))
                }
                MotionEvent.ACTION_UP -> {}
                else -> false
            }
            true
        }) {
        action.value?.let {
            collectList.add(it)
            drawPath(
                path = collectList.toPath(),
                color = Color.Green,
                alpha = 1f,
                style = Stroke(10f)
            )
//            drawPath(
//                path = triggerList.toPath(),
//                color = Color.Red,
//                alpha = 1f,
//                style = Stroke(4f)
//            )

            if(collectList != triggerList){
                Log.d("Track", "Different! ${triggerList.subtract(collectList)}")
            }
        }
    }
}
fun List<Pair<Boolean, Pair<Float, Float>>>.toPath() : Path{
    val path = Path()
    forEach{
        if (it.first){
            path.moveTo(it.second.first, it.second.second)
        }else{
            path.lineTo(it.second.first, it.second.second)
        }
    }
    return path
}

