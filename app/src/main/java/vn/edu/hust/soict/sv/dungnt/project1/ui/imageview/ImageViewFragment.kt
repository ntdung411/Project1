package vn.edu.hust.soict.sv.dungnt.project1.ui.imageview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import vn.edu.hust.soict.sv.dungnt.project1.R
import vn.edu.hust.soict.sv.dungnt.project1.data.model.DeviceSettingsRequest
import vn.edu.hust.soict.sv.dungnt.project1.data.remote.Api
import vn.edu.hust.soict.sv.dungnt.project1.databinding.DialogSettingsBinding
import vn.edu.hust.soict.sv.dungnt.project1.databinding.FragmentImageViewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ImageViewFragment : Fragment() {
    private var _binding: FragmentImageViewBinding? = null
    private val binding get() = _binding!!
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    private val viewModel: ImageViewViewModel by viewModels()
    private lateinit var deviceName: String
    private lateinit var deviceID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.imageViewDevice.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.loading_animation
            )
        )

        deviceID = arguments?.getString("deviceID") ?: ""
        deviceName = arguments?.getString("deviceName") ?: ""
        val delayTime = arguments?.getInt("delayTime")
        val resolution = arguments?.getInt("resolution")

        viewModel.deviceName.value = deviceName
        viewModel.delayTime.value = delayTime
        viewModel.resolution.value = resolution

        loadLatestImageForDevice(deviceID)
        binding.deviceName = deviceName

        binding.buttonHistory.setOnClickListener {
            navigateToHistoryViewFragment()
        }

        binding.buttonSettings.setOnClickListener {
            showSettingsDialog()
        }

        binding.buttonRefresh.setOnClickListener {
            loadLatestImageForDevice(deviceID)
            Toast.makeText(
                requireContext(), "Refreshed", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadLatestImageForDevice(deviceID: String) {
        val apiService = Api.retrofitService
        lifecycleScope.launch {
            try {
                val images = apiService.getListImages(deviceID)

                if (images.isNotEmpty()) {
                    val sortedImages = images.sortedByDescending { it.createdAt }
                    val latestImage = sortedImages.first()

                    val formattedDate = dateFormat.format(latestImage.createdAt)
                    binding.createdAt = formattedDate

                    Glide.with(requireContext())
                        .load("http://103.176.178.96:8000/api/v1/photo/${latestImage.imageID}")
                        .into(binding.imageViewDevice)
                } else {
                    binding.imageViewDevice.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.baseline_hide_image_24
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "ImageViewFragment", "Error loading images: ${e.message}", e
                )
                binding.imageViewDevice.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.baseline_error_24
                    )
                )
            }
        }
    }

    private fun showSettingsDialog() {
        val binding: DialogSettingsBinding = DialogSettingsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        viewModel.deviceName.value?.let { binding.editTextName.setText(it) }
        viewModel.delayTime.value?.let { binding.editTextDelayTime.setText(it.toString()) }
        viewModel.resolution.value?.let { binding.spinnerResolution.setSelection(it) }

        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        val alertDialog = alertDialogBuilder.setCancelable(false).setView(binding.root)
            .setPositiveButton("Save", null).setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()

        alertDialog.setOnShowListener {
            val saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton.setOnClickListener {
                val newName = binding.editTextName.text.toString()
                val newDelayTime = binding.editTextDelayTime.text.toString().toIntOrNull() ?: 0
                val newResolution = binding.spinnerResolution.selectedItemPosition

                var hasError = false

                if (newName.isEmpty()) {
                    binding.editTextName.error = "Device Name is required."
                    hasError = true
                }

                if (newDelayTime == 0) {
                    binding.editTextDelayTime.error = "Delay Time is required and not equal 0."
                    hasError = true
                }

                if (!hasError) {
                    viewModel.deviceName.value = newName
                    viewModel.delayTime.value = newDelayTime
                    viewModel.resolution.value = newResolution

                    val farmID = arguments?.getString("farmID") ?: return@setOnClickListener
                    val settingsRequest =
                        DeviceSettingsRequest(farmID, newName, newDelayTime, newResolution)
                    sendDeviceSettings(deviceID, settingsRequest)

                    alertDialog.dismiss()
                }
            }
        }

        alertDialog.show()

    }

    private fun sendDeviceSettings(deviceID: String, settingsRequest: DeviceSettingsRequest) {
        lifecycleScope.launch {
            try {
                Api.retrofitService.putDeviceSettings(deviceID, settingsRequest)
                Log.d("YourFragment", "Success")

                val newName = settingsRequest.name
                viewModel.deviceName.value = newName

                viewModel.delayTime.value = settingsRequest.delayTime
                viewModel.resolution.value = settingsRequest.resolution
                binding.deviceName = settingsRequest.name

                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(), "Settings updated successfully", Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("YourFragment", "Error posting device settings: ${e.message}", e)
            }
        }
    }

    private fun navigateToHistoryViewFragment() {
        val bundle = Bundle().apply {
            putString("deviceID", deviceID)
        }

        findNavController().navigate(
            R.id.action_imageViewFragment_to_imageHistoryFragment, bundle
        )
    }
}