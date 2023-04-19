package com.example.kodegojobsearchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.kodegojobsearchapp.api.JobSearchAPIClient
import com.example.kodegojobsearchapp.api_model.JobDetailsData
import com.example.kodegojobsearchapp.api_model.JobDetailsResponse
import com.example.kodegojobsearchapp.dao.JobDetailsDAO
import com.example.kodegojobsearchapp.dao.JobDetailsDatabase
import com.example.kodegojobsearchapp.dao.KodegoJobSearchApplication
import com.example.kodegojobsearchapp.databinding.ActivityJobDetailsBinding
import com.example.kodegojobsearchapp.firebase.FirebaseJobApplicationDAOImpl
import com.example.kodegojobsearchapp.model.Applicant
import com.example.kodegojobsearchapp.model.JobApplication
import com.example.kodegojobsearchapp.utils.ProgressDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailsBinding
    
//    private lateinit var jobDetailsList: ArrayList<JobDetailsData>
    private lateinit var jobDetailsData: JobDetailsData //TODO: DifferentModel???
    private lateinit var applicant: Applicant
    private lateinit var dao: FirebaseJobApplicationDAOImpl
    private lateinit var roomsDAO: JobDetailsDAO
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val jobID = bundle?.getString("job_id")

        progressDialog = ProgressDialog(binding.root.context, R.string.sending_job_application)

        dao = FirebaseJobApplicationDAOImpl(applicationContext)
        getApplicant()

        
        supportActionBar?.apply{
            title = "Job Details"
            setDisplayHomeAsUpEnabled(true)
        }

        getDataFromDB(jobID!!)
//        getData(jobID!!)

        binding.btnApply.setOnClickListener{
            /**
             * TODO: DAO has been implemented but has a chance of modification
             */
            apply()
        }
        
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getDataFromDB(jobId: String){
        binding.scrollJobDetails.visibility = View.GONE
        binding.btnApply.visibility = View.GONE
        binding.loadingData.visibility = View.VISIBLE

        roomsDAO = (application as KodegoJobSearchApplication).jobDetailsDatabase.jobDetailsDAO()
        lifecycleScope.launch {
            val data = roomsDAO.getJobDetails(jobId)
            Log.d("DB Data", data.toString())
            if (data != null){
                jobDetailsData = data
                setJobDetailsData()
                binding.scrollJobDetails.visibility = View.VISIBLE
                binding.btnApply.visibility = View.VISIBLE
                binding.loadingData.visibility = View.GONE
            }else{
                getData(jobId)
            }
        }
    }
    
    private fun getData(jobId: String){
        binding.scrollJobDetails.visibility = View.GONE
        binding.btnApply.visibility = View.GONE
        binding.loadingData.visibility = View.VISIBLE

        val call: Call<JobDetailsResponse> = JobSearchAPIClient
            .getJobDetailsData.getJobDetails(jobId, false)

        call.enqueue(object : Callback<JobDetailsResponse> {
            override fun onFailure(call: Call<JobDetailsResponse>, t: Throwable) {
                binding.scrollJobDetails.visibility = View.VISIBLE
                binding.btnApply.visibility = View.VISIBLE
                binding.loadingData.visibility = View.GONE

                Log.d("API CALL", "Failed API CALL")
                Log.e("error", t.message.toString())
            }

            override fun onResponse(
                call: Call<JobDetailsResponse>,
                response: Response<JobDetailsResponse>
            ) {
                binding.scrollJobDetails.visibility = View.VISIBLE
                binding.btnApply.visibility = View.VISIBLE
                binding.loadingData.visibility = View.GONE
                if (response.isSuccessful) {
                    val response: JobDetailsResponse = response.body()!!

                    jobDetailsData = response.data[0]
                    lifecycleScope.launch { roomsDAO.insert(jobDetailsData) }
                    setJobDetailsData()
                }else{
                    Log.e("JobDetail Call", response.message())
                    Toast.makeText(
                        applicationContext,
                        "Failed to retrieve Job Details",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        })
    }

    private fun setJobDetailsData(){
        binding.jobTitle.text = jobDetailsData.jobTitle
        binding.jobCompany.text = jobDetailsData.employerName
        binding.jobLocation.text = "${jobDetailsData.jobCity}, ${jobDetailsData.jobState}, ${jobDetailsData.jobCountry}"
        binding.jobPostedTime.text = jobDetailsData.jobPostedAtDatetimeUtc
        binding.jobEmploymentType.text = jobDetailsData.jobEmploymentType
        binding.jobPublisher.text = jobDetailsData.jobPublisher
        binding.jobDescription.text = jobDetailsData.jobDescription
    }

    private fun getApplicant(){
        lifecycleScope.launch {
            applicant = dao.getApplicant(Firebase.auth.currentUser!!.uid)
        }
    }

    private fun apply(){
        progressDialog.show()
        lifecycleScope.launch {
            val jobApplication = JobApplication(jobDetailsData.jobId, applicant.applicantID)
            if (dao.addJobApplication(jobApplication)){
                Toast.makeText(
                    applicationContext,
                    "Job Application has been sent",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }else{
                Toast.makeText(
                    applicationContext,
                    "Error applying for the job",
                    Toast.LENGTH_SHORT
                ).show()
            }
            progressDialog.dismiss()
        }
    }
}