package com.dufekja.spacexapp.models

class Info(val name: String, val founder: String,  val founded: String, val employees: String, val valuation: String) {
    override fun toString(): String {
        return "Info(name='$name', " +
                "founder='$founder', " +
                "founded='$founded', " +
                "employees='$employees', " +
                "valuation='$valuation')"
    }
}