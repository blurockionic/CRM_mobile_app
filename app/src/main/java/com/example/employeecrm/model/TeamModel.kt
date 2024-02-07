package com.example.employeecrm.model

//this model for team request
data class TeamRequestModel(
    val teamName: String,
    val teamDescription: String,
    val adminProfile: String,
    val selectedMembers: MutableList<String>,
    val selectedManager: String,
    val selectedProject: String,
)


//this model for team response
data class TeamResponseModel(
    val success: Boolean,
    val allTeamsData: List<AllTeamsData>,
    val message: String,
)

data class AllTeamsData(
    val _id: String,
    val teamName: String,
    val teamDescription: String,
    val adminProfile: String,
    val selectedMembers: List<SelectedMember>,
    val selectedManager: Any?,
    val selectedProject: SelectedProject,
)

data class SelectedMember(
    val _id: String,
    val employeeName: String,
    val gender: String,
    val employeeEmail: String,
    val password: String,
    val employeePhoneNumber: String,
    val dateOfBirth: String,
    val address: String,
    val postOffice: String,
    val policeStation: String,
    val city: String,
    val state: String,
    val pinNumber: String,
    val designation: String,
    val designationType: String,
    val department: String,
    val createdAt: String,
    val updatedAt: String,
)

data class SelectedProject(
    val _id: String,
    val projectName: String,
    val projectStartDate: String,
    val projectEndDate: String,
    val priority: String,
    val description: String,
    val adminId: String,
    val managerId: String,
    val websiteUrl: String,
    val isCompleted: Boolean,
    val isScrap: Boolean,
    val completedPercent: Long,
    val createdAt: String,
    val updatedAt: String,
)
