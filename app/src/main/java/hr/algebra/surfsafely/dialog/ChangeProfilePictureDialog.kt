package hr.algebra.surfsafely.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.adapter.ImageRecycleAdapter
import hr.algebra.surfsafely.databinding.FragmentChangeProfilePictureDialogBinding
import hr.algebra.surfsafely.viewmodel.UserViewModel
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

        val images = userViewModel.images.value
        if (!images.isNullOrEmpty()) {
            val adapter = ImageRecycleAdapter(images)
            binding.imageList.adapter = adapter
        }
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            val selectedImage = (binding.imageList.adapter as ImageRecycleAdapter).selectedImage
            userViewModel.updateProfileImage(selectedImage!!)
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