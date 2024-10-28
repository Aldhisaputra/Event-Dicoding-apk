package com.bangkit.eventdicoding.Finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.eventdicoding.Activity.DetailActivity
import com.bangkit.eventdicoding.Event.EventAdapter
import com.bangkit.eventdicoding.R
import com.bangkit.eventdicoding.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private val viewModel: FinishedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_finished, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFinishedBinding.bind(view)

        val adapter = EventAdapter { dicodingEvent ->
            startActivity(Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT, dicodingEvent)
            })
        }

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.adapter = adapter

        viewModel.eventlist.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
