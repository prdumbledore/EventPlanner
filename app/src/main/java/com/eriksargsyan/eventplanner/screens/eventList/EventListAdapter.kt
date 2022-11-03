package com.eriksargsyan.eventplanner.screens.eventList


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eriksargsyan.eventplanner.R
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.data.model.domain.EventStatus.Companion.fromId
import com.eriksargsyan.eventplanner.databinding.ItemLayoutEventBinding
import com.eriksargsyan.eventplanner.util.EventTxtTransform.dateToDMY


class EventListAdapter(
    private val onCardClickListener: (Event, View) -> Unit,
    private val onCheckedStateChangeListener: (Event) -> Unit,
) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private val list: MutableList<Event> = mutableListOf()


    fun submitList(newList: List<Event>) {
//        if (list.size < newList.size) {
//            val newListFiltered = newList.filter { !list.contains(it) }
//            list.addAll(newListFiltered)
//            notifyItemRangeInserted(list.size - 1, newListFiltered.size)
//        }
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
        //todo diffUtil

    }


    inner class EventViewHolder(
        private val binding: ItemLayoutEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: Event) {
            with(binding) {

                eventName.text = listItem.eventName
                eventPlace.text = listItem.cityName
                weatherIcon.setImageResource(R.drawable.weather_icon)
                weatherTemp.text = "9 C"
                eventDate.text = dateToDMY(listItem.date)
                chipGroup.check(listItem.status.id)
                cardEvent.setOnClickListener {
                    onCardClickListener.invoke(listItem, cardEvent)
                }

                chipGroup.setOnCheckedStateChangeListener { _, _ ->
                    onCheckedStateChangeListener.invoke(
                        listItem.copy(status = fromId(chipGroup.checkedChipId))
                    )
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
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}