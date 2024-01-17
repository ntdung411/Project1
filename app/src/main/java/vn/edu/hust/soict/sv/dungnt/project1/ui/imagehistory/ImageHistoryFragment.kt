package vn.edu.hust.soict.sv.dungnt.project1.ui.imagehistory

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vn.edu.hust.soict.sv.dungnt.project1.data.remote.Api
import vn.edu.hust.soict.sv.dungnt.project1.databinding.FragmentImageHistoryBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ImageHistoryFragment : Fragment() {

    private var _binding: FragmentImageHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var deviceID: String
    private lateinit var imageAdapter: ImageAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageAdapter = ImageAdapter(emptyList())

        val recyclerView: RecyclerView =
            binding.recyclerViewImages
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = imageAdapter

        deviceID = arguments?.getString("deviceID") ?: ""

        val editTextFromDateTime = binding.editTextFromDateTime
        val editTextToDateTime = binding.editTextToDateTime
        val buttonFromDateTimePicker = binding.buttonFromDateTimePicker
        val buttonToDateTimePicker = binding.buttonToDateTimePicker

        editTextFromDateTime.setOnClickListener {
            showDateTimePickerDialog(editTextFromDateTime)
        }

        editTextToDateTime.setOnClickListener {
            showDateTimePickerDialog(editTextToDateTime)
        }

        buttonFromDateTimePicker.setOnClickListener {
            showDateTimePickerDialog(editTextFromDateTime)
        }

        buttonToDateTimePicker.setOnClickListener {
            showDateTimePickerDialog(editTextToDateTime)
        }

        editTextFromDateTime.setOnClickListener {
            editTextFromDateTime.text = null
        }

        editTextToDateTime.setOnClickListener {
            editTextToDateTime.text = null
        }

        val buttonSubmit = binding.buttonSubmit

        buttonSubmit.setOnClickListener {
            val editTextFromDateTime = binding.editTextFromDateTime
            val editTextToDateTime = binding.editTextToDateTime

            val fromTime = editTextFromDateTime.text.toString()
            val toTime = editTextToDateTime.text.toString()

            if ((editTextFromDateTime.text.isEmpty()) && (editTextToDateTime.text.isEmpty())) {
                getListImages(deviceID)
                return@setOnClickListener
            }

            if (editTextFromDateTime.text.isEmpty()) {
                getListImagesByToTime(deviceID, convertDateTimeFormat(toTime))
                return@setOnClickListener
            }

            if (editTextToDateTime.text.isEmpty()) {
                getListImagesByFromTime(deviceID, convertDateTimeFormat(fromTime))
                return@setOnClickListener
            }

            getListImagesByTime(
                deviceID,
                convertDateTimeFormat(fromTime),
                convertDateTimeFormat(toTime)
            )
        }
    }

    private fun showDateTimePickerDialog(editText: EditText) {
        val currentDateTime = Calendar.getInstance()
        val year = currentDateTime.get(Calendar.YEAR)
        val month = currentDateTime.get(Calendar.MONTH)
        val day = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val hour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentDateTime.get(Calendar.MINUTE)
        val second = currentDateTime.get(Calendar.SECOND)

        val dateTimePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
                        val selectedDateTime = String.format(
                            "%02d/%02d/%d %02d:%02d:%02d",
                            selectedDay, selectedMonth + 1, selectedYear,
                            selectedHour, selectedMinute, second
                        )
                        editText.setText(selectedDateTime)
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        dateTimePickerDialog.show()
    }

    private fun getListImagesByTime(deviceID: String, fromTime: String, toTime: String) {
        lifecycleScope.launch {
            try {
                val images = Api.retrofitService.getListImagesByTime(deviceID, fromTime, toTime)
                Log.d(
                    "ImageHistoryFragment",
                    "${images.size} images, ${deviceID}, ${fromTime}, ${toTime}."
                )
                if (images.isNotEmpty()) {
                    imageAdapter.updateImages(images)
                    imageAdapter.notifyDataSetChanged()
                } else {
                    showEmptyListMessage()
                }
            } catch (e: Exception) {
                Log.e(
                    "ImageHistoryFragment",
                    "Error getting images by time: ${e.message}, ${deviceID}, ${fromTime}, ${toTime}",
                    e
                )
                Toast.makeText(
                    requireContext(),
                    "Error getting images: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getListImagesByToTime(deviceID: String, toTime: String) {
        lifecycleScope.launch {
            try {
                val images = Api.retrofitService.getListImagesByToTime(deviceID, toTime)
                Log.d("ImageHistoryFragment", "${images.size} images, ${deviceID},  ${toTime}.")

                if (images.isNotEmpty()) {
                    imageAdapter.updateImages(images)
                    imageAdapter.notifyDataSetChanged()
                } else {
                    showEmptyListMessage()
                }
            } catch (e: Exception) {
                Log.e(
                    "ImageHistoryFragment",
                    "Error getting images to time: ${e.message}, ${deviceID},  ${toTime}",
                    e
                )
                Toast.makeText(
                    requireContext(),
                    "Error getting images: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getListImagesByFromTime(deviceID: String, fromTime: String) {
        lifecycleScope.launch {
            try {
                val images = Api.retrofitService.getListImagesByFromTime(deviceID, fromTime)
                Log.d("ImageHistoryFragment", "${images.size} images, ${deviceID}, ${fromTime}.")

                if (images.isNotEmpty()) {
                    imageAdapter.updateImages(images)
                    imageAdapter.notifyDataSetChanged()
                } else {
                    showEmptyListMessage()
                }
            } catch (e: Exception) {
                Log.e(
                    "ImageHistoryFragment",
                    "Error getting images from time: ${e.message} , ${deviceID}, ${fromTime}",
                    e
                )
                Toast.makeText(
                    requireContext(),
                    "Error getting images: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getListImages(deviceID: String) {
        lifecycleScope.launch {
            try {
                val images = Api.retrofitService.getListImages(deviceID)
                Log.d("ImageHistoryFragment", "${images.size} images.")

                if (images.isNotEmpty()) {
                    imageAdapter.updateImages(images)
                    imageAdapter.notifyDataSetChanged()
                } else {
                    showEmptyListMessage()
                }
            } catch (e: Exception) {
                Log.e("ImageHistoryFragment", "Error getting images: ${e.message}, $deviceID", e)
                Toast.makeText(
                    requireContext(),
                    "Error getting images: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showEmptyListMessage() {
        Toast.makeText(requireContext(), "No images available", Toast.LENGTH_SHORT).show()
    }

    fun convertDateTimeFormat(inputDateTime: String): String {
        val inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        return try {
            val localDateTime = LocalDateTime.parse(inputDateTime, inputFormat)
            val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"))
                .minusHours(7)

            zonedDateTime.format(outputFormat)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
