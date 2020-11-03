package undo;

import java.awt.Color;
import java.awt.Font;

import graphicalObjects_BasicShapes.ComplexTextGraphic;
import graphicalObjects_BasicShapes.TextGraphic;
import utilityClassesForObjects.TextParagraph;

public class UndoTextEdit extends AbstractUndoableEdit2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextGraphic textItem;
	private TextParagraph innitial;
	private String startingText;
	private String endingText;
	private TextParagraph finalParagraph;
	private int ihighlight;
	private int iCursor;
	private int fHighlight;
	private int fCursor;
	private Font iFont;
	private Font fFont;
	private UndoSnappingChange isnap;
	private Color iColor;
	private Color fColor;
	private int iDim;
	private int fDim;

	public UndoTextEdit(TextGraphic t) {
		
		 setTextItem(t);
		startingText = t.getText();
		if (t instanceof ComplexTextGraphic) {
			ComplexTextGraphic comp = (ComplexTextGraphic) t;
			innitial=comp.copyParagraph();
		}
		
		ihighlight=t.getHighlightPosition();
		iCursor=t.getCursorPosition();
		
		iFont=t.getFont().deriveFont(t.getFont().getStyle());
		isnap=new UndoSnappingChange(t);
		iColor=t.getTextColor();
		iDim=t.getDimming();
	}
	
	public void setUpFinalState() {
		endingText = getTextItem().getText();
		if (getTextItem() instanceof ComplexTextGraphic) {
			ComplexTextGraphic comp = (ComplexTextGraphic) getTextItem();
			finalParagraph=comp.copyParagraph();
		}
		
		fHighlight= getTextItem().getHighlightPosition();
		fCursor= getTextItem().getCursorPosition();
		fFont=getTextItem().getFont().deriveFont(getTextItem().getFont().getStyle());
		fColor=getTextItem().getTextColor();
		fDim=getTextItem().getDimming();
	}
	

	public void redo() {
		if (getTextItem() instanceof ComplexTextGraphic) {
			((ComplexTextGraphic) getTextItem()).setParagraph(this.finalParagraph);
		} else getTextItem().setText(endingText);
		getTextItem().setSelectedRange(fHighlight, fCursor);
		getTextItem().setFont(fFont);
		getTextItem().setTextColor(fColor);
		getTextItem().setDimming(fDim);
		isnap.redo();
	}
	
	public void undo() {
		if (getTextItem() instanceof ComplexTextGraphic) {
			((ComplexTextGraphic) getTextItem()).setParagraph(innitial);
		} else getTextItem().setText(startingText);
		getTextItem().setSelectedRange(ihighlight, iCursor);
		getTextItem().setFont(iFont);
		getTextItem().setTextColor(iColor);
		getTextItem().setDimming(iDim);
		isnap.undo();
	}

	public TextGraphic getTextItem() {
		return textItem;
	}

	public void setTextItem(TextGraphic textItem) {
		this.textItem = textItem;
	}

}