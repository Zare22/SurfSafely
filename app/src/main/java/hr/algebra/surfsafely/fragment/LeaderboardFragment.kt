package hr.algebra.surfsafely.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.adapter.LeaderboardAdapter
import hr.algebra.surfsafely.databinding.FragmentLeaderboardBinding
import hr.algebra.surfsafely.viewmodel.LeaderboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeaderboardFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding
    private val leaderboardViewModel by viewModel<LeaderboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        setAdapter()
        hideBottomNavigation()
        return binding.root
    }

    override fun onDestroyView() {
        showBottomNavigation()
        super.onDestroyView()
    }

    private fun showBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun setAdapter() {
        leaderboardViewModel.userList.observe(viewLifecycleOwner) { userList ->
            if (userList != null) {
                val adapter = LeaderboardAdapter(userList)
                binding.leaderboard.adapter = adapter
            }
        }
    }
}