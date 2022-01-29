package com.millionpugs.api.nbp.dto

data class NbpConversionResponse(val code: String, val rates: Collection<NbpRateResponse>)
