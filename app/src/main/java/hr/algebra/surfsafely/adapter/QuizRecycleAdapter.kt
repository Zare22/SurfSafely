package hr.algebra.surfsafely.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.surfsafely.databinding.ItemQuizBinding
import hr.algebra.surfsafely.dto.quiz.QuizDto

class QuizRecycleAdapter(private val quizList: List<QuizDto>,  private val onItemClick: (Long) -> Unit) : RecyclerView.Adapter<QuizRecycleAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuizBinding.inflate(inflater, parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizList[position]
        holder.bind(quiz)
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    inner class QuizViewHolder(private val binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: QuizDto) {
            binding.quizTitle.text = quiz.title
            binding.quizDescription.text = quiz.description

            binding.root.setOnClickListener {
                onItemClick(quiz.id!!)
            }
        }
    }
}