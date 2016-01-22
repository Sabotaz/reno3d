/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * This is a default implementation of the enumeration type IfcEvaporativeCoolerTypeEnum<br><br>
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class IfcEvaporativeCoolerTypeEnum extends ENUM implements TypeInterface
{
	/**
 * The default constructor for the enumeration object IfcEvaporativeCoolerTypeEnum.
**/
	public IfcEvaporativeCoolerTypeEnum(){}

	/**
 * Constructs a new IfcEvaporativeCoolerTypeEnum enumeration object using the given parameter.
	**/
	public IfcEvaporativeCoolerTypeEnum(java.lang.String value)
	{
	this.value = IfcEvaporativeCoolerTypeEnum_internal.valueOf(value);
	}
	/**
	* This method sets the value of this enumeration type.
	* The value has to be of type IfcEvaporativeCoolerTypeEnum_internal.
	* @param value the value to set
	*/
	public void setValue(Object value)
	{
		this.value = (IfcEvaporativeCoolerTypeEnum_internal)value;
	}
	/**
	* This method sets the value of this enumeration taken from a String.
	* The String must be one of: "DIRECTEVAPORATIVERANDOMMEDIAAIRCOOLER", "DIRECTEVAPORATIVERIGIDMEDIAAIRCOOLER", "DIRECTEVAPORATIVESLINGERSPACKAGEDAIRCOOLER", "DIRECTEVAPORATIVEPACKAGEDROTARYAIRCOOLER", "DIRECTEVAPORATIVEAIRWASHER", "INDIRECTEVAPORATIVEPACKAGEAIRCOOLER", "INDIRECTEVAPORATIVEWETCOIL", "INDIRECTEVAPORATIVECOOLINGTOWERORCOILCOOLER", "INDIRECTDIRECTCOMBINATION", "USERDEFINED", "NOTDEFINED".
	*
	* @param value the value to set
	*/
	public void setValue(String value)
	{
		this.value = IfcEvaporativeCoolerTypeEnum_internal.valueOf(value);
	}
	/**
	 * This method clones the enumeration object (deep cloning).
	 *
	 * @return the cloned object
	**/
	public Object clone()
	{		IfcEvaporativeCoolerTypeEnum fcEvaporativeCoolerTypeEnum = new IfcEvaporativeCoolerTypeEnum();
		fcEvaporativeCoolerTypeEnum.setValue(this.value);
		return fcEvaporativeCoolerTypeEnum;
	}

	public static enum IfcEvaporativeCoolerTypeEnum_internal
	{
		DIRECTEVAPORATIVERANDOMMEDIAAIRCOOLER,
		DIRECTEVAPORATIVERIGIDMEDIAAIRCOOLER,
		DIRECTEVAPORATIVESLINGERSPACKAGEDAIRCOOLER,
		DIRECTEVAPORATIVEPACKAGEDROTARYAIRCOOLER,
		DIRECTEVAPORATIVEAIRWASHER,
		INDIRECTEVAPORATIVEPACKAGEAIRCOOLER,
		INDIRECTEVAPORATIVEWETCOIL,
		INDIRECTEVAPORATIVECOOLINGTOWERORCOILCOOLER,
		INDIRECTDIRECTCOMBINATION,
		USERDEFINED,
		NOTDEFINED
	}	/**
	 * This method is used internally and should NOT be used for own purposes.
	**/
	public String getStepParameter(boolean isSelectType)
	{
		if(isSelectType) return new String("IFCEVAPORATIVECOOLERTYPEENUM("+super.getStepParameter(false)+")");
		else return super.getStepParameter(false);
	}


}
