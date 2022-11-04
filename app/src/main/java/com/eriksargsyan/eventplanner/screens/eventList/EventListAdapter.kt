package com.eriksargsyan.eventplanner.screens.eventList


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.data.model.network.WeatherIconType.Companion.fromId
import com.eriksargsyan.eventplanner.databinding.ItemLayoutEventBinding
import com.eriksargsyan.eventplanner.util.EventTxtTransform.dateToDMY
import com.squareup.picasso.Picasso


class EventListAdapter (
    private val onCardClickListener: (Event, View) -> Unit,
    private val picasso: Picasso
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
                try {
                    picasso
                        .load(fromId(listItem.weather.weatherIcon).wIconURL.url)
                        .placeholder(R.drawable.baseline_double_dash_24dp)
                        .error(R.drawable.baseline_double_dash_24dp)
                        .fit()
                        .into(weatherIcon)
                } catch (e: IllegalArgumentException) {
                    weatherIcon.setImageResource(R.drawable.baseline_double_dash_24dp)
                }

                if (listItem.weather.weatherTemp.isNotEmpty())
                    weatherTemp.text = root
                        .resources.getString(R.string.weather_temp, listItem.weather.weatherTemp)
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