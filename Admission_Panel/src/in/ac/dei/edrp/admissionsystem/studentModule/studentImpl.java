package in.ac.dei.edrp.admissionsystem.studentModule;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import in.ac.dei.edrp.admissionsystem.Bean.BubbleFormBean;
import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.ReportInfoGetter;
import in.ac.dei.edrp.admissionsystem.Bean.TimeTableBean;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;
import in.ac.dei.edrp.admissionsystem.Bean.cca_intBean;
import in.ac.dei.edrp.admissionsystem.Bean.transferBean;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class studentImpl extends SqlMapClientDaoSupport implements studentDao {

	TransactionTemplate transactionTemplate =null;
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public List<admissionBean> getCustomeGridData(admissionBean input) {
		
		List<admissionBean> getData = new ArrayList<admissionBean>();
		
		if (input.getFlag().equalsIgnoreCase("getLectureData"))
		{
			getData=getSqlMapClientTemplate().queryForList("teaching.getLectureData", input); 
		}
		
		return getData;
	}

	public String generateCourseDetailPdf(admissionBean input, ServletContext sc) {
		
		String unit=input.getUnits();
		String lectureType=input.getLectureType();
		String course_code=input.getCourse_code();
		String serverPath=sc.getRealPath(File.separator)+"LECTURES";
		
		String filePath = serverPath
		+File.separator+
		course_code
		+File.separator+
		unit
		+File.separator+
		lectureType
		+File.separator+course_code+"_"+unit+"_"+lectureType+".zip";
		
//		String filePath = serverPath
//		+File.separator+ "ABC101_unit-1_L1"+".zip";
		
		String directoryPath = serverPath
			+File.separator+
			course_code
			+File.separator+
			unit
			+File.separator+
			lectureType;
		File admitcardref = new File(directoryPath);
		admitcardref.mkdirs();
		
		
		return filePath;
	}
	
	
}
