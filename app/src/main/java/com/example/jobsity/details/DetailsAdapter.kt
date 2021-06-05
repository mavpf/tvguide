package com.example.jobsity.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.R
import com.example.jobsity.network.ShowEpisodes
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class DetailsAdapter(
    private val dataset: List<ShowEpisodes>
) : RecyclerView.Adapter<DetailsAdapter.EpisodesViewHolder>() {

    class EpisodesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var episodePoster: ImageView = view.findViewById(R.id.episode_poster)
        var episodeName: TextView = view.findViewById(R.id.episode_name)
        var episodeCard: MaterialCardView = view.findViewById(R.id.episode_card)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )
            : EpisodesViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_episodes, parent, false)

        return EpisodesViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val item = dataset[position]
        val episodeNameNumber = item.number.toString() + ". " + item.name
        Picasso.get().load(item.image?.medium).into(holder.episodePoster)
        holder.episodeName.text = episodeNameNumber

        holder.episodeCard.setOnClickListener {
            val bundle = bundleOf("idEpisode" to item.idEpisode.toString())
            it.findNavController().navigate(R.id.EpisodeFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}