package co.aione.classtrack.ui.stream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.aione.classtrack.R

class StreamAdapter(private val listener: OnItemClickListener): ListAdapter<String, StreamAdapter.MyViewHolder>(Diffutils) {
    class MyViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val title = item.findViewById(R.id.title_text) as TextView
        val removeIcon = item.findViewById( R.id.deleteSem) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_details, parent, false)
        return  MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.title.text = data
        holder.removeIcon.setOnClickListener{
            listener.onDeleteButtonCLicked(data)
        }
        holder.itemView.setOnClickListener{
            listener.OnItemCicked(data)
        }
    }
    object Diffutils : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
interface  OnItemClickListener{
    fun onDeleteButtonCLicked(sem: String)
    fun OnItemCicked(sem: String)
}