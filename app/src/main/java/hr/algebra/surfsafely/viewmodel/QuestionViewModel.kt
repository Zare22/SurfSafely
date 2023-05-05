package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.model.Question

class QuestionViewModel : ViewModel() {

    private var questionLiveData = MutableLiveData<Question>()

}