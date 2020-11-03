package graphicalObjectHandles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

import applicationAdapters.CanvasMouseEventWrapper;
import applicationAdapters.DisplayedImageWrapper;
import genericMontageLayoutToolKit.MontageLayoutRowColNumberTool;
import graphicalObjects_LayoutObjects.MontageLayoutGraphic;
import graphicalObjects_LayoutObjects.PanelLayoutGraphic;

import java.awt.geom.Rectangle2D;

import gridLayout.BasicMontageLayout;
import gridLayout.GenericMontageEditor;
import gridLayout.MontageSpaces;
import imageMenu.CanvasAutoResize;


public class AddRowHandle extends SmartHandle implements MontageSpaces{

	private MontageLayoutGraphic layout;
	private int type;
	private boolean subtract=false;
	boolean dragType=true;
	private DisplayedImageWrapper wrap;
	int a=0;

	public AddRowHandle(int x, int y) {
		super(x, y);
	}

	public AddRowHandle(MontageLayoutGraphic montageLayoutGraphic, int y, boolean sub) {
		this(0,0);
		this.subtract=sub;
		this.layout=montageLayoutGraphic;
		this.type=y;
		int offset = -a; if(subtract) offset=-offset;
		Rectangle2D space = layout.getPanelLayout().getSelectedSpace(1, ALL_OF_THE+PANELS).getBounds();
		
		double x2 = space.getCenterX()+offset;
		double y2 = space.getMaxY()+20;
		if(type==COLS) {
			offset = -a; if(!subtract) offset=a;
			y2 = space.getCenterY()+offset;
			x2 = space.getMaxX()+20;
		}
		this.setCordinateLocation(new Point2D.Double(x2, y2));
	//this.setLocation(50,50);
		
		super.handlesize=4;
		
		int plusSize = 5;
		Area a = addSubtractShape(plusSize);
		specialShape=a;//AffineTransform.getTranslateInstance(x2,y2).createTransformedShape(a);
		if (subtract)this.setHandleColor(Color.red);
		else setHandleColor(Color.green);
		
		if(type==COLS) this.setHandleNumber(PanelLayoutGraphic.AddColHandle); else
		this.setHandleNumber(PanelLayoutGraphic.AddRowHandle);
		
		if(dragType)return;
		message="Add ";
		if(subtract) message="Remove ";
		if(type==COLS) message+="Column"; else message+="Row";
		
	}

	private Area addSubtractShape(int plusSize) {
		Area a=new Area();
		a.add(new Area(new Rectangle(-plusSize, 0, plusSize*3, plusSize)));
		 if(!subtract) a.add(new Area(new Rectangle(0, -plusSize, plusSize, plusSize*3)));
		return a;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean containsClickPoint(Point2D p) {
		return super.containsClickPoint(p);
	}
	
	public void handlePress(CanvasMouseEventWrapper canvasMouseEventWrapper) {
		wrap=canvasMouseEventWrapper.getAsDisplay();
		
		if(dragType&&canvasMouseEventWrapper.clickCount()<2) return;
		if (this.subtract) {
			if(type==COLS&&layout.getPanelLayout().nColumns()>1) 
				layout.getEditor().addCols(layout.getPanelLayout(), -1);
			if (type==ROWS&&layout.getPanelLayout().nRows()>1) layout.getEditor().addRows(layout.getPanelLayout(), -1);
			
		} else {
		if(type==COLS) layout.getEditor().addCols(layout.getPanelLayout(), 1);
			else layout.getEditor().addRows(layout.getPanelLayout(), 1);
		}
		
		new CanvasAutoResize().performActionDisplayedImageWrapper(wrap);

		
	}
	
	public void handleDrag(CanvasMouseEventWrapper lastDragOrRelMouseEvent) {
		if(!dragType) return;
		Point p2 = lastDragOrRelMouseEvent.getCordinatePoint();
		BasicMontageLayout bm = layout.getPanelLayout();
		GenericMontageEditor edit = layout.getEditor();
		int[] rowcol = MontageLayoutRowColNumberTool.findAddedRowsCols((int)p2.getX(), (int)p2.getY(), bm);
		
		if (rowcol[0]+bm.nRows()>=1 &&type==ROWS)edit.addRows(bm, rowcol[0]);
		if (rowcol[1]+bm.nColumns()>=1 &&type==COLS)edit.addCols(bm, rowcol[1]);
		
		new CanvasAutoResize().performActionDisplayedImageWrapper(lastDragOrRelMouseEvent.getAsDisplay());

	}
	/**What to do when a handle is moved from point p1 to p2*/
	public void handleMove(Point2D p1, Point2D p2) {
		
	}
	
	protected Font getMessageFont() {
		return new Font("Arial", 0, 6);
	}

	
}