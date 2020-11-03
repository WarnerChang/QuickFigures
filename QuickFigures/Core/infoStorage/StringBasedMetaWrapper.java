package infoStorage;

import java.io.Serializable;

//import ij.IJ;

/**A class that keeps a set of key value pains in a string. Methods innitially written 
  to modify the info of ImagePlus metadata*/
public class StringBasedMetaWrapper extends BasicMetaInfoWrapper implements MetaInfoWrapper, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String st="";
	
	public  String getProperty() {
		return st;
	}
	
	public void setProperty(String newProp) {
		st=newProp;
	}
	
	public String getInfo() {
		
		String oldProp=(String) getProperty();
		if (oldProp==null) {
				setProperty("");
				oldProp=(String) getProperty();
			}
		return oldProp;
		
	}
	
	

	
	@Override
	public void setEntry(String name, String number) {
		//ImagePlus img = image;
		if (name==null) return;
		String oldProp=(String) getProperty();
		if (oldProp==null) {
				setProperty( name+"= "+number);
				oldProp=(String) getProperty();
			}
		if (!oldProp.contains(name+"= ")){
				String newProp=oldProp+"\n"+name+"= "+number;
				setProperty(newProp);
				}
		else {replaceInfoMetaDataEntry(name, number);}
		
	}
	

	@Override
	public String getEntryAsString(String entryname) {
		 String b = entryname;
		if (b==null) return null;
		    Object property=getProperty();
		    String ss;
			ss=(String) property;
		    if (ss==null||ss.equals(null) || ss.equals("") ) return null;
		    ss=getMetaDataEntry(ss, b);
		    if (ss==null||ss.equals(null) || ss.equals("") ) return null;
		    ;
		    
		    try {
		    	String output=ss.substring(b.length()+2);
		    	//IssueLog.log("Parsing string "+output);
		    	return output;
		    	} catch (StringIndexOutOfBoundsException si) { throw new NullPointerException(); }
	}
	
	/**replaces the old value of entryname with a new value, similar to set entry but cannot
	 * create a new entry*/
	public void replaceInfoMetaDataEntry(String entryname, String newValue) {
	 String b = entryname;
		if (b==null|| newValue==null) return;
		String entry=getMetaDataEntry( getProperty(), b);
		if (entry==null) return;
		String newMeta=((String) getProperty()).replace(entry, b+"= "+newValue);
		setProperty(newMeta);
	}

	
	
	
	@Override
	public void removeEntry(String entryname) {
		String b = entryname;
		if (b==null ) return;
		String entry=getMetaDataEntry( getProperty(), b);
		if (entry==null) return;
		String newMeta=((String) getProperty()).replace(entry, "");
		setProperty( newMeta);
		
	}


}