package com.callmonitor.task.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.callmonitor.network.BuildConfig
import com.callmonitor.task.model.UIState
import com.callmonitor.task.R
import com.callmonitor.task.databinding.FragmentCallMonitorBinding
import com.callmonitor.task.viewmodel.CallMonitorViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import android.content.ComponentName
import android.view.View.VISIBLE
import android.view.View.GONE
import android.view.View.INVISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.callmonitor.model.CallRecord
import com.callmonitor.task.utils.CallReceiver
import com.callmonitor.task.view.adapter.CallRecordListAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallMonitorFragment : Fragment() {

    private val viewModel: CallMonitorViewModel by viewModels()
    private lateinit var checkPermissions: ActivityResultLauncher<Array<String>>

    private var _binding: FragmentCallMonitorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCallMonitorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatesListener()
        setClickLister()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        checkForPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.READ_PHONE_STATE] == true &&
                permissions[Manifest.permission.READ_CALL_LOG] == true
                && permissions[Manifest.permission.READ_CONTACTS] == true
                && permissions[Manifest.permission.WRITE_CONTACTS] == true
            ) {
                viewModel.getServicesList()
            } else {
                showPermissionMessage()
            }
        }
    }

    private fun onRequestFinished(message: String) {
        view?.let {
            val snackBar = Snackbar.make(
                it,
                message,
                Snackbar.LENGTH_LONG
            )
            snackBar.show()
        }
    }

    private fun setClickLister() {
        with(binding) {
            stopCommunication.setOnClickListener {
                val pm: PackageManager = requireActivity().packageManager
                val componentName = ComponentName(requireActivity(),
                    CallReceiver::class.java)
                pm.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setStatesListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewStateFlow.collect { callMonitorState ->
                when (callMonitorState.uiState) {
                    UIState.LOADING -> {
                        binding.progressBar.visibility = VISIBLE
                    }
                    UIState.SERVICES_CONTENT -> {
                        binding.toolbar.title =
                            "${getString(R.string.connected_server_title)} ${BuildConfig.API_BASE_URL}"
                        viewModel.getCallLog()
                    }
                    UIState.ERROR -> {
                        binding.progressBar.visibility = GONE
                        onRequestFinished(getString(R.string.snack_bar_error_message))
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.viewStateCallLogFlow.collect { callMonitorState ->
                when (callMonitorState.uiState) {
                    UIState.LOADING -> {
                        binding.progressBar.visibility = VISIBLE
                    }
                    UIState.CALLS_CONTENT -> setCallLogListInfo(callMonitorState.recordedCallsList)
                    UIState.ERROR -> {
                        binding.progressBar.visibility = GONE
                        onRequestFinished(getString(R.string.snack_bar_error_message))
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun setCallLogListInfo(recordedCallsList: List<CallRecord>?) {
        with(binding) {
            if (recordedCallsList.isNullOrEmpty().not()) {
                content.layoutManager = LinearLayoutManager(requireContext())
                val callLogAdapter = CallRecordListAdapter(recordedCallsList)
                content.setHasFixedSize(true)
                content.adapter = callLogAdapter
                content.visibility = VISIBLE
                progressBar.visibility = GONE
            } else {
                content.visibility = INVISIBLE
                progressBar.visibility = GONE
                emptyMessage.visibility = VISIBLE
            }
        }
    }

    private fun checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.getServicesList()
        } else {
            checkPermissions.launch(
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                )
            )
        }
    }

    private fun showPermissionMessage() {
        with(binding) {
            progressBar.visibility = GONE
            permissionTitle.setText(R.string.permission_title_access)
            permissionMessage.setText(R.string.permission_message_access)
            permissionIcon.setImageResource(R.drawable.ic_warning_message)
            permissionLayout.visibility = VISIBLE
            stopCommunication.visibility = GONE
            allow.setOnClickListener {
                stopCommunication.visibility = VISIBLE
                checkForPermissions()
            }
        }
    }
}
