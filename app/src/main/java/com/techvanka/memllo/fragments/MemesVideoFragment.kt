package com.techvanka.memllo.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
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
import com.techvanka.memllo.ui.IntentLinkActivity
import com.techvanka.memllo.ui.ShareVideoView
import kotlinx.coroutines.*


class MemesVideoFragment : Fragment() {
    private lateinit var binding: FragmentMemesBinding
    private val videos = ArrayList<VideoModel>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private lateinit var adapter: VideoAdapter
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val adapterScope = CoroutineScope(lifecycleScope.coroutineContext + Dispatchers.Main)
        val binding: FragmentMemesBinding = FragmentMemesBinding.inflate(inflater, container, false)
        var list = arrayListOf<Int>()
        list.add(R.layout.fragment_comments_a)
        list.add(R.id.comment_rv)
        list.add(R.id.comments_et)
        list.add(R.id.comments_sendbtn)

        FirebaseDatabase.getInstance().getReference("Videos")
            .addValueEventListener(object : ValueEventListener {
                var intentUriList = arrayListOf<VideoUploadModel>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    var data = snapshot.children
                    var list1 = ArrayList<VideoUploadModel>()
                    for (snap in snapshot.children) {
                        var data = snap.getValue(VideoUploadModel::class.java)



                        list1.add(data!!)


                    }

                    list1.shuffle()
                    try {
                        adapter =
                            VideoAdapter(requireContext(), list1, list)
                        binding.memesView.adapter = adapter

                    }catch (e:Exception){

                    }


                }


                override fun onCancelled(error: DatabaseError) {

                }

            })












        return binding.root
    }


    override fun onStart() {
        var i = 0
        super.onStart()


    }
}





