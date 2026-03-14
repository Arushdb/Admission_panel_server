package in.ac.dei.edrp.admissionsystem.GenerateAdmitCard;

import java.io.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import java.util.Map;
import java.util.HashMap;

public class generateAdmitCardController extends MultiActionController {

    private GenerateAdmitCardDao generateAdmitCardDao;
    private final Gson gson = new Gson();

    public void setGenerateAdmitCardDao(GenerateAdmitCardDao generateAdmitCardDao) {
        this.generateAdmitCardDao = generateAdmitCardDao;
    }

    /**
     * Fetch all programs (for dropdown in Angular)
     */
    public ModelAndView getProgramList(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<GenerateAdmitCardBean> programs = generateAdmitCardDao.getPrograms();
            String json = gson.toJson(programs);

            response.setContentType("application/json");
            response.getWriter().write("{\"programs\":" + json + "}");
            response.getWriter().flush();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Error fetching programs: " + e.getMessage() + "\"}");
                response.getWriter().flush();
            } catch (Exception ignored) {}
            return null;
        }
    }
    
    

    /**
     * Generate Admit Cards for given program_id
     */
    public ModelAndView generateAdmitCard(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Read JSON payload
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
            String programId = json.get("programId").getAsString();

            // Fetch program info
            List<GenerateAdmitCardBean> programs = generateAdmitCardDao.getPrograms();
            GenerateAdmitCardBean program = programs.stream()
                    .filter(p -> p.getProgramId().equals(programId))
                    .findFirst()
                    .orElse(null);

            if (program == null) {
                writeJsonResponse(response, "Program not found");
                return null;
            }

            
            // Fetch applicants
            List<GenerateAdmitCardBean> applicants;
            if ("Y".equalsIgnoreCase(program.getEntranceTestExist())) {
                applicants = generateAdmitCardDao.getApplicantsEntrance(programId);
            } else {
                applicants = generateAdmitCardDao.getApplicantsDirect(programId);
            }

            if (applicants == null || applicants.isEmpty()) {
                writeJsonResponse(response, "No applicants found");
                return null;
            }
            
            GenerateAdmitCardBean sessionBean = generateAdmitCardDao.getSessionYear();
            String sessionYear = (sessionBean != null) ? sessionBean.getSessionYear() : "";
            // Generate PDF for each applicant
            for (GenerateAdmitCardBean applicant : applicants) {
            	applicant.setSessionYear(sessionYear);
            	generateAdmitCardPdf(request, applicant, programId, program.getEntranceTestExist());
                generateAdmitCardDao.updateAdmitCardPath(applicant.getRegistrationNumber(), applicant.getAdmitCardPath());
                generateAdmitCardDao.setAdmitCardAvailable(applicant.getRegistrationNumber());
            }

            generateAdmitCardDao.setAdmitCardPublished(programId);
            writeJsonResponse(response, "Admit Cards generated successfully");
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                writeJsonResponse(response, "Error generating admit cards: " + e.getMessage());
            } catch (Exception ignored) {}
            return null;
        }
    }

    /**
     * Generate a single admit card PDF with logo, photo/signature placeholders,
     * details table, QR, instructions and disclaimer.
     */
    private void generateAdmitCardPdf
    (HttpServletRequest request, GenerateAdmitCardBean applicant,
            String programId, String entranceTestExist) throws Exception {
        System.out.println("=== Generating Admit Card ===");
        System.out.println("Program ID       : " + programId);
        System.out.println("Application No.  : " + applicant.getApplicationNumber());
        System.out.println("Registration No. : " + applicant.getRegistrationNumber());
        System.out.println("Applicant Name   : " + applicant.getName());
        System.out.println("Doc Path         : " + applicant.getDocPath());
        System.out.println("Venue            : " + applicant.getVenue());
        System.out.println("Instructions     : " + applicant.getInstructions());
        System.out.println("DEBUG >> testdate = " + applicant.getTestdate());
        System.out.println("DEBUG >> testtime = " + applicant.getTesttime());
        System.out.println("DEBUG >> subjects = " + applicant.getSubjects());
        System.out.println("DEBUG >> entranceTestExist = " + applicant.getEntranceTestExist());
        System.out.println("Session Year     : " + applicant.getSessionYear());
       // System.out.println("Ret Session Year     : " + applicant.getRetMonthYear());
        System.out.println("Tencodes: " + applicant.getTencodes());
        System.out.println("=============================");

      //  String basePath = System.getProperty("user.home") + File.separator + "AdmitCards" + File.separator + programId;
      //  File dir = new File(basePath);
     //   if (!dir.exists()) dir.mkdirs();

     //   String pdfPath = basePath + File.separator + applicant.getApplicationNumber() + "_AdmitCard.pdf";
     //   applicant.setAdmitCardPath(pdfPath);
        
        // Detect OS
        String os = System.getProperty("os.name").toLowerCase();
        String baseRoot = os.contains("win") ? "C:\\Users\\Admin\\AdmitCards" : "/home/cmsadmin/AdmitCards";

        // Path till program_id
        String basePath = baseRoot + File.separator + programId;

        // Create folder if not exists
        File dir = new File(basePath);
        if (!dir.exists()) dir.mkdirs();

        // File name for PDF
        String pdfPath = basePath + File.separator + applicant.getApplicationNumber() + "_AdmitCard.pdf";

        // Save only folder path (till program_id) in applicant object
        applicant.setAdmitCardPath(basePath);
        
        

        generateAdmitCardDao.updateAdmitCardPath(applicant.getRegistrationNumber(), basePath);

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        document.open();

        // --- HEADER (Logo + Heading) ---
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{15, 60});

      //  try {
       //     String logoPath = request.getServletContext().getRealPath("/images/dei_logo.png");
       //     Image logo = Image.getInstance(logoPath);
        //    logo.scaleAbsolute(60, 60);
        //    PdfPCell logoCell = new PdfPCell(logo);
        //    logoCell.setBorder(Rectangle.NO_BORDER);
        //    logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        //    headerTable.addCell(logoCell);
      //  } catch (Exception e) {
       //     PdfPCell emptyLogo = new PdfPCell(new Phrase(""));
       //     emptyLogo.setBorder(Rectangle.NO_BORDER);
        //    headerTable.addCell(emptyLogo);
        //    System.err.println("Logo not found: " + e.getMessage());
      //  }
        
     // ---------- Logo ----------
        try {
            String logoBasePath = request.getServletContext().getRealPath("/images");
            File logoFile = new File(logoBasePath, "dei_logo.png");  // ✅ cross-platform
            if (logoFile.exists()) {
                Image logo = Image.getInstance(logoFile.getAbsolutePath());
                logo.scaleAbsolute(60, 60);
                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(logoCell);
            } else {
                PdfPCell emptyLogo = new PdfPCell(new Phrase(""));
                emptyLogo.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(emptyLogo);
                System.err.println("Logo not found: " + logoFile.getAbsolutePath());
            }
        } catch (Exception e) {
            PdfPCell emptyLogo = new PdfPCell(new Phrase(""));
            emptyLogo.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyLogo);
            System.err.println("Logo load failed: " + e.getMessage());
        }

       // Paragraph headerText = new Paragraph(
       //         "DAYALBAGH EDUCATIONAL INSTITUTE, DAYALBAGH, AGRA \n\n" +
       //         "                            ADMIT CARD : " + (applicant.getSessionYear() != null ? applicant.getSessionYear() : ""),
       //         FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)
      //  );
      //  headerText.setAlignment(Element.ALIGN_CENTER);
      //  headerText.setSpacingAfter(10f); // extra spacing under title
      //  PdfPCell headerCell = new PdfPCell(headerText);
      //  headerCell.setBorder(Rectangle.NO_BORDER);
      //  headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      //  headerTable.addCell(headerCell);
        
        String headerSession;
        if ("PH".equalsIgnoreCase(applicant.getTencodes())) {
            GenerateAdmitCardBean retBean = generateAdmitCardDao.getRetMonthYear();
            System.out.println("RET Month-Year from DB: " + (retBean != null ? retBean.getRetMonthYear() : "NULL"));
            headerSession = (retBean != null && retBean.getRetMonthYear() != null) 
                              ? retBean.getRetMonthYear() : "";
        } else {
            GenerateAdmitCardBean sessionBean = generateAdmitCardDao.getSessionYear();
            headerSession = (sessionBean != null && sessionBean.getSessionYear() != null) 
                              ? sessionBean.getSessionYear() : "";
        }

        Paragraph headerText = new Paragraph(
            "DAYALBAGH EDUCATIONAL INSTITUTE, DAYALBAGH, AGRA \n\n" +
            "                            ADMIT CARD : " + headerSession,
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)
        );
        headerText.setAlignment(Element.ALIGN_CENTER);
        headerText.setSpacingAfter(10f);

        PdfPCell headerCell = new PdfPCell(headerText);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable.addCell(headerCell);


        document.add(headerTable);
      //  document.add(Chunk.NEWLINE);
      //  document.add(Chunk.NEWLINE);
      //  document.add(Chunk.NEWLINE);
        headerText.setSpacingAfter(8f);

        // --- APPLICANT INFO (Left: details, Right: photo/signature) ---
        PdfPTable infoTable = new PdfPTable(2);
       // infoTable.setWidthPercentage(100);
        infoTable.setWidthPercentage(80);
        infoTable.setWidths(new float[]{70, 30});

        // Left side: Program, Name, Father, Address
     //   StringBuilder addr = new StringBuilder();
     //   if (applicant.getAddress() != null) addr.append(applicant.getAddress());
    //    PdfPCell leftCell = new PdfPCell(new Phrase(
     //           "Program: " + safe(applicant.getProgramName()) + "\n\n" +
      //          "Name: " + safe(applicant.getName()) + "\n\n" +
      //          "Father Name: " + safe(applicant.getFatherName()) + "\n\n" +
      //          "Address: " + safe(addr.toString()),
       //         FontFactory.getFont(FontFactory.HELVETICA, 9)
     //   ));
     //   leftCell.setBorder(Rectangle.NO_BORDER);
     //   leftCell.setPadding(5f);
     //   infoTable.addCell(leftCell);
        
        
     // Left side: Program, Name, Father, Address
        StringBuilder addr = new StringBuilder();
        if (applicant.getAddress() != null) addr.append(applicant.getAddress());

        // Define fonts
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);

        // Build paragraph with mixed formatting
        Paragraph leftContent = new Paragraph();
        leftContent.setLeading(20f); // line spacing

        leftContent.add(new Chunk("Program: ", boldFont));
        leftContent.add(new Chunk(safe(applicant.getProgramName()) + "\n\n", normalFont));

        leftContent.add(new Chunk("Name: ", boldFont));
        leftContent.add(new Chunk(safe(applicant.getName()) + "\n\n", normalFont));

        leftContent.add(new Chunk("Father Name: ", boldFont));
        leftContent.add(new Chunk(safe(applicant.getFatherName()) + "\n\n", normalFont));

        leftContent.add(new Chunk("Address: ", boldFont));
        leftContent.add(new Chunk(safe(addr.toString()), normalFont));

        // Wrap in cell
        PdfPCell leftCell = new PdfPCell(leftContent);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(5f);

        infoTable.addCell(leftCell);

       
        
     // ---------- Right side: Photo + Signature stacked ----------
        PdfPTable photoSignTable = new PdfPTable(1);
        photoSignTable.setWidthPercentage(100);

        // --- Photo ---
        try {
            if (applicant.getDocPath() != null) {
                File photoFile = new File(applicant.getDocPath(), "photo.jpg");  // ✅ cross-platform
                System.out.println("Looking for photo: " + photoFile.getAbsolutePath());
                if (photoFile.exists()) {
                	System.out.println("[DEBUG] Applicant Photo Path: " + photoFile.getAbsolutePath());
                    Image photo = Image.getInstance(photoFile.getAbsolutePath());
                    //photo.scaleAbsolute(100, 125);
                  //  photo.scaleAbsolute(90, 125);
                 // Scale photo smaller (width × height)
                  //  photo.scaleToFit(70f, 90f);
                    photo.scaleToFit(70f, 80f);
                    
                    PdfPCell photoCell = new PdfPCell(photo, true);
                    photoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                   
                    photoCell.setBorder(Rectangle.NO_BORDER); // ✅ no border
                    photoCell.setFixedHeight(80f);
                    photoCell.setPaddingBottom(8f); 
                    photoSignTable.addCell(photoCell);
                } else {
                    PdfPCell emptyPhoto = new PdfPCell(new Phrase("Photo Not Available",
                            FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                    emptyPhoto.setHorizontalAlignment(Element.ALIGN_CENTER);
                    emptyPhoto.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    emptyPhoto.setBorder(Rectangle.BOX);
                   // emptyPhoto.setBorder(Rectangle.NO_BORDER); // ✅ no border
                    emptyPhoto.setFixedHeight(80f);
                    emptyPhoto.setPaddingBottom(8f); 
                    photoSignTable.addCell(emptyPhoto);
                    System.err.println("Photo not found: " + photoFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Photo load failed: " + e.getMessage());
        }

        // --- Signature ---
        try {
            if (applicant.getDocPath() != null) {
                File signFile = new File(applicant.getDocPath(), "signature.jpg");  // ✅ cross-platform
                if (signFile.exists()) {
                    Image signature = Image.getInstance(signFile.getAbsolutePath());
                
                    signature.scaleToFit(70f, 35f);   // ✅ same width, smaller height
                    PdfPCell signCell = new PdfPCell(signature, true);
                    signCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                   // signCell.setBorder(Rectangle.BOX);
                    signCell.setBorder(Rectangle.NO_BORDER); // ✅ no border
                    signCell.setFixedHeight(35f);
                    photoSignTable.addCell(signCell);
                } else {
                    PdfPCell emptySign = new PdfPCell(new Phrase("Signature Not Available",
                            FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                    emptySign.setHorizontalAlignment(Element.ALIGN_CENTER);
                    emptySign.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    emptySign.setBorder(Rectangle.BOX);
                   // emptySign.setBorder(Rectangle.NO_BORDER);
                    emptySign.setFixedHeight(35f);
                    photoSignTable.addCell(emptySign);
                    System.err.println("Signature not found: " + signFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Signature load failed: " + e.getMessage());
        }

        PdfPCell rightCell = new PdfPCell(photoSignTable);
        rightCell.setBorder(Rectangle.NO_BORDER);
        infoTable.addCell(rightCell);
        infoTable.setSpacingBefore(10f);
        infoTable.setSpacingAfter(6f);
        document.add(infoTable);
       // document.add(Chunk.NEWLINE);
        headerText.setSpacingAfter(8f);
       
        
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        // --- DETAILS TABLE ---
        PdfPTable details = new PdfPTable(2);
       // details.setWidthPercentage(100);
        
        details.setWidthPercentage(80);
        details.setWidths(new float[]{20, 80});

        addCell(details, "Application No.", safe(applicant.getApplicationNumber()), smallFont);
        addCell(details, "Roll No.", safe(applicant.getRollNumber()), smallFont);
        addCell(details, "Venue", safe(applicant.getVenue()), smallFont);

        if ("Y".equalsIgnoreCase(entranceTestExist)) {
            addCell(details, "Written Test on", safe(applicant.getTestdate()), smallFont);
            addCell(details, "Written Test Reporting Time", safe(applicant.getTesttime()), smallFont);
            if (applicant.getSubjects() != null && !applicant.getSubjects().trim().isEmpty()) {
                addCell(details, "Entrance Test Options", safe(applicant.getSubjects()), smallFont);
            }
        }

        // Check program tencodes for Interview On/Time
        if ("PF".equalsIgnoreCase(applicant.getTencodes())) {
            addCell(details, "Personal Interview On", safe(applicant.getInterviewdate()), smallFont);
            addCell(details, "Interview Time", safe(applicant.getInterviewtime()), smallFont);
        } else {
            String notified = "AS NOTIFIED ON THE DEI WEBSITE- www.dei.ac.in";
            addCell(details, "Personal Interview On", notified, smallFont);
            addCell(details, "Interview Time", notified, smallFont);
        }

        details.setSpacingAfter(6f);
        document.add(details);
       // document.add(Chunk.NEWLINE);
        

       
        
     // For Notice
        Paragraph notice = new Paragraph();
        notice.add(new Chunk("Notice:\n ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10))); // Bold
        notice.add(new Chunk(
            "FOR ALL ADMISSION RELATED QUERIES/ANNOUNCEMENTS, KINDLY SEE DEI WEBSITE (https://www.dei.ac.in/dei/admission) REGULARLY.",
            FontFactory.getFont(FontFactory.HELVETICA, 9) // Normal
        ));
        notice.setSpacingBefore(6f);
        notice.setSpacingAfter(6f);
        document.add(notice);

        // --- QR CODE ---
      //  try {
      //      String qrData = safe(applicant.getApplicationNumber()) + "|" + safe(applicant.getName()) + "|" + safe(applicant.getDob());
      //      Image qrImage = generateQRCode(qrData);
      //      qrImage.scaleAbsolute(60, 60);
      //      qrImage.setAlignment(Image.ALIGN_LEFT);
      //      document.add(new Paragraph("QR Code:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
      //      document.add(qrImage);
      //  } catch (Exception e) {
      //      System.err.println("QR generation failed: " + e.getMessage());
     //   }
        
     // --- QR CODE + OTHER PROGRAMS ---
        PdfPTable qrTable = new PdfPTable(2);
        qrTable.setWidthPercentage(60);
        qrTable.setWidths(new float[]{10, 30});

        // Left: QR Code
        PdfPCell qrCell = new PdfPCell();
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCell.setVerticalAlignment(Element.ALIGN_TOP);

        Paragraph qrLabel = new Paragraph("QR Code:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9));
        qrLabel.setSpacingAfter(3f);
        qrCell.addElement(qrLabel);

        try {
           // String qrData = safe(applicant.getApplicationNumber()) + "|" + safe(applicant.getName()) + "|" + safe(applicant.getDob());
        	String qrData = safe(applicant.getApplicationNumber()) + "|" +
                    safe(applicant.getName()) + "|" +
                    safe(applicant.getDob()) + "|" +
                    safe(applicant.getProgramId()) + "|" +
                    safe(applicant.getTestdate()) + "|" +
                    safe(applicant.getTesttime());
        	Image qrImage = generateQRCode(qrData);
            qrImage.scaleAbsolute(30, 30);
            qrImage.setAlignment(Image.ALIGN_LEFT);
            qrCell.addElement(qrImage);
        } catch (Exception e) {
            qrCell.addElement(new Phrase("QR generation failed", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
        }
        qrTable.addCell(qrCell);

        // Right: Other Programs
        PdfPCell programsCell = new PdfPCell();
        programsCell.setBorder(Rectangle.NO_BORDER);

        Paragraph progLabel = new Paragraph("Other Applied Programs:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
      //  progLabel.setSpacingAfter(5f);
        programsCell.addElement(progLabel);

        // Fetch other programs from DB
        String applicationNumber = applicant.getApplicationNumber();
        String selectedProgramId = applicant.getProgramId(); 
        List<GenerateAdmitCardBean> otherPrograms = generateAdmitCardDao.getOtherPrograms(applicant.getApplicationNumber(), programId);

     //   if (otherPrograms != null && !otherPrograms.isEmpty()) {
       //     PdfPTable progTable = new PdfPTable(1);
       //     progTable.setWidthPercentage(100);

          //  for (GenerateAdmitCardBean prog : otherPrograms) {
             
           // 	String progInfo = " App No: " + safe(prog.getApplicationNumber()) + "-"+safe(prog.getProgramName())  ;
                
           //     PdfPCell pCell = new PdfPCell(new Phrase(progInfo, 
          //              FontFactory.getFont(FontFactory.HELVETICA, 9)));
         //       pCell.setBorder(Rectangle.NO_BORDER);
         //       progTable.addCell(pCell);
        //    }
       //     programsCell.addElement(progTable);
      //  } else {
       //     programsCell.addElement(new Phrase("None", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC)));
      //  }

        if (otherPrograms != null && !otherPrograms.isEmpty()) {
            PdfPTable progTable = new PdfPTable(1);
            progTable.setWidthPercentage(100);

            int slNo = 1; // serial number counter

            for (GenerateAdmitCardBean prog : otherPrograms) {
                // Sl. No. + Application Number + Program Name
                String progInfo = slNo + ". " + safe(prog.getApplicationNumber()) + " - " + safe(prog.getProgramName());

                PdfPCell pCell = new PdfPCell(new Phrase(progInfo,
                        FontFactory.getFont(FontFactory.HELVETICA, 9)));
                pCell.setBorder(Rectangle.NO_BORDER);
                progTable.addCell(pCell);

                slNo++; // increment counter
            }
            programsCell.addElement(progTable);
        } else {
            programsCell.addElement(new Phrase("None",
                    FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC)));
        }

        
        qrTable.addCell(programsCell);

        // Add the whole table to document
        qrTable.setSpacingBefore(4f);
       // qrTable.setSpacingAfter(6f);
        document.add(qrTable);


        // --- INSTRUCTIONS ---
      //  if (applicant.getInstructions() != null) {
      //      Paragraph inst = new Paragraph("\nInstructions:\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
      //      for (String ins : applicant.getInstructions().split(":")) {
      //          inst.add(new Phrase(ins + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
      //      }
       //     document.add(inst);
      //  }

        // --- DISCLAIMER ---
        
        Paragraph disclaimer = new Paragraph();
        disclaimer.add(new Chunk("Disclaimer: \n ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10))); // Bold
        disclaimer.add(new Chunk(
        "1. The admit card is not a proof of eligibility for admission. Please refer to the prospectus of the Institute to ensure \n"
                 +"that the conditions for eligibility are satisfied. \n"
        		+"2. It is the responsibility of the candidate to ensure that they meet the minimum criteria for their respective category \n"
        		+"before appearing for written test and/or interview. \n"
        		+"3. If it is detected at any stage that the candidate does not meet the eligibility for the category and program they have \n"
        		+"applied for, their application is liable to be rejected. \n"
        		+"4. Candidates who do not belong to the General Category will be required to produce documents in original to prove \n"
        		+"their eligibility for the category they have declared at the time of the interview, if they are shortlisted for interview. \n"
        		+"5. Candidates must reach the venue on/before reporting time. No candidate, in any case will be allowed to enter the \n"
        		+"examination center after the distribution of the question paper. \n"
        		+"6. Electronic gadgets/devices or any calculating devices are not allowed in the examination hall. \n"
        		+"7. It is mandatory for all candidates to strictly follow the COVID-19 guidelines. They are also required to maintain \n"
        		+"social distancing, wearing of masks and Helmet/Cap. \n"
        		+"8. Candidates must carry and produce their Admit Card and Photo ID Proof (Aadhar/PAN/Driving License etc.) during \n"
        		+"entrance test and at the exam Venue/Centre.",
                FontFactory.getFont(FontFactory.HELVETICA, 8)
                ));
      //  disclaimer.setSpacingBefore(6f);
        document.add(disclaimer);

        document.close();
    }


    
  
    
    private void addCell(PdfPTable table, String label, String value, Font valueFont) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);

        // Label cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
       
        labelCell.setPadding(3f);
        table.addCell(labelCell);

        // Value cell
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
       
        valueCell.setPadding(3f);
        table.addCell(valueCell);
    }



    
    private String safe(String s) {
        return (s != null) ? s : "";
    }
    
    private PdfPCell createImageCell(String imagePath, float boxWidth, float boxHeight) {
        try {
            Image img = Image.getInstance(imagePath);
            
            // Scale image proportionally to fit inside fixed box
            img.scaleToFit(boxWidth, boxHeight);
            img.setAlignment(Element.ALIGN_CENTER);

            // Create fixed-size cell
            PdfPCell cell = new PdfPCell();
            cell.setFixedHeight(boxHeight);   // fixed height
            cell.setMinimumHeight(boxHeight); // prevent auto-shrink
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // Add scaled image
            cell.addElement(img);

            // Border for debugging (remove if not needed)
            cell.setBorder(Rectangle.BOX);

            return cell;

        } catch (Exception e) {
            e.printStackTrace();
            return new PdfPCell(new Phrase("No Image"));
        }
    }

    
    


private Image generateQRCode(String text) throws Exception {
    int size = 120;
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);

    int width = bitMatrix.getWidth();
    int height = bitMatrix.getHeight();
    java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            image.setRGB(x, y, bitMatrix.get(x, y) ? java.awt.Color.BLACK.getRGB() : java.awt.Color.WHITE.getRGB());
        }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    javax.imageio.ImageIO.write(image, "png", baos);
    byte[] pngData = baos.toByteArray();

    Image qrImage = Image.getInstance(pngData);
    qrImage.scaleAbsolute(100, 100);
    return qrImage;
}



/** Helper for JSON responses */
private void writeJsonResponse(HttpServletResponse response, String message) throws Exception {
    response.setContentType("application/json");
    response.getWriter().write("{\"message\":\"" + message + "\"}");
    response.getWriter().flush();
}
}
