package com.test.serverapp.ui.server

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.server.server.CallMonitorServer
import com.test.serverapp.R
import com.test.serverapp.databinding.ServerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ServerFragment : Fragment() {

    private var _binding: ServerFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var serverApi: CallMonitorServer

    companion object {
        fun newInstance() = ServerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ServerFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setClickListeners() {
        with(binding) {

            startServerButton.setOnClickListener {
                serverApi.startServer()
                message.text = getString(R.string.server_started_title, serverApi.getIpServer())
            }

            stopServerButton.setOnClickListener {
                serverApi.stopServer()
                message.text = getString(R.string.server_stop_title)
            }
        }
    }

}