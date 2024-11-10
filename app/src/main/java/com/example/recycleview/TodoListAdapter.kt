package com.example.recycleview

import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleview.databinding.ItemTaskRvBinding
import com.example.recycleview.model.Todo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Collections

class TodoListAdapter(
    private val data: MutableList<Todo>,
    private val mStartDragListener: StartDragListener
) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    // Holds the views for adding it to image and text
    class ViewHolder(val binding: ItemTaskRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun dragItemChange(from: Int, to: Int) {
        Collections.swap(data, from, to)
        notifyItemMoved(from, to)
    }

    fun notifyChangedItem(position: Int){
        notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            data[position].let {
                title.text = it.title
                subTitle.text = it.subTitle
                day.text = it.day.toDayFormat()
                staus.text = it.status.value
            }
           btnSort.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    event?.run {
                        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_DOWN) {
                            mStartDragListener.requestDrag(holder)
                        }
                    }
                    return false
                }
            })
//            btnSort.setOnClickListener(object :View.OnClickListener{
//                override fun onClick(v: View?) {
//
//                }
//
//            })
        }
    }
}

private const val DAY_FORMAT = "d MMMM yyyy"

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDayFormat(): String {
    val formatter = DateTimeFormatter.ofPattern(DAY_FORMAT)
    return this.format(formatter)
}