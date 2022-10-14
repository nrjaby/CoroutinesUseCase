package com.example.asyncaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.asyncaction.handlerThread.AsyncTask

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task = object : AsyncTask<String, Int, String>() {

            override fun doInBackground(vararg many: String): String {
                println("started doing work on ${Thread.currentThread().name}")
                (0..100).forEach {
                    onProgressUpdate(it)
                    Thread.sleep(100)
                }
                println("completed on ${Thread.currentThread().name}")

                return "done"
            }

            override fun onPostExecute(data: String) {

            }

            override fun onCompleted(isInterrupted: Boolean) {
                println("asyncTask onCompleted on ${Thread.currentThread().name} $isInterrupted")
            }

            override fun onPreExecute() {
                println("inside onPreExecute on ${Thread.currentThread().name} == UI ")
            }

            override fun onProgressUpdate(progress: Int) = runOnUiThread() {
                println("inside onProgressUpdate on ${Thread.currentThread().name} $progress")
            }

        }.execute()

    }


}