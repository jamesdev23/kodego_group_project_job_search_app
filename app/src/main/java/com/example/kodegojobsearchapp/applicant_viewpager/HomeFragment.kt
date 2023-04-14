package com.example.kodegojobsearchapp.applicant_viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodegojobsearchapp.R
import com.example.kodegojobsearchapp.adapter.JobListingDataAdapter
import com.example.kodegojobsearchapp.api.JobSearchAPIClient
import com.example.kodegojobsearchapp.api_model.JobListingData
import com.example.kodegojobsearchapp.api_model.JobSearchResultResponse
import com.example.kodegojobsearchapp.databinding.FragmentHomeBinding
import com.example.kodegojobsearchapp.utils.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: (anyone) applicant home implementation. might need to wait for API
// TODO: additional: fix failed api call issue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobListingDataAdapter: JobListingDataAdapter
    private var jobListingDatas: ArrayList<JobListingData> = arrayListOf()
//    private lateinit var progressDialog: ProgressDialog

    init {
        if(this.arguments == null) {
            getTabInfo()
        }
    }
    private fun getTabInfo(){
        this.arguments = Bundle().apply {
            putString(FragmentKeys.TabName, "Home")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        progressDialog = ProgressDialog(binding.root.context, R.string.loading_job_listing)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobListingDataAdapter = JobListingDataAdapter(requireContext(), jobListingDatas, requireActivity().supportFragmentManager)
        binding.jobListingList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.jobListingList.adapter = jobListingDataAdapter

//        progressDialog.show()

        getData()  // uncomment to check api

    }

    private fun getData(){
        val call: Call<JobSearchResultResponse> = JobSearchAPIClient
            .getJobSearchData.getJobListing(query, page, numPages)
        binding.jobListingList.visibility = View.GONE
        binding.loadingData.visibility = View.VISIBLE

        call.enqueue(object : Callback<JobSearchResultResponse> {
            override fun onFailure(call: Call<JobSearchResultResponse>, t: Throwable) {
//                progressDialog.dismiss()
                binding.jobListingList.visibility = View.VISIBLE
                binding.loadingData.visibility = View.GONE

                Log.d("API CALL", "Failed API CALL")
                Log.e("error", t.message.toString())
            }

            override fun onResponse(
                call: Call<JobSearchResultResponse>,
                response: Response<JobSearchResultResponse>
            ) {
//                progressDialog.dismiss()
                binding.jobListingList.visibility = View.VISIBLE
                binding.loadingData.visibility = View.GONE

                var response: JobSearchResultResponse = response.body()!! //TODO: NullPointerException

                jobListingDataAdapter.setList(response.dataList)

                val dataLists = response.dataList
                for(data in dataLists) {
                    Log.d("API CALL", "${data.jobTitle} ${data.employerName}")
                }
            }
        })
    }

    companion object {
        val query = "Android developer in Texas, USA"
        val page = 1
        val numPages = 1
    }

}