package io.chartdemo.ui.main

import androidx.lifecycle.ViewModel
import java.util.Random

class MainViewModel : ViewModel() {

    var numDataPoints = 50

    internal fun getRandomData(): List<Float> {
        val data = mutableListOf<Float>()

        fun IntRange.random() = Random().nextInt(endInclusive + 1 - start).toFloat() + start

        repeat(numDataPoints) {
            data.add((0..numDataPoints).random())
        }

        return data
    }
}
