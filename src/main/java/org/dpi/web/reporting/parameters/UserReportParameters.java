package org.dpi.web.reporting.parameters;

import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;

public class UserReportParameters
{

	OutputFormat outputFormat;
	
	String reportCode;
	
	private String selectedPeriodName;
	

	public OutputFormat getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
	}


	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

    public String getSelectedPeriodName() {
        return selectedPeriodName;
    }

    public void setSelectedPeriodName(String selectedPeriodName) {
        this.selectedPeriodName = selectedPeriodName;
    }



}
