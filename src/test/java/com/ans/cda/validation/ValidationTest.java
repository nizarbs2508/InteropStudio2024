package com.ans.cda.validation;

import java.io.File;

import com.ans.cda.service.validation.ValidationService;
import com.ans.cda.utilities.general.Constant;

/**
 * ValidationTest
 * 
 * @author bensalem Nizar
 */
public class ValidationTest {
	/**
	 * testValidateMeta
	 */
	@org.junit.jupiter.api.Test
	void testValidateMeta() {
		ValidationService.validateMeta(new File(Constant.FILEPATH), Constant.MODEL, Constant.ASIPXDM,
				Constant.URLVALIDATION);
	}

	/**
	 * testValidateCda
	 */
	@org.junit.jupiter.api.Test
	void testValidateCda() {
		ValidationService.validateCda(new File(Constant.FILEPATHCDA), Constant.MODELCDA, Constant.URLVALIDATION,
				Constant.API, null);
	}

	/**
	 * displayLastReport
	 */
	@org.junit.jupiter.api.Test
	void displayLastReport() {
		ValidationService.displayLastReport();
	}
}
