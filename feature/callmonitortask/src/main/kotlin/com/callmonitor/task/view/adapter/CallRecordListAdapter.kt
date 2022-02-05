package com.callmonitor.task.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callmonitor.model.CallRecord
import com.callmonitor.task.R
import com.callmonitor.task.view.adapter.viewholder.ProductViewHolder
import kotlinx.android.synthetic.main.call_record_item_view.view.textViewBeginning
import kotlinx.android.synthetic.main.call_record_item_view.view.textViewDuration
import kotlinx.android.synthetic.main.call_record_item_view.view.textViewName
import kotlinx.android.synthetic.main.call_record_item_view.view.textViewPhoneNumber
import kotlinx.android.synthetic.main.call_record_item_view.view.textViewTimeQueried

class CallRecordListAdapter(private val callList: List<CallRecord>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.call_record_item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        callList?.let {
            holder.itemView.textViewBeginning.text = it[position].beginning
            holder.itemView.textViewDuration.text = it[position].duration
            holder.itemView.textViewName.text = it[position].name
            holder.itemView.textViewPhoneNumber.text = it[position].number
            holder.itemView.textViewTimeQueried.text = it[position].timeQueried.toString()
        }
    }

    override fun getItemCount(): Int {
        return callList?.size ?: 0
    }
}
