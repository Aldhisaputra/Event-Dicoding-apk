package com.bangkit.eventdicoding.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.eventdicoding.ViewModelFactory
import com.bangkit.eventdicoding.databinding.FragmentSettingBinding
import dataStore

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val switchTheme = binding.darkMode

        val preferences = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val viewModel = ViewModelProvider(this, ViewModelFactory(preferences))[SettingViewModel::class.java]

        viewModel.getThemeSettings().observe(viewLifecycleOwner) {isDarkMode ->
            if(isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener {_, isChecked: Boolean ->
            viewModel.saveThemeSettings(isChecked)
        }
    }
}