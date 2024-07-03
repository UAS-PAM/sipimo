package com.example.sipimo


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(private val itemList: List<Schedule>): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewAdapter>()  {


    class ScheduleViewAdapter(private val view: View): RecyclerView.ViewHolder(view) {
        val diseaseName: TextView = view.findViewById(R.id.tvPenyakit)
        val medicineName: TextView = view.findViewById(R.id.tvObat)
        val timePeriod: TextView = view.findViewById(R.id.tvTime)
        val timeBackground: LinearLayout = view.findViewById(R.id.timeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return ScheduleViewAdapter(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ScheduleViewAdapter, position: Int) {
        val schedule = itemList[position]
        holder.diseaseName.text = schedule.diseaseName
        holder.medicineName.text = schedule.medicineName
        holder.timePeriod.text = schedule.startAt

        if (schedule.startAt == "Noon"){
            holder.timeBackground.setBackgroundResource(R.drawable.element2_home)
        } else if (schedule.startAt == "Night") {
            holder.timeBackground.setBackgroundResource(R.drawable.element3_home)
        }
    }

}