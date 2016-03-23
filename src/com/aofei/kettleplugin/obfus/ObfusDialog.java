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

/*
 * Created on 18-mei-2003
 *
 */

package com.aofei.kettleplugin.obfus;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


public class ObfusDialog extends BaseStepDialog implements StepDialogInterface
{	private CCombo       wConnection;



private Label        wlFieldName;
private Text         wFieldName;
private FormData     fdlFieldName, fdFieldName;

private Label        wlNewFieldName;
private Text         wNewFieldName;
private FormData     fdlNewFieldName, fdNewFieldName;

private Label        wlFixPreN;
private Button       wFixPreN;
private FormData     fdlFixPreN, fdFixPreN;

private Label        wlNumberN;
private Text         wNumberN;
private FormData     fdlN, fdN;

private Label        wlReplaceChars;
private Text         wReplaceChars;
private FormData     fdlReplaceChars, fdReplaceChars;

private ObfusMeta meta;

public ObfusDialog(Shell parent, Object in, TransMeta tr, String sname)
{
	super(parent, (BaseStepMeta)in, tr, sname);
	meta=(ObfusMeta)in;
}

public String open()
{
	Shell parent = getParent();
	Display display = parent.getDisplay();

	shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
		props.setLook(shell);

	ModifyListener lsMod = new ModifyListener() 
	{
		public void modifyText(ModifyEvent e) 
		{
			meta.setChanged();
		}
	};
	changed = meta.hasChanged();

	FormLayout formLayout = new FormLayout ();
	formLayout.marginWidth  = Const.FORM_MARGIN;
	formLayout.marginHeight = Const.FORM_MARGIN;

	shell.setLayout(formLayout);
	shell.setText(Messages.getString("ObfusDialog.Shell.Title")); //$NON-NLS-1$
	
	int middle = props.getMiddlePct();
	int margin = Const.MARGIN;

	// Stepname line
	wlStepname=new Label(shell, SWT.RIGHT);
	wlStepname.setText(Messages.getString("ObfusDialog.Stepname.Label")); //$NON-NLS-1$
		props.setLook(wlStepname);
	fdlStepname=new FormData();
	fdlStepname.left = new FormAttachment(0, 0);
	fdlStepname.top  = new FormAttachment(0, margin);
	fdlStepname.right= new FormAttachment(middle, -margin);
	wlStepname.setLayoutData(fdlStepname);	
	wStepname=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
	wStepname.setText(stepname);
		props.setLook(wStepname);
	wStepname.addModifyListener(lsMod);
	fdStepname=new FormData();
	fdStepname.left = new FormAttachment(middle, 0);
	fdStepname.top  = new FormAttachment(0, margin);
	fdStepname.right= new FormAttachment(100, 0);
	wStepname.setLayoutData(fdStepname);

	// FieldName
	wlFieldName = new Label(shell, SWT.RIGHT);
	wlFieldName.setText(Messages.getString("ObfusDialog.fieldName.Label")); //$NON-NLS-1$
	props.setLook(wlFieldName);
	fdlFieldName = new FormData();
	fdlFieldName.left = new FormAttachment(0, 0);
	fdlFieldName.top = new FormAttachment(wStepname, margin);
	fdlFieldName.right = new FormAttachment(middle, -margin);
	wlFieldName.setLayoutData(fdlFieldName);
	wFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
	props.setLook(wFieldName);
	fdFieldName = new FormData();
	fdFieldName.left = new FormAttachment(middle, 0);
	fdFieldName.top = new FormAttachment(wStepname, margin);
	fdFieldName.right = new FormAttachment(100, 0);
	wFieldName.setLayoutData(fdFieldName);
    
	// NewFieldName
	wlNewFieldName = new Label(shell, SWT.RIGHT);
	wlNewFieldName.setText(Messages.getString("ObfusDialog.newFieldName.Label")); //$NON-NLS-1$
	props.setLook(wlNewFieldName);
	fdlNewFieldName = new FormData();
	fdlNewFieldName.left = new FormAttachment(0, 0);
	fdlNewFieldName.top = new FormAttachment(wFieldName, margin);
	fdlNewFieldName.right = new FormAttachment(middle, -margin);
	wlNewFieldName.setLayoutData(fdlNewFieldName);
	wNewFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
	props.setLook(wNewFieldName);
	fdNewFieldName = new FormData();
	fdNewFieldName.left = new FormAttachment(middle, 0);
	fdNewFieldName.top = new FormAttachment(wFieldName, margin);
	fdNewFieldName.right = new FormAttachment(100, 0);
	wNewFieldName.setLayoutData(fdNewFieldName);

	// Fix pre N check box
	wlFixPreN = new Label(shell, SWT.RIGHT);
	wlFixPreN.setText(Messages.getString("ObfusDialog.FixPreN.label")); //$NON-NLS-1$
    props.setLook(wlFixPreN);
    fdlFixPreN = new FormData();
    fdlFixPreN.left = new FormAttachment(0, 0);
    fdlFixPreN.top = new FormAttachment(wNewFieldName, margin);
    fdlFixPreN.right = new FormAttachment(middle, -margin);
    wlFixPreN.setLayoutData(fdlFixPreN);
    wFixPreN = new Button(shell, SWT.CHECK); 
    props.setLook(wFixPreN);
    fdFixPreN= new FormData();
    fdFixPreN.left = new FormAttachment(middle, 0);
    fdFixPreN.top = new FormAttachment(wNewFieldName, margin);
    fdFixPreN.right = new FormAttachment(100, 0);
    wFixPreN.setLayoutData(fdFixPreN);
    wFixPreN.addSelectionListener(new SelectionAdapter() {
    	public void widgetSelected(SelectionEvent arg0) { 
    		meta.setChanged();
    			wlNumberN.setEnabled(wFixPreN.getSelection());
    			wNumberN.setEnabled(wFixPreN.getSelection());
    			wlReplaceChars.setEnabled(wFixPreN.getSelection());
    			wReplaceChars.setEnabled(wFixPreN.getSelection());
    		} 
    	}
    );

	//N
	wlNumberN = new Label(shell, SWT.RIGHT);
	wlNumberN.setText(Messages.getString("ObfusDialog.N.Label")); //$NON-NLS-1$
	props.setLook(wlNumberN);
	fdlN = new FormData();
	fdlN.left = new FormAttachment(0, 0);
	fdlN.top = new FormAttachment(wFixPreN, margin);
	fdlN.right = new FormAttachment(middle, -margin);
	wlNumberN.setLayoutData(fdlN);
	wNumberN = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
	props.setLook(wNumberN);
	fdN = new FormData();
	fdN.left = new FormAttachment(middle, 0);
	fdN.top = new FormAttachment(wFixPreN, margin);
	fdN.right = new FormAttachment(100, 0);
	wNumberN.setLayoutData(fdN);
    
	// ReplaceChars
	wlReplaceChars = new Label(shell, SWT.RIGHT);
	wlReplaceChars.setText(Messages.getString("ObfusDialog.replaceChars.Label")); //$NON-NLS-1$
	props.setLook(wlReplaceChars);
	fdlReplaceChars = new FormData();
	fdlReplaceChars.left = new FormAttachment(0, 0);
	fdlReplaceChars.top = new FormAttachment(wNumberN, margin);
	fdlReplaceChars.right = new FormAttachment(middle, -margin);
	wlReplaceChars.setLayoutData(fdlReplaceChars);
	wReplaceChars = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
	props.setLook(wReplaceChars);
	fdReplaceChars = new FormData();
	fdReplaceChars.left = new FormAttachment(middle, 0);
	fdReplaceChars.top = new FormAttachment(wNumberN, margin);
	fdReplaceChars.right = new FormAttachment(70, 0);
	wReplaceChars.setLayoutData(fdReplaceChars);

	// THE BUTTONS
	wOK=new Button(shell, SWT.PUSH);
	wOK.setText(Messages.getString("System.Button.OK")); //$NON-NLS-1$
	wCancel=new Button(shell, SWT.PUSH);
	wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$

	setButtonPositions(new Button[] { wOK, wCancel }, margin, null);
	
	// Add listeners
	lsOK       = new Listener() { public void handleEvent(Event e) { ok();        } };
	lsCancel   = new Listener() { public void handleEvent(Event e) { cancel();    } };
	
	wOK.addListener    (SWT.Selection, lsOK    );
	wCancel.addListener(SWT.Selection, lsCancel);
	
	lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
	
	wStepname.addSelectionListener( lsDef );
	
	// Detect X or ALT-F4 or something that kills this window...
	shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );



	// Set the shell size, based upon previous time...
	setSize();
	
	getData();
	meta.setChanged(changed);

	shell.open();
	while (!shell.isDisposed())
	{
			if (!display.readAndDispatch()) display.sleep();
	}
	return stepname;
}

public void setFlags()
{
}

/**
 * Copy information from the meta-data input to the dialog fields.
 */ 
public void getData()
{
	log.logDebug(toString(), Messages.getString("ObfusDialog.Log.GettingKeyInfo")); //$NON-NLS-1$
	
	wStepname.selectAll();
	if(!Const.isEmpty(meta.getFieldName()))
		wFieldName.setText(meta.getFieldName());
	if(!Const.isEmpty(meta.getNewFieldName()))
		wNewFieldName.setText(meta.getNewFieldName());
	if(!Const.isEmpty(meta.getReplaceChars()))	
		wReplaceChars.setText(meta.getReplaceChars());
	if(meta.getNumberN()!=0)	
		wNumberN.setText(new Integer(meta.getNumberN()).toString());
	wFixPreN.setSelection(meta.isFixPreN());
    setFlags();
}

private void cancel()
{
	stepname=null;
	meta.setChanged(changed);
	dispose();
}

private void getInfo(ObfusMeta meta)
{
	meta.setFieldName(wFieldName.getText());
	meta.setNewFieldName(wNewFieldName.getText());
	meta.setFixPreN(wFixPreN.isEnabled());
	meta.setReplaceChars(wReplaceChars.getText());
	try{
		meta.setNumberN(new Integer(wNumberN.getText()).intValue());
	}catch (Exception ex)
	{
		log.logError(toString(),wNumberN.getText()+ " cant conver to int"); //$NON-NLS-1$
		MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR );
		mb.setMessage("cant conver to int"); //$NON-NLS-1$
		mb.setText("cant conver to int"); //$NON-NLS-1$
		mb.open();
	}
	stepname = wStepname.getText(); // return value

}


private void ok()
{
	// Get the information for the dialog into the input structure.
	getInfo(meta);
	if (wFixPreN.isEnabled())
	{
		if((Const.isEmpty(wNumberN.getText()) || Const.isEmpty(wReplaceChars.getText())))
		{
			MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR );
			mb.setMessage("not be null"); //$NON-NLS-1$
			mb.setText("not be null"); //$NON-NLS-1$
			mb.open();
			return;
		}
	}
	
	dispose();
}



public String toString()
{
	return this.getClass().getName();
}
}
