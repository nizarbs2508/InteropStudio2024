package com.ans.cda.service.parametrage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import com.ans.cda.utilities.general.Constant;

/**
 * IniFile
 * 
 * @author bensalem Nizar
 */
public final class IniFile {
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(IniFile.class);
	/**
	 * file
	 */
	private static final File FILE = new File(Constant.INTEROPFOLDER + "\\InteropStudio2024.ini");
	/**
	 * API
	 */
	private static final String API = "API-MAPPING";
	/**
	 * NOS
	 */
	private static final String NOS = "NOS";

	/**
	 * IniFile
	 */
	private IniFile() {
		// empty constructor
	}

	/**
	 * init
	 */
	public static void init() {
		try {
			FILE.createNewFile();
			final Ini ini = new Ini(FILE);
			String dir = System.getProperty("user.dir");
			dir = dir.replace("\\", "/");
			ini.put("MEMORY", "LAST-CDA-FILE", "");
			ini.put("MEMORY", "LAST-PATH-USED", dir);
			ini.put("MEMORY", "LAST-META-FILE", "");
			ini.put("LOINC", "FILE-NAME", "Loinc.csv");
			ini.put("DIAGNOSTIC", "LAST-REQUEST",
					"string-length(/ClinicalDocument/effectiveTime[1]/@value/string()) < 19");
			ini.put("API", "API-URL-PROD", "https://interop.esante.gouv.fr/evs/rest/validations");
			ini.put("API", "CDA-VALIDATION-SERVICE-NAME", "Schematron Based CDA Validator");
			ini.put("API", "METADATA-VALIDATION-SERVICE-NAME", "Model-based XDS Validator");

			ini.put("SCH-CLEANER", "LAST_DIRECTORY_SELECTED", dir);

			ini.put(API, "1.2.250.1.213.1.1.1.40", "ANEST-CR-ANEST_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.41", "ANEST-CR-CPA_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.15", "AVC-AUNV_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.16", "AVC-EUNV_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.25", "AVC-PAVC_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.17", "AVC-SUNV_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.32", "CANCER-CR-GM_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.28", "CANCER-D2LM-FIDD_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.27", "CANCER-D2LM-FIN_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.8", "CANCER-FRCP-2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.26", "CANCER-PPS_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.5.3", "CSE-CS24_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.5.1", "CSE-CS8_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.5.2", "CSE-CS9_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.5.4", "CSE-MDE_2023.01");
			ini.put(API, "1.2.250.1.213.1.1.1.22", "DLU-EHPAD-DLU_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.24", "DLU-EHPAD-FLUDR_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.23", "DLU-EHPAD-FLUDT_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.21", "LDL-EES_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.29", "LDL-SES_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.12.1", "OBP-SAP_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.12.5", "OBP-SCE_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.12.4", "OBP-SCM_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.12.3", "OBP-SNE-2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.12.2", "OBP-SNM-2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.42", "OPH-BRE_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.18", "OPH-CR-RTN_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.30", "SDMMR_2022.01");
			ini.put(API, "1.3.6.1.4.1.19376.1.2.20", "TLM_CR_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.38", "TLM_DA_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.37", "VAC-2023.01");
			ini.put(API, "1.2.250.1.213.1.1.1.46", "VAC-NOTE_2023.01");
			ini.put(API, "1.2.250.1.213.1.1.1.13", "VSM_2013 (v1.4)");
			ini.put(API, "1.2.250.1.213.1.1.1.13", "VSM_2022.01 (v1.4 de 2013)");
			ini.put(API, "1.2.250.1.213.1.1.1.20", "ANS-PPS-PAERPA_2022.01");
			ini.put(API, "1.2.250.1.213.1.1.1.59", "ANS-PPS-BIO-ATTEST-DEPIST_COVID-19_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.59", "BIO-ATTEST-DEPIST_COVID-19_Grippe-A_Grippe-B_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.55", "BIO-CR-BIO-2021.01");
			ini.put(API, "1.2.250.1.213.1.1.1.55", "BIO-CR-BIO-2023.01");
			ini.put(API, "1.2.250.1.213.1.1.1.2.1.1", "CARD-F-PRC-AVK_2022");
			ini.put(API, "1.2.250.1.213.1.1.1.2.1.3", "CARD-F-PRC-DCI_2022");
			ini.put(API, "1.2.250.1.213.1.1.1.2.1.5", "CARD-F-PRC-PPV_2022");
			ini.put(API, "1.2.250.1.213.1.1.1.2.1.4", "CARD-F-PRC-PSC_2022");
			ini.put(API, "1.2.250.1.213.1.1.1.2.1.2", "CARD-F-PRC-TAP_2022");
			ini.put(API, "1.2.250.1.213.1.1.1.39", "EP-MED-DM_2023.01");
			ini.put(API, "1.2.250.1.213.1.1.1.45", "IMG-CR-IMG_2024.01");
			ini.put(API, "1.2.250.1.213.1.1.1.47", "IMG-DA-IMG_2024.01");
			ini.put(API, "METADATA", "ASIP XDM ITI-32 FR Distribute Document Set on Media");

			ini.put(NOS, "ASS_A11",
					"https://mos.esante.gouv.fr/NOS/ASS_A11-CorresModeleCDA-XdsFormatCode-CISIS/ASS_A11-CorresModeleCDA-XdsFormatCode-CISIS.xml");
			ini.put(NOS, "ASS_X04",
					"https://mos.esante.gouv.fr/NOS/ASS_X04-CorrespondanceType-Classe-CISIS/ASS_X04-CorrespondanceType-Classe-CISIS.xml");
			ini.put(NOS, "JDV_J01",
					"https://mos.esante.gouv.fr/NOS/JDV_J01-XdsAuthorSpecialty-CISIS/JDV_J01-XdsAuthorSpecialty-CISIS.xml");
			ini.put(NOS, "JDV_J02",
					"https://mos.esante.gouv.fr/NOS/JDV_J02-XdsHealthcareFacilityTypeCode-CISIS/JDV_J02-XdsHealthcareFacilityTypeCode-CISIS.xml");
			ini.put(NOS, "JDV_J04",
					"https://mos.esante.gouv.fr/NOS/JDV_J04-XdsPracticeSettingCode-CISIS/JDV_J04-XdsPracticeSettingCode-CISIS.xml");
			ini.put(NOS, "JDV_J06",
					"https://mos.esante.gouv.fr/NOS/JDV_J06-XdsClassCode-CISIS/JDV_J06-XdsClassCode-CISIS.xml");
			ini.put(NOS, "JDV_J10",
					"https://mos.esante.gouv.fr/NOS/JDV_J10-XdsFormatCode-CISIS/JDV_J10-XdsFormatCode-CISIS.xml");
			ini.put(NOS, "TRE_A04", "https://mos.esante.gouv.fr/NOS/TRE_A04-Loinc/TRE_A04-Loinc.xml");
			ini.put(NOS, "TRE_A05",
					"https://mos.esante.gouv.fr/NOS/TRE_A05-TypeDocComplementaire/TRE_A05-TypeDocComplementaire.xml");
			ini.put(NOS, "TRE_A06",
					"https://mos.esante.gouv.fr/NOS/TRE_A06-FormatCodeComplementaire/TRE_A06-FormatCodeComplementaire.xml");

			ini.put("JOSHUA5", "TYPEWRITER-ACTIVATED", "FALSE");
			ini.put("JOSHUA5", "SILENCE", "FALSE");

			ini.put("MAINFORM", "URL-ANS-LOGO", "https://esante.gouv.fr/offres-services/ci-sis/espace-publication");

			ini.put("IHE_XDM", "DEFAULT_CDA_NAME", "DOC0001.XML");
			ini.put("IHE_XDM", "DEFAULT_METADATA_NAME", "METADATA.XML");

			ini.put("FHIR", "URL-ONTOSERVER", "http://r4.ontoserver.csiro.au/fhir");
			ini.put("FHIR", "FHIR2SVS-CONVERTER", "Fhir2ArtDecor-SVS.xslt");

			ini.put("PH-SCHEMATRON", "ROOT-DIRECTORY", "TOOLS\\ph-schematron");
			
			ini.put("SCHEMATRONS", "ENTRY-FILE-NAME", "schematron.sch");
			ini.put("SCHEMATRONS", "COMMON", "COMMON");
			ini.put("SCHEMATRONS", "VALUESETS", "VALUESETS");
			ini.put("SCHEMATRONS", "RULES", "RULES");
			ini.put("SCHEMATRONS", "PATH", "/opt/SchematronValidator_prod/bin/schematron/cda_asip/COURANT");
			
			ini.store();
		} catch (final IOException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
	}

	/**
	 * read
	 * 
	 * @param key
	 * @param section
	 * @return
	 */
	public static String read(final String key, final String section) {
		String value = null;
		try {
			if (FILE.exists()) {
				final Ini ini = new Ini(FILE);
				value = ini.get(section, key, String.class);
			}
		} catch (final IOException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
		return value;
	}

	/**
	 * read
	 * 
	 * @param section
	 * @return
	 */
	public static Section read(final String section) {
		Section value = null;
		try {
			if (FILE.exists()) {
				final Ini ini = new Ini(FILE);
				value = ini.get(section);
			}
		} catch (final IOException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
		return value;
	}

	/**
	 * write
	 * 
	 * @param key
	 * @param value
	 * @param section
	 */
	public static boolean write(final String key, final String value, final String section) {
		boolean bool = true;
		if (FILE.exists()) {
			try {
				final Ini ini = new Ini(FILE);
				final Ini.Section sec = ini.get(section);
				ini.put(section, key, value);
				ini.store();
				if (sec == null) {
					bool = true;
				} else {
					bool = false;
				}
			} catch (final IOException e) {
				if (LOG.isInfoEnabled()) {
					final String error = e.getMessage();
					LOG.error(error);
				}
			}
		}
		return bool;
	}

	/**
	 * readFileContents
	 * 
	 * @param selectedFile
	 * @throws IOException
	 */
	public static String readFileContents(final InputStream file) throws IOException {
		String singleString;
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(file))) {
			final StringBuilder sbuilder = new StringBuilder();
			String line = bReader.readLine();
			while (line != null) {
				sbuilder.append(line).append(Constant.RETOURCHARIOT);
				line = bReader.readLine();
			}
			singleString = sbuilder.toString();
		}
		return singleString;
	}

	/**
	 * readFileContents
	 * 
	 * @param selectedFile
	 * @throws IOException
	 */
	public static int readSectionFile(final String file) throws IOException {
		int count = 0;
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				if (line != null && line.startsWith("[")) {
					count++;
				}
			}
		}
		return count;
	}
}