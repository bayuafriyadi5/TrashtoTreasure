package com.capstone.trashtotreasure.view.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.remote.response.AddFeedBackResponse
import com.capstone.trashtotreasure.model.data.repository.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(private val feedbackRepository: FeedbackRepository) : ViewModel() {
    suspend fun postFeedback(
        email: String, rate: String, text : String
    ): LiveData<Result<AddFeedBackResponse>> = feedbackRepository.feedback(email, rate, text)

}