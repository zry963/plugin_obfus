/**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

package com.aofei.kettleplugin.obfus;

import java.util.ArrayList;  
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

public class ObfusMeta extends BaseStepMeta implements
		StepMetaInterface {


	
	/** the field need to be obfuscated */
	private String fieldName;
	
	/** new obfuscated field name */
	private String newFieldName;
	
	/**obfuscation method */
	private boolean isFixPreN;
	
	/**Number N */
	private int numberN;
	
	/**Replace chars*/
	private String replaceChars;
	

	public ObfusMeta() {
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List databases, Map counters)
			throws KettleXMLException {
		readData(stepnode, databases);
	}


	public Object clone() {
		ObfusMeta retval = (ObfusMeta) super.clone();
		return retval;
	}

	private void readData(Node stepnode, List databases)
			throws KettleXMLException {
		try {

			fieldName = XMLHandler.getTagValue(stepnode, "FIELD_NAME"); //$NON-NLS-1$
			newFieldName = XMLHandler.getTagValue(stepnode, "NEW_FIELD_NAME"); //$NON-NLS-1$
			isFixPreN = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "FIX_PRE_N"))?new Boolean("true"):new Boolean("false"); //$NON-NLS-1$
			numberN = Const.toInt(XMLHandler.getTagValue(stepnode, "N"), 0); //$NON-NLS-1$
			replaceChars = XMLHandler.getTagValue(stepnode, "REPALCE_CHARS"); //$NON-NLS-1$

		} catch (Exception e) {
			throw new KettleXMLException(
					Messages
							.getString("ObfusMeta.Exception.UnableToReadStepInfoFromXML"), e); //$NON-NLS-1$
		}
	}

	public void setDefault() {
		numberN = 1;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();

		retval.append("    " + XMLHandler.addTagValue("FIELD_NAME", fieldName)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    " + XMLHandler.addTagValue("NEW_FIELD_NAME", newFieldName)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    " + XMLHandler.addTagValue("FIX_PRE_N", isFixPreN?"Y":"N")); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    " + XMLHandler.addTagValue("N", numberN)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    " + XMLHandler.addTagValue("REPALCE_CHARS", replaceChars)); //$NON-NLS-1$ //$NON-NLS-2$
				
		return retval.toString();
	}

	public void readRep(Repository rep, ObjectId id_step, List databases,
			Map counters) throws KettleException {
		try {
			fieldName =rep.getStepAttributeString(id_step, "FIELD_NAME"); //$NON-NLS-1$
			newFieldName =rep.getStepAttributeString(id_step, "NEW_FIELD_NAME"); //$NON-NLS-1$
			isFixPreN = "Y".equals(rep.getStepAttributeString(id_step, "FIX_PRE_N"))?new Boolean("true"):new Boolean("false"); //$NON-NLS-1$
			numberN = (int) rep.getStepAttributeInteger(id_step, "N"); //$NON-NLS-1$
			replaceChars =rep.getStepAttributeString(id_step, "REPALCE_CHARS"); //$NON-NLS-1$

		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("ObfusMeta.Exception.UnexpectedErrorInReadingStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step,
					"FIELD_NAME", fieldName); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"NEW_FIELD_NAME", newFieldName); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"FIX_PRE_N", isFixPreN?"Y":"N"); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"commit", numberN); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"REPALCE_CHARS", replaceChars); //$NON-NLS-1$
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("ObfusMeta.Exception.UnableToSaveStepInfo") + id_step, e); //$NON-NLS-1$
		}
	}

	public void getFields(RowMetaInterface row, String origin,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		int size = row.size();
		ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(newFieldName), 
				ValueMeta.TYPE_STRING);
		v.setOrigin(origin);
		row.addValueMeta(v);
	}

	public void check(ArrayList remarks, StepMeta stepinfo, RowMeta prev,
			String input[], String output[], RowMeta info) {
		CheckResult cr;
		String error_message = ""; //$NON-NLS-1$

	}


	public void analyseImpact(ArrayList impact, TransMeta transMeta,
			StepMeta stepMeta, RowMeta prev, String[] input, String[] output,
			RowMeta info) throws KettleStepException {

	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface info,
			TransMeta transMeta, String name) {
		return new ObfusDialog(shell, info, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new Obfus(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new ObfusData();
	}
	
	public void check(List<CheckResultInterface> arg0, TransMeta arg1,
			StepMeta arg2, RowMetaInterface arg3, String[] arg4, String[] arg5,
			RowMetaInterface arg6) {
		// TODO Auto-generated method stub

	}
	
	public int getNumberN() {
		return numberN;
	}

	public void setNumberN(int n) {
		numberN = n;
	}

	public String getReplaceChars() {
		return replaceChars;
	}

	public void setReplaceChars(String replaceChars) {
		this.replaceChars = replaceChars;
	}

	public boolean isFixPreN() {
		return isFixPreN;
	}

	public void setFixPreN(boolean fixPreN) {
		this.isFixPreN = fixPreN;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getNewFieldName() {
		return newFieldName;
	}

	public void setNewFieldName(String newFieldName) {
		this.newFieldName = newFieldName;
	}

}
