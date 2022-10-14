package com.example.asyncaction.handlerThread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper


class WorkerHandler : HandlerThread("worker Thread!!") {

    private val handler : Handler

    init {
        this.start()
        handler = Handler(looper)
    }

    fun execute(task: () -> Unit ) : WorkerHandler {
        handler.post {
            task.invoke()
        }
        return this
    }
}


abstract class AsyncTask<Params,Progress, Result> {
    abstract fun onPreExecute()
    abstract fun doInBackground(vararg many: Params) : Result
    abstract fun onPostExecute(result: Result)
    abstract fun onProgressUpdate(progress: Progress)
    abstract fun onCompleted(isInterrupted : Boolean)


    private val workerHandler = WorkerHandler( )
    private var _isInterrupted : Boolean = false;

    fun execute(vararg many : Params){
        val parentHandler = Handler(Looper.getMainLooper())
        println("execute on ${Thread.currentThread().name}")
        parentHandler.post { onPreExecute() }

        workerHandler.execute {
            println("Inside worker thread start on ${Thread.currentThread().name}")
            val result = runCatching {
               doInBackground(*many)
            }.getOrNull()

            if (result!=null){
                parentHandler.post{
                    onPostExecute(result)
                }
            }

            parentHandler.post { onCompleted(_isInterrupted) }

            println("End of worker thread on ${Thread.currentThread().name}")

        }

    }

    fun cancel(){
        workerHandler.interrupt()
        _isInterrupted=true
    }


}