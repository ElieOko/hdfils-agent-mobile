package com.partners.hdfils_agent.domain.models

import com.partners.hdfils_agent.data.utils.Constants.Companion.routeList
import com.partners.hdfils_agent.R

data class Route(
    val icon        : Int           = R.drawable.doc,
    val title       : String        = "",
    val routeName   : String        = "",
    val position    : Int           = 0,
){
    fun bottomNavigationItems() = routeList
}
