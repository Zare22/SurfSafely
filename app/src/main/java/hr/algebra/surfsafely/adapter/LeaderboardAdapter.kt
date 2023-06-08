package hr.algebra.surfsafely.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.surfsafely.databinding.ItemLeaderboardEntryBinding
import hr.algebra.surfsafely.model.UserLeaderboard

class LeaderboardAdapter(private val userList: List<UserLeaderboard?>?) : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLeaderboardEntryBinding.inflate(inflater, parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = userList!![position]
        holder.bind(user!!, position + 1)
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

    inner class LeaderboardViewHolder(private val binding: ItemLeaderboardEntryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserLeaderboard, position: Int) {
            binding.rankLeaderboard.text = position.toString()
            binding.usernameLeaderboard.text = user.username
            binding.pointsLeaderboard.text = user.points.toString()
        }
    }
}
