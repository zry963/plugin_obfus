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


import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;


/**
 * 
 * @author 
 * @since
 */

public class Obfus extends BaseStep implements StepInterface {
	private ObfusMeta meta;
	private ObfusData data;

	public Obfus(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta=(ObfusMeta)smi;
		data=(ObfusData)sdi;
		
		super.init(smi, sdi);
		return true;
	}

	//
	// Run is were the action happens!
	public void run() {
		try {
			logBasic(Messages.getString("Obfus.Log.StartingToRun")); //$NON-NLS-1$
			while (processRow(meta, data) && !isStopped())
				;
		} catch (Exception e) {
			logError(Messages.getString("Obfus.Log.UnexpectedError") + " : " + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		} finally {
			dispose(meta, data);
			logSummary();
			markStop();
		}
	}
	
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
		meta=(ObfusMeta)smi;
		data=(ObfusData)sdi;

		//���ϸ� �����ȡһ��
		Object[] r=getRow();  
		
		//����Ҫ�������ݵ��ֶ�λ��
		int fieldIndex = this.getInputRowMeta().indexOfValue(meta.getFieldName());
		
		//ǰ�沽������ݶ����꣬�򷵻�
		if (r==null)
		{
			setOutputDone();
			return false;
		}
		
		//���ڶ����ĵ�һ�����ݣ���������е�Ԫ����
        if (first)
        {
            first=false;
            data.outputRowMeta = getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
        }
        
        //�������������
		Object[] outputRowData = null;
        
		//��������
		String value = getNewValue(r[fieldIndex].toString());
        
        
        
      //������������� �� ����1
        outputRowData = RowDataUtil.addValueData(r, getInputRowMeta().size(), value);  
        
        //������������� ���� 2
		//outputRowData = r;
		//outputRowData[data.outputRowMeta.size()-1]=value;		
        
		putRow(data.outputRowMeta, outputRowData); 
		return true;
	}

	private String getNewValue( String value) {
		String result;
		if(value.length()<meta.getNumberN())
				result= value;
		else
		{
			int leng = value.length();
			result = value.substring(0,meta.getNumberN());
			while(result.length()<leng)
			{
				result+=meta.getReplaceChars().charAt(0);
			}
			
		}
		return result;
	}
	
}
