package com.techvanka.memllo.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.techvanka.memllo.model.*
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.VideoAdapter
import com.techvanka.memllo.databinding.FragmentMemesBinding
import com.techvanka.memllo.model.ExoPlayerItem
import com.techvanka.memllo.model.VideoModel
import com.techvanka.memllo.model.VideoUploadModel
import kotlinx.coroutines.*
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList


class MemesVideoFragment : Fragment() {
    var elapsedTime = 0L
    private lateinit var chronometer: Chronometer
    private lateinit var binding: FragmentMemesBinding
    private val videos = ArrayList<VideoModel>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private lateinit var adapter: VideoAdapter
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var sharedPreferences: SharedPreferences
    var timer: Timer? = null
    var timerTask: TimerTask? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        chronometer = Chronometer(context)
        // Start the chronometer
        chronometer.start()

        val username = sharedPreferences.getString("time", "default_username")
       // Toast.makeText(context, "$username", Toast.LENGTH_SHORT).show()
        //Toast.makeText(requireContext(), "$elapsedTime", Toast.LENGTH_SHORT).show()




        // userDao.deleteUser(newUser)

        val adapterScope = CoroutineScope(lifecycleScope.coroutineContext + Dispatchers.Main)
        val binding: FragmentMemesBinding = FragmentMemesBinding.inflate(inflater, container, false)
        var list = arrayListOf<Int>()
        list.add(R.layout.fragment_comments_a)
        list.add(R.id.comment_rv)
        list.add(R.id.comments_et)
        list.add(R.id.comments_sendbtn)
        val prefs: SharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("list", null)

        CoroutineScope(Dispatchers.Default).launch{
        FirebaseDatabase.getInstance().getReference("Videos")
            .addValueEventListener(object : ValueEventListener {
                var intentUriList = arrayListOf<VideoUploadModel>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    var data = snapshot.children
                    var list1 = ArrayList<VideoUploadModel>()
                    for (snap in snapshot.children) {
                        var data = snap.getValue(VideoUploadModel::class.java)


                        if (json != null) {
                            val jsonArray = JSONArray(json)
                            val list4 = ArrayList<String>()
                            for (i in 0 until jsonArray.length()) {
                                val item = jsonArray.getString(i)
                                list4.add(item)
                            }

                            if (data?.list!!.containsAll(list4) || list4.containsAll(data.list) || data.list.contains(
                                    "eng"
                                )
                            ) {
                                list1.add(data)
                            } else {

                                list1.add(data)
                            }
                        }else{

                        }


                    }

                    list1.shuffle()

                    try {
                        adapter =
                            VideoAdapter(requireContext(), list1, list)
                        binding.memesView.adapter = adapter

                    } catch (e: Exception) {

                    }


                }


                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
        binding.memesView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Cancel any existing TimerTask and Timer
                timerTask?.cancel()
                timer?.cancel()

                // Start a new TimerTask that will execute after 5 minutes
                timerTask = object : TimerTask() {
                    override fun run() {
                        requireActivity().finish()

                    }
                }
                timer = Timer()
                timer?.schedule(timerTask, 5 * 60 * 1000)
            }
        })











        return binding.root
    }


    override fun onStart() {
        var i = 0
        super.onStart()


    }

    override fun onDestroy() {
        //val chronometer = Chronometer(context)
        super.onDestroy()
        // When the user exits the app, stop the chronometer
      chronometer.stop()
// Get the elapsed time in milliseconds
        elapsedTime = SystemClock.elapsedRealtime() - chronometer.base

// Calculate the total time spent in seconds
        val totalTimeSpent = elapsedTime / (1000)
        val editor = sharedPreferences.edit()
//        editor.putString("time",totalTimeSpent.toString())
//        editor.apply()

        val username = sharedPreferences.getString("time", "default_username")
        if (username.equals("default_username")){
            editor.putString("time",totalTimeSpent.toString())
            editor.apply()
        }else{
            var tim = username?.toFloat()?.plus(totalTimeSpent.toFloat())
            editor.remove("time")
            editor.putString("time",tim.toString())
            editor.apply()
          //  Toast.makeText(requireContext(), "$tim", Toast.LENGTH_SHORT).show()
        }
        timerTask?.cancel()
        timer?.cancel()


    }

    override fun onPause() {
        super.onPause()
        //val chronometer = Chronometer(context)
        super.onDestroy()
        // When the user exits the app, stop the chronometer
        chronometer.stop()
// Get the elapsed time in milliseconds
        elapsedTime = SystemClock.elapsedRealtime() - chronometer.base

// Calculate the total time spent in seconds
        val totalTimeSpent = elapsedTime / (1000)
        val editor = sharedPreferences.edit()
//        editor.putString("time",totalTimeSpent.toString())
//        editor.apply()

        val username = sharedPreferences.getString("time", "default_username")
        if (username.equals("default_username")){
            editor.putString("time",totalTimeSpent.toString())
            editor.apply()
        }else{
            var tim = username?.toFloat()?.plus(totalTimeSpent.toFloat())
            editor.remove("time")
            editor.putString("time",tim.toString())
            editor.apply()
           // Toast.makeText(requireContext(), "$tim", Toast.LENGTH_SHORT).show()
        }
    }

}





