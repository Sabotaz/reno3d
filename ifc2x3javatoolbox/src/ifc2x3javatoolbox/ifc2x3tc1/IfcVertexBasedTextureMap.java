/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the entity IfcVertexBasedTextureMap<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcVertexBasedTextureMap extends InternalAccessClass implements ClassInterface
{
	private static final String[] nonInverseAttributes = new String[]{"LIST<IfcTextureVertex>","LIST<IfcCartesianPoint>"};
	private java.util.ArrayList<CloneableObject> stepParameter = null;
	private java.util.HashSet<ObjectChangeListener> listenerList = null;
	protected int stepLineNumber;
	/** TextureVertices is an DEMANDED attribute - may not be null**/
	protected LIST<IfcTextureVertex> TextureVertices;
	/** TexturePoints is an DEMANDED attribute - may not be null**/
	protected LIST<IfcCartesianPoint> TexturePoints;
	/**
	* The default constructor.
	**/
	public IfcVertexBasedTextureMap(){}

	/**
	* Constructs a new IfcVertexBasedTextureMap object using the given parameters.
	*
	* @param TextureVertices DEMANDED parameter of type LIST<IfcTextureVertex> - may not be null.
	* @param TexturePoints DEMANDED parameter of type LIST<IfcCartesianPoint> - may not be null.
	**/
	public IfcVertexBasedTextureMap(LIST<IfcTextureVertex> TextureVertices, LIST<IfcCartesianPoint> TexturePoints)
	{
		this.TextureVertices = TextureVertices;
		this.TexturePoints = TexturePoints;
		resolveInverses();
	}

	/**
	 * This method initializes the IfcVertexBasedTextureMap object using the given parameters.
	*
	* @param TextureVertices DEMANDED parameter of type LIST<IfcTextureVertex> - may not be null.
	* @param TexturePoints DEMANDED parameter of type LIST<IfcCartesianPoint> - may not be null.
	**/
	public void setParameters(LIST<IfcTextureVertex> TextureVertices, LIST<IfcCartesianPoint> TexturePoints)
	{
		this.TextureVertices = TextureVertices;
		this.TexturePoints = TexturePoints;
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	@SuppressWarnings("unchecked")
	void initialize(java.util.ArrayList<CloneableObject> parameters)
	{
		this.TextureVertices = (LIST<IfcTextureVertex>) parameters.get(0);
		this.TexturePoints = (LIST<IfcCartesianPoint>) parameters.get(1);
		resolveInverses();
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void destruct()
	{
		listenerList = null;
	}

	private void resolveInverses()
	{
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	String[] getNonInverseAttributeTypes()
	{
		return IfcVertexBasedTextureMap.nonInverseAttributes;	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	private java.util.HashSet<String> getRedefinedDerivedAttributeTypes()
	{
		java.util.HashSet<String> redefinedDerivedAttributes = new java.util.HashSet<String>();
		return redefinedDerivedAttributes;	}

	/**
 * This method returns the object IFC  STEP representation. This method is called by the IfcModel object to write IFC STEP files.
	 *
	 * @return the IFC STEP representation of this object
	**/
	public String getStepLine()
	{
		String stepString = new String("#"+this.stepLineNumber+"= ");
		stepString = stepString.concat("IFCVERTEXBASEDTEXTUREMAP(");
		if(getRedefinedDerivedAttributeTypes().contains("TextureVertices")) stepString = stepString.concat("*,");
		else{
		if(this.TextureVertices != null)		stepString = stepString.concat(((RootInterface)this.TextureVertices).getStepParameter(IfcTextureVertex.class.isInterface())+",");
		else		stepString = stepString.concat("$,");
		}
		if(getRedefinedDerivedAttributeTypes().contains("TexturePoints")) stepString = stepString.concat("*);");
		else{
		if(this.TexturePoints != null)		stepString = stepString.concat(((RootInterface)this.TexturePoints).getStepParameter(IfcCartesianPoint.class.isInterface())+");");
		else		stepString = stepString.concat("$);");
		}
		return stepString;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	public String getStepParameter(boolean isSelectType)
	{
		return "#" + this.stepLineNumber;
	}

	/**
	 * This method returns the line number within a IFC  STEP representation. This method is called from other objects, where this one is referenced.
	 *
	 * @return the STEP line number
	**/
	public int getStepLineNumber()
	{
		return this.stepLineNumber;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void setStepLineNumber(int number)
	{
		this.stepLineNumber = number;
	}

	/**
	* This method sets the TextureVertices attribute to the given value.
	*
	* @param TextureVertices OPTIONAL value to set
	**/
	public void setTextureVertices(LIST<IfcTextureVertex> TextureVertices)
	{
		this.TextureVertices = TextureVertices;
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the list of the TextureVertices attribute.
	*
	* @return a copy of the TextureVertices list
	**/
	public LIST<IfcTextureVertex> getTextureVertices()
	{
		if(this.TextureVertices != null)
			return new LIST<IfcTextureVertex>(this.TextureVertices);
		return null;
	}

	/**
	* This method adds an IfcTextureVertex object to the TextureVertices list.
	* @param TextureVertices element to be appended to this list.
	**/
	public void addTextureVertices(IfcTextureVertex TextureVertices)
	{
		if(this.TextureVertices == null)
			this.TextureVertices = new LIST<IfcTextureVertex>();
		this.TextureVertices.add(TextureVertices);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of IfcTextureVertex objects to the TextureVertices list.
	* @param TextureVertices collection containing elements to be added to this list.
	**/
	public void addAllTextureVertices(java.util.Collection<IfcTextureVertex> TextureVertices)
	{
		if(this.TextureVertices == null)
			this.TextureVertices = new LIST<IfcTextureVertex>();
		this.TextureVertices.addAll(TextureVertices);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the TextureVertices list.
	**/
	public void clearTextureVertices()
	{
		if(this.TextureVertices != null)
		{
			this.TextureVertices.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an IfcTextureVertex object from the TextureVertices list.
	* @param TextureVertices element to be removed from this list.
	**/
	public void removeTextureVertices(IfcTextureVertex TextureVertices)
	{
		if(this.TextureVertices != null)
		{
			this.TextureVertices.remove(TextureVertices);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of IfcTextureVertex objects from the TextureVertices list.
	* @param TextureVertices collection containing elements to be removed from this list.
	**/
	public void removeAllTextureVertices(java.util.Collection<IfcTextureVertex> TextureVertices)
	{
		if(this.TextureVertices != null)
		{
			this.TextureVertices.removeAll(TextureVertices);
			fireChangeEvent();
		}
	}

	/**
	* This method sets the TexturePoints attribute to the given value.
	*
	* @param TexturePoints OPTIONAL value to set
	**/
	public void setTexturePoints(LIST<IfcCartesianPoint> TexturePoints)
	{
		this.TexturePoints = TexturePoints;
		fireChangeEvent();
	}

	/**
	* This method returns a copy of the list of the TexturePoints attribute.
	*
	* @return a copy of the TexturePoints list
	**/
	public LIST<IfcCartesianPoint> getTexturePoints()
	{
		if(this.TexturePoints != null)
			return new LIST<IfcCartesianPoint>(this.TexturePoints);
		return null;
	}

	/**
	* This method adds an IfcCartesianPoint object to the TexturePoints list.
	* @param TexturePoints element to be appended to this list.
	**/
	public void addTexturePoints(IfcCartesianPoint TexturePoints)
	{
		if(this.TexturePoints == null)
			this.TexturePoints = new LIST<IfcCartesianPoint>();
		this.TexturePoints.add(TexturePoints);
		fireChangeEvent();
	}

	/**
	* This method adds a collection of IfcCartesianPoint objects to the TexturePoints list.
	* @param TexturePoints collection containing elements to be added to this list.
	**/
	public void addAllTexturePoints(java.util.Collection<IfcCartesianPoint> TexturePoints)
	{
		if(this.TexturePoints == null)
			this.TexturePoints = new LIST<IfcCartesianPoint>();
		this.TexturePoints.addAll(TexturePoints);
		fireChangeEvent();
	}

	/**
	* This method removes all elements from the TexturePoints list.
	**/
	public void clearTexturePoints()
	{
		if(this.TexturePoints != null)
		{
			this.TexturePoints.clear();
			fireChangeEvent();
		}
	}

	/**
	* This method removes an IfcCartesianPoint object from the TexturePoints list.
	* @param TexturePoints element to be removed from this list.
	**/
	public void removeTexturePoints(IfcCartesianPoint TexturePoints)
	{
		if(this.TexturePoints != null)
		{
			this.TexturePoints.remove(TexturePoints);
			fireChangeEvent();
		}
	}

	/**
	* This method removes a collection of IfcCartesianPoint objects from the TexturePoints list.
	* @param TexturePoints collection containing elements to be removed from this list.
	**/
	public void removeAllTexturePoints(java.util.Collection<IfcCartesianPoint> TexturePoints)
	{
		if(this.TexturePoints != null)
		{
			this.TexturePoints.removeAll(TexturePoints);
			fireChangeEvent();
		}
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	void setStepParameter(java.util.ArrayList<CloneableObject> parameter)
	{
		this.stepParameter = parameter;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	java.util.ArrayList<CloneableObject> getStepParameter()
	{
		return this.stepParameter;
	}

	/**
	 * This method registers an ObjectChangeListener to this object. An event is fired whenever one of its values was changed.
	 * 
	 *@param listener the listener to register
	**/
	public void addObjectChangeListener(ObjectChangeListener listener)
	{
		if (listenerList == null)	listenerList = new java.util.HashSet<ObjectChangeListener>(1,1);
		listenerList.add(listener);
	}

	/**
	 * This method unregisters an ObjectChangeListener from this object.
	 * 
	 *@param listener the listener to unregister
	**/
	public void removeObjectChangeListener(ObjectChangeListener listener)
	{
		if (listenerList == null)	return;
		listenerList.remove(listener);
		if (listenerList.size()==0) listenerList = null;
	}

	/**
	 * This method removes all currently registered ObjectChangeListeners from this object.
	**/
	public void removeAllObjectChangeListeners()
	{
		listenerList = null;
	}

	protected void fireChangeEvent()
	{
		if(listenerList == null) return;
		for(ObjectChangeListener listener : listenerList)
			listener.ifcModelObjectChange(this);
	}

	/**
	 * This method clones the object (deep cloning).
	 *
	 * @return the cloned object
	**/
	@SuppressWarnings("unchecked")
	public Object clone()
	{
		IfcVertexBasedTextureMap ifcVertexBasedTextureMap = new IfcVertexBasedTextureMap();
		if(this.TextureVertices != null)
			ifcVertexBasedTextureMap.setTextureVertices((LIST<IfcTextureVertex>)this.TextureVertices.clone());
		if(this.TexturePoints != null)
			ifcVertexBasedTextureMap.setTexturePoints((LIST<IfcCartesianPoint>)this.TexturePoints.clone());
		return ifcVertexBasedTextureMap;
	}

	/**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 *
	 * @return the cloned object
	**/
	public Object shallowCopy()
	{
		IfcVertexBasedTextureMap ifcVertexBasedTextureMap = new IfcVertexBasedTextureMap();
		if(this.TextureVertices != null)
			ifcVertexBasedTextureMap.setTextureVertices(this.TextureVertices);
		if(this.TexturePoints != null)
			ifcVertexBasedTextureMap.setTexturePoints(this.TexturePoints);
		return ifcVertexBasedTextureMap;
	}

	/**
	* This method returns the objects standard description.
	*
	* @return the standard description
	**/
	public String toString()
	{
		return "#"+ this.getStepLineNumber() + " " + this.getClass().getSimpleName();
	}


}
