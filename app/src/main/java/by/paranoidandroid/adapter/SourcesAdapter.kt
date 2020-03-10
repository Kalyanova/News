package by.paranoidandroid.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.paranoidandroid.databinding.SourceItemBinding
import by.paranoidandroid.model.Source

class SourcesAdapter(
    private val sources: List<Source>,
    private val toggleListener: OnSourceItemClickListener
) : RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = SourceItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sources[position], toggleListener)
    }

    class ViewHolder(
        private val viewBinding: SourceItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        private val TAG = "SourcesViewHolder"

        fun bind(source: Source, toggleListener: OnSourceItemClickListener) {
            viewBinding.apply {
                sourceTitle.text = source.name
                with(sourceDesc) {
                    text = source.description
                    isChecked = source.enabled
                    setOnClickListener { _ ->
                        val isChecked = !source.enabled
                        Log.d(TAG, "Source toggle is changed: $isChecked")
                        toggleListener.onSourceItemClick(source, isChecked)
                    }
                }
            }
        }
    }

    interface OnSourceItemClickListener {
        fun onSourceItemClick(source: Source, isChecked: Boolean)
    }
}