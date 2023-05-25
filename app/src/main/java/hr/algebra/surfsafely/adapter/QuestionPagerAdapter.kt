package hr.algebra.surfsafely.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.dto.quiz.QuestionDto
import hr.algebra.surfsafely.interfaces.AnswerSelectionListener

class QuestionPagerAdapter(
    private val context: Context,
    private val questions: List<QuestionDto>,
    private val answerSelectionListener: AnswerSelectionListener
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_question, container, false)

        val question = questions[position]

        val textQuestion = view.findViewById<MaterialTextView>(R.id.textQuestion)
        textQuestion.text = question.questionText

        val layoutAnswerOptions = view.findViewById<GridLayout>(R.id.gridAnswerOptions)
        layoutAnswerOptions.removeAllViews()

        for (answer in question.answerDtoList) {
            val checkBox = MaterialCheckBox(context)
            checkBox.text = answer.text

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val answerId = answer.id
                answerSelectionListener.let {
                    if (isChecked) it.onAnswerSelected(answerId) else it.onAnswerDeselected(answerId)
                }
            }
            layoutAnswerOptions.addView(checkBox)
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return questions.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}