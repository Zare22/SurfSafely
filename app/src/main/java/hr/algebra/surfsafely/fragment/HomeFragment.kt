package hr.algebra.surfsafely.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.adapter.BuyAvatarRecycleAdapter
import hr.algebra.surfsafely.databinding.FragmentHomeBinding
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.viewmodel.BuyAvatarViewModel
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val buyAvatarViewModel by viewModel<BuyAvatarViewModel>()
    private val userViewModel by activityViewModel<UserViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setAdapter()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAdapter() {
        buyAvatarViewModel.avatars.observe(viewLifecycleOwner) { avatars ->
            avatars?.let { avatarList ->
                val existingAvatarIds = userViewModel.avatars.value?.keys ?: emptySet()
                val filteredAvatars = avatarList.filter { avatar ->
                    avatar.id !in existingAvatarIds
                }
                val adapter = BuyAvatarRecycleAdapter(filteredAvatars) { id ->
                    buyAvatarViewModel.viewModelScope.launch {
                        buyAvatarViewModel.buySelectedAvatar(id).onSuccess {
                            userViewModel.getUserAvatars()
                            userViewModel.getUserPoints()
                            buyAvatarViewModel.getAllAvatars()
                            activity?.showToast("Successfully bought avatar")
                        }.onFailure { error ->
                            activity?.showToast(error.message.toString())
                        }
                    }
                }
                binding.imageList.adapter = adapter
            }
        }
    }
}