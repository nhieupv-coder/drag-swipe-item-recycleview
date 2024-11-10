package com.example.recycleview

import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleview.databinding.FragmentFirstBinding
import com.example.recycleview.model.Status
import com.example.recycleview.model.Todo
import java.time.LocalDate
import kotlin.math.max

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), StartDragListener {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var todoListAdapter: TodoListAdapter
    private var previousAdapterPosition: Int? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                (recyclerView.adapter as TodoListAdapter).dragItemChange(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != previousAdapterPosition) {
                    previousAdapterPosition?.run {
                        todoListAdapter.notifyChangedItem(previousAdapterPosition!!)
                    }
                    previousAdapterPosition = position
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val todoListAdapter = (recyclerView.adapter as TodoListAdapter)
                val itemView = viewHolder.itemView
                val buttonWidth = max(-200f, dX)
                if (dX < 0) {
                    c.translate(itemView.right + buttonWidth, itemView.top.toFloat())
                    drawButtonDelete(
                        viewHolder.itemView.height,
                        200,
                        canvas = c,
                        todoListAdapter
                    )
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    buttonWidth,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 1f
            }

            override fun isLongPressDragEnabled(): Boolean = false

        }
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = mutableListOf(
            Todo(
                id = 1,
                title = "Buy groceries",
                subTitle = "Get fruits, vegetables, and milk",
                day = LocalDate.now(),
                status = Status.PENDING
            ), Todo(
                id = 2,
                title = "Finish project report",
                subTitle = "Complete the draft and review it",
                day = LocalDate.now().plusDays(1),
                status = Status.IN_PROGRESS
            ), Todo(
                id = 3,
                title = "Workout",
                subTitle = "Go to the gym for strength training",
                day = LocalDate.now().plusDays(2),
                status = Status.COMPLETED
            ), Todo(
                id = 4,
                title = "Call friend",
                subTitle = "Catch up with an old friend over the phone",
                day = LocalDate.now().plusDays(3),
                status = Status.PENDING
            ), Todo(
                id = 5,
                title = "Plan weekend trip",
                subTitle = "Research destinations and make a booking",
                day = LocalDate.now().plusDays(4),
                status = Status.PENDING
            )
        )
        todoListAdapter = TodoListAdapter(data, this)
        binding.recyclerView.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MarginItemDecoration(15))

        }
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    fun drawButtonDelete(height: Int, width: Int, canvas: Canvas, adapter: TodoListAdapter) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        val inflater = LayoutInflater.from(context)
        val buttonLayout = inflater.inflate(R.layout.part_item_delete_rv, null)
        buttonLayout.apply {
            measure(widthSpec, heightSpec)
            layout(0, 0, buttonLayout.measuredWidth, buttonLayout.measuredHeight)
            draw(canvas)

        }
    }
}