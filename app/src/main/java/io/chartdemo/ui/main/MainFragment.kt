package io.chartdemo.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import io.chartdemo.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        randomizeButton.setOnClickListener { updateCharts() }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.numDataPoints = progress
                updateCharts()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        seekBar.progress = 50
        lineChart.onScrubListener = { xPos, yPos -> scrubLabel.text = "X: $xPos Y: $yPos" }
    }

    private fun updateCharts() {
        with(viewModel.getRandomData()) {
            histogram.data = this
            lineChart.data = this
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
