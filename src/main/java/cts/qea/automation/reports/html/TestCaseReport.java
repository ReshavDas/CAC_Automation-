package cts.qea.automation.reports.html;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import cts.qea.automation.reports.StepElement;
import cts.qea.automation.reports.TestCaseElement;

@XmlRootElement(name="report")
public class TestCaseReport {
	@XmlAttribute public String doctype="testcase";
	@XmlElement TestCaseElement testcase;
	@XmlElement List<StepElement> step = new ArrayList<StepElement>();
	
	public void addTc(TestCaseElement tcInfo){
		testcase = tcInfo;
	}
	
	public void addSteps(List<StepElement> tSteps){
		step = tSteps;
	}
}
