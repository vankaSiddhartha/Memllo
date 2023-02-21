package com.techvanka.memllo.model

import android.net.Uri
import android.os.AsyncTask
import android.widget.VideoView

class VideoLoaderTask(val videoView: VideoView, val videoUri: Uri) : AsyncTask<Unit, Unit, Unit>() {

    override fun doInBackground(vararg params: Unit?) {
        // Load video in the background
        videoView.setVideoURI(videoUri)
    }

    override fun onPostExecute(result: Unit?) {
        // Start playing the video after it has been loaded
        videoView.start()
    }
}