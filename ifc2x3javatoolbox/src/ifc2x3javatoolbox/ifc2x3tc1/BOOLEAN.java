/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class BOOLEAN implements RootInterface
{
	/**
	 * the value
	 */
	public boolean value = false;

	/**
	 * Constructs a new BOOLEAN object. The default value is false.
	 */
	public BOOLEAN()
	{
	}

	/**
	 * Constructs a new BOOLEAN object using the given value.
	 * @param value
	 *            the value
	 */
	public BOOLEAN(boolean value)
	{
		this.value = value;
	}

	/**
	 * This method sets the value of this object.
	 * @param value
	 *            the value to set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}

	/**
	 * This method sets the value of this object.
	 * @param value
	 *            the value to set
	 */
	public void setValue(BOOLEAN value) {
		this.value = value.value;
	}

	/**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
	public String getStepParameter(boolean isSelectType) {
		if (this.value == false) return ".F.";
		else
			return ".T.";
	}

	/**
	 * This method has no use for simple types.
	 * @return null
	 **/
	public String getStepLine() {
		return null;
	}

	/**
	 * This method clones the object (deep cloning).
	 * @return the cloned object
	 **/
	public Object clone() {
		BOOLEAN boolean1 = new BOOLEAN(this.value);
		return boolean1;
	}

	/**
	 * This method returns the objects value as String representation.
	 * @return the value as String representation
	 */
	public String toString() {
		return Boolean.toString(this.value);
	}

}
