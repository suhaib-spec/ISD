package com.isd.DentonMasjid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class
navigation_videos : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: YoutubeLinkAdapter
    private val linkList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = YoutubeLinkAdapter(linkList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            fetchYoutubeLinks()
        }
        
    }

    private fun fetchYoutubeLinks() {
        val db = FirebaseFirestore.getInstance()
        val youtubeCollection = db.collection("youtube")

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val documents = youtubeCollection.get().await()
                for (document in documents) {
                    val link = document.getString("link")
                    link?.let {
                        linkList.add(it)
                    }
                }
                // Update UI on the main thread
                launch(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                println("Error getting documents: $e")
            }
        }

    }


}