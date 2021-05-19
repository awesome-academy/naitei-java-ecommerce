package com.triplet.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.triplet.bean.UserInfo;
import com.triplet.validate.UserValidation;

public class ExcelFileUtils {
	Logger logger = Logger.getLogger(ExcelFileUtils.class);

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	@Async
	public List<UserInfo> convertToUserInfos(MultipartFile file) {
		Workbook wb;
		List<UserInfo> listUserInfo = new ArrayList<UserInfo>();
		Errors errors = null;
		UserValidation userVali = new UserValidation();

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes());
			if (file.getOriginalFilename().endsWith("xls")) {
				wb = new HSSFWorkbook(bis);
			} else if (file.getOriginalFilename().endsWith("xlsx")) {
				wb = new XSSFWorkbook(bis);
			} else {
				logger.info("Received file does not have a standard excel extension.");
				return null;
			}
			Sheet sheet = wb.getSheetAt(0);
			DataFormatter dataFormatter = new DataFormatter();
			Iterator<Row> rowIterator = sheet.rowIterator();
			Row header = rowIterator.next();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				UserInfo userInfo = new UserInfo();
				int cellIdx = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(cell);
					switch (cellIdx) {
					case 0:
						userInfo.setFullname(cellValue);
						break;
					case 1:
						userInfo.setUsername(cellValue);
						break;
					case 2:
						userInfo.setEmail(cellValue);
						break;
					case 3:
						userInfo.setAvatar(cellValue);
						break;
					case 4:
						userInfo.setPhone(cellValue);
						break;
					case 5:
						userInfo.setAddress(cellValue);
						break;
					case 6:
						userInfo.setPassword(cellValue);
						break;
					default:
						break;
					}
					cellIdx++;
				}
				userInfo.setConfirmPassword(userInfo.getPassword());
				userVali.validate(userInfo, errors);
				if (errors != null && errors.hasErrors()) {
					logger.info("Errors in validation user information");
					return null;
				}
				listUserInfo.add(userInfo);
			}
			wb.close();
		} catch (IOException e) {
			logger.error("Error in read excel file: ", e);
			return null;
		}
		return listUserInfo;
	}
}
