package com.example.easycook.ui.recipeDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easycook.R
import com.example.easycook.model.Ingredient

class StepsAdapter : RecyclerView.Adapter<StepsAdapter.StepsViewHolder>() {

    private val steps: MutableList<String> = mutableListOf()

    fun show(steps: List<String>) {
        this.steps.addAll(steps)
        notifyDataSetChanged()
    }

    fun addStep(step : String){
        this.steps.add(step)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StepsAdapter.StepsViewHolder(itemView = inflater.inflate(R.layout.step_item, parent, false))
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        holder.bind(steps[position], position+1)
    }

    override fun getItemCount(): Int = steps.size

    class StepsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val number: TextView = itemView.findViewById(R.id.step_number)
        private val description: TextView = itemView.findViewById(R.id.step_description)

        fun bind(step : String, position: Int){
            number.text = position.toString()
            description.text = step
        }
    }
}