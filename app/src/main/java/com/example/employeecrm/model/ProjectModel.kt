package com.example.employeecrm.model

//model for request the project
//this model for create the new project details
data class ProjectRequestModel(
    val projectName: String,
    val projectStartDate: String,
    val projectEndDate: String,
    val priority: String,
    val description: String,
    val teamId: String,
    val websiteUrl: String,
    val isCompleted: Boolean,
    val isScrap: Boolean,
)


//model for response the project
// this model for fetch all the project
data class ProjectResponseModel(
    val success: Boolean,
    val allProject: List<AllProject>,
    val message: String,
)

data class AllProject(
    val _id: String,
    val projectName: String,
    val projectStartDate: String,
    val projectEndDate: String,
    val priority: String,
    val description: String,
    val adminId: String,
    val teamId: String,
    val websiteUrl: String,
    val isCompleted: Boolean,
    val isScrap: Boolean,
    val completedPercent: Long,
    val createdAt: String,
    val updatedAt: String,
)
