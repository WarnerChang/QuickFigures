package genericMontageKit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import graphicalObjectHandles.SmartHandleList;
import graphicalObjects.CordinateConverter;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_BasicShapes.BasicShapeGraphic;
import graphicalObjects_BasicShapes.RectangularGraphic;
import graphicalObjects_BasicShapes.ShapeGraphic;
import utilityClassesForObjects.DefaultPaintProvider;
import utilityClassesForObjects.LocatedObject2D;
import utilityClassesForObjects.Selectable;
import utilityClassesForObjects.StrokedItem;

public class SelectionManager {
	private ZoomableGraphic selectionGraphic=null;
	protected ZoomableGraphic selectionGraphic2=null;
	protected ZoomableGraphic selectionGraphic3=null;
	private Color selColor=Color.blue;
	private Color selColor2=Color.green;
	private SmartHandleList shlist;
	
	/**Allows for the selection of 3 primary objects*/
	public ArrayList<ZoomableGraphic> getSelectionGraphics() {
		ArrayList<ZoomableGraphic> out = new ArrayList<ZoomableGraphic>();
		out.add(selectionGraphic);
		out.add(selectionGraphic2);
		out.add(selectionGraphic3);
		if (shlist!=null) out.add(shlist);
		return out;
	}
	
	public void drawSelections(Graphics2D g2, CordinateConverter<?> cc ) {
		
		for(ZoomableGraphic G: getSelectionGraphics()) {
			if (G==null) continue;
			if (G instanceof Selectable) {
				Selectable s=(Selectable) G;
				s.select();
			}
			G.draw(g2, cc);
		}
		
	}

	public ZoomableGraphic getSelectionGraphic() {
		return selectionGraphic;
	}

	public void setSelectionGraphic(ZoomableGraphic selectionGraphic) {
		 ensureSelected();
		if (selectionGraphic==this.selectionGraphic) return;
		
		if (this.selectionGraphic instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic;
			s.deselect();
		}
		
		this.selectionGraphic = selectionGraphic;
		 ensureSelected();
	}

	
	public void ensureSelected() {
		if (this.selectionGraphic instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic;
			s.select();
		}
		if (this.selectionGraphic2 instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic2;
			s.select();
		}
		if (this.selectionGraphic3 instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic3;
			s.select();
		}
	}
	
	public ZoomableGraphic getSelectionGraphic2() {
	
		return selectionGraphic2;
	}

	
	public ZoomableGraphic getSelectionGraphic3() {
		
		return selectionGraphic3;
	}
	
	public void setSelectionGraphic2(ZoomableGraphic selectionGraphic2) {
		ensureSelected() ;
		if (selectionGraphic2==this.selectionGraphic2) return;
		if (selectionGraphic2 instanceof Selectable) {
			Selectable s=(Selectable) selectionGraphic2;
			s.select();
		}
		if (this.selectionGraphic2 instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic2;
			s.deselect();
		}
		this.selectionGraphic2 = selectionGraphic2;
		ensureSelected() ;
	}
	
	public void setSelectionGraphicWithoutSelecting(ZoomableGraphic selectionGraphic2) {
		
		if (selectionGraphic2==this.selectionGraphic2) return;
		
		if (this.selectionGraphic2 instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic2;
			s.deselect();
		}
		this.selectionGraphic2 = selectionGraphic2;
		
	}
	
	public void setSelectionGraphic3(ZoomableGraphic selectionGraphic3) {
		ensureSelected() ;
		if (selectionGraphic3==this.selectionGraphic3) return;
		if (selectionGraphic3 instanceof Selectable) {
			Selectable s=(Selectable) selectionGraphic3;
			s.select();
		}
		if (this.selectionGraphic3 instanceof Selectable) {
			Selectable s=(Selectable) this.selectionGraphic3;
			s.deselect();
		}
		this.selectionGraphic3 = selectionGraphic3;
		ensureSelected() ;
	}

	public LocatedObject2D getSelection(int i) {
		ZoomableGraphic z = getSelectionGraphic(i);
		if (z instanceof LocatedObject2D) {
			return (LocatedObject2D) z;
		}
		return null;
	}

	/**eliminates the two selections*/
	public void setSelectionstoNull() {
		setSelection(null, 2);
		setSelection(null, 1);
		setSelection(null, 0);
	}
	
	public void setSelection(LocatedObject2D l, int i) {
	//	IssueLog.log("Selection setter method called on  "+this.getSelectionGraphic());
		if (l instanceof ZoomableGraphic) {
			ZoomableGraphic z=(ZoomableGraphic) l;
			if (i==0) setSelectionGraphic(z);
			if (i==1) setSelectionGraphic2(z);
			if (i==2) setSelectionGraphic3(z);
			
		}
		if (l instanceof Selectable) {
			Selectable s=(Selectable) l;
			s.select();
		}
		
		if (l==null&&i==0) {
			this.setSelectionGraphic(null);
		}
		if (l==null&&i==1) {
			this.setSelectionGraphic2(null);
		}
		if (l==null&&i==2) {
			this.setSelectionGraphic3(null);
		}
		//IssueLog.log("Selection is "+this.getSelectionGraphic());
	
	}

public void removeSelections() {
	this.setSelectionGraphic(null);
	this.setSelectionGraphic2(null);
	this.setSelectionGraphic3(null);
}


/**sets the selected area to the shape. uses a stroke width i for the shape*/
public void select(Rectangle2D r1, int i) {
	
	RectangularGraphic bb = new RectangularGraphic();
	bb.setStrokeWidth(i);
	selectionGraphic=bb;
	bb.hideCenterAndRotationHandle=true;
	bb.hideStrokeHandle=true;
	
	if(r1==null) selectionGraphic=null; else 
		bb.setRectangle(r1);
	setUpShapeProperties(bb);
	
	
}

/**methods below set the properties of a shape to be a certain default*/
public void setUpShapeProperties(ShapeGraphic bb ) {
	DefaultPaintProvider dp = new DefaultPaintProvider(selColor);
	dp.setType(DefaultPaintProvider.shapegradient);
	dp.setnCycles(40);
	bb.setStrokeColor(selColor);
	bb.setFillPaintProvider(dp);
	bb.setFillColor(new Color(50,50,250,50));
	bb.setFilled(true);
	setSelectionPropTodefault(bb);
}
/**sets up the default colors for a selection*/
void setSelectionPropTodefault(StrokedItem r1) {
	r1.setDashes(new float[] {2,5});
	r1.setStrokeCap(BasicStroke.CAP_ROUND);
	r1.setStrokeColor(Color.blue);
	r1.setStrokeWidth(0);
}


/**returns the bounding box of the current main selection*/
public Rectangle getSelectionBounds1() {
	if (selectionGraphic instanceof LocatedObject2D) {
		LocatedObject2D l=(LocatedObject2D) selectionGraphic;
		return l.getBounds();
	}
	return new Rectangle();
}

/**sets the strokewidth and color of one of the slection*/
public void setSelectionWidthColor(int strokeWidth, Color strokeColor, int selNum) {
	ZoomableGraphic z=getSelectionGraphic(selNum);
	StrokedItem s = stroked(z);
	s.setStrokeWidth(strokeWidth);
	s.setStrokeColor(strokeColor);
}


private ZoomableGraphic getSelectionGraphic(int i) {
	if (i==0) return getSelectionGraphic() ;
	if (i==1) return this.getSelectionGraphic2();
	if (i==2) return this.getSelectionGraphic3();
	return null;
}

private StrokedItem stroked(Object o) {
	if (o instanceof StrokedItem ) {
		return (StrokedItem )o;
	}
	return null;
}

public void movePrimarySelectionTo2nd() {
	selectionGraphic2=selectionGraphic;
	selectionGraphic=null;
}

public boolean hasSelection1() {
	if (selectionGraphic==null) return false;
	return true;
}

/**sets the selection to the rectangluar one between the points*/
void createDefaultSelection(Point2D p1, Point2D p2) {
	RectangularGraphic r1=new RectangularGraphic(createRectangleFrom2Points(p1,p2).getBounds());
	this.setSelection(r1, 0);
	setSelectionPropTodefault(r1);
	
}



/**this is uses by the selection tool to make a region that results from dragging the mouse
  from point 1 to point 2*/
public static Rectangle2D createRectangleFrom2Points(Point2D p1, Point2D p2) {
	double x0 = p1.getX();
	double y0=p1.getY();
	double x1 = p2.getX();
	double y1=p2.getY();
	
	if (p2.getX()<x0) {
		x0=p2.getX();
		x1=p1.getX();
	}
	if (p2.getY()<y0) {
		y0=p2.getY();
		y1=p1.getY();
	}
	Rectangle2D r1=new Rectangle2D.Double(x0,y0, x1-x0, y1-y0);
	return r1;
}

public void select(Shape s, int strokeWidth, int i) {
	BasicShapeGraphic bb = new BasicShapeGraphic(s);
	this.setUpShapeProperties(bb);
	bb.setStrokeWidth(strokeWidth);
	if (i>=1) {bb.setFillColor(selColor2);
	bb.setStrokeColor(selColor2);}
	
	this.setSelection(bb, i);
}

public void setSelectionHandles(SmartHandleList createList) {
	shlist = createList;
	
}



	
}
