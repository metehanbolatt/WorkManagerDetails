package com.metehanbolat.workmanagerdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.metehanbolat.workmanagerdetails.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val data = Data.Builder().putInt("intKey", 1).build()
        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .setInitialDelay(2, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        val myWorkRequestTwo: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        //WorkManager.getInstance(this).enqueue(myWorkRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this) {
            when (it.state) {
                WorkInfo.State.RUNNING -> { println("running") }
                WorkInfo.State.FAILED -> { println("failed") }
                WorkInfo.State.SUCCEEDED -> { println("succeed") }
                else -> { }
            }
        }
        // WorkManager.getInstance(this).cancelAllWork()

        /** Chaining */

        /*
        val oneTimeRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this)
            .beginWith(oneTimeRequest)
            .then(oneTimeRequest)
            .then(oneTimeRequest)
            .enqueue()

         */
    }
}