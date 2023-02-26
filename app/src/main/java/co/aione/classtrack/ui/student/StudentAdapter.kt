package co.aione.classtrack.ui.student

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.aione.classtrack.R
import co.aione.classtrack.model.StudentModel

class StudentAdapter (val listener: OnItemClickListener): ListAdapter<StudentModel, StudentAdapter.MyViewHolder> (MyDiffutils){
    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val roll = item.findViewById(R.id.roll_no) as TextView
        val name = item.findViewById(R.id.std_name) as TextView
        val present = item.findViewById(R.id.total_present) as TextView
        val removeIcon = item.findViewById(R.id.delete_student) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.name.text = data.name
        holder.roll.text = data.roll.toString()
        holder.present.text = data.present.toString()

        holder.removeIcon.setOnClickListener {
            listener.onDeleteButtonCLicked(data.roll)
        }
    }

    object MyDiffutils : DiffUtil.ItemCallback<StudentModel>(){
        override fun areItemsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean {
           return oldItem.roll == newItem.roll
        }

    }
}

interface OnItemClickListener {
    fun onDeleteButtonCLicked(roll: Int)
}