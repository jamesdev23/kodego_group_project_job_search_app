package com.example.kodegojobsearchapp.api_model

import com.google.gson.annotations.SerializedName

data class JobDetailsData(
    @SerializedName("employer_name")
    val employerName: String,

    @SerializedName("employer_logo")
    val employerLogo: String,

    @SerializedName("employer_website")
    val employerWebsite: String,

    @SerializedName("employer_company_type")
    val employerCompanyType: String,

    @SerializedName("job_publisher")
    val jobPublisher: String,

    @SerializedName("job_id")
    val jobId: String,

    @SerializedName("job_employment_type")
    val jobEmploymentType: String,

    @SerializedName("job_title")
    val jobTitle: String,

    @SerializedName("job_apply_link")
    val jobApplyLink: String,

    @SerializedName("job_apply_is_direct")
    val jobApplyIsDirect: Boolean,

    @SerializedName("job_apply_quality_score")
    val jobApplyQualityScore: Double,

    @SerializedName("job_description")
    val jobDescription: String,

    @SerializedName("job_is_remote")
    val jobIsRemote: Boolean,

    @SerializedName("job_posted_at_timestamp")
    val jobPostedAtTimestamp: Int,

    @SerializedName("job_posted_at_datetime_utc")
    val jobPostedAtDatetimeUtc: String,

    @SerializedName("job_city")
    val jobCity: String,

    @SerializedName("job_state")
    val jobState: String,

    @SerializedName("job_country")
    val jobCountry: String,

    @SerializedName("job_latitude")
    val jobLatitude: Double,

    @SerializedName("job_longitude")
    val jobLongitude: Double,

    @SerializedName("job_benefits")
    val jobBenefits: String?,

    @SerializedName("job_google_link")
    val jobGoogleLink: String
)

data class JobDetailsResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("request_id")
    val requestId: String,

    @SerializedName("parameters")
    val parameters: JobDetailsParameters,

    @SerializedName("data")
    val data: List<JobDetailsData>
)

data class JobDetailsParameters(
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("extended_publisher_details")
    val extendedPublisherDetails: Boolean
)