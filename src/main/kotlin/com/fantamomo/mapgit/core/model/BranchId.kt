package com.fantamomo.mapgit.core.model

data class BranchId(val name: String) {
    init {
        require(name.isNotBlank()) { "Branch name cannot be blank" }
        require(name.length <= 100) { "Branch name cannot exceed 100 characters" }
        require(regex.matches(name)) { "Branch name must only contain letters, numbers, underscores, hyphens, and slashes" }
    }

    companion object {
        private val regex = Regex("^[a-zA-Z0-9_/-]+$")
    }
}