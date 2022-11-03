package com.eriksargsyan.eventplanner.screens.eventList


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.databinding.ItemLayoutEventBinding

import com.eriksargsyan.eventplanner.util.EventTxtTransform.dateToDMY


class EventListAdapter(
    private val onCardClickListener: (Event, View) -> Unit,
) : ListAdapter<Event, EventListAdapter.EventViewHolder>(EventDiffCallback) {

    object EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }


    inner class EventViewHolder(
        private val binding: ItemLayoutEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: Event) {
            with(binding) {

                eventName.text = listItem.eventName
                eventPlace.text = listItem.cityName
                weatherIcon.setImageResource(R.drawable.baseline_double_dash_24dp)
                weatherTemp.text = "st"
                eventDate.text = dateToDMY(listItem.date)
                cardEvent.setOnClickListener {
                    onCardClickListener.invoke(listItem, cardEvent)
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemViewBinding = ItemLayoutEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}