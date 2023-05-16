package hr.algebra.surfsafely.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentLoginBinding
import hr.algebra.surfsafely.databinding.FragmentScanBinding
import hr.algebra.surfsafely.dto.checkurl.CheckUrlRequest
import hr.algebra.surfsafely.dto.checkurl.ThreatEntry
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import java.io.File

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private var selectedFilePath: String? = null
    private val apiService by inject<ApiService>()

    private val selectFileContract = registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
        selectedFilePath = contentUri?.let { uri ->
            val contentResolver = requireContext().contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(requireContext().cacheDir, "file_name")
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            file.absolutePath
        }
        binding.selectedFileName.text = contentUri.toString()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        setButtonListeners()
        return binding.root
    }

    private fun setButtonListeners() {
        binding.btnScanUrl.setOnClickListener {
            lifecycleScope.launch {
                val entries = listOf(ThreatEntry(binding.urlInput.text.toString()))
                val checkUrlRequest = CheckUrlRequest(threatEntries = entries)
                val response = withContext(Dispatchers.IO) {
                    apiService.checkUrl(checkUrlRequest).execute()
                }
                if (response.body()?.data?.matches == null) {
                    binding.resultsMessage.text = "This url is safe!"
                } else {
                    val countOfMatches = response.body()?.data?.matches?.count()
                    binding.resultsMessage.text =
                        "This url has been flagged as dangerous by $countOfMatches vendors!"
                }
            }
        }

        binding.btnSelectFile.setOnClickListener {
            selectFileContract.launch("application/*")
        }

        binding.btnScanFile.setOnClickListener {

            val file = File(selectedFilePath!!)
            val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestFile)

            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    apiService.checkFile(filePart).execute()
                }
                binding.resultsMessage.text = response.body()?.data?.data?.attributes?.stats?.malicious.toString()
            }
        }
    }
}