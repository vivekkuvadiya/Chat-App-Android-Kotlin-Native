package com.vivek.chatingapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vivek.chatingapp.R
import com.vivek.chatingapp.adapter.RecentConversationsAdapter
import com.vivek.chatingapp.databinding.MainFragmentBinding
import com.vivek.chatingapp.ui.registration.RegistrationActivity
import com.vivek.chatingapp.utils.Constant
import com.vivek.chatingapp.utils.decodeToBitmap
import com.vivek.chatingapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: RecentConversationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        clickListener()
        updateDetails()
        setRecyclerview()

        viewModel.recentMessageEventListener(adapter.getRecentList()) {
            adapter.updateRecentConversion(it)
            binding.rvRecentConversation.visibility = View.VISIBLE
            binding.pb.visibility = View.GONE
            binding.rvRecentConversation.smoothScrollToPosition(0)
        }

    }

    private fun updateDetails() {
        binding.ivProfile.setImageBitmap(viewModel.loadUserDetails().decodeToBitmap())
        binding.tvName.text = viewModel.getName()
    }

    private fun clickListener() {
        binding.ivSearch.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_usersFragment) }
        binding.ivMore.setOnClickListener { showMoreMenu() }
    }

    private fun setRecyclerview() {
        adapter = RecentConversationsAdapter()
        binding.rvRecentConversation.apply {
            setHasFixedSize(true)
            adapter = this@MainFragment.adapter
        }
        adapter.onClickConversation = { user ->
            val bundle = Bundle()
            bundle.putSerializable(Constant.KEY_USER, user)
            findNavController().navigate(R.id.action_mainFragment_to_chatFragment, bundle)
        }
    }

    private fun signOut() {

        viewModel.signOut().observe(viewLifecycleOwner) {
            if (it) {
                requireContext().toast("SignOut")
                val intent = Intent(requireActivity(), RegistrationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            } else {
                requireContext().toast("Unable to signOut")
            }
        }


    }

    fun showMoreMenu() {
        val moreMenu = PopupMenu(requireContext(), binding.ivMore)
        moreMenu.inflate(R.menu.menu_more)
        moreMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_sign_out){
                signOut()
                return@setOnMenuItemClickListener true
            }
            false
        }
        moreMenu.show()
    }

}