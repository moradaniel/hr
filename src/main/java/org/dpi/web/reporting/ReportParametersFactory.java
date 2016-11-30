package org.dpi.web.reporting;

public class ReportParametersFactory {
    
    public static IReportParameters buildReportParameters(ReportParameters reportParameters){
       if(reportParameters == null){
          return null;
       }     
       if(reportParameters.getReportCode().equalsIgnoreCase(ReportService.Reports.EmployeeAdditionsPromotionsReport.name())){
           EmployeeAdditionsPromotionsReportParameters employeeAdditionsPromotionsReportParameters = new EmployeeAdditionsPromotionsReportParameters();
           employeeAdditionsPromotionsReportParameters.setOutputFormat(reportParameters.getSelectedOutputFormat());
           employeeAdditionsPromotionsReportParameters.setGeneratedByUser(reportParameters.getGeneratedByUser());
           employeeAdditionsPromotionsReportParameters.setDepartmentId(reportParameters.getDepartmentIds().iterator().next());
           employeeAdditionsPromotionsReportParameters.addCreditPeriodNames(reportParameters.getSelectedPeriodName());
           return employeeAdditionsPromotionsReportParameters;
          
       } else if(reportParameters.getReportCode().equalsIgnoreCase(ReportService.Reports.CreditsEntriesReport.name())){
           
           
           CreditsEntriesReportParameters creditsEntriesReportParameters = new CreditsEntriesReportParameters();
           creditsEntriesReportParameters.setOutputFormat(reportParameters.getSelectedOutputFormat());
           creditsEntriesReportParameters.setGeneratedByUser(reportParameters.getGeneratedByUser());
           creditsEntriesReportParameters.addCreditPeriodNames(reportParameters.getSelectedPeriodName());
           return creditsEntriesReportParameters;
          
       }
       
       return null;
    }
 }