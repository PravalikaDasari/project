package com.feuji.timesheetentryservice.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feuji.timesheetentryservice.bean.CommonReferenceDetailsBean;
import com.feuji.timesheetentryservice.bean.TimesheetDayBean;
import com.feuji.timesheetentryservice.dto.TimeSheetDayHistoryDto;
import com.feuji.timesheetentryservice.entity.TimesheetDayEntity;
import com.feuji.timesheetentryservice.exception.WeekNotFoundException;
import com.feuji.timesheetentryservice.repository.TimesheetDayRepo;
import com.feuji.timesheetentryservice.service.TimesheetDayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class TimesheetDayServiceImpl implements TimesheetDayService {
	private static Logger log = LoggerFactory.getLogger(TimesheetDayServiceImpl.class);
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	TimesheetDayRepo timesheetDayRepo;

	@Override
	public TimesheetDayEntity saveTimesheetDay(TimesheetDayBean timesheetDayBean) {
		log.info("bean from frontend", timesheetDayBean);
		TimesheetDayEntity timesheetDayEntity = modelMapper.map(timesheetDayBean, TimesheetDayEntity.class);
		timesheetDayEntity = timesheetDayRepo.save(timesheetDayEntity);
		log.info("bean from frontend", timesheetDayEntity);
		return timesheetDayEntity;
	}

	@Override
	public TimesheetDayEntity getTimeSheetDayByuuid(Integer id) {
		try {
			Optional<TimesheetDayEntity> optionalDayTimesheet = timesheetDayRepo.findById(id);
			if (optionalDayTimesheet.isPresent()) {
				return optionalDayTimesheet.get();

			} else {
				throw new WeekNotFoundException("week with id not found");
			}
		} catch (Exception e) {

			log.error("Day not found: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public List<CommonReferenceDetailsBean> getDetailsByTypeId(String typeName) {
		log.info("getDetailsByTypeId start");
		List<String> detailsByTypeName = timesheetDayRepo.getDetailsByTypeName(typeName);
		if (detailsByTypeName != null) {
			List<CommonReferenceDetailsBean> list = new ArrayList<>();
			for (String item : detailsByTypeName) {
				CommonReferenceDetailsBean bean = new CommonReferenceDetailsBean();
				String[] split = item.split(",");

				bean.setReferenceDetailValue(split[1]);
				list.add(bean);
			}
			log.info("getDetailsByTypeId end");
			return list;
		} else {
		}
		return null;
	}

	public List<TimeSheetDayHistoryDto> getTimeSheetDayHistory(String uuId) {
		try {
			List<TimeSheetDayHistoryDto> timeSheetHistory = timesheetDayRepo.getTimeSheetDayHistory(uuId);

			List<TimeSheetDayHistoryDto> sortedHistory = timeSheetHistory.stream()
					.sorted(Comparator.comparing(TimeSheetDayHistoryDto::getDate)).collect(Collectors.toList());

			log.info("Sorted timeSheetHistory: {}", sortedHistory);
			return sortedHistory;
		} catch (Exception e) {
			log.error("An error occurred while sorting timeSheetHistory: {}", e.getMessage());
		}
		return null;
	}
}
