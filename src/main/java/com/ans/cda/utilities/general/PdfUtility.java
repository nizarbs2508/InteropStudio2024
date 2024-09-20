package com.ans.cda.utilities.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.ans.cda.ihm.WebViewSample;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public final class PdfUtility {

	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(PdfUtility.class);

	/**
	 * PdfUtility constructor
	 */
	private PdfUtility() {
		// empty constructor
	}

	/**
	 * mergePDFs
	 * 
	 * @param pdfFiles
	 * @param outputFilePath
	 */
	public static void mergePDFs(final List<String> pdfFiles, final String outputFilePath) {
		try {
			final com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			final PdfCopy copy = new PdfCopy(document, new FileOutputStream(outputFilePath));
			document.open();
			for (final String filePath : pdfFiles) {
				final PdfReader reader = new PdfReader(filePath);
				for (int i = 1; i <= reader.getNumberOfPages(); i++) {
					copy.addPage(copy.getImportedPage(reader, i));
				}
				reader.close();
			}
			document.close();
		} catch (final IOException | DocumentException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
	}

	/**
	 * xmlToPdf
	 */
	public static String xmlToPdf(final String xmlFilePath) {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		StreamResult result = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			final org.w3c.dom.Document doc = dBuilder.parse(xmlFilePath);
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			result = new StreamResult(new java.io.StringWriter());
			final DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
		} catch (final ParserConfigurationException | SAXException | IOException | TransformerException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
		return result.getWriter().toString();
	}

	/**
	 * exportXMLToPDF
	 * 
	 * @param xmlContent
	 * @param pdfFilePath
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void exportXMLToPDF(final String xmlContent, final String pdfFilePath) {
		try {
			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
			document.open();
			Font font = new Font(Font.FontFamily.COURIER, 10);
			Paragraph paragraph = new Paragraph(xmlContent, font);
			paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
			document.add(paragraph);

			document.close();
		} catch (final IOException | DocumentException e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
	}

	/**
	 * loadResources
	 * 
	 * @param tempDirectory
	 */
	public static File loadResources(final Path tempDirectory, final String xmlInput, final String xsltInput) {
		File file = null;
		try {
			// Step 1: Construct FopFactory and FOUserAgent
			final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
			final FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			final String namePDF = new File(xmlInput).getName();
			// Step 2: Set up output stream
			final FileOutputStream out = new FileOutputStream(
					new File(tempDirectory + "\\" + Utility.removeExtension(namePDF) + ".pdf"));
			file = new File(tempDirectory + "\\" + Utility.removeExtension(namePDF) + ".pdf");
			try {
				// Step 3: Create a Fop instance for PDF output
				final Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
				// Step 4: Set up XSLT transformation
				final TransformerFactory factory = TransformerFactory.newInstance();
				final Transformer transformer = factory.newTransformer(new StreamSource(xsltInput));
				// Step 5: Perform transformation and FOP processing
				final Source src = new StreamSource(xmlInput);
				transformer.transform(src, new javax.xml.transform.sax.SAXResult(fop.getDefaultHandler()));
			} finally {
				// Step 6: Close the output stream
				out.close();
				file.deleteOnExit();
			}
		} catch (final Exception e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
		}
		return file;

	}

	/**
	 * copyResourceFolderToTemp
	 * 
	 * @param resourceFolderPath
	 * @param tempDirectory
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void copyResourceFolderToTemp(final String resourceFolderPath, final Path tempDirectory)
			throws IOException, URISyntaxException {
		try {
			// The resource folder name within the JAR
			String resourceFolder = "fop";
			// Temporary directory
			File tempDir = Files.createTempDirectory("extracted_resources").toFile();
			// Get the JAR file from the classpath
			String jarPath = PdfUtility.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			// Create a ZipFile object for the JAR
			try (ZipFile jarFile = new ZipFile(new File(jarPath))) {
				Enumeration<? extends ZipEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					String entryName = entry.getName();
					// Check if the entry is inside the resource folder
					if (entryName.startsWith(resourceFolder + "/")) {
						File outputFile = new File(tempDir, entryName.substring(resourceFolder.length() + 1));
						if (entry.isDirectory()) {
							// Create directories
							outputFile.mkdirs();
						} else {
							// Create parent directories if needed
							outputFile.getParentFile().mkdirs();
							// Extract file
							try (InputStream is = jarFile.getInputStream(entry);
									OutputStream os = new FileOutputStream(outputFile)) {
								byte[] buffer = new byte[1024];
								int len;
								while ((len = is.read(buffer)) > 0) {
									os.write(buffer, 0, len);
								}
							}
						}
					}
				}
			}

			System.out.println("Resources extracted to: " + tempDir.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * copyFolder
	 * 
	 * @param sourceFolder
	 * @param destinationFolder
	 * @throws IOException
	 */
	public static void copyFolder(final Path sourceFolder, final Path destinationFolder) throws IOException {
		Files.walk(sourceFolder).forEach(source -> {
			try {
				final Path destination = destinationFolder.resolve(sourceFolder.relativize(source).toString());
				if (Files.isDirectory(source)) {
					Files.createDirectories(destination);
				} else {
					Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (final IOException e) {
				if (LOG.isInfoEnabled()) {
					final String error = e.getMessage();
					LOG.error(error);
				}
			}
		});
	}

	/**
	 * Recursively schedules a directory and all its contents for deletion on JVM
	 * exit.
	 *
	 * @param dir The directory to delete.
	 */
	public static void scheduleDirForDeletionOnExit(final File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File child : files) {
					scheduleDirForDeletionOnExit(child);
				}
			}
		}
		dir.deleteOnExit();
	}

	/**
	 * Copies a resource file from inside the JAR to a temporary file.
	 *
	 * @param resourcePath The path to the resource inside the JAR.
	 * @return The temporary file to which the resource was copied.
	 * @throws IOException If an I/O error occurs during copying.
	 */
	public static File copyFromJarFile(final String resourcePath, final Path tempDir) throws IOException {
		// Get the resource as an InputStream from the JAR
		try (InputStream inputStream = WebViewSample.class.getClassLoader().getResourceAsStream(resourcePath)) {
			if (inputStream == null) {
				throw new IllegalArgumentException("Resource not found: " + resourcePath);
			}
			// Create a temporary file in the directory, preserving the resource's name
			final Path tempFile = tempDir.resolve(resourcePath);
			final File tempFileAsFile = tempFile.toFile();
			try (FileOutputStream outputStream = new FileOutputStream(tempFileAsFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}
			return tempFileAsFile;
		}
	}

}
