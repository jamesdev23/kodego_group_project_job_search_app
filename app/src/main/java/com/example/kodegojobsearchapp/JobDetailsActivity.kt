package com.example.kodegojobsearchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kodegojobsearchapp.api.JobSearchAPIClient
import com.example.kodegojobsearchapp.api_model.JobDetailsData
import com.example.kodegojobsearchapp.api_model.JobDetailsResponse
import com.example.kodegojobsearchapp.databinding.ActivityJobDetailsBinding
import com.example.kodegojobsearchapp.utils.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailsBinding
    
    private lateinit var jobDetailsList: ArrayList<JobDetailsData>
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val jobID = bundle?.getString("job_id")
        
        supportActionBar?.apply{
            title = "Job Details"
            setDisplayHomeAsUpEnabled(true)
        }

        progressDialog.show()

        getData(jobID!!)

        binding.btnApply.setOnClickListener{
            // TODO: (for dave) add code when applicant clicks the apply button
        }
        
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    
    private fun getData(jobId: String){
        val call: Call<JobDetailsResponse> = JobSearchAPIClient
            .getJobDetailsData.getJobDetails(jobId, false)

        call.enqueue(object : Callback<JobDetailsResponse> {
            override fun onFailure(call: Call<JobDetailsResponse>, t: Throwable) {
                Log.d("API CALL", "Failed API CALL")
                Log.e("error", t.message.toString())
            }

            override fun onResponse(
                call: Call<JobDetailsResponse>,
                response: Response<JobDetailsResponse>
            ) {
                var response: JobDetailsResponse = response!!.body()!!

                jobDetailsList = response.data

                for(jobDetails in jobDetailsList){
                    binding.jobTitle.text = jobDetails.jobTitle
                    binding.jobCompany.text = jobDetails.employerName
                    binding.jobLocation.text = "${jobDetails.jobCity}, ${jobDetails.jobState}, ${jobDetails.jobCountry}"
                    binding.jobPostedTime.text = jobDetails.jobPostedAtDatetimeUtc
                    binding.jobEmploymentType.text = jobDetails.jobEmploymentType
                    binding.jobPublisher.text = jobDetails.jobPublisher
                    binding.jobDescription.text = jobDetails.jobDescription

                    Log.d("API CALL", "${jobDetails.jobTitle} ${jobDetails.employerName}")
                }
            }
        })
    }
}