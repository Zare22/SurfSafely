package hr.algebra.surfsafely.dialog

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.adapter.ImageRecycleAdapter
import hr.algebra.surfsafely.databinding.FragmentChangeProfilePictureDialogBinding
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

@RequiresApi(Build.VERSION_CODES.O)
class ChangeProfilePictureDialog : DialogFragment() {

    private lateinit var binding: FragmentChangeProfilePictureDialogBinding
    private val userViewModel by activityViewModel<UserViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeProfilePictureDialogBinding.inflate(inflater, container, false)
        setAdapter()
        initButtonClickListeners()
        return binding.root
    }

    private fun setAdapter() {
        userViewModel.avatars.observe(viewLifecycleOwner) { avatarList ->
            val adapter = ImageRecycleAdapter(avatarList)
            binding.imageList.adapter = adapter
        }
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            userViewModel.viewModelScope.launch {
                val selectedId = (binding.imageList.adapter as ImageRecycleAdapter).selectedId
                userViewModel.updateProfileAvatar(selectedId!!).onSuccess {
                    activity?.showToast("Changed avatar successfully!")
                }.onFailure {
                    activity?.showToast(it.message.toString())
                }
            }
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        }
    }
}