package com.example.stepcounter

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.stepcounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {
    // region variables
    private var sensorManager: SensorManager? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel: StepsViewModel by viewModels()
    private var running = false
//    private var totalSteps = 0f
//    private var previousTotalSteps = 0f

    //endregion

    //region lifecycles

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        loadData()
        resetSteps()
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_LONG).show()

        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        }
    }
    //endregion

    private fun init() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (running) {
           // viewModel.totalSteps.value = p0!!.values[0]

            viewModel.setTotalSteps(p0!!.values[0])

            viewModel.setCurrentSteps(viewModel.totalSteps.value!! - viewModel.previousTotalSteps.value!!)
//            viewModel.currentSteps.value =
//                viewModel.totalSteps.value!! - viewModel.previousTotalSteps.value!!
            //  binding.tvStepsTaken.text = currentSteps.toString()

            viewModel.currentSteps.observe(this) {
                binding.tvStepsTaken.text = viewModel.currentSteps.value.toString()
            }

            binding.progressCircular.apply {
                setProgressWithAnimation(viewModel.currentSteps.value!!.toFloat())
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun resetSteps() {
        binding.tvStepsTaken.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_LONG).show()

        }

        binding.tvStepsTaken.setOnLongClickListener {
            viewModel.totalSteps.value?.let { it1 -> viewModel.setPreviousTotalSteps(it1) }
            viewModel.previousTotalSteps.value = viewModel.totalSteps.value
            binding.tvStepsTaken.text = 0.toString()
            saveData()

            true
        }
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        viewModel.previousTotalSteps.value?.let {
            editor.putFloat("key1", it)
            editor.apply()
        }

    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber: Float = sharedPreferences.getFloat("key1", 0f)
        viewModel.setPreviousTotalSteps(savedNumber)
       // viewModel.previousTotalSteps.value = savedNumber
    }


}
