package com.example.sipimo


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class ScheduleAdapter(private val itemList: List<Schedule>): RecyclerView.Adapter<ScheduleAdapter.ScheduleViewAdapter>()  {


    class ScheduleViewAdapter(private val view: View): RecyclerView.ViewHolder(view) {
        val diseaseName: TextView = view.findViewById(R.id.tvPenyakit)
        val medicineName: TextView = view.findViewById(R.id.tvObat)
        val timePeriod: TextView = view.findViewById(R.id.tvTime)
        val timeBackground: LinearLayout = view.findViewById(R.id.timeLayout)
        val doBtn: Button = view.findViewById(R.id.doBtn)
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
        } else if (schedule.startAt == "Morning") {
            holder.timeBackground.setBackgroundResource(R.drawable.element6_home)
        }

        holder.doBtn.setOnClickListener {
            val schedule = itemList[position]
            val database = FirebaseDatabase.getInstance("https://sipimo-pam-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val myRef = database.getReference("schedules")

            if (schedule.startAt == "Morning") {
                schedule.startAt = "Noon"
            } else if (schedule.startAt == "Noon") {
                schedule.startAt = "Night"
            } else if (schedule.startAt == "Night") {
                schedule.startAt = "Morning"
            }

            val newSchedule = Schedule(
                schedule.userId,
                schedule.scheduleId,
                schedule.diseaseName,
                schedule.medicineName,
                schedule.drugType,
                schedule.amountOfDrug - schedule.used,
                schedule.used, schedule.during, schedule.startAt
            )
            myRef.child(schedule.scheduleId).setValue(newSchedule)

            if(schedule.amountOfDrug - schedule.used <= 0) {
                myRef.child(schedule.scheduleId).removeValue()
            }

        }
    }

    fun generateUniqueID(): String {
        return UUID.randomUUID().toString()
    }

}