package in.ac.dei.edrp.admissionsystem.admitcard;

import in.ac.dei.edrp.admissionsystem.Bean.GenerateAdmitCardBean;
import in.ac.dei.edrp.admissionsystem.Bean.admissionBean;

import java.util.List;

import javax.servlet.ServletContext;



public interface admitcardDao {

	public String generateAdmitCard(GenerateAdmitCardBean input,ServletContext sc);
}
