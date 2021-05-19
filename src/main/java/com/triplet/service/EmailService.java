package com.triplet.service;

import java.util.List;

import com.triplet.bean.Feedback;

public interface EmailService {

	void sendFeedbackMail(Feedback feedback);

	void sendImportExcelError(String message, List<Integer> linesError, String fullname, String filename);
}
