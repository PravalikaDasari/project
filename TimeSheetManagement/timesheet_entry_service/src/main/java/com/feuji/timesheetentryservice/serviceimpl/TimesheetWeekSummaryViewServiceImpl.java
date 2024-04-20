package com.feuji.timesheetentryservice.serviceimpl;


import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feuji.timesheetentryservice.dto.AccountNameDto;
import com.feuji.timesheetentryservice.dto.ProjectNameDto;
import com.feuji.timesheetentryservice.dto.TimeSheeApprovalDto;

import com.feuji.timesheetentryservice.dto.TimeSheetHistoryDto;
import com.feuji.timesheetentryservice.dto.TimesheetApprovalSecondDto;
import com.feuji.timesheetentryservice.entity.TimesheetWeekSummaryViewEntity;
import com.feuji.timesheetentryservice.repository.TimesheetWeekSummaryRepo;
import com.feuji.timesheetentryservice.service.TimesheetWeekSummaryService;
@Service
public class TimesheetWeekSummaryViewServiceImpl implements TimesheetWeekSummaryService{
	private static Logger log = LoggerFactory.getLogger(TimesheetWeekSummaryViewServiceImpl.class);
	@Autowired
	private TimesheetWeekSummaryRepo timesheetWeekSummaryRepo;
	


	 @Override
	 public List<TimesheetWeekSummaryViewEntity> getTimesheetsForManager(Integer approvedBy, Integer accountId, Integer weekNumber) {
	     List<TimesheetWeekSummaryViewEntity> timesheets = null;
	     try {
	         timesheets = timesheetWeekSummaryRepo.getTimesheetsForManager(approvedBy, accountId, weekNumber);
	         log.info("Retrieved {} timesheets for manager {} for week {} on account/project {}", timesheets.size(), approvedBy, accountId, weekNumber);
	     } catch (Exception ex) {
	         log.error("An error occurred while retrieving timesheets for manager {} for week {} on account/project {}: {}", approvedBy, accountId, weekNumber, ex.getMessage());
	     }
	     return timesheets;
	 }
	 
	 @Override
	 public List<TimesheetApprovalSecondDto> getAccountProjects(Integer accountId,Integer approvedBy) {
	     try {
	         log.info("Fetching account projects for accountId: {}", accountId);
	         List<TimesheetApprovalSecondDto> accountProjects = timesheetWeekSummaryRepo.getAccountProjects(accountId,approvedBy);
	         return accountProjects;
	     } catch (Exception e) {
	         log.error("Error occurred while fetching account projects: {}", e.getMessage());
	         return null;
	     }
	 }

	 @Override
	 public List<AccountNameDto> getAccounts(Integer approvedBy) {
	     try {
	         log.info("Fetching accounts for approvedBy: {}", approvedBy);
	         List<AccountNameDto> accounts = timesheetWeekSummaryRepo.getAccounts(approvedBy);
	         return accounts != null ? accounts : Collections.emptyList(); // Return empty list if accounts is null
	     } catch (Exception e) {
	         log.error("Error occurred while fetching accounts: {}", e.getMessage());
	         return Collections.emptyList(); // Return empty list in case of exception
	     }
	 }

	 public Integer getTotalHours(Integer employeeId, Integer accountProjectId,Integer weekNumber) {
	     try {
	         log.info("Fetching total hours for employeeId {} and accountProjectId {};", employeeId, accountProjectId);
	         Integer totalHours = timesheetWeekSummaryRepo.getTotalHours(employeeId, accountProjectId,weekNumber);
	         return totalHours;
	     } catch(Exception e) {
	         log.error("Error occurred while fetching total hours: {}", e.getMessage());
	         return null;
	     }    
	}
	 
//	--------------------
	 @Override
	 public List<TimesheetApprovalSecondDto> getAllTimesheets() {
	     List<TimesheetApprovalSecondDto> dtos = new ArrayList<>();
	     List<TimesheetWeekSummaryViewEntity> entities = timesheetWeekSummaryRepo.findAll();
	     for (TimesheetWeekSummaryViewEntity entity : entities) {
	         TimesheetApprovalSecondDto dto = new TimesheetApprovalSecondDto();
	         
	         // Map entity fields to DTO fields
	         dto.setEmployeeId(entity.getEmployeeId());
	         dto.setWeekStartDate(entity.getWeekStartDate());
	         dto.setWeekEndDate(entity.getWeekEndDate());
	         dto.setPlannedStartDate(entity.getPlannedStartDate());
	         dto.setPlannedEndDate(entity.getPlannedEndDate());
//	         dto.setAccountName(entity.getAccountId());  // Uncommented line
	         dto.setDesignation(entity.getDesignation());
//	         dto.setManagerId(entity.getManagerId());  // Uncommented line
	         dto.setEmail(entity.getEmail());
	         dto.setEmpCode(entity.getEmployeeCode());
	         dto.setFullName(entity.getFullName());
	         dto.setProjectName(entity.getProjectName());
	         dto.setBillingHours(entity.getTotalBillingHours());
	         dto.setNonBillinghours(entity.getTotalNonBillingHours());
	         dto.setLeaveDays(entity.getTotalLeaveHours());
	         dto.setTimesheetStatus(entity.getTimesheetStatus());
	         
	         dtos.add(dto);
	     }
	     return dtos;
	 }

	 @Override
	 public List<TimeSheeApprovalDto> getTimeSheetApproval(Integer projectManagerId, Integer year, Integer accountId) {
	     try {
	         List<TimeSheeApprovalDto> timeSheetHistory = timesheetWeekSummaryRepo.getTimeSheetApproval(projectManagerId, year, accountId);
	         log.info("timeSheetHistory: {}", timeSheetHistory);
	         return timeSheetHistory;
	     } catch (Exception e) {
	         log.error("An error occurred while fetching time sheet approvals: {}", e.getMessage());
	     }
	     return null;
	 }

	 @Override
	 public List<TimeSheeApprovalDto> getTimeSheetApprovalByEmployeeId(Integer projectManagerId, String month, Integer year,
	         Integer accountId, Integer employeeId) {
	     try {
	         List<TimeSheeApprovalDto> timeSheetHistory = timesheetWeekSummaryRepo.getTimeSheetApprovalByEmployeeId(projectManagerId, month, year, accountId, employeeId);
	         log.info("timeSheetHistory: {}", timeSheetHistory);
	         return timeSheetHistory;
	     } catch (Exception e) {
	         log.error("An error occurred while fetching time sheet approvals by employee ID: {}", e.getMessage());
	     }
	     return null;
	 }
	
}