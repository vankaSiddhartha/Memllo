package com.techvanka.memllo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techvanka.memllo.databinding.FragmentUploadBinding
import com.techvanka.memllo.ui.UploadVideoActivity
import java.util.*


class UploadFragment :BottomSheetDialogFragment() {
    private lateinit var binding:FragmentUploadBinding
  private lateinit var permission:ActivityResultLauncher<String>
  private var isGiven = false
   private var list:ArrayList<String> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUploadBinding.inflate(inflater,container,false)
        binding.uploadTv.setOnClickListener {

            readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        }


        return binding.root
    }

    private val readExternalPermissionContract =  registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionAccepted ->
        if(isPermissionAccepted) {
         getVideo.launch("video/*")
        } else {
            Toast.makeText(requireContext(), "Permission is declined", Toast.LENGTH_SHORT).show()

        }
    }
    private var getVideo = registerForActivityResult(ActivityResultContracts.GetContent()){

             list.add(it.toString())
       IntentToUpload(it)

    }

    private fun IntentToUpload(it: Uri?) {
        var intent = Intent(activity,UploadVideoActivity::class.java)
       intent.putExtra("imgLink",it.toString())

         startActivity(intent)


    }
    override fun onStart() {
        super.onStart()
        ExoPlayer.Builder(requireContext()).build().release()
    }

    override fun onPause() {
        super.onPause()
        ExoPlayer.Builder(requireContext()).build().release()
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayer.Builder(requireContext()).build().release()
    }
}