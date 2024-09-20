package com.ans.cda.service.artdecor;

/**
 * ArtDecorServiceTest
 * 
 * @author bensalem Nizar
 */
final class ArtDecorServiceTest {

	/**
	 * ArtDecorServiceTest
	 */
	private ArtDecorServiceTest() {
		// empty constructor
	}

	/**
	 * ArtDecorService
	 */

	/**
	 * testArtDecorCleaning
	 */
	@org.junit.jupiter.api.Test
	void testArtDecorCleaning() {
		ArtDecorService.transform("C:\\Users\\User\\Downloads\\Backup BBR 2024-06-21.xml");
	}
	
	/**
	 * testConvertXmlToJson
	 */
	@org.junit.jupiter.api.Test
	void testConvertXmlToJson() {
		ArtDecorService.convertXmlToJson("C:\\Users\\User\\Downloads\\Backup BBR 2024-06-21.xml");
	}
}
