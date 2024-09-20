package com.ans.cda.service.validation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ans.cda.utilities.general.Constant;
import com.ans.cda.utilities.general.LocalUtility;
import com.ans.cda.utilities.general.Utility;

/**
 * SaxonValidator
 * 
 * @author bensalem Nizar
 */
public final class SaxonValidator {
	/**
	 * CONFIG
	 */
	private static final File CONFIG = new File(Constant.INTEROPFOLDER + "\\config.properties");
	/**
	 * newFilePath
	 */
	private static Path apiFilePath = Paths.get(Constant.FILENAME + "//interopStudio//Validation//API");
	/**
	 * newFilePath
	 */
	private static Path newFilePath;
	/**
	 * newFilePath1
	 */
	private static Path newFilePath1;
	/**
	 * newFilePath2
	 */
	private static Path newFilePath2;
	/**
	 * newFilePath3
	 */
	private static Path newFilePath3;
	/**
	 * NEWFILEPATH4
	 */
	private static Path newFilePath4;
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(SaxonValidator.class);
	/**
	 * Logger
	 */
	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("MyLog");

	/**
	 * SaxonValidator
	 */
	private SaxonValidator() {
		// empty
	}

	/**
	 * displayLastReport
	 * 
	 * @param webEngine
	 */
	public static String displayLastReport() {
		String ret = getNewFilePath4().toString();
		File dest = null;
		final String reportURL = getNewFilePath2().toString();
		try {
			final StreamSource myXPathDoc = new StreamSource(new File(reportURL));
			final String fileF = SaxonValidator.class.getResource("/API/svrl-to-html.xsl").toExternalForm();
			dest = new File(Constant.INTEROPFOLDER + "\\svrl-to-html.xsl");
			if (!dest.exists()) {
				try {
					FileUtils.copyURLToFile(new URL(fileF), dest);
				} catch (final IOException e) {
					if (LOG.isInfoEnabled()) {
						final String error = e.getMessage();
						LOG.error(error);
					}
				}
			}
			final Transformer myXslTrans = TransformerFactory.newInstance().newTransformer(new StreamSource(dest));
			final File outputFile = new File(getNewFilePath4().toString());
			myXslTrans.transform(myXPathDoc, new StreamResult(outputFile));
		} catch (final TransformerException ex) {
			if (LOG.isInfoEnabled()) {
				final String error = ex.getMessage();
				LOG.error(error);
				ret = "error";
			}
		} finally {
			dest.deleteOnExit();
		}
		return ret;
	}

	/**
	 * displayLastReport
	 * 
	 * @param webEngine
	 */
	public static String displayLastReport(final String svrl) {
		String ret = getNewFilePath4().toString();
		File dest = null;
		try {
			final StreamSource myXPathDoc = new StreamSource(new File(svrl));
			final String fileF = SaxonValidator.class.getResource("/API/svrl-to-html.xsl").toExternalForm();
			dest = new File(Constant.INTEROPFOLDER + "\\svrl-to-html.xsl");
			if (!dest.exists()) {
				try {
					FileUtils.copyURLToFile(new URL(fileF), dest);
				} catch (final IOException e) {
					if (LOG.isInfoEnabled()) {
						final String error = e.getMessage();
						LOG.error(error);
					}
				}
			}
			final Transformer myXslTrans = TransformerFactory.newInstance().newTransformer(new StreamSource(dest));
			final File outputFile = new File(getNewFilePath4().toString());
			myXslTrans.transform(myXPathDoc, new StreamResult(outputFile));
		} catch (final TransformerException ex) {
			if (LOG.isInfoEnabled()) {
				final String error = ex.getMessage();
				LOG.error(error);
				ret = "error";
			}
		} finally {
			dest.deleteOnExit();
		}
		return ret;
	}

	/**
	 * valideDocument
	 * 
	 * @param pDocumentContent
	 * @param pValidationServiceName
	 * @param pValidatorName
	 * @return
	 */
	public static String valideDocument(final String pDocumentContent, final String pValidationSName,
			final String pValidatorName, final String validationUrl) {
		FileHandler fhandler = null;
		final SimpleFormatter formatter = new SimpleFormatter();
		getNewFilePath3().toFile().delete();
		getNewFilePath2().toFile().delete();
		getNewFilePath1().toFile().delete();
		getNewFilePath().toFile().delete();
		final String encodedDocument = Utility.encodeBase64(pDocumentContent);
		String locationHeader;
		boolean done = false;
		// Récupérer le nom du validateur CDA
		final String fileF = Utility.getContextClassLoader().getResource("API/templateRequeteValidation.txt")
				.toExternalForm();
		File dest;
		dest = new File(Constant.INTEROPFOLDER + "\\templateRequeteValidation.txt");
		if (!dest.exists()) {
			try {
				FileUtils.copyURLToFile(new URL(fileF), dest);
			} catch (final IOException e) {
				if (LOG.isInfoEnabled()) {
					final String error = e.getMessage();
					LOG.error(error);
				}
			}
		}
		// Set cursor as hourglass
		String sRequete = null;
		try {
			fhandler = new FileHandler(Constant.LOGFOLDFER + "\\log.log", true);
			LOGGER.addHandler(fhandler);
			fhandler.setFormatter(formatter);
			if (dest.exists()) {
				sRequete = readFile(dest.getAbsolutePath());
				sRequete = sRequete.replace("$CONTENT$", encodedDocument);
				sRequete = sRequete.replace("$VALIDATION-SERVICE-NAME$", pValidationSName);
				sRequete = sRequete.replace("$VALIDATOR$", pValidatorName);
				if (!apiFilePath.toFile().exists()) {
					apiFilePath.toFile().mkdirs();
				}
				if (!getNewFilePath3().toFile().exists()) {
					Files.createFile(getNewFilePath3());
				}
				if (!getNewFilePath().toFile().exists()) {
					Files.createFile(getNewFilePath());
				}
				if (!getNewFilePath1().toFile().exists()) {
					Files.createFile(getNewFilePath1());
				}
				if (!getNewFilePath2().toFile().exists()) {
					Files.createFile(getNewFilePath2());
				}
				try {
					Files.writeString(getNewFilePath3().toFile().toPath(), sRequete, StandardCharsets.UTF_8);
				} catch (final IOException e) {
					if (LOG.isInfoEnabled()) {
						final String error = e.getMessage();
						LOG.error(error);
					}
				}
			}
		} catch (final IOException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
				LOGGER.log(Level.SEVERE, error, fhandler);
			}
		}
		try {
			fhandler = new FileHandler(Constant.LOGFOLDFER + "\\log.log", true);
			LOGGER.addHandler(fhandler);
			fhandler.setFormatter(formatter);
			final URL url = new URL(validationUrl);
			final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty("Content-Type", "application/xml");
			httpCon.setConnectTimeout(60_000);
			final String postData = sRequete;
			try (OutputStream ostream = Utility.getOutputStream(httpCon)) {
				final byte[] input = postData.getBytes("utf-8");
				ostream.write(input, 0, input.length);
			}
			final String message = httpCon.getResponseMessage();
			if (!message.contains("Time-out")) {
				final int responseCode = httpCon.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED
						|| responseCode == HttpURLConnection.HTTP_CREATED) {
					locationHeader = httpCon.getHeaderField("Location");
					writeFile(getNewFilePath().toFile().getAbsolutePath(), locationHeader);
					final URL url1 = new URL(locationHeader);
					final HttpURLConnection httpConnnection = (HttpURLConnection) url1.openConnection();
					httpConnnection.setRequestMethod("GET");
					httpConnnection.setRequestProperty("Accept", "application/xml");
					httpConnnection.setConnectTimeout(60_000);
					final int responseCode1 = httpConnnection.getResponseCode();
					if (responseCode1 == HttpURLConnection.HTTP_OK || responseCode1 == HttpURLConnection.HTTP_ACCEPTED
							|| responseCode1 == HttpURLConnection.HTTP_CREATED) {
						try (InputStream inputStream = httpConnnection.getInputStream();
								InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
							final BufferedReader reader = new BufferedReader(inputStreamReader);
							final StringBuffer response = new StringBuffer();
							String line;
							while ((line = reader.readLine()) != null) {
								response.append(line);
							}
							reader.close();
							Files.writeString(getNewFilePath1().toFile().toPath(), response.toString(),
									StandardCharsets.UTF_8);
							done = true;
						}
					} else {
						Files.writeString(getNewFilePath1().toFile().toPath(),
								LocalUtility.getString("message.error.timeout"), StandardCharsets.UTF_8);
					}
					final URL url2 = new URL(locationHeader + "/report");
					final HttpURLConnection httpConnnection2 = (HttpURLConnection) url2.openConnection();
					httpConnnection2.setRequestMethod("GET");
					httpConnnection2.setRequestProperty("Accept", "application/xml");
					httpConnnection2.setConnectTimeout(60_000);
					final int responseCode2 = httpConnnection2.getResponseCode();
					if (responseCode2 == HttpURLConnection.HTTP_OK || responseCode2 == HttpURLConnection.HTTP_ACCEPTED
							|| responseCode2 == HttpURLConnection.HTTP_CREATED) {
						try (InputStream inputStream = httpConnnection2.getInputStream();
								InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
							final StringBuilder response = new StringBuilder();
							int read;
							final char[] buffer = new char[200_000];
							while ((read = inputStreamReader.read(buffer)) != -1) {
								response.append(buffer, 0, read);
							}
							Files.writeString(getNewFilePath2().toFile().toPath(), response.toString(),
									StandardCharsets.UTF_8);
							done = true;
						}
					} else {
						Files.writeString(getNewFilePath2().toFile().toPath(),
								LocalUtility.getString("message.error.timeout"), StandardCharsets.UTF_8);
					}
				} else {
					writeFile(getNewFilePath().toFile().getAbsolutePath(),
							LocalUtility.getString("message.error.server"));
				}
			} else {
				Files.writeString(getNewFilePath2().toFile().toPath(), LocalUtility.getString("message.error.timeout"),
						StandardCharsets.UTF_8);
			}

		} catch (final Exception e) {
			writeFile(getNewFilePath().toFile().getAbsolutePath(), LocalUtility.getString("message.error.server"));
			LOGGER.log(Level.SEVERE, e.getMessage(), fhandler);
		} finally {
			dest.delete();
			fhandler.close();
		}
		return Utility.getXpathSingleValue(getNewFilePath1().toFile(), done);
	}

	/**
	 * readFile
	 * 
	 * @param filePath
	 * @return
	 */
	private static String readFile(final String filePath) {
		final Path path = Path.of(filePath);
		String content = null;
		try {
			content = Files.readString(path);
		} catch (final IOException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
		return content;
	}

	/**
	 * writeFile
	 * 
	 * @param filePath
	 * @param content
	 */
	private static void writeFile(final String filePath, final String content) {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
			writer.write(content);
			writer.close();
		} catch (final IOException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
	}

	/**
	 * getNewFilePath
	 * 
	 * @return
	 */
	public static Path getNewFilePath() {
		if (CONFIG.exists()) {
			try (InputStream input = new FileInputStream(CONFIG)) {
				final Properties prop = new Properties();
				prop.load(input);
				final String lastUrl = prop.getProperty("url.last.result");
				if (lastUrl != null && !lastUrl.isEmpty()) {
					newFilePath = new File(lastUrl).toPath();
				} else {
					newFilePath = Paths.get(apiFilePath + "//document_validation_last_result_URL.txt");
				}
			} catch (final IOException ex) {
				if (LOG.isInfoEnabled()) {
					final String error = ex.getMessage();
					LOG.error(error);
				}
			}
		} else {
			newFilePath = Paths.get(apiFilePath + "//document_validation_last_result_URL.txt");
		}
		return newFilePath;
	}

	/**
	 * setNewFilePath
	 * 
	 * @param newFilePath
	 */
	public static void setNewFilePath(final Path newFilePath) {
		SaxonValidator.newFilePath = newFilePath;
	}

	/**
	 * getNewFilePath1
	 * 
	 * @return
	 */
	public static Path getNewFilePath1() {
		if (CONFIG.exists()) {
			try (InputStream input = new FileInputStream(CONFIG)) {
				final Properties prop = new Properties();
				prop.load(input);
				final String lastResult = prop.getProperty("last.result");
				if (lastResult != null && !lastResult.isEmpty()) {
					newFilePath1 = new File(lastResult).toPath();
				} else {
					newFilePath1 = Paths.get(apiFilePath + "//document_validation_last_result.xml");
				}
			} catch (final IOException ex) {
				if (LOG.isInfoEnabled()) {
					final String error = ex.getMessage();
					LOG.error(error);
				}
			}
		} else {
			newFilePath1 = Paths.get(apiFilePath + "//document_validation_last_result.xml");
		}
		return newFilePath1;
	}

	/**
	 * setNewFilePath1
	 * 
	 * @param newFilePath1
	 */
	public static void setNewFilePath1(final Path newFilePath1) {
		SaxonValidator.newFilePath1 = newFilePath1;
	}

	/**
	 * getNewFilePath2
	 * 
	 * @return
	 */
	public static Path getNewFilePath2() {
		if (CONFIG.exists()) {
			try (InputStream input = new FileInputStream(CONFIG)) {
				final Properties prop = new Properties();
				prop.load(input);
				final String lastReport = prop.getProperty("last.report");
				if (lastReport != null && !lastReport.isEmpty()) {
					newFilePath2 = new File(lastReport).toPath();
				} else {
					newFilePath2 = Paths.get(apiFilePath + "//document_validation_last_report.xml");
				}
			} catch (final IOException ex) {
				if (LOG.isInfoEnabled()) {
					final String error = ex.getMessage();
					LOG.error(error);
				}
			}
		} else {
			newFilePath2 = Paths.get(apiFilePath + "//document_validation_last_report.xml");
		}
		return newFilePath2;
	}

	/**
	 * setNewFilePath2
	 * 
	 * @param newFilePath2
	 */
	public static void setNewFilePath2(final Path newFilePath2) {
		SaxonValidator.newFilePath2 = newFilePath2;
	}

	/**
	 * getNewFilePath3
	 * 
	 * @return
	 */
	public static Path getNewFilePath3() {
		if (CONFIG.exists()) {
			try (InputStream input = new FileInputStream(CONFIG)) {
				final Properties prop = new Properties();
				prop.load(input);
				final String lastRequest = prop.getProperty("last.request");
				if (lastRequest != null && !lastRequest.isEmpty()) {
					newFilePath3 = new File(lastRequest).toPath();
				} else {
					newFilePath3 = Paths.get(apiFilePath + "//document_validation_last_request.xml");
				}

			} catch (final IOException ex) {
				if (LOG.isInfoEnabled()) {
					final String error = ex.getMessage();
					LOG.error(error);
				}
			}
		} else {
			newFilePath3 = Paths.get(apiFilePath + "//document_validation_last_request.xml");
		}
		return newFilePath3;
	}

	/**
	 * setNewFilePath3
	 * 
	 * @param newFilePath3
	 */
	public static void setNewFilePath3(final Path newFilePath3) {
		SaxonValidator.newFilePath3 = newFilePath3;
	}

	/**
	 * getNewFilePath4
	 * 
	 * @return
	 */
	public static Path getNewFilePath4() {
		if (CONFIG.exists()) {
			try (InputStream input = new FileInputStream(CONFIG)) {
				final Properties prop = new Properties();
				prop.load(input);
				final String lastReportHtml = prop.getProperty("last.report.html");
				if (lastReportHtml != null && !lastReportHtml.isEmpty()) {
					newFilePath4 = new File(lastReportHtml).toPath();
				} else {
					newFilePath4 = Paths.get(apiFilePath + "//document_last_report_HTML.html");
				}

			} catch (final IOException ex) {
				if (LOG.isInfoEnabled()) {
					final String error = ex.getMessage();
					LOG.error(error);
				}
			}
		} else {
			newFilePath4 = Paths.get(apiFilePath + "//document_last_report_HTML.html");
		}
		return newFilePath4;
	}

	/**
	 * setNewFilePath4
	 * 
	 * @param newFilePath4
	 */
	public static void setNewFilePath4(final Path newFilePath4) {
		SaxonValidator.newFilePath4 = newFilePath4;
	}
}