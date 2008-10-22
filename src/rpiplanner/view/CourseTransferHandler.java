package rpiplanner.view;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.TransferHandler;

import rpiplanner.POSController;
import rpiplanner.model.Course;

public class CourseTransferHandler extends TransferHandler {
	private POSController controller;

	public CourseTransferHandler(POSController controller) {
		this.controller = controller;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		if(c instanceof JList){
			Course toExport = (Course)((JList)c).getSelectedValue();
			return new CourseTransfer(toExport); 
		}
		else if(c instanceof JLabel){
			// dragging a course out of the plan
			
		}
		return super.createTransferable(c);
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return MOVE | COPY;
	}

	@Override
	public boolean importData(JComponent comp, Transferable t) {
		if(controller == null)
			return false;
		try {
			Course toAdd = (Course) t.getTransferData(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType+";class=rpiplanner.model.Course"));
			if(comp instanceof CourseDisplay)
				comp = (JComponent) comp.getParent();
			Component[] panels = comp.getParent().getComponents();
			int index = 0;
			while(comp != panels[index])
				index++;
			controller.addCourse(index, toAdd);
			return true;
		} catch (UnsupportedFlavorException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		for(DataFlavor f : transferFlavors){
			if(f.getRepresentationClass() == Course.class)
				return true;
		}
		return false;
	}
	
	public class CourseTransfer implements Transferable {
		private Course payload;
		
		public CourseTransfer(Course toExport) {
			payload = toExport;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if(flavor.getRepresentationClass() == Course.class){
				return payload;
			} else{
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public DataFlavor[] getTransferDataFlavors() {
			try{
				DataFlavor[] flavors = {new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType+";class=rpiplanner.model.Course")};
				return flavors;
			} catch (ClassNotFoundException e){
				// TODO Auto-generated exception handler
				e.printStackTrace();
			}
			return null;
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.getRepresentationClass() == Course.class;
		}
	}
}
